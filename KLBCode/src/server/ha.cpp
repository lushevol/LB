#include <stdio.h>
#include <fstream>
#include <deque>
#include <iomanip>

#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>

#include "share/utility.h"

#include "rpc.h"
#include "serialize.h"
#include "base.h"
#include "logger.h"

#include "ethernet.h"
#include "bonding.h"
#include "ha.h"
#include "network.h"
#include "services.h"
#include "hvs.h"

using namespace std;

static const char* const HeartConfig = "/etc/ha.d/ha.cf";
static const char* const ResourceConfig = "/etc/ha.d/haresources";
static const char* const AuthConfig = "/etc/ha.d/authkeys";
static const char* const ResourceNode1 = "/etc/ha.d/resource.d/node1";
static const char* const ResourceNode2 = "/etc/ha.d/resource.d/node2";
String Addr;
bool Sd;

HAControl::HAControl()
    : HA(Configure::GetValue()["HA"])
{
    if(!HA.Enabled.Valid())
    {
        HA.Enabled = false;
        HA.Initdead = 20000;
        HA.Keepalive = 1000;
        HA.Warntime = 5000;
        HA.Deadtime = 10000;
        HA.Port = 694;
        HA.IP = "";
        HA.Hostname = "";
        HA.Dev = "";
        HA.DevStatus = false;
        HA.Sync = 0;
    }
}

void HAControl::GenerateConf()
{
    int fd;
    if((fd = creat(AuthConfig, 0600)) != -1)
    {
        fdostream content(fd);
        content << "auth 1" << endl << "1 crc" << endl;
        content.flush();
        close(fd);
    }

    HostnameControl control;
    String node1, node2, index1, index2;
    if((const String&)control.Name() > (const String&)HA.Hostname)
    {
        node1 = control.Name();
        node2 = HA.Hostname;
        index1 = "self";
        index2 = "other";
    } else {
        node1 = HA.Hostname;
        node2 = control.Name();
        index1 = "other";
        index2 = "self";
    }

    ofstream ha(HeartConfig);
    ha << "keepalive " << HA.Keepalive << "ms" << endl;
    ha << "deadtime " << setprecision(3) << ((double)HA.Deadtime) / 1000 << endl;
    ha << "warntime " << HA.Warntime << "ms" << endl;
    ha << "initdead " << HA.Initdead << "ms" << endl;
    ha << "udpport " << HA.Port << endl;
    ha << "ucast " << HA.Dev << " " << HA.IP << endl;
    ha << "auto_failback " << (HA.Autoback ? "on" : "off") << endl;
    ha << "node " << node1 << endl;
    ha << "node " << node2 << endl;
//#if __WORDSIZE == 64
//    ha << "respawn hacluster /usr/lib64/heartbeat/ipfail" << endl;
//#else
    ha << "respawn hacluster /usr/lib/heartbeat/ipfail" << endl;
//#endif
    ha << "apiauth ipfail gid=haclient uid=hacluster" << endl;
    int count = 0;
    ENUM_LIST(List<DomainStringValue>, HA.Ping, group)
    {
        if(group->IsEmpty())
            continue;
        ha << "ping_group group" << (count++);
        ENUM_LIST(DomainStringValue, *group, domain)
        {
            ha << " " << *domain;
        }
        ha << endl;
    }
    ha.close();

    ofstream resources(ResourceConfig);
    resources << node1 << " node1" << endl;
    resources << node2 << " node2" << endl;
    resources.close();

    if((fd = creat(ResourceNode1, 0755)) != -1)
    {
        fdostream content(fd);
        content << "#!/bin/bash" << endl;
        content << "case \"$1\" in" << endl;
        content << "start)" << endl;
        content << Configure::GetProcessPath() << " -h " << index1 << " $1" << endl;
        content << ";;" << endl;
        content << "stop)" << endl;
        content << Configure::GetProcessPath() << " -h " << index1 << " $1" << endl;
        content << ";;" << endl;
        content << "esac" << endl;
        content << "exit 0" << endl;
        content.flush();
        close(fd);
    }

    if((fd = creat(ResourceNode2, 0755)) != -1)
    {
        fdostream content(fd);
        content << "#!/bin/bash" << endl;
        content << "case \"$1\" in" << endl;
        content << "start)" << endl;
        content << Configure::GetProcessPath() << " -h " << index2 << " $1" << endl;
        content << ";;" << endl;
        content << "stop)" << endl;
        content << Configure::GetProcessPath() << " -h " << index2 << " $1" << endl;
        content << ";;" << endl;
        content << "esac" << endl;
        content << "exit 0" << endl;
        content.flush();
        close(fd);
    }

}

namespace HA
{

    void ExecuteHAStatusChanged(Value& params, Value& result)
    {
	String Msg;
        HAControl control;
        HAItem item(params);
	String address = item.Address;

	Msg = item.Hostname;
	Msg += "HA status changed:";
        if(item.Self.Valid())
        {
            control.HA.Self = item.Self;
            Msg += " HA self resource ";
	    if(control.HA.Self) 
		Msg += "up.";
	    else
		Msg += "down.";
            LOGGER_NOTICE("HA self resource " << (control.HA.Self ? "up" : "down") << ".");
        } else {
            control.HA.Other = item.Other;
            Msg += " HA other resource ";
	    if(control.HA.Other) 
		Msg += "up.";
	    else
		Msg += "down.";
            LOGGER_NOTICE("HA other resource " << (control.HA.Other ? "up" : "down") << ".");
        }
        DEBUG_PRINT("VirtualService", "HA Call Back........................." << control.HA.Self << " " << control.HA.Other);
	if(Sd)
	{
		Exec::System("echo " + Msg + "|" + "mail -s \" HA status changed\"" + " \"" + Addr + "\"");
	}
        VirtualServiceControl().RefreshHA();
        HttpControl().RefreshHA();
    }

    DECLARE_RPC_METHOD(FuncHAChanged, ExecuteHAStatusChanged, false, false);

};

void HAControl::StatusChanged(const String& target, bool start)
{
    Value params;
    HAItem item(params);
    if(target == "self")
        item.Self = start;
    else
        item.Other = start;
    RpcServer::InnerCall(FuncHAChanged, &params);
}

void HAControl::StartHA()
{
    HA.Self = false;
    HA.Other = false;
    VirtualServiceControl().RefreshHA();
    GenerateConf();
    unlink("/var/lib/heartbeat/hb_uuid");
    Exec exe("service");
    exe << "heartbeat" << "start";
    exe.Execute(true);
    if(HA.Sync)
    {
        Exec::System("ipvsadm --start-daemon master --mcast-interface " + (const String&)HA.Dev + " --syncid " + IntToString(HA.Sync));
        Exec::System("ipvsadm --start-daemon backup --mcast-interface " + (const String&)HA.Dev + " --syncid " + IntToString(HA.Sync));
    }
}

void HAControl::StopHA()
{
    HA.Self = false;
    HA.Other = false;
    Exec::System("killall -9 heartbeat");
    Exec::System("killall -9 ha_logd");
    VirtualServiceControl().RefreshHA();
    if(HA.Sync)
    {
        Exec::System("ipvsadm --stop-daemon master");
        Exec::System("ipvsadm --stop-daemon backup");
    }
}

void HAControl::Get(HAItem& item)
{
    item.Send = HA.Send;
    item.Address = HA.Address;
    item.Keepalive = HA.Keepalive;
    item.Deadtime = HA.Deadtime;
    item.Warntime = HA.Warntime;
    item.Initdead = HA.Initdead;
    item.Autoback = HA.Autoback;
    item.Port = HA.Port;
    item.IP = HA.IP;
    item.Dev = HA.Dev;
    item.DevStatus = HA.DevStatus;
    item.Enabled = HA.Enabled;
    item.Hostname = HA.Hostname;
    item.Self = HA.Self;
    item.Other = HA.Other;
    item.Ping = HA.Ping;
    item.Sync = HA.Sync;
}

void HAControl::Set(HAItem& item)
{
    if(HA.Enabled)
        ERROR(Exception::IPVS::HA::Running, "");
    String address = item.Address.Valid() ? item.Address : HA.Address;
    bool send = item.Send.Valid() ? item.Send : HA.Send;
    int keepalive = item.Keepalive.Valid() ? item.Keepalive : HA.Keepalive;
    int warntime = item.Warntime.Valid() ? item.Warntime : HA.Warntime;
    int deadtime = item.Deadtime.Valid() ? item.Deadtime : HA.Deadtime;
    int initdead = item.Initdead.Valid() ? item.Initdead : HA.Initdead;
    if(warntime <= keepalive || deadtime <= warntime || initdead <= deadtime)
        ERROR(Exception::IPVS::HA::Time, "");
    bool autoback = item.Autoback.Valid() ? item.Autoback : HA.Autoback;
    int port = item.Port.Valid() ? item.Port : HA.Port;
    String ip = item.IP.Valid() ? item.IP : HA.IP;
    String dev = item.Dev.Valid() ? item.Dev : HA.Dev;
    if(dev.find("eth", 0) == 0)
        EthernetInterfaceControl().Find(dev);
    else
	BondingInterfaceControl().Find(dev);
    String hostname = item.Hostname.Valid() ? item.Hostname : HA.Hostname;
    typedef deque<StringList> PingList;
    PingList group;
    if(item.Ping.Valid())
    {
        ENUM_LIST(List<DomainStringValue>, item.Ping, list)
        {
            if(!list->IsEmpty())
            {
                group.resize(group.size() + 1);
                StringList& ping = group.back();
                ENUM_LIST(DomainStringValue, *list, domain)
                {
                    if(*domain != "")
                        ping.push_back(*domain);
                }
            }
        }
    }
    int syncid = item.Sync.Valid() ? item.Sync : HA.Sync;
    HA.Address = address;
    Addr = HA.Address;
    HA.Send = send;
    Sd = HA.Send;
    HA.Keepalive = keepalive;
    HA.Warntime = warntime;
    HA.Deadtime = deadtime;
    HA.Initdead = initdead;
    HA.Autoback = autoback;
    HA.Port = port;
    HA.IP = ip;
    HA.Dev = dev;
    HA.Hostname = hostname;
    if(item.Ping.Valid())
    {
        HA.Ping.Clear();
        ENUM_STL(PingList, group, g)
        {
            List<DomainStringValue> list(HA.Ping.Append());
            ENUM_STL(StringList, *g, l)
            {
                list.Append() = *l;
            }
        }
    }
    HA.Sync = syncid;
    RefreshDevStatus();
    LOGGER_NOTICE("Set HA options done.");
}

void HAControl::SetEnabled(HAItem& item)
{
    if(!item.Enabled.Valid())
        ERROR(Exception::Server::Params, "Enabled");
    if(item.Enabled != HA.Enabled)
    {
        if(HA.Enabled)
            StopHA();
        else {
            if(HA.Dev == "")
                ERROR(Exception::IPVS::HA::Dev::Empty, "");
            if(!HA.DevStatus)
                ERROR(Exception::IPVS::HA::Dev::Status, HA.Dev);
            if(HA.Hostname == "")
                ERROR(Exception::IPVS::HA::Hostname, "");
            if(HA.IP == "")
                ERROR(Exception::IPVS::HA::IP, "");
            StartHA();
        }
        HA.Enabled = item.Enabled;
    }
    LOGGER_NOTICE((HA.Enabled ? "Enabled" : "Disable") << " HA.");
}

void HAControl::Refresh()
{
    if(HA.Enabled && HA.DevStatus)
        StopHA();
    RefreshDevStatus();
    if(HA.Enabled && HA.DevStatus)
        StartHA();
}

void HAControl::Refresh(const StringCollection& dev)
{
    HAControl control;
    if(dev.count(control.HA.Dev))
        control.Refresh();
}

void HAControl::RefreshDevStatus()
{
    String dev = HA.Dev;
    if(HA.Dev == "")
        HA.DevStatus = false;
    else if(dev.find("eth", 0) == 0)
    {
        EthernetInterfaceControl control;
        EthernetInterface eth(control.Find(HA.Dev));
        HA.DevStatus = eth.Adsl == "" && eth.Master == "" && !eth.Dhcp && eth.Enabled && !eth.IP.IsEmpty();
    }
    else
    {
        BondingInterfaceControl control;
        BondingInterface bond(control.Find(HA.Dev));
        HA.DevStatus = !bond.Dhcp && bond.Enabled && !bond.IP.IsEmpty();
    }
}

namespace HA
{
    DECLARE_INTERFACE_REFRESH(HAControl::Refresh, 5);

#define EXECUTE_RPC(methodname,methodfunc)\
    void LINE_NAME(methodfunc)(Value& params, Value& result)\
    {\
        RpcMethod::CheckLicence();\
        HAControl control;\
        HAItem item(params);\
        control.methodfunc(item);\
        (bool&)result = true;\
    }\
    DECLARE_RPC_METHOD(methodname,LINE_NAME(methodfunc),true,true)

    EXECUTE_RPC(FuncHASet, Set);
    EXECUTE_RPC(FuncHAEnabled, SetEnabled);

    void ExecuteHAGet(Value& params, Value& result)
    {
        RpcMethod::CheckLicence();
        HAControl control;
        HAItem item(result);
        control.Get(item);
    }

    DECLARE_RPC_METHOD(FuncHAGet, ExecuteHAGet, true, true);

    void HABeforeImport(Value& data, bool reload)
    {
        HAControl control;
        Value temp;
        HAItem ha(temp);
        ha.Enabled = false;
        control.SetEnabled(ha);
    }

    void HAImport(Value& data, bool reload)
    {
        HAItem ha(data["HA"]);
        HAControl control;
        if(reload)
            NO_ERROR(control.Set(ha));
        else
            control.Set(ha);
        if(ha.Enabled)
        {
            if(reload)
                NO_ERROR(control.SetEnabled(ha));
            else
                control.SetEnabled(ha);
        }
    }

    void HAExport(Value& data)
    {
        HAItem result(data["HA"]);
        HAControl control;
        control.Get(result);
        result.Self.Data.clear();
        result.Other.Data.clear();
        result.DevStatus.Data.clear();
    }

    DECLARE_SERIALIZE(HABeforeImport, HAImport, NULL, HAExport, 6);

    void InitHA()
    {
        Exec::System("killall -9 heartbeat");
        Exec::System("killall -9 ha_logd");
        Exec::System("killall -9 ipfail");
        Exec::System("ipvsadm --stop-daemon master");
        Exec::System("ipvsadm --stop-daemon backup");
    }

    DECLARE_INIT(InitHA, InitHA, 0);

};
