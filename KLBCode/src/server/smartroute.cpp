#include <sstream>
#include <fstream>

#include "share/utility.h"

#include "rpc.h"
#include "serialize.h"
#include "base.h"
#include "logger.h"

#include "isp.h"
#include "smartroute.h"

using namespace std;

void SmartRouteControl::GenerateCMDTarget(OStream& cmd, RouteItem& route)
{
    cmd << "default table " << SmartRouteItem(route.Data).Mark;
}

void SmartRouteControl::OnRefreshRouteDone()
{
    RefreshDefaultRoute();
}

String SmartRouteControl::GetISPRulePath(int id)
{
    return "/tmp/isp." + IntToString(id) + ".rule";
}

void SmartRouteControl::AddMangle(int mark)
{
    String markstr0(IntToString(mark));
    String markstr1(IntToString(mark << 16));
    String markstr2(IntToString((mark << 16) + mark));
    Exec::System("ip rule add fwmark " + markstr0 + " table " + markstr0);
    Exec::System("ip rule add fwmark " + markstr1 + " table " + markstr0);
    Exec::System("ip rule add fwmark " + markstr2 + " table " + markstr0);
}

void SmartRouteControl::DelMangle(int mark)
{
    String markstr0(IntToString(mark));
    String markstr1(IntToString(mark << 16));
    String markstr2(IntToString((mark << 16) + mark));
    Exec::System("ip rule del fwmark " + markstr2 + " table " + markstr0);
    Exec::System("ip rule del fwmark " + markstr1 + " table " + markstr0);
    Exec::System("ip rule del fwmark " + markstr0 + " table " + markstr0);
}

SmartRouteControl::SmartRouteControl()
    : RouteControl(Configure::GetValue()["SmartRoute"])
    , Holder(RouteControl::Holder.Data)
{}

void SmartRouteControl::EnsureUniqueISP(int isp, int route)
{
    if(isp == 0)
        return;
    ENUM_LIST(SmartRouteItem, Holder, recent)
    {
        if(recent->ID != route)
        {
            if(recent->ISP == isp)
                ERROR(Exception::Route::Smart::Duplicate, isp);
        }
    }
    Value temp;
    ISPItem request(temp);
    request.ID = isp;
    ISPControl().Find(request);
}

void SmartRouteControl::RefreshSmartModule()
{
    bool running = false;
    ENUM_LIST(SmartRouteItem, Holder, route)
    {
        if(route->ISP != 0)
        {
            running = true;
            break;
        }
    }
    Exec::System("killall klbroute");
    do
    {
        sched_yield();
        if(Exec::System("ps -A|grep klbroute"))
            break;
    } while(true);
    if(running)
    {
        Exec exe("klbroute");
        exe << "default" << 39;
        ENUM_LIST(SmartRouteItem, Holder, route)
        {
            if(route->ISP != 0)
            {
                exe << "static" << ((route->Mark << 16) + route->Mark) << GetISPRulePath(route->ISP);
                exe << "check" << route->Mode << route->Ip << route->Port << route->Frequency << route->Timeout;
            }
        }
        exe.Execute(true);
    }
}

void SmartRouteControl::GenerateMark(SmartRouteItem& route)
{
    if(Holder.GetCount() == route.Mark.Max - route.Mark.Min + 1)
        ERROR(Exception::Route::Smart::Count, "");
    IntCollection markset;
    ENUM_LIST(SmartRouteItem, Holder, recent)
    {
        markset.insert(recent->Mark);
    }
#ifdef __DEBUG__
    route.Mark.Data.clear();
#endif
    for(int i = route.Mark.Min; i <= route.Mark.Max; ++i)
    {
        if(markset.count(i) == 0)
        {
            route.Mark = i;
            break;
        }
    }
    ASSERT(route.Mark.Valid());
}

void SmartRouteControl::Add(SmartRouteItem& route)
{
    if(!route.ISP.Valid())
        ERROR(Exception::Server::Params, "ISP");
    GenerateMark(route);
    EnsureUniqueISP(route.ISP, -1);
    route.ID = Holder.GetCount();
    AddRoute(route);
    SmartRouteItem recent(Find(route.ID));
    recent.ISP = route.ISP;
    recent.Mark = route.Mark;
    recent.Mode = route.Mode;
    recent.Ip = route.Ip;
    recent.Port = route.Port;
    recent.Frequency = route.Frequency;
    recent.Timeout = route.Timeout;
    AddMangle(recent.Mark);
    if(recent.ISP != 0)
        RefreshSmartModule();
    RefreshDefaultRoute();
    LOGGER_NOTICE("Add smart route item done.");
}

void SmartRouteControl::SetCheck(SmartRouteItem& route)
{
    if(!route.ID.Valid())
        ERROR(Exception::Server::Params, "ID");
    SmartRouteItem recent(Find(route.ID));
    if(!route.Mode.Valid())
        ERROR(Exception::Server::Params, "mode");
    if(route.Mode != SmartRouteItem::CheckModeState::disable)
    {
        if(!route.Ip.Valid())
            ERROR(Exception::Server::Params, "chenck ip");
        if(route.Mode == SmartRouteItem::CheckModeState::tcp
                || route.Mode == SmartRouteItem::CheckModeState::udp)
        {
            if(!route.Port.Valid())
                ERROR(Exception::Server::Params, "Port");
        }
        if(!route.Frequency.Valid())
            ERROR(Exception::Server::Params, "frequency");
        if(!route.Timeout.Valid())
            ERROR(Exception::Server::Params, "timeout");
        recent.Ip = route.Ip;
        recent.Port = route.Port;
        recent.Frequency = route.Frequency;
        recent.Timeout = route.Timeout;
    }
    if(route.Mode != SmartRouteItem::CheckModeState::disable ||
            route.Mode != SmartRouteItem::CheckModeState::disable)
    {
        recent.Mode = route.Mode;
        RefreshSmartModule();
    }
    LOGGER_NOTICE("Set smart route check done.");
}

void SmartRouteControl::Set(SmartRouteItem& route)
{
    if(!route.ID.Valid())
        ERROR(Exception::Server::Params, "ID");
    SmartRouteItem recent(Find(route.ID));
    int isp;
    if(route.ISP.Valid())
    {
        isp = route.ISP;
        EnsureUniqueISP(isp, recent.ID);
    } else
        isp = recent.ISP;
    SetRoute(route, recent);
    if(recent.ISP != isp)
    {
        recent.ISP = isp;
        RefreshSmartModule();
    }
    RefreshDefaultRoute();
    LOGGER_NOTICE("Set smart route gates done.");
}

void SmartRouteControl::Del(SmartRouteItem& route)
{
    if(!route.ID.Valid())
        ERROR(Exception::Server::Params, "ID");
    SmartRouteItem recent(Find(route.ID));
    bool isp = recent.ISP != 0;
    DelMangle(recent.Mark);
    DelRoute(recent);
    if(isp)
        RefreshSmartModule();
    RefreshDefaultRoute();
    LOGGER_NOTICE("Del smart route item done.");
}

void SmartRouteControl::SetDescription(SmartRouteItem& route)
{
    if(!route.ID.Valid())
        ERROR(Exception::Server::Params, "ID");
    SmartRouteItem recent(Find(route.ID));
    recent.Description = route.Description;
    LOGGER_NOTICE("Set smart route item description done.");
}

void SmartRouteControl::Get(GetListItem<SmartRouteItem>& get)
{
    int start, end;
    get.Ready(Holder.GetCount(), start, end);
    for(int i = start; i < end; ++i)
    {
        SmartRouteItem route(get.Result.Append());
        SmartRouteItem recent(Find(i));
        RouteControl::Get(route, recent);
        route.ISP = recent.ISP;
        route.Mode = recent.Mode;
        route.Ip = recent.Ip;
        route.Port = recent.Port;
        route.Frequency = recent.Frequency;
        route.Timeout = recent.Timeout;
    }
}

void SmartRouteControl::RefreshDefaultRoute()
{
    bool status = false;
    ENUM_LIST(SmartRouteItem, Holder, recent)
    {
        if(recent->Status)
        {
            status = true;
            break;
        }
    }
    if(status)
    {
        ostringstream stream;
        stream << "ip route replace default scope global mpath wrandom table 39 ";
        ENUM_LIST(SmartRouteItem, Holder, recent)
        {
            ENUM_LIST(GateItem, recent->Gates, gate)
            {
                if(gate->Status)
                {
                    stream << " nexthop";
                    if(gate->IP != "")
                        stream << " via " << gate->IP;
                    stream << " dev " << gate->Dev << " weight " << gate->Weight;
                }
            }
        }
        Exec::System(stream.str());
        Exec::System("ip route replace default scope global dev lo table 252");
    } else {
        Exec::System("ip route del default table 252");
        Exec::System("ip route flush table 39");
    }
}

void SmartRouteControl::RefreshISP()
{
    SmartRouteControl control;
    ISPControl ispcontrol;
    Value temp;
    List<ISPItem> list(temp);
    ispcontrol.GetAll(list);
    IntCollection idlist;
    ENUM_LIST(ISPItem, list, isp)
    {
        idlist.insert(isp->ID);
        ofstream stream(GetISPRulePath(isp->ID).c_str());
        if(stream)
        {
            ENUM_LIST(NetPackStringValue, isp->Net, net)
            {
                stream << *net << '\n';
            }
            stream.close();
        }
    }
    ENUM_LIST(SmartRouteItem, control.Holder, route)
    {
        if(idlist.count(route->ISP) == 0)
            route->ISP = 0;
    }
    control.RefreshSmartModule();
}

namespace SmartRoute
{

    DECLARE_ISP_REFRESH(SmartRouteControl::RefreshISP, 0);

#define EXECUTE_RPC(methodname,methodfunc)\
    void LINE_NAME(methodfunc)(Value& params, Value& result)\
    {\
        RpcMethod::CheckLicence();\
        SmartRouteControl control;\
        SmartRouteItem route(params);\
        control.methodfunc(route);\
        (bool&)result = true;\
    }\
    DECLARE_RPC_METHOD(methodname,LINE_NAME(methodfunc),true,true)

    EXECUTE_RPC(FuncSmartRouteAdd, Add);
    EXECUTE_RPC(FuncSmartRouteDelete, Del);
    EXECUTE_RPC(FuncSmartRouteSet, Set);
    EXECUTE_RPC(FuncSmartRouteDescription, SetDescription);
    EXECUTE_RPC(FuncSmartRouteCheck, SetCheck);

    void ExecuteSmartRouteGetCount(Value& params, Value& result)
    {
        RpcMethod::CheckLicence();
        SmartRouteControl control;
        IntValue res(result);
        control.GetCount(res);
    }

    DECLARE_RPC_METHOD(FuncSmartRouteGetCount, ExecuteSmartRouteGetCount, true, true);

    void ExecuteSmartRouteGet(Value& params, Value& result)
    {
        RpcMethod::CheckLicence();
        SmartRouteControl control;
        GetListItem<SmartRouteItem> get(params);
        control.Get(get);
        result = get.Result.Data;
    }

    DECLARE_RPC_METHOD(FuncSmartRouteGet, ExecuteSmartRouteGet, true, true);

    void SmartRouteBeforeImport(Value& data, bool reload)
    {
        SmartRouteControl control;
        while(control.GetCount())
        {
            Value temp;
            SmartRouteItem route(temp);
            route.ID = control.GetCount() - 1;
            control.Del(route);
        }
    }

    void SmartRouteImport(Value& data, bool reload)
    {
        List<SmartRouteItem> list(data["SmartRoute"]);
        SmartRouteControl control;
        ENUM_LIST(SmartRouteItem, list, e)
        {
            SmartRouteItem route(*e);
            if(reload)
                NO_ERROR(control.Add(route));
            else
                control.Add(route);
        }
    }

    void SmartRouteExport(Value& data)
    {
        List<Model> result(data["SmartRoute"]);
        SmartRouteControl control;
        Value temp;
        GetListItem<SmartRouteItem> list(temp);
        list.All = true;
        control.Get(list);
        ENUM_LIST(SmartRouteItem, list.Result, e)
        {
            SmartRouteItem route(*e);
            route.ID.Data.clear();
            route.Status.Data.clear();
            route.Mark.Data.clear();
            ENUM_LIST(RouteItem::GateItem, route.Gates, g)
            {
                RouteItem::GateItem gate(*g);
                gate.Status.Data.clear();
            }
            result.Append().Data = route.Data;
        }
    }

    DECLARE_SERIALIZE(SmartRouteBeforeImport, SmartRouteImport, NULL, SmartRouteExport, 10);//After ISP

    void InitSmartRouteMangle()
    {
        //0x27==39
        Exec::System("ip rule add fwmark 0x00000027 table 39");
        Exec::System("ip rule add fwmark 0x00270000 table 39");
        Exec::System("ip rule add fwmark 0x00270027 table 39");
        Exec::System("ip route flush table 39");
        Exec::System("iptables -t mangle -N SMART");
        Exec::System("iptables -A FORWARD -t mangle -o lo -m mark --mark 0 -j SMART");
        Exec::System("iptables -A SMART -t mangle -j MKDEV --set-mark 0x00270027");
        Exec::System("iptables -A OUTPUT -t mangle -j MKDEV --set-mark 0x00270027");
        Exec::System("iptables -t mangle -A SMART -m state --state NEW,RELATED,ESTABLISHED_ORIG -j CONNMARK --save-mark --mask 0x0000FFFF");
        Exec::System("iptables -t mangle -A SMART -m state --state ESTABLISHED_REPLY -j CONNMARK --save-mark --mask 0xFFFF0000");
    }

    DECLARE_INIT(InitSmartRouteMangle, NULL, 7);
}
