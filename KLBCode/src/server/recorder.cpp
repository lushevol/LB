#include <map>
#include <deque>
#include <fstream>

#include <time.h>
#include <unistd.h>
#include <pthread.h>
#include <sys/stat.h>

#include "share/utility.h"

#include "base.h"
#include "rpc.h"
#include "logger.h"
#include "serialize.h"

#include "services.h"
#include "recorder.h"

#define CHECKPOINT() \
    do{\
        DEBUG_PRINT("Recorder","Stable test......");\
    }while(0)

using namespace std;

namespace Recorder
{
    enum TimeInterval
    {
        Minute = 60,
        Hour = Minute * 60,
        Day = Hour * 24
    };

    static const char* const FuncSnapshot = "Recorder.Snapshot";

    static const char* const StatPath = "/var/klb/stat/";

    String inline GetFilePath(const String& target, int day)
    {
        return StatPath + target + "/" + IntToString(day);
    }

    String inline GetTargetPath(const String& target)
    {
        return StatPath + target;
    }

    int inline GetTimeNow()
    {
        tzset();
        DEBUG_PRINT("Recorder", "~~~~~~~~~~~~~~~~TimeZone: " << timezone);
        return time(NULL) - timezone;
    }

    struct TargetData
    {
        int64_t Day;
        int64_t Hour[24];
        int64_t Minute[24*60];
        TargetData()
        {
            memset(this, 0, sizeof(TargetData));
        }
    };

    typedef map<String, TargetData> StatisticMap;

    bool BufferValid = false;
    int DayBuffer = 0;
    StatisticMap StatBuffer;

    void GetTargetCollection(StringCollection& target)
    {
        target.clear();
        VirtualServiceControl control;
        ENUM_LIST(VirtualServiceItem, control.Holder, service)
        {
            target.insert(IntToString(service->Mark));
            ENUM_LIST(VirtualServiceItem::ServerItem, service->Servers, server)
            {
                target.insert(server->IP);
            }
        }
    }

    void SaveToFile()
    {
        if(BufferValid)
        {
            LOGGER_INFO("Save klb statistic.");
            DEBUG_PRINT("Recorder", "SaveToFile.........");
            static TargetData empty;
            ENUM_STL(StatisticMap, StatBuffer, e)
            {
                mkdir(GetTargetPath(e->first).c_str(), S_IRWXU);
                TargetData& data = e->second;
                if(memcmp(&data, &empty, sizeof(TargetData)) != 0)
                {
                    ofstream stream(GetFilePath(e->first, DayBuffer).c_str());
                    if(stream)
                    {
                        stream.write((const char*) &data, sizeof(TargetData));
                        stream.close();
                    } else
                        LOGGER_WARNING("Cannot write statistic file.");
                } else {
                    unlink(GetFilePath(e->first, DayBuffer).c_str());
                }
            }
        }
    }

    void LoadFromFile(int now, const String& target, TargetData& result)
    {
        ifstream stream(GetFilePath(target, now / Day).c_str());
        if(stream)
        {
            stream.read((char*)&result, sizeof(TargetData));
            stream.close();
            int minute = (now % Day) / Minute;
            if(minute + 1 < Day / Minute)
            {
                result.Day = 0;
                memset(&result.Hour, 0, sizeof(int64_t) * (Day / Hour));
                for(int i = 0; i < Day / Minute; ++i)
                {
                    if(i <= minute)
                    {
                        result.Day += result.Minute[i];
                        result.Hour[i/Minute] += result.Minute[i];
                    } else
                        result.Minute[i] = 0;
                }
            }
        } else
            memset(&result, 0, sizeof(TargetData));
    }

    void LoadFromFile(int now)
    {
        DEBUG_PRINT("Recorder", "LoadFromFile............" << now);
        StatBuffer.clear();
        StringCollection list;
        GetTargetCollection(list);
        ENUM_STL(StringCollection, list, e)
        {
            LoadFromFile(now, *e, StatBuffer[*e]);
        }
	DEBUG_PRINT("Recorder", "LoadFromFile............ Done");
    }

    void DeleteFile(const String& target)
    {
        DEBUG_PRINT("Recorder", "DeleteFile..............." << target);
        Exec::System("rm -rf " + GetTargetPath(target));
    }

    void SaveStat(int time)
    {
        VirtualServiceControl control;
        typedef map<String, int64_t> StatDeltaMap;
        StatDeltaMap stat;
        ENUM_LIST(VirtualServiceItem, control.Holder, service)
        {
            stat[IntToString(service->Mark)] += service->Statistic.InPacketRate;
            ENUM_LIST(VirtualServiceItem::ServerItem, service->Servers, server)
            {
                stat[server->IP] += server->Statistic.InPacketRate;
            }
        }
        if(time < DayBuffer * Day)
        {
            DEBUG_PRINT("Recorder", "time < DayBuffer * Day");
            StatBuffer.clear();
            DayBuffer = time / Day;
        }
        ENUM_STL(StatisticMap, StatBuffer, e)
        {
            if(stat.count(e->first) == 0)
            {
                DeleteFile(e->first);
                StatBuffer.erase(e);
            }
        }
        if(time >= DayBuffer * Day + Day || !BufferValid)
        {
            SaveToFile();
            DayBuffer = time / Day;
            LoadFromFile(time);
            BufferValid = true;
        }
	ASSERT((time - DayBuffer * Day) < Day);
	int hour = (time - DayBuffer * Day) / Hour;
	int minute = (time - DayBuffer * Day) / Minute;
	ASSERT(hour < 24);
	ASSERT(hour >= 0);
	ASSERT(minute >= 0);
	ASSERT(minute < 24 * 60);
        ENUM_STL(StatDeltaMap, stat, e)
        {
            TargetData& data = StatBuffer[e->first];
            data.Day += e->second;
            data.Hour[hour] += e->second;
            data.Minute[minute] += e->second;
        }
#ifdef __DEBUG__
        if(DebugOutput::IsShowType("Recorder"))
        {
            cout << "Day: " << DayBuffer << endl;
            ENUM_STL(StatisticMap, StatBuffer, e)
            {
                cout << e->first << ":" << e->second.Day << endl;
            }
        }
#endif
    }

    typedef map<String, String> StringMap;

    void GetTargetMap(StringMap& target)
    {
        target.clear();
        VirtualServiceControl control;
        ENUM_LIST(VirtualServiceItem, control.Holder, service)
        {
            VirtualServiceControl::ServiceType type = control.CheckSupportPortMap(*service);
            int port = 0;
            switch(type)
            {
                case VirtualServiceControl::TCP:
                    port = service->TcpPorts.Head().Min;
                    target["TCP"+(const String&)service->IP.Head().IP+":"+IntToString(port)] = IntToString(service->Mark);
                    break;
                case VirtualServiceControl::UDP:
                    port = service->UdpPorts.Head().Min;
                    target["UDP"+(const String&)service->IP.Head().IP+":"+IntToString(port)] = IntToString(service->Mark);
                    break;
                default:
                    ASSERT(type == VirtualServiceControl::Mark);
                    target["FWM"+IntToString(service->Mark)] = IntToString(service->Mark);
            }
            ENUM_LIST(VirtualServiceItem::ServerItem, service->Servers, server)
            {
                if(type != VirtualServiceControl::Mark)
                {
                    if(server->Action == VirtualServiceItem::ServerItem::ActionState::Masq && server->MapPort != 0)
                        target["->"+(const String&)server->IP+":"+IntToString(server->MapPort)] = server->IP;
                    else
                        target["->"+(const String&)server->IP+":"+IntToString(port)] = server->IP;
                } else
                    target["->"+(const String&)server->IP+":0"] = server->IP;
            }
        }
    }

    void ParseStat(const StringMap& targetmap)
    {
        DEBUG_PRINT("Recorder", "ParseStat.............................");
        Exec exe("ipvsadm");
        exe << "-ln" << "--stats";
        exe.Execute();
        fdistream stream(exe.ReadOut());
        String line;
        getline(stream, line);
        getline(stream, line);
        getline(stream, line);
        Value* value = NULL;
        VirtualServiceControl control;
        ENUM_LIST(VirtualServiceItem, control.Holder, service)
        {
            service->Statistic.InPacketRate = 0;
            ENUM_LIST(VirtualServiceItem::ServerItem, service->Servers, server)
            {
                server->Statistic.InPacketRate = 0;
            }
        }
        while(true)
        {
            String tag, index;
            int64_t conn, inpacket, outpacket, inbytes, outbytes;
            if(stream >> tag >> index >> conn >> inpacket >> outpacket >> inbytes >> outbytes)
            {
                CHECKPOINT();
                StringMap::const_iterator pos = targetmap.find(tag + index);
                if(pos != targetmap.end())
                {
                    const String& target = pos->second;
                    if(tag != "->") //VirtualService
                    {
                        CHECKPOINT();
                        value = NULL;
                        ENUM_LIST(VirtualServiceItem, control.Holder, service)
                        {
                            if(IntToString(service->Mark) == target)
                            {
                                value = &(service->Data);
                                service->Statistic.InPacketRate = inpacket - service->Statistic.InPacket;
                                service->Statistic.InPacket = inpacket;
                                DEBUG_PRINT("Recorder", "Get Service Stat: " << service->Statistic.InPacket);
                                DEBUG_PRINT("Recorder", "Get Service Stat Rate: " << service->Statistic.InPacketRate);
                                break;
                            }
                        }
                        CHECKPOINT();
                    } else if(value) //RealServer
                    {
                        CHECKPOINT();
                        VirtualServiceItem service(*value);
                        ENUM_LIST(VirtualServiceItem::ServerItem, service.Servers, server)
                        {
                            if(server->IP == target)
                            {
                                server->Statistic.InPacketRate = inpacket - server->Statistic.InPacket;
                                server->Statistic.InPacket = inpacket;
                                DEBUG_PRINT("Recorder", "Get Server Stat: " << server->Statistic.InPacket);
                                DEBUG_PRINT("Recorder", "Get Server Stat Rate: " << server->Statistic.InPacketRate);
                                break;
                            }
                        }
                        CHECKPOINT();
                    } else {
                        DEBUG_PRINT("Recorder", "ipvsadm -ln --stats  ???????????????????????????????????????????");
                    }
                } else {
                    DEBUG_PRINT("Recorder", "ipvsadm -ln --stats  ???????????????????????????????????????????");
                }
                CHECKPOINT();
            } else
                break;
        }
    }

#ifdef __DEBUG__
    void GetData(const String& target, int from,
                 int to, int interval,
                 StringValue& result);
#endif

    void SaveSnapshot(Value& params, Value& result)
    {
        DEBUG_PRINT("Recorder", "GetTargetCollection.....");
        StringMap targetmap;
        GetTargetMap(targetmap);
        DEBUG_PRINT("Recorder", "Parse.....");
        ParseStat(targetmap);
        DEBUG_PRINT("Recorder", "Save.....");
        SaveStat(GetTimeNow());
#ifdef __DEBUG__
        if(DebugOutput::IsShowType("Recorder"))
        {
            StringCollection collection;
            GetTargetCollection(collection);
            DEBUG_PRINT("Recorder", "Show.....");
            ENUM_STL(StringCollection, collection, e)
            {
                int to = GetTimeNow();
                int interval = Minute;
                int from = to - interval * 7;
                Value temp;
                StringValue res(temp);
                GetData(*e, from, to, interval, res);
                cout << "Record: " << *e << "  Time: " << from / interval << "   " << res << endl;
            }
        }
#endif
    }

    DECLARE_RPC_METHOD(FuncSnapshot, SaveSnapshot, false, false);

    void Sync()
    {
        CHECKPOINT();
        StringCollection list;
        GetTargetCollection(list);
        Exec exe("ls");
        exe << StatPath;
        exe.Execute();
        StringCollection del;
        {
            fdistream stream(exe.ReadOut());
            String name;
            while(stream >> name)
            {
                if(list.count(name) == 0)
                    del.insert(name);
            }
        }
        exe.Close();
        ENUM_STL(StringCollection, del, e)
        {
            StatisticMap::iterator pos = StatBuffer.find(*e);
            if(pos != StatBuffer.end())
                StatBuffer.erase(pos);
            DeleteFile(*e);
        }
        CHECKPOINT();
    }

    void StatInit(Value& data, bool reload)
    {
        Sync();
    }

    DECLARE_SERIALIZE(NULL, StatInit, NULL, NULL, 6);

    void GetData(const String& target, int from,
                 int to, int interval,
                 StringValue& result)
    {
#ifdef __DEBUG__
        StringCollection collection;
        GetTargetCollection(collection);
        ASSERT(collection.count(target) != 0);
#endif
        switch(interval)
        {
            case Minute:
            case Hour:
            case Day:
                break;
            default:
                if(interval % Day != 0 || interval < Day)
                    ERROR(Exception::IPVS::Statistic::Interval, interval);
        }
        {
            int now = GetTimeNow();
            if(from <= now)
                from = from / interval;
            else
                ERROR(Exception::IPVS::Statistic::Range, from << '\n' << to);
            if(to <= now)
                to = to / interval;
            else
                to = now / interval;
            ASSERT(from <= to);
        }
        ostringstream stream;
        bool valid = false;
        int daybuffer = 0;
        TargetData buffer;
        for(int now = from; now <= to; ++now)
        {
            int64_t stat = 0;
            int startday = now * interval / Day;
            int endday = startday + (interval - 1) / Day;
            for(int day = startday; day <= endday; ++day)
            {
                if(!valid || day != daybuffer)
                {
                    if(!BufferValid || day != DayBuffer)
                        LoadFromFile(day * Day + Day - 1, target, buffer);
                    else
                        buffer = StatBuffer[target];
                    daybuffer = day;
                    valid = true;
                }
                switch(interval)
                {
                    case Minute:
                        ASSERT(stat == 0);
                        stat = buffer.Minute[now %(Day/Minute)];
                        break;
                    case Hour:
                        ASSERT(stat == 0);
                        stat = buffer.Hour[now %(Day/Hour)];
                        break;
                    case Day:
                        ASSERT(stat == 0);
                        stat = buffer.Day;
                        break;
                    default:
                        stat += buffer.Day;
                        break;
                }
            }
            stream << stat << ";";
        }
        result = stream.str();
    }

    void CheckRange(StatisticItem& item)
    {
        if(!item.From.Valid())
            ERROR(Exception::Server::Params, "From");
        if(!item.To.Valid())
            ERROR(Exception::Server::Params, "To");
        if(!item.Interval.Valid())
            ERROR(Exception::Server::Params, "Interval");
        if(item.To <= item.From)
            ERROR(Exception::IPVS::Statistic::Range, item.From << '\n' << item.To);
    }

    void GetService(StatisticServiceItem& item)
    {
        if(!item.ID.Valid())
            ERROR(Exception::Server::Params, "ID");
        CheckRange(item);
        VirtualServiceControl control;
        if(item.ID >= control.Holder.GetCount())
            ERROR(Exception::IPVS::Statistic::ID, item.ID);
        GetData(IntToString(control.Holder.Get(item.ID).Mark), item.From, item.To, item.Interval, item.Result);
    }

    void GetServer(StatisticServerItem& item)
    {
        if(!item.IP.Valid())
            ERROR(Exception::Server::Params, "IP");
        CheckRange(item);
        StringCollection collection;
        GetTargetCollection(collection);
        if(collection.count(item.IP) == 0)
            ERROR(Exception::IPVS::Statistic::IP, item.IP);
        GetData(item.IP, item.From, item.To, item.Interval, item.Result);
    }

#define EXECUTE_RPC(methodname,methodfunc,model)\
    void LINE_NAME(methodfunc)(Value& params, Value& result)\
    {\
        RpcMethod::CheckLicence();\
        model res(params);\
        methodfunc(res);\
        result = res.Result.Data;\
    }\
    DECLARE_RPC_METHOD(methodname,LINE_NAME(methodfunc),true,true)

    EXECUTE_RPC(FuncStatisticServer, GetServer, StatisticServerItem);
    EXECUTE_RPC(FuncStatisticService, GetService, StatisticServiceItem);

    void* AlarmThread(void* arg)
    {
        while(true)
        {
#ifndef __DEBUG__
            int second = Minute - GetTimeNow() % Minute;
#else
            int second = 300;
#endif
            while(second)
            {
                pthread_testcancel();
                int delta = sleep(second);
                if(delta)
                    second -= delta;
                else
                    break;
            }
            try
            {
                CHECKPOINT();
                if(!Configure::GetShutdown())
                    RpcServer::InnerCall(FuncSnapshot);
                else
                    break;
                CHECKPOINT();
            } catch(ValueException&)
            {
                DEBUG_PRINT("Recorder", "InnerCall Exception...........");
            }
        }
        pthread_exit(NULL);
    }

    pthread_t AlarmThreadID = 0;

    void InitRecorder()
    {
        mkdir("/var/klb", S_IRWXU);
        mkdir("/var/klb/stat", S_IRWXU);
        pthread_create(&AlarmThreadID, NULL, AlarmThread, NULL);
    }

    void UninitRecorder()
    {
        if(AlarmThreadID != 0)
        {
            pthread_cancel(AlarmThreadID);
            void* temp;
            pthread_join(AlarmThreadID, &temp);
        }
        SaveToFile();
    }

    DECLARE_INIT(InitRecorder, UninitRecorder, 0xFF);
};
