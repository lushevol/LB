#include <set>
#include <fcntl.h>
#include <fstream>

#include "share/utility.h"

#include "rpc.h"
#include "serialize.h"
#include "base.h"
#include "logger.h"

#include "interface.h"
#include "bind.h"
#include "isp.h"

using namespace std;

static const char* const NamedConfig = "/etc/named.conf";
static const char* const WorkPath = "/var/named";

BindControl::BindControl()
    : Bind(Configure::GetValue()["Bind"])
{
    if(!Bind.Enabled.Valid())
    {
        Bind.Port = 53;
        Bind.Reverse = false;
        Bind.Enabled = false;
        Bind.A.Clear();
    }
}

void BindControl::WriteZoneConfigure(int ispid)
{
    ofstream zone((String(WorkPath) + "/" + IntToString(ispid) + ".all.zone").c_str());
    zone << "@\t" << "3600000\tIN\tSOA\tA.ROOT-SERVERS.NET." << "\troot\t(" << endl
         << "\t42" << endl
         << "\t3H" << endl
         << "\t30M" << endl
         << "\t1W" << endl
         << "\t1D)" << endl
         << ".\t3600000\tIN\tNS\tA.ROOT-SERVERS.NET." << endl
         << ".\t3600000\tIN\tNS\tB.ROOT-SERVERS.NET." << endl
         << ".\t3600000\tIN\tNS\tC.ROOT-SERVERS.NET." << endl
         << ".\t3600000\tIN\tNS\tD.ROOT-SERVERS.NET." << endl
         << ".\t3600000\tIN\tNS\tE.ROOT-SERVERS.NET." << endl
         << ".\t3600000\tIN\tNS\tF.ROOT-SERVERS.NET." << endl
         << ".\t3600000\tIN\tNS\tJ.ROOT-SERVERS.NET." << endl
         << ".\t3600000\tIN\tNS\tH.ROOT-SERVERS.NET." << endl
         << ".\t3600000\tIN\tNS\tK.ROOT-SERVERS.NET." << endl
         << ".\t3600000\tIN\tNS\tL.ROOT-SERVERS.NET." << endl
         << ".\t3600000\tIN\tNS\tM.ROOT-SERVERS.NET." << endl
         << ".\t3600000\tIN\tNS\tG.ROOT-SERVERS.NET." << endl
         << ".\t3600000\tIN\tNS\tI.ROOT-SERVERS.NET." << endl
         << "A.ROOT-SERVERS.NET.\t3600000\tIN\tA\t198.41.0.4" << endl
         << "B.ROOT-SERVERS.NET.\t3600000\tIN\tA\t192.228.79.201" << endl
         << "C.ROOT-SERVERS.NET.\t3600000\tIN\tA\t192.33.4.12" << endl
         << "D.ROOT-SERVERS.NET.\t3600000\tIN\tA\t128.8.10.90" << endl
         << "E.ROOT-SERVERS.NET.\t3600000\tIN\tA\t192.203.230.10" << endl
         << "F.ROOT-SERVERS.NET.\t3600000\tIN\tA\t192.5.5.241" << endl
         << "G.ROOT-SERVERS.NET.\t3600000\tIN\tA\t192.112.36.4" << endl
         << "H.ROOT-SERVERS.NET.\t3600000\tIN\tA\t128.63.2.53" << endl
         << "I.ROOT-SERVERS.NET.\t3600000\tIN\tA\t192.36.148.17" << endl
         << "J.ROOT-SERVERS.NET.\t3600000\tIN\tA\t192.58.128.30" << endl
         << "K.ROOT-SERVERS.NET.\t3600000\tIN\tA\t193.0.14.129" << endl
         << "L.ROOT-SERVERS.NET.\t3600000\tIN\tA\t199.7.83.42" << endl
         << "M.ROOT-SERVERS.NET.\t3600000\tIN\tA\t202.12.27.33" << endl;

    ENUM_LIST(ARecord, Bind.A, a)
    {
        if(a->Enabled)
        {
            ENUM_LIST(DomainStringValue, a->Alias, cname)
            {
                zone << *cname << "\t" << a->TTL << "\tIN\tCNAME\t" << a->Name << endl;
            }
            if(a->ReturnAll)
            {
                ENUM_LIST(ServerItem, a->Servers, server)
                {
                    zone << a->Name << "\t" << a->TTL << "\tIN\tA\t" << server->IP << endl;
                }
            } else {
                bool match = false;
                ENUM_LIST(ServerItem, a->Servers, server)
                {
                    if(server->ISP == ispid)
                    {
                        zone << a->Name << "\t" << a->TTL << "\tIN\tA\t" << server->IP << endl;
                        match = true;
                    }
                }
                if(!match)
                {
                    ENUM_LIST(ServerItem, a->Servers, server)
                    {
                        zone << a->Name << "\t" << a->TTL << "\tIN\tA\t" << server->IP << endl;
                    }
                }
            }
        }
    }
}

void BindControl::WriteReverseZoneConfigure()
{
    ofstream zone((String(WorkPath) + "/reverse.zone").c_str());
    zone << "@\t" << "3600000\tIN\tSOA\tA.ROOT-SERVERS.NET." << "\troot\t(" << endl
         << "\t42" << endl
         << "\t3H" << endl
         << "\t30M" << endl
         << "\t1W" << endl
         << "\t1D)" << endl
         << "@\t3600000\tIN\tNS\tA.ROOT-SERVERS.NET." << endl;
    ENUM_LIST(ARecord, Bind.A, a)
    {
        if(a->Enabled)
        {
            ENUM_LIST(ServerItem, a->Servers, s)
            {
                uint32_t addr;
                VERIFY(Address::StringToAddress(s->IP, addr));
                addr = htonl(addr);
                String ip;
                VERIFY(Address::AddressToString(addr, ip));
                zone << ip << "\tPTR\t" << a->Name << "." << endl;
            }
        }
    }
}

void BindControl::WriteConfigure()
{
    ofstream dns(NamedConfig);
    dns << "options" << endl;
    dns << "{" << endl;
    dns << "  directory \"" << WorkPath << "\";" << endl;
    dns << "  allow-query {any;};" << endl;
    dns << "  listen-on port " << Bind.Port << " {any;};" << endl;
    dns << "  avoid-v4-udp-ports  {" << Bind.Port << ";};" << endl;
    dns << "  additional-from-auth yes;" << endl;
    dns << "  additional-from-cache yes;" << endl;
    dns << "};" << endl;
    dns <<  endl;

    IntCollection ispidlist;
    ispidlist.insert(0);
    ENUM_LIST(ARecord, Bind.A, a)
    {
        if(a->Enabled)
        {
            ENUM_LIST(ServerItem, a->Servers, s)
            {
                if(ispidlist.count(s->ISP) == 0)
                    ispidlist.insert(s->ISP);
            }
        }
    }

    if(Bind.Reverse)
        WriteReverseZoneConfigure();

    ENUM_STL_R(IntCollection, ispidlist, ispid)
    {
        ISPControl control;

        //Generate ACL
        dns << "acl acl_" << *ispid << " {" << endl;
        if(*ispid == 0)
            dns << "  any;" << endl;
        else
        {
            Value temp;
            ISPItem item(temp);
            item.ID = *ispid;
            control.GetAll(item);
            ENUM_LIST(NetPackStringValue, item.Net, net)
            {
                dns << " " << *net << "; " << endl;
            }
        }
        dns << "};" << endl;

        String matchlist(IntToString(*ispid));
        matchlist += "_nets";
        String view(IntToString(*ispid));
        StringCollection iplist;

        dns << "view \"" << *ispid << "\" IN {" << endl
            << "  match-clients { acl_" << *ispid << "; };" << endl
            << "  recursion no;" << endl
            //Generate Zone
            << "  zone \".\" IN {" << endl
            << "    type master;" << endl
            << "    file \"" << *ispid << ".all.zone\";" << endl
            << "  };" << endl;
        if(Bind.Reverse)
        {
            //Generate Reverse Zone
            dns << "  zone \"in-addr.arpa\" IN {" << endl
                << "    type master;" << endl
                << "    file \"reverse.zone\";" << endl
                << "  };" << endl;
        }
        WriteZoneConfigure(*ispid);
        dns << "};" << endl << endl;
    }
}

void BindControl::EnsureARecordID(ARecord& item)
{
    if(item.ID.Valid())
        return;
    if(!item.Name.Valid())
        ERROR(Exception::Server::Params, "Name");
    if(item.Name == "")
        ERROR(Exception::Bind::Domain::Empty, "");
    ENUM_LIST(ARecord, Bind.A, a)
    {
        if(a->Name == item.Name)
            item.ID = a->ID;
    }
    if(!item.ID.Valid())
        ERROR(Exception::Bind::Domain::NotFound, item.Name);
}

void BindControl::CheckDomain(StringCollection& existdomain, ARecord& item)
{
    if(!item.Name.Valid())
        ERROR(Exception::Server::Params, "Name");
    if(item.Name == "")
        ERROR(Exception::Bind::Domain::Empty, "");
    if(existdomain.count(item.Name))
        ERROR(Exception::Bind::Domain::Duplicate, item.Name);
    existdomain.insert(item.Name);
    if(item.Alias.Valid())
    {
        ENUM_LIST(DomainStringValue, item.Alias, cname)
        {
            if(*cname == "")
                ERROR(Exception::Bind::Alias::Empty, "");
            if(existdomain.count(*cname))
                ERROR(Exception::Bind::Alias::Duplicate, *cname);
            existdomain.insert(*cname);
        }
    }
    if(item.Servers.Valid())
    {
        IntCollection idlist;
        GetISP(idlist);
        ENUM_LIST(ServerItem, item.Servers, s)
        {
            ServerItem server(*s);
            if(idlist.count(server.ISP) == 0)
                ERROR(Exception::Bind::Server::ISP, server.IP << '\n' << server.ISP);
            if(server.IP == "")
                ERROR(Exception::Bind::Server::Empty, "");
        }
    }
}

void BindControl::RefreshConfigure()
{
    if(Bind.Enabled)
    {
        WriteConfigure();
        Exec exe("service");
        exe << "named" << "reload";
        exe.Execute();
    }
}

void BindControl::GetISP(IntCollection& collection)
{
    Value temp;
    List<ISPItem> list(temp);
    ISPControl().GetName(list);
    collection.clear();
    ENUM_LIST(ISPItem, list, e)
    {
        ISPItem item(*e);
        collection.insert(item.ID);
    }
    collection.insert(0);
}

void BindControl::GetDomainCollection(StringCollection& domains, const IntCollection& except)
{
    domains.clear();
    ENUM_LIST(ARecord, Bind.A, a)
    {
        if(except.count(a->ID) == 0)
        {
            domains.insert(a->Name);
            ENUM_LIST(DomainStringValue, a->Alias, cname)
            {
                domains.insert(*cname);
            }
        }
    }
}

void BindControl::GetDomainCollection(StringCollection& domains, int except)
{
    IntCollection temp;
    temp.insert(except);
    GetDomainCollection(domains, temp);
}

void BindControl::GetDomainCollection(StringCollection& domains)
{
    IntCollection temp;
    GetDomainCollection(domains, temp);
}

void BindControl::CopyARecord(ARecord& target, ARecord& recent)
{
    target.ID = recent.ID;
    target.Name = recent.Name;
    target.TTL = recent.TTL;
    target.Enabled = recent.Enabled;
    target.Name = recent.Name;
    target.ReturnAll = recent.ReturnAll;
    target.Alias = recent.Alias;
    target.Servers = recent.Servers;
}

void BindControl::SetBind(BindItem& item)
{
    int port = item.Port.Valid() ? item.Port : Bind.Port;
    bool reverse = item.Reverse.Valid() ? item.Reverse : Bind.Reverse;
    Bind.Port = port;
    Bind.Reverse = reverse;
    RefreshConfigure();
}

void BindControl::SetEnabled(BindItem& item)
{
    if(!item.Enabled.Valid())
        ERROR(Exception::Server::Params, "Enabled");
    if(item.Enabled)
    {
        if(!Bind.Enabled)
        {
            Bind.Enabled = item.Enabled;
            WriteConfigure();
            Exec exe("service");
            exe << "named" << "start";
            exe.Execute();
            LOGGER_NOTICE("DNS server is running.");
        }
    } else {
        if(Bind.Enabled)
        {
            Bind.Enabled = item.Enabled;
            Exec::System("killall named");
            LOGGER_NOTICE("DNS server is stopped.");
        }
    }
}

void BindControl::AddARecord(ARecord& item)
{
    StringCollection domains;
    GetDomainCollection(domains);
    CheckDomain(domains, item);
    ARecord recent(Bind.A.Append());
    recent.ID = Bind.A.GetCount() - 1;
    item.ID = recent.ID;
    recent.Name = item.Name;
    recent.Enabled = item.Enabled.Valid() ? item.Enabled : true;
    recent.TTL = item.TTL;
    recent.ReturnAll = item.ReturnAll;
    if(item.Alias.Valid())
        recent.Alias = item.Alias;
    else
        recent.Alias.Clear();
    if(item.Servers.Valid())
        recent.Servers = item.Servers;
    else
        recent.Servers.Clear();
    RefreshConfigure();
}

void BindControl::SetARecord(ARecord& item)
{
    EnsureARecordID(item);
    ARecord recent(Bind.A.Get(item.ID));
    if(!item.Name.Valid())
        item.Name = recent.Name;
    StringCollection domains;
    GetDomainCollection(domains, recent.ID);
    CheckDomain(domains, item);
    recent.Name = item.Name;
    recent.Enabled = item.Enabled.Valid() ? item.Enabled : recent.Enabled;
    recent.TTL = item.TTL.Valid() ? item.TTL : recent.TTL;
    recent.ReturnAll = item.ReturnAll.Valid() ? item.ReturnAll : recent.ReturnAll;
    if(item.Alias.Valid())
        recent.Alias = item.Alias;
    if(item.Servers.Valid())
        recent.Servers = item.Servers;
    RefreshConfigure();
}

void BindControl::DelARecord(ARecord& item)
{
    EnsureARecordID(item);
    Bind.A.Delete(item.ID);
    int count = 0;
    ENUM_LIST(ARecord, Bind.A, a)
    {
        a->ID = count++;
    }
    RefreshConfigure();
}

void BindControl::GetBind(BindItem& item)
{
    item.Port = Bind.Port;
    item.Reverse = Bind.Reverse;
    item.Enabled = Bind.Enabled;
}

void BindControl::GetAll(BindItem& item)
{
    GetBind(item);
    item.A.Clear();
    ENUM_LIST(ARecord, Bind.A, a)
    {
        ARecord target(item.A.Append());
        CopyARecord(target, *a);
    }
}

void BindControl::GetARecord(ARecord& item)
{
    EnsureARecordID(item);
    ARecord recent(Bind.A.Get(item.ID));
    CopyARecord(item, recent);
}

void BindControl::GetIDByName(List<ARecord>& list)
{
    ENUM_LIST(ARecord, list, a)
    {
        if(a->Name == "")
            ERROR(Exception::Bind::Domain::Empty, "");
        a->ID.Data.clear();
        ENUM_LIST(ARecord, Bind.A, recenta)
        {
            if(recenta->Name == a->Name)
            {
                a->ID = recenta->ID;
            }
        }
        if(!a->ID.Valid())
            ERROR(Exception::Bind::Domain::NotFound, a->Name);
    }
}

void BindControl::ISPRefresh()
{
    BindControl control;
    IntCollection idlist;
    GetISP(idlist);
    ENUM_LIST(ARecord, control.Bind.A, a)
    {
        ENUM_LIST(ServerItem, a->Servers, s)
        {
            if(idlist.count(s->ISP) == 0)
                s->ISP = 0;
        }
    }
    control.RefreshConfigure();
}

void BindControl::InterfaceRefresh(const StringCollection& dev)
{
    BindControl().RefreshConfigure();
}

namespace Bind
{

    DECLARE_INTERFACE_REFRESH(BindControl::InterfaceRefresh, 0);

    DECLARE_ISP_REFRESH(BindControl::ISPRefresh, 0)

#define EXECUTE_RPC(methodname,methodfunc,model)\
    void LINE_NAME(methodfunc)(Value& params, Value& result)\
    {\
        RpcMethod::CheckLicence();\
        BindControl control;\
        model item(params);\
        control.methodfunc(item);\
        (bool&)result = true;\
    }\
    DECLARE_RPC_METHOD(methodname,LINE_NAME(methodfunc),true,true)
#define EXECUTE_RPC_BIND(methodname,methodfunc) \
    EXECUTE_RPC(methodname,methodfunc,BindItem)
#define EXECUTE_RPC_A(methodname,methodfunc) \
    EXECUTE_RPC(methodname,methodfunc,BindItem::ARecord)

    EXECUTE_RPC_BIND(FuncBindService, SetBind);
    EXECUTE_RPC_BIND(FuncBindEnabled, SetEnabled);
    EXECUTE_RPC_A(FuncBindARecordAdd, AddARecord);
    EXECUTE_RPC_A(FuncBindARecordDel, DelARecord);
    EXECUTE_RPC_A(FuncBindARecordSet, SetARecord);

#define EXECUTE_RPC_GET(methodname,methodfunc,model)\
    void LINE_NAME(methodfunc)(Value& params, Value& result)\
    {\
        RpcMethod::CheckLicence();\
        BindControl control;\
        model item(params);\
        control.methodfunc(item);\
        result = params;\
    }\
    DECLARE_RPC_METHOD(methodname,LINE_NAME(methodfunc),true,true)

    EXECUTE_RPC_GET(FuncBindGetAll, GetAll, BindItem);
    EXECUTE_RPC_GET(FuncBindGetService, GetBind, BindItem);
    EXECUTE_RPC_GET(FuncBindGetIDByName, GetIDByName, List<BindItem::ARecord>);
    EXECUTE_RPC_GET(FuncBindGetARecord, GetARecord, BindItem::ARecord);

    void BindBeforeImport(Value& data, bool reload)
    {
        BindControl control;
        Value temp;
        BindItem bind(temp);
        bind.Enabled = false;
        control.SetEnabled(bind);
        control.Bind.A.Clear();
    }

    void BindImport(Value& data, bool reload)
    {
        BindItem bind(data["Bind"]);
        BindControl control;
        if(reload)
        {
            NO_ERROR(control.SetBind(bind));
            ENUM_LIST(BindItem::ARecord, bind.A, a)
            {
                NO_ERROR(control.AddARecord(*a));
            }
        } else {
            control.SetBind(bind);
            ENUM_LIST(BindItem::ARecord, bind.A, a)
            {
                control.AddARecord(*a);
            }
        }
        if(bind.Enabled)
        {
            if(reload)
                NO_ERROR(control.SetEnabled(bind));
            else
                control.SetEnabled(bind);
        }
    }

    void BindExport(Value& data)
    {
        BindItem result(data["Bind"]);
        BindControl control;
        control.GetAll(result);
        ENUM_LIST(BindItem::ARecord, result.A, a)
        {
            a->ID.Data.clear();
        }
    }

    DECLARE_SERIALIZE(BindBeforeImport, BindImport, NULL, BindExport, 10);

    void InitBind()
    {
        Exec::System("killall named");
    }

    DECLARE_INIT(InitBind, NULL, 0);
}

