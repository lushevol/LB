#include <sstream>
#include <fstream>
#include <iomanip>

#include <sys/time.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <unistd.h>

#include "share/utility.h"
#include "cipher/cipher.h"
#include "licence/licence.h"

#include "base.h"
#include "rpc.h"
#include "logger.h"

#include "system.h"

using namespace std;

void ExecuteServerCheck(Value& params, Value& result)
{
    result = FuncCheckServer;
}

DECLARE_RPC_METHOD(FuncCheckServer, ExecuteServerCheck, true, true);

#define EXECUTE_RPC_SET_NULL(methodname,methodfunc,control)\
    void LINE_NAME(rpcsetnullsystem)(Value& params, Value& result)\
    {\
        control LINE_NAME(_control);\
        LINE_NAME(_control).methodfunc();\
        (bool&)result = true;\
    }\
    DECLARE_RPC_METHOD(methodname, LINE_NAME(rpcsetnullsystem), true, true);

#define EXECUTE_RPC_SET(methodname,methodfunc,control,item)\
    void LINE_NAME(rpcsetsystem)(Value& params, Value& result)\
    {\
        control LINE_NAME(_control);\
        item LINE_NAME(_item)(params);\
        LINE_NAME(_control).methodfunc(LINE_NAME(_item));\
        (bool&)result = true;\
    }\
    DECLARE_RPC_METHOD(methodname, LINE_NAME(rpcsetsystem), true, true);

#define EXECUTE_RPC_GET(methodname,methodfunc,control,item)\
    void LINE_NAME(rpcgetsystem)(Value& params, Value& result)\
    {\
        control LINE_NAME(_control);\
        item LINE_NAME(_item)(result);\
        LINE_NAME(_control).methodfunc(LINE_NAME(_item));\
    }\
    DECLARE_RPC_METHOD(methodname, LINE_NAME(rpcgetsystem), true, true);

void TimeControl::Set(TimeItem& time)
{
    if(!time.Tick.Valid() || !time.Zone.Valid())
        ERROR(Exception::Server::Params, "Tick Zone");
    tzset();
    DEBUG_PRINT("Time", "Set Time: " << time.Tick << " " << time.Zone);
    struct timeval val;
    val.tv_usec = 0;
    val.tv_sec = time.Tick + time.Zone * 3600;
    struct timezone zone;
    zone.tz_minuteswest = time.Zone * 60;
    zone.tz_dsttime = 0;
    if(settimeofday(&val, &zone) == 0)
    {
        static const char* const localtime = "/etc/localtime";
        unlink(localtime);
        if(time.Zone != 0)
        {
            static const char* const gmt = "/usr/share/zoneinfo/Etc/GMT";
            ostringstream stream;
            stream << gmt;
            if(time.Zone > 0)
                stream << "+";
            stream << time.Zone;
            link(stream.str().c_str(), localtime);
        } else {
            static const char* const utc = "/usr/share/zoneinfo/Etc/UTC";
            link(utc, localtime);
        }
    } else
        ERROR(Exception::System::Time, "");
    tzset();
    LOGGER_NOTICE("Set system time done.");
}

EXECUTE_RPC_SET(FuncTimeSet, Set, TimeControl, TimeItem);

void TimeControl::Get(TimeItem& time)
{
    tzset();
    struct timeval now;
    gettimeofday(&now, NULL);
    time.Tick = now.tv_sec - timezone;
    time.Zone = timezone / 3600;
    DEBUG_PRINT("Time", "Get Time: " << time.Tick << " " << time.Zone);
}

EXECUTE_RPC_GET(FuncTimeGet, Get, TimeControl, TimeItem);

void PasswordControl::Set(CommonStringValue& password)
{
    Configure::SetPassword(password);
    LOGGER_NOTICE("Set password done.");
}

EXECUTE_RPC_SET(FuncPasswordSet, Set, PasswordControl, CommonStringValue);

double CoreStatusControl::FUsage;
unsigned long int CoreStatusControl::FThread;

void CoreStatusControl::Init()
{
    FUsage = 0;
    pthread_create(&FThread, NULL, &ReadStatThread, &FUsage);
}

void CoreStatusControl::Uninit()
{
    pthread_cancel(FThread);
    Pointer res;
    pthread_join(FThread, &res);
}

DECLARE_INIT(CoreStatusControl::Init, CoreStatusControl::Uninit, 0);

void CoreStatusControl::ReadStat(unsigned int& idel, unsigned& total)
{
    static const char* const path = "/proc/stat";
    string name;
    ifstream cpu(path);
    cpu >> name;
    unsigned int temp;
    total = 0;
    for(int i = 0; i < 3; ++i)
    {
        cpu >> temp;
        total += temp;
    }
    cpu >> idel;
    total += idel;
    for(int i = 0; i < 4; ++i)
    {
        cpu >> temp;
        total += temp;
    }
    cpu.close();
}

Pointer CoreStatusControl::ReadStatThread(Pointer data)
{
    double* result = (double*)data;
    unsigned idel, total;
    ReadStat(idel, total);
    *result = 0;
    while(true)
    {
        pthread_testcancel();
        sleep(5);
        unsigned int tempidel, temptotal;
        ReadStat(tempidel, temptotal);
        *result = ((temptotal - total) - (tempidel - idel)) * 100;
        *result /= temptotal - total;
        total = temptotal;
        idel = tempidel;
    }
    pthread_exit(0);
}

void CoreStatusControl::Get(CoreStatusItem& item)
{
    static const char* const path = "/proc/cpuinfo";
    item.Core.Clear();
    ifstream file(path);
    if(file)
    {
        String line;
        while(getline(file, line))
        {
            String word;
            ostringstream indexstream;
            istringstream linestream(line.c_str());
            while(linestream >> word)
                if(word != ":")
                    indexstream << word;
                else
                    break;
            if(indexstream.str() == "cpuMHz")
            {
                linestream >> word;
                NormalStringValue str(item.Core.Append());
                str = word;
            }
        }
        file.close();
    }
    ostringstream stream;
    stream << setprecision(2) << FUsage << "%";
    item.Usage = stream.str();
}

EXECUTE_RPC_GET(FuncStatusCore, Get, CoreStatusControl, CoreStatusItem);

void MemoryStatusControl::Get(MemoryStatusItem& item)
{
    static const char* const path = "/proc/meminfo";
    ifstream mem(path);
    if(mem)
    {
        String temp;
        unsigned int total, free, buffer, cached;
        mem >> temp >> total >> temp;
        mem >> temp >> free >> temp;
        mem >> temp >> buffer >> temp;
        mem >> temp >> cached;
        mem.close();
        item.Total = total;
        item.Free = free + buffer + cached;
    } else {
        item.Total = 0;
        item.Free = 0;
    }
}

EXECUTE_RPC_GET(FuncStatusMemory, Get, MemoryStatusControl, MemoryStatusItem);

void DiskStatusControl::Get(DiskStatusItem& item)
{
    Exec exe("df");
    exe << "-B" << "M";
    exe << "--type=ext2";
    exe << "--type=ext3";
    exe << "--type=ext4";
    exe.Execute();
    fdistream res(exe.ReadOut());
    String temp;
    getline(res, temp);
    unsigned int total = 0, used = 0, free = 0;
    while(res >> temp)
    {
        char m;
        unsigned int temptotal, tempused, tempfree;
        res >> temptotal >> m >> tempused >> m >> tempfree >> m >> temp >> temp;
        total += temptotal;
        used += tempused;
        free += tempfree;
    }
    item.Total = total;
    item.Used = used;
    item.Free = free;
}

EXECUTE_RPC_GET(FuncStatusDisk, Get, DiskStatusControl, DiskStatusItem);

const char* UpdaterControl::FTempFile = "/tmp/klb-updater-buffer-file";

void UpdaterControl::Reset()
{
    unlink(FTempFile);
    if(access(FTempFile, F_OK) == 0)
        ERROR(Exception::Updater::Reset, "");
}

EXECUTE_RPC_SET_NULL(FuncUpdaterReset, Reset, UpdaterControl);

void UpdaterControl::Upload(BinaryValue& data)
{
    ofstream stream;
    stream.open(FTempFile, ios_base::app | ios_base::binary);
    if(!stream)
        goto error;
    try
    {
        ENUM_STL_CONST(BinaryData, (const BinaryData&)data, e)
        {
            stream.write(&(*e), sizeof(*e));
        }
        stream.close();
    } catch(ios_base::failure&)
    {
        stream.close();
        goto error;
    }
    return;
error:
    ERROR(Exception::Updater::Upload, "");
}

EXECUTE_RPC_SET(FuncUpdaterUpload, Upload, UpdaterControl, BinaryValue);

void UpdaterControl::Update()
{
    if(access(FTempFile, R_OK | F_OK) != 0)
        ERROR(Exception::Updater::Failed, "");
    Cipher::Key key;
    Cipher::GetKeyByStatic(key);
    String unpack;
    do
    {
        unpack = "/tmp/" + RandomString();
    } while(access(unpack.c_str(), F_OK) == 0);
    mkdir("/var/klb", S_IRWXU);
    mkdir("/var/klb/updater", S_IRWXU);
    ifstream fin(FTempFile);
    ofstream fout(unpack.c_str());
    if(!fin || !fout || !Cipher::DecryptStream(key, fin, fout))
    {
        fin.close();
        fout.close();
        unlink(unpack.c_str());
        ERROR(Exception::Updater::Failed, "");
    } else {
        fin.close();
        fout.close();
        chmod(unpack.c_str(), S_IRUSR | S_IWUSR | S_IXUSR);
        bool ret = Exec::System(unpack + " /var/klb/updater") != 0;
        unlink(unpack.c_str());
        if(ret)
            ERROR(Exception::Updater::Failed, "");
    }
}

EXECUTE_RPC_SET_NULL(FuncUpdaterStart, Update, UpdaterControl);

void LicenceControl::IsExist(BoolValue& exist)
{
    exist = LicenceManager::IsExist();
}

EXECUTE_RPC_GET(FuncLicenceIsExist, IsExist, LicenceControl, BoolValue);

void LicenceControl::GetMachine(NormalStringValue& code)
{
    String machine;
    LicenceManager::GetMachine(machine);
    code = machine;
}

EXECUTE_RPC_GET(FuncLicenceGetMachine, GetMachine, LicenceControl, NormalStringValue);

void LicenceControl::Import(NormalStringValue& code)
{
    if(!LicenceManager::Import(code))
        ERROR(Exception::Server::ImportLicence, code);
}

EXECUTE_RPC_SET(FuncLicenceImport, Import, LicenceControl, NormalStringValue);

void SystemControl::Reboot()
{
        Exec::System("reboot");
}

EXECUTE_RPC_SET_NULL(FuncSystemReboot, Reboot, SystemControl);

void SystemControl::Shutdown()
{
        Exec::System("init 0");
}

EXECUTE_RPC_SET_NULL(FuncSystemShutdown, Shutdown, SystemControl);
