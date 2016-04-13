#include <sstream>
#include <fstream>

#include <time.h>

#include "share/include.h"
#include "share/utility.h"

#include "rpc.h"
#include "serialize.h"
#include "base.h"
#include "logger.h"

#include "adsl.h"
#include "ethernet.h"
#include "staticroute.h"

using namespace std;

AdslControl::AdslControl()
    : Holder(Configure::GetValue()["Adsl"])
{}

int ReadProcInt(const String& path)
{
    int result = 0;
    ifstream stream(path.c_str());
    if(stream)
    {
        try
        {
            stream >> result;
        } catch(ios_base::failure& e)
        {
        }
        stream.close();
    }
    return result;
}

void AdslControl::Copy(AdslItem& target, AdslItem& source)
{
    target.ID = source.ID;
    target.Description = source.Description;
    target.Dev = source.Dev;
    target.Ethernet = source.Ethernet;
    target.User = source.User;
    target.Password = source.Password;
    target.Timeout = source.Timeout;
    target.IP = source.IP;
    target.Dns = source.Dns;
    target.Gate = source.Gate;
    target.MTU = source.MTU;
    target.Status = source.Status;
    if(source.Status == DialState::Connected)
    {
        target.Time = time(NULL) - source.Time;
        target.RX = ReadProcInt("/sys/class/net/" + (const String&)source.Dev + "/statistics/rx_packets");
        target.TX = ReadProcInt("/sys/class/net/" + (const String&)source.Dev + "/statistics/tx_packets");
    } else {
        target.Time = 0;
        target.RX = 0;
        target.TX = 0;
    }
}

void AdslControl::CheckDevValid(AdslItem& adsl)
{
    if(!adsl.Dev.Valid())
        ERROR(Exception::Server::Params, "Dev");
}

Value& AdslControl::Find(const String& dev)
{
    Value* result = NULL;
    ENUM_LIST(AdslItem, Holder, e)
    {
        AdslItem recent(*e);
        if(recent.Dev == dev)
        {
            result = &recent.Data;
            break;
        }
    }
    if(!result)
        ERROR(Exception::Interface::NotFoundDev, dev);
    return *result;
}

void AdslControl::GetAll(List<AdslItem>& list)
{
    list.Clear();
    ENUM_LIST(AdslItem, Holder, e)
    {
        AdslItem recent(*e);
        AdslItem target(list.Append());
        Copy(target, recent);
    }
}

void AdslControl::Get(AdslItem& adsl)
{
    CheckDevValid(adsl);
    AdslItem recent(Find(adsl.Dev));
    Copy(adsl, recent);
}

void AdslControl::RefreshID()
{
    int count = 0;
    ENUM_LIST(AdslItem, Holder, e)
    {
        AdslItem adsl(*e);
        adsl.ID = count++;
    }
}

void AdslControl::SetAdsl(AdslItem& adsl)
{
    PRINTF("Set Adsl..................");
    Exec exe("lb_adsl-setup");
    exe << adsl.User << adsl.Password
        << adsl.Dev << adsl.Ethernet
        << adsl.MTU
        << "no"
        << adsl.Timeout
        << adsl.Dev
        << Configure::GetProcessPath();
    exe.Execute();
}

void AdslControl::DelAdsl(AdslItem& adsl)
{
    PRINTF("Del Adsl..................");
    ostringstream path;
    path << "/etc/ppp/pppoe-" << adsl.Ethernet << ".conf";
    unlink(path.str().c_str());
}

void AdslControl::StartDial(AdslItem& adsl)
{
    PRINTF("Start Adsl..................");
    Exec exe("lb_adsl-start");
    exe << adsl.Ethernet;
    exe.Execute(true);
}

namespace Adsl
{

    void ExecuteAdslStatusChanged(Value& params, Value& result)
    {
        AdslControl control;
        AdslItem adsl(params);
        PRINTF("Adsl Status Changed....." << adsl.IP << " " << adsl.Dns << " " << adsl.Gate << " " << adsl.Status);
        AdslItem recent(control.Find(adsl.Dev));
        if(adsl.Status == AdslItem::DialState::Connected)
        {
            recent.Time = time(NULL);
            recent.IP = adsl.IP;
            recent.Dns = adsl.Dns;
            recent.Gate = adsl.Gate;
            recent.Status = AdslItem::DialState::Connected;
        } else {
            recent.IP = "";
            recent.Dns = "";
            recent.Gate = "";
            if(recent.Timeout == 0)
                recent.Status = AdslItem::DialState::Dial;
            else
                recent.Status = AdslItem::DialState::Stop;
        }
        InterfaceRefresher::Refresh(recent.Dev);
        StaticRouteControl().RefreshDefault();
        LOGGER_NOTICE(recent.Dev << " is connected.");
    }

    DECLARE_RPC_METHOD(FuncAdslStatusChanged, ExecuteAdslStatusChanged, false, false);

};

void AdslControl::StatusChanged(const String& dev, const String& ip, const String& gate, const String& dns, bool ok)
{
    Value params;
    AdslItem adsl(params);
    adsl.Dev = dev;
    adsl.IP = (ip != "0.0.0.0") ? ip : "";
    adsl.Dns = (dns != "0.0.0.0") ? dns : "";
    adsl.Gate = (gate != "0.0.0.0") ? gate : "";
    if(ok)
        adsl.Status = DialState::Connected;
    else
        adsl.Status = DialState::Stop;
    RpcServer::InnerCall(FuncAdslStatusChanged, &params);
}

void AdslControl::StopDial(AdslItem& adsl)
{
    PRINTF("Stop Adsl..................");
    Exec::System("lb_adsl-stop " + (const String&)adsl.Ethernet);
}

void AdslControl::Add(AdslItem& adsl)
{
    CheckDevValid(adsl);
    if(!adsl.Ethernet.Valid())
        ERROR(Exception::Server::Params, "Ethernet");
    ENUM_LIST(AdslItem, Holder, e)
    {
        AdslItem recent(*e);
        if(recent.Dev == adsl.Dev)
            ERROR(Exception::Interface::ExistDev, adsl.Dev);
    }
    EthernetInterfaceControl control;
    EthernetInterface ether(control.Find(adsl.Ethernet));
    if(ether.Master != "" || ether.Adsl != "")
        ERROR(Exception::Interface::Adsl::Used, ether.Dev);
    if(Exec::System("ip link set " + (const String&)adsl.Ethernet + " up") != 0)
        ERROR(Exception::Interface::Adsl::Ethernet, adsl.Ethernet);
    AdslItem recent(Holder.Append());
    recent.ID = Holder.GetCount() - 1;
    recent.Description = adsl.Description;
    recent.Dev = adsl.Dev;
    recent.Ethernet = adsl.Ethernet;
    recent.User = adsl.User;
    recent.Password = adsl.Password;
    recent.Timeout = adsl.Timeout;
    recent.MTU = adsl.MTU;
    if(recent.User == "" || recent.Password == "")
        recent.Status = DialState::Invalid;
    else
        recent.Status = DialState::Stop;
    recent.IP = "";
    recent.Dns = "";
    recent.Gate = "";
    ether.Adsl = recent.Dev;
    control.RefreshInterface(ether.Dev);
    if(recent.Status == DialState::Stop)
        SetAdsl(recent);
    StringCollection dev;
    dev.insert(ether.Dev);
    dev.insert(adsl.Dev);
    InterfaceRefresher::Refresh(dev);
    LOGGER_NOTICE("Add " << recent.Dev << " is done.");
    if(recent.Timeout == 0 && recent.Status == DialState::Stop)
    {
        StartDial(recent);
        recent.Status = DialState::Dial;
        LOGGER_NOTICE("Auto dial " << recent.Dev << " ...");
    }
}

void AdslControl::Set(AdslItem& adsl)
{
    CheckDevValid(adsl);
    AdslItem recent(Find(adsl.Dev));
    if(recent.Status == DialState::Connected || recent.Status == DialState::Dial)
        ERROR(Exception::Interface::Adsl::Busy, adsl.Dev);
    if(adsl.User.Valid())
        recent.User = adsl.User;
    if(adsl.Password.Valid())
        recent.Password = adsl.Password;
    if(recent.User == "" || recent.Password == "")
        recent.Status = DialState::Invalid;
    else
        recent.Status = DialState::Stop;
    if(adsl.Timeout.Valid())
        recent.Timeout = adsl.Timeout;
    if(adsl.MTU.Valid())
        recent.MTU = adsl.MTU;
    if(recent.Status == DialState::Stop)
        SetAdsl(recent);
    LOGGER_NOTICE("Set " << recent.Dev << " is done.");
    if(recent.Timeout == 0 && recent.Status == DialState::Stop)
    {
        StartDial(recent);
        recent.Status = DialState::Dial;
        LOGGER_NOTICE("Auto dial " << recent.Dev << " ...");
    }
}

void AdslControl::Del(AdslItem& adsl)
{
    CheckDevValid(adsl);
    int id;
    {
        AdslItem recent(Find(adsl.Dev));
        id = recent.ID;
        EthernetInterfaceControl control;
        EthernetInterface ether(control.Find(recent.Ethernet));
        if(recent.Status == DialState::Dial || recent.Status == DialState::Connected)
            StopDial(recent);
        DelAdsl(recent);
        ether.Adsl = "";
        control.RefreshInterface(ether.Dev);
        StringCollection dev;
        dev.insert(ether.Dev);
        dev.insert(recent.Dev);
        InterfaceRefresher::Refresh(dev);
    }
    Holder.Delete(id);
    RefreshID();
    LOGGER_NOTICE("Del " << adsl.Dev << " is done.");
}

void AdslControl::Dial(AdslItem& adsl)
{
    CheckDevValid(adsl);
    AdslItem recent(Find(adsl.Dev));
    if(recent.User == "")
        ERROR(Exception::Interface::Adsl::User, recent.Dev);
    if(recent.Password == "")
        ERROR(Exception::Interface::Adsl::Password, recent.Dev);
    switch(recent.Status)
    {
        case DialState::Stop:
            {
                PRINTF("Start Dial...." << recent.User << " " << recent.Password);
                StartDial(recent);
                recent.Status = DialState::Dial;
                LOGGER_NOTICE("Dial " << recent.Dev << " ...");
            }
            break;
        case DialState::Dial:
            ERROR(Exception::Interface::Adsl::Dial, recent.Dev);
            break;
        case DialState::Connected:
            ERROR(Exception::Interface::Adsl::Connected, recent.Dev);
            break;
        default:
            break;
    }
}

void AdslControl::Stop(AdslItem& adsl)
{
    CheckDevValid(adsl);
    AdslItem recent(Find(adsl.Dev));
    switch(recent.Status)
    {
        case DialState::Stop:
            ERROR(Exception::Interface::Adsl::Stopped, recent.Dev);
            break;
        case DialState::Dial:
        case DialState::Connected:
            {
                StopDial(recent);
                recent.Status = DialState::Stop;
                recent.IP = "";
                recent.Dns = "";
                recent.Gate = "";
                LOGGER_NOTICE("Disconnect " << recent.Dev << " is done.");
            }
            break;
        default:
            ERROR(Exception::Interface::Adsl::Stopped, recent.Dev);
            break;
    }
}

void AdslControl::SetDescription(AdslItem& adsl)
{
    CheckDevValid(adsl);
    AdslItem recent(Find(adsl.Dev));
    recent.Description = adsl.Description;
    LOGGER_NOTICE("Set " << recent.Dev << "'s description done.");
}

namespace Adsl
{
#define EXECUTE_RPC(methodname,methodfunc)\
    void LINE_NAME(methodfunc)(Value& params, Value& result)\
    {\
        RpcMethod::CheckLicence();\
        AdslControl LINE_NAME(control);\
        AdslItem LINE_NAME(model)(params);\
        LINE_NAME(control).methodfunc(LINE_NAME(model));\
        (bool&)result = true;\
    }\
    DECLARE_RPC_METHOD(methodname,LINE_NAME(methodfunc),true,true)

    EXECUTE_RPC(FuncAdslAdd, Add);
    EXECUTE_RPC(FuncAdslDel, Del);
    EXECUTE_RPC(FuncAdslSet, Set);
    EXECUTE_RPC(FuncAdslDial, Dial);
    EXECUTE_RPC(FuncAdslStop, Stop);
    EXECUTE_RPC(FuncAdslDescription, SetDescription);

#define EXECUTE_RPC_GET(methodname,methodfunc,model)\
    void LINE_NAME(methodfunc)(Value& params, Value& result)\
    {\
        RpcMethod::CheckLicence();\
        AdslControl LINE_NAME(control);\
        model LINE_NAME(instance)(params);\
        LINE_NAME(control).methodfunc(LINE_NAME(instance));\
        result = params;\
    }\
    DECLARE_RPC_METHOD(methodname,LINE_NAME(methodfunc),true,true)

    EXECUTE_RPC_GET(FuncAdslGet, Get, AdslItem)
    EXECUTE_RPC_GET(FuncAdslGetAll, GetAll, List<AdslItem>)

    void AdslBeforeImport(Value& data, bool reload)
    {
        AdslControl control;
        StringList ppp;
        {
            Value temp;
            List<AdslItem> list(temp);
            control.GetAll(list);
            ENUM_LIST(AdslItem, list, e)
            {
                AdslItem adsl(*e);
                ppp.push_back(adsl.Dev);
            }
        }
        ENUM_STL(StringList, ppp, e)
        {
            Value temp;
            AdslItem adsl(temp);
            adsl.Dev = *e;
            control.Del(adsl);
        }
    }

    void AdslImport(Value& data, bool reload)
    {
        List<AdslItem> list(data["Adsl"]);
        AdslControl control;
        ENUM_LIST(AdslItem, list, e)
        {
            AdslItem adsl(*e);
            if(reload)
                NO_ERROR(control.Add(adsl));
            else
                control.Add(adsl);
        }
    }

    void AdslExport(Value& data)
    {
        List<AdslItem> list(data["Adsl"]);
        AdslControl control;
        control.GetAll(list);
        ENUM_LIST(AdslItem, list, e)
        {
            AdslItem adsl(*e);
            adsl.Gate.Data.clear();
            adsl.Dns.Data.clear();
            adsl.IP.Data.clear();
            adsl.ID.Data.clear();
            adsl.Status.Data.clear();
            adsl.RX.Data.clear();
            adsl.TX.Data.clear();
            adsl.Time.Data.clear();
        }
    }

    DECLARE_SERIALIZE(AdslBeforeImport, NULL, AdslImport, AdslExport, 2);

    void InitAdsl()
    {
        Exec::System("killall -9 pppoe");
        Exec::System("killall -9 pppd");
        Exec::System("killall -9 lb_adsl-connect");
    }

    DECLARE_INIT(InitAdsl, InitAdsl, -1);

};
