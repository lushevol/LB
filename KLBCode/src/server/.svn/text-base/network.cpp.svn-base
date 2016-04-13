#include <fstream>

#include <netdb.h>

#include "share/include.h"
#include "share/utility.h"

#include "model/network.h"

#include "rpc.h"
#include "serialize.h"
#include "base.h"
#include "logger.h"

#include "network.h"
#include "ethernet.h"
#include "bonding.h"
#include "adsl.h"
#include "ha.h"

using namespace std;

#define EXECUTE_RPC_SET(methodname,methodfunc,control,item,licence)\
    void LINE_NAME(rpcsetinnetwork)(Value& params, Value& result)\
    {\
        if (licence)\
            RpcMethod::CheckLicence();\
        control LINE_NAME(_control);\
        item LINE_NAME(_item)(params);\
        LINE_NAME(_control).methodfunc(LINE_NAME(_item));\
        (bool&)result = true;\
    }\
    DECLARE_RPC_METHOD(methodname, LINE_NAME(rpcsetinnetwork), true, true);

#define EXECUTE_RPC_GET(methodname,methodfunc,control,item,licence)\
    void LINE_NAME(rpcgetinnetwork)(Value& params, Value& result)\
    {\
        if (licence)\
            RpcMethod::CheckLicence();\
        control LINE_NAME(_control);\
        item LINE_NAME(_item)(result);\
        LINE_NAME(_control).methodfunc(LINE_NAME(_item));\
    }\
    DECLARE_RPC_METHOD(methodname, LINE_NAME(rpcgetinnetwork), true, true);

#define EXECUTE_SERIALIZE(control,item,getfunc,setfunc,name)\
    void LINE_NAME(importfuncinnetwork)(Value& data, bool reload)\
    {\
        item LINE_NAME(_item)(data[name]);\
        control LINE_NAME(_control);\
        if(reload)\
            NO_ERROR(LINE_NAME(_control).setfunc(LINE_NAME(_item)));\
        else\
            LINE_NAME(_control).setfunc(LINE_NAME(_item));\
    }\
    \
    void LINE_NAME(exportfuncinnetwork)(Value& data)\
    {\
        item LINE_NAME(_item)(data[name]);\
        control LINE_NAME(_control);\
        LINE_NAME(_control).getfunc(LINE_NAME(_item));\
    }\
    \
    DECLARE_SERIALIZE(NULL, LINE_NAME(importfuncinnetwork), NULL, LINE_NAME(exportfuncinnetwork), 0);

Network::InterfaceType Network::GetDevType(const String& dev)
{
    try
    {
        EthernetInterfaceControl control;
        control.Find(dev);
        return Ethernet;
    } catch(ValueException& e)
    {
    }
    try
    {
        BondingInterfaceControl control;
        control.Find(dev);
        return Bonding;
    } catch(ValueException& e)
    {
    }
    try
    {
        AdslControl control;
        control.Find(dev);
        return Adsl;
    } catch(ValueException& e)
    {
    }
    return Unknown;
}

int Network::GetProtocol(const String& name)
{
    if(name.empty())
        return 0;
    ENUM_STL_CONST(String, name, e)
    {
        if(!isdigit(*e))
        {
            if(struct protoent* pro = getprotobyname(name.c_str()))
                return pro->p_proto;
            else
                ERROR(Exception::Network::Protocol, name);
        }
    }
    istringstream stream(name);
    int result;
    if(stream >> result)
        return result;
    else
        ERROR(Exception::Network::Protocol, name);
}

int Network::GetServiceByProtocol(int protocol, const String& service)
{
    if(service.empty())
        return 0;
    ENUM_STL_CONST(String, service, e)
    {
        if(!isdigit(*e))
        {
            if(!IsSupportPort(protocol))
                ERROR(Exception::Network::SupportPort, protocol);
            struct protoent* pro = getprotobynumber(protocol);
            if(!pro)
                ERROR(Exception::Network::Protocol, protocol);
            struct  servent *ser = getservbyname(service.c_str(), pro->p_name);
            if(!ser)
                ERROR(Exception::Network::Service, service);
            return ntohs(ser->s_port);
        }
    }
    istringstream stream(service);
    int result;
    if(stream >> result)
    {
        if(result != 0 && !IsSupportPort(protocol))
            ERROR(Exception::Network::SupportPort, protocol);
        return result;
    } else
        ERROR(Exception::Network::Service, service);
}

bool Network::IsSupportPort(int protocol)
{
    switch(protocol)
    {
        case 6:
        case 17:
            return true;
        default:
            return false;
    };
    return false;
}

DnsServerControl::DnsServerControl()
    : DnsList(Configure::GetValue()["DnsServer"])
{}

void DnsServerControl::Get(List<HostStringValue>& dns)
{
    dns.SetSize(0);
    ENUM_LIST(HostStringValue, DnsList, e)
    {
        HostStringValue target(dns.Append());
        HostStringValue recent(*e);
        target = recent;
    }
}

static const char * const ResolvFile = "/etc/resolv.conf";

void DnsServerControl::Set(List<HostStringValue>& dns)
{
    ENUM_LIST(HostStringValue, dns, e)
    {
        HostStringValue target(*e);
        if(target == "")
            ERROR(Exception::Network::DnsServer, "");
    }
    ofstream stream(ResolvFile);
    if(stream)
    {
        DnsList.Data = dns.Data;
        ENUM_LIST(HostStringValue, DnsList, e)
        {
            stream << "nameserver " << (*e) << endl;
        }
        stream.close();
    } else
        ERROR(Exception::Network::DnsServer, "");
    LOGGER_NOTICE("Set dns server done.");
}

void DnsServerControl::Refresh()
{
    ofstream stream(ResolvFile);
    if(stream)
    {
        ENUM_LIST(HostStringValue, DnsList, e)
        {
            stream << "nameserver " << (*e) << endl;
        }
        stream.close();
    }
}

EXECUTE_RPC_GET(FuncDnsServerGet, Get, DnsServerControl, List<HostStringValue>, false);
EXECUTE_RPC_SET(FuncDnsServerSet, Set, DnsServerControl, List<HostStringValue>, false);
EXECUTE_SERIALIZE(DnsServerControl, List<HostStringValue>, Get, Set, "DnsServer");

const char * const HostNameProc = "/proc/sys/kernel/hostname";

HostnameControl::HostnameControl()
    : FName(Configure::GetValue()["Hostname"])
{
    if(!FName.Valid())
    {
        ifstream proc(HostNameProc);
        String name;
        proc >> name;
        proc.close();
        try
        {
            FName = name;
        } catch(ValueException& e)
        {
            FName = "KLB";
            SetName(FName);
        }
    }
}

void HostnameControl::Get(HostnameStringValue& name)
{
    name = FName;
}

const String& HostnameControl::Name()
{
    return FName;
}

void HostnameControl::SetName(const String& name)
{
    ofstream proc(HostNameProc);
    proc << name << endl;
    proc.close();
}

void HostnameControl::Set(HostnameStringValue& name)
{
    if(!name.Valid())
        ERROR(Exception::Server::Params, "Hostname");
    if(name == "")
        ERROR(Exception::Network::Hostname, "");
    bool changed = FName != name;
    FName = name;
    SetName(FName);
    if(changed)
        HAControl().Refresh();
    LOGGER_NOTICE("Set hostname " << name << " done.");
}

EXECUTE_RPC_GET(FuncHostnameGet, Get, HostnameControl, HostnameStringValue, false);
EXECUTE_RPC_SET(FuncHostnameSet, Set, HostnameControl, HostnameStringValue, false);
EXECUTE_SERIALIZE(HostnameControl, HostnameStringValue, Get, Set, "Hostname");

AntiDosControl::AntiDosControl()
    : FAnti(Configure::GetValue()["AntiDos"])
{
    if(!FAnti.Valid())
        FAnti = false;
}

void AntiDosControl::Get(BoolValue& anti)
{
    anti = FAnti;
}

void AntiDosControl::Set(BoolValue& anti)
{
    if(!anti.Valid())
        ERROR(Exception::Server::Params, "AntiDos");
    FAnti = anti;
    String value = FAnti ? "1" : "0";
    Exec::System("echo " + value + " >> /proc/sys/net/ipv4/vs/drop_entry");
    Exec::System("echo " + value + " >> /proc/sys/net/ipv4/vs/drop_packet");
    Exec::System("echo " + value + " >> /proc/sys/net/ipv4/vs/secure_tcp");
    LOGGER_NOTICE("Set antidos " << (FAnti ? "enabled" : "disabled") << " done.");
}

const bool& AntiDosControl::Anti()
{
    return (const bool&)FAnti;
}

void InitAntiDos()
{
    Exec::System("modprobe ip_vs");
    Exec::System("echo 0 >> /proc/sys/net/ipv4/vs/drop_entry");
    Exec::System("echo 0 >> /proc/sys/net/ipv4/vs/drop_packet");
    Exec::System("echo 0 >> /proc/sys/net/ipv4/vs/secure_tcp");
}

DECLARE_INIT(InitAntiDos, NULL, 0);

EXECUTE_RPC_GET(FuncAntiDosGet, Get, AntiDosControl, BoolValue, true);
EXECUTE_RPC_SET(FuncAntiDosSet, Set, AntiDosControl, BoolValue, true);
EXECUTE_SERIALIZE(AntiDosControl, BoolValue, Get, Set, "AntiDos");

void InitIpTables()
{
    Exec::System("service iptables stop");
    Exec::System("iptables -t mangle -F");
    Exec::System("iptables -t nat -F");
    Exec::System("iptables -t filter -F");
}

DECLARE_INIT(InitIpTables, NULL, 0);
