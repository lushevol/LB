#include <fstream>
#include <sstream>
#include <iomanip>

#include "share/utility.h"
#include "share/include.h"

#include "rpc.h"
#include "base.h"
#include "serialize.h"
#include "logger.h"

#include "arp.h"
#include "interface.h"
#include "ethernet.h"
#include "bonding.h"
#include "network.h"

using namespace std;

ArpControl::ArpControl()
    : Holder(Configure::GetValue()["Arp"])
{}

void ArpControl::GetStatic(List<ArpItem>& list)
{
    list.Clear();
    ENUM_LIST(ArpItem, Holder, e)
    {
        ArpItem arp(list.Append());
        ArpItem recent(*e);
        arp.IP = recent.IP;
        arp.MAC = recent.MAC;
        arp.Dev = recent.Dev;
        arp.Status = recent.Status;
    }
}

void ArpControl::GetDynamic(List<ArpItem>& list)
{
    list.Clear();
    ifstream proc("/proc/net/arp");
    String line;
    getline(proc, line);
    while(getline(proc, line))
    {
        istringstream stream(line);
        String ip, mask, dev, mac;
        int type, flag;
        if(stream >> ip >> hex >> type >> flag >> mac >> mask >> dev)
        {
            if(type == 1 && (flag & 0x4) == 0 && mac != "00:00:00:00:00:00")
            {
                ArpItem arp(list.Append());
                arp.IP = ip;
                arp.Dev = dev;
                arp.MAC = mac;
                arp.Status = ArpState::Dynamic;
                PRINTF("dynamic arp: " << ip << " " << mac << " " << dev);
            }
        }
    }
    proc.close();
}

bool ArpControl::CheckDev(const String& dev)
{
    bool result = false;
    switch(Network::GetDevType(dev))
    {
        case Network::Ethernet:
            {
                EthernetInterfaceControl control;
                EthernetInterface eth(control.Find(dev));
                if(eth.Enabled && eth.Master == "" && eth.Adsl == "")
                    result = true;
            }
            break;
        case Network::Bonding:
            {
                BondingInterfaceControl control;
                BondingInterface bond(control.Find(dev));
                if(bond.Enabled && !bond.Slaves.IsEmpty())
                    result = true;
            }
            break;
        default:
            break;
    }
    return result;
}

void ArpControl::Set(ArpItem& arp)
{
    if(!arp.IP.Valid())
        ERROR(Exception::Server::Params, "IP");
    if(!arp.Dev.Valid())
        ERROR(Exception::Server::Params, "Dev");
    if(!arp.MAC.Valid())
        ERROR(Exception::Server::Params, "MAC");
    ENUM_LIST(ArpItem, Holder, e)
    {
        ArpItem recent(*e);
        if(recent.IP == arp.IP && recent.Dev == arp.Dev)
            ERROR(Exception::Arp::Set, "");
    }
    ArpItem recent(Holder.Append());
    recent.IP = arp.IP;
    recent.Dev = arp.Dev;
    recent.MAC = arp.MAC;
    if(CheckDev(recent.Dev) && Exec::System("arp -i " + (const String&)recent.Dev + " -s " + (const String&)recent.IP + " " + (const String&)recent.MAC) == 0)
        recent.Status = ArpState::Static;
    else
        recent.Status = ArpState::Invalid;
    LOGGER_NOTICE("Set static arp item " << recent.IP << " done.");
}

void ArpControl::Del(ArpItem& arp)
{
    if(!arp.IP.Valid())
        ERROR(Exception::Server::Params, "IP");
    if(!arp.Dev.Valid())
        ERROR(Exception::Server::Params, "Dev");
    int id = 0;
    bool del = true;
    ENUM_LIST(ArpItem, Holder, e)
    {
        ArpItem recent(*e);
        if(recent.IP == arp.IP && recent.Dev == arp.Dev)
        {
            del = recent.Status != ArpState::Invalid;
            break;
        }
        ++id;
    }
    if(id < Holder.GetCount())
        Holder.Delete(id);
    if(del)
        if(Exec::System("arp -i " + (const String&)arp.Dev + " -d " + (const String&)arp.IP))
            ERROR(Exception::Arp::Del, "");
    LOGGER_NOTICE("Del arp item " << arp.IP << " done.");
}

void ArpControl::Refresh(const StringCollection& dev)
{
    ArpControl control;
    ENUM_LIST(ArpItem, control.Holder, e)
    {
        ArpItem recent(*e);
        if(dev.count(recent.Dev))
        {
            if(CheckDev(recent.Dev) && Exec::System("arp -i " + (const String&)recent.Dev + " -s " + (const String&)recent.IP + " " + (const String&)recent.MAC) == 0)
                recent.Status = ArpState::Static;
            else {
                recent.Status = ArpState::Invalid;
                Exec::System("arp -i " + (const String&)recent.Dev + " -d " + (const String&)recent.IP);
            }
        }
    }
}

namespace Arp
{

    DECLARE_INTERFACE_REFRESH(ArpControl::Refresh, 5);

#define EXECUTE_RPC(methodname,methodfunc)\
    void LINE_NAME(methodfunc)(Value& params, Value& result)\
    {\
        RpcMethod::CheckLicence();\
        ArpControl control;\
        ArpItem arp(params);\
        control.methodfunc(arp);\
        (bool&)result = true;\
    }\
    DECLARE_RPC_METHOD(methodname,LINE_NAME(methodfunc),true,true)

    EXECUTE_RPC(FuncArpSet, Set);
    EXECUTE_RPC(FuncArpDel, Del);

#define EXECUTE_RPC_GET(methodname,methodfunc)\
    void LINE_NAME(methodfunc)(Value& params, Value& result)\
    {\
        RpcMethod::CheckLicence();\
        ArpControl control;\
        List<ArpItem> list(result);\
        control.methodfunc(list);\
    }\
    DECLARE_RPC_METHOD(methodname,LINE_NAME(methodfunc),true,true)

    EXECUTE_RPC_GET(FuncArpGetDynamic, GetDynamic);
    EXECUTE_RPC_GET(FuncArpGetStatic, GetStatic);

    void ArpBeforeImport(Value& data, bool reload)
    {
        ArpControl control;
        Value temp;
        List<ArpItem> list(temp);
        control.GetStatic(list);
        ENUM_LIST(ArpItem, list, e)
        {
            ArpItem arp(*e);
            control.Del(arp);
        }
    }

    void ArpImport(Value& data, bool reload)
    {
        List<ArpItem> list(data["Arp"]);
        ArpControl control;
        ENUM_LIST(ArpItem, list, e)
        {
            ArpItem arp(*e);
            if(reload)
                NO_ERROR(control.Set(arp));
            else
                control.Set(arp);
        }
    }

    void ArpExport(Value& data)
    {
        List<ArpItem> result(data["Arp"]);
        ArpControl control;
        control.GetStatic(result);
        ENUM_LIST(ArpItem, result, e)
        {
            ArpItem arp(*e);
            arp.Status.Data.clear();
        }
    }

    DECLARE_SERIALIZE(ArpBeforeImport, ArpImport, NULL, ArpExport, 5);

    void InitArp()
    {
        Exec::System("ip neigh flush all");
    }

    DECLARE_INIT(InitArp, NULL, 0);

};
