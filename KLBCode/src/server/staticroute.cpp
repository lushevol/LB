#include "share/include.h"
#include "share/utility.h"

#include "rpc.h"
#include "base.h"
#include "serialize.h"
#include "logger.h"

#include "staticroute.h"

StaticRouteControl::StaticRouteControl()
    : RouteControl(Configure::GetValue()["StaticRoute"])
    , Holder(RouteControl::Holder.Data)
{
}

void StaticRouteControl::GenerateCMDTarget(OStream& cmd, RouteItem& route)
{
    StaticRouteItem item(route.Data);
    cmd << item.Net << " metric " << item.Metric;
}

bool StaticRouteControl::IsBaseStaticRoute(StaticRouteItem& route)
{
    return route.Status && route.Link;
}

void StaticRouteControl::EnsureRouteID(StaticRouteItem& route)
{
    if(route.ID.Valid())
        return;
    if(!route.Net.Valid())
        ERROR(Exception::Server::Params, "Net");
    if(route.Net == "")
        ERROR(Exception::Route::Static::Empty, "");
    ENUM_LIST(StaticRouteItem, Holder, e)
    {
        StaticRouteItem recent(*e);
        if(recent.Net == route.Net && recent.Metric == route.Metric)
        {
            route.ID = recent.ID;
            break;
        }
    }
    if(!route.ID.Valid())
        ERROR(Exception::Route::Static::NotFound, route.Net << '\n' << route.Metric);
}

void StaticRouteControl::RefreshDefault()
{
    ENUM_LIST(StaticRouteItem, Holder, e)
    {
        StaticRouteItem route(*e);
        if(route.Net == "0.0.0.0/0" && route.Metric == 0)
        {
            RefreshRoute(route);
            if(route.Link)
                RefreshRecursion(route.Net);
            break;
        }
    }
}

void StaticRouteControl::Add(StaticRouteItem& route)
{
    if(!route.Net.Valid())
        ERROR(Exception::Server::Params, "Net");
    if(route.Net == "")
        ERROR(Exception::Route::Static::Empty, "");
    if(!route.Metric.Valid())
        route.Metric = 0;
    uint32_t addr;
    int mask;
    Address::StringToAddressPack(route.Net, addr, mask);
    addr = htonl(addr);
    int id = 0;
    ENUM_LIST(StaticRouteItem, Holder, e)
    {
        StaticRouteItem match(*e);
        uint32_t maddr;
        int mmask;
        Address::StringToAddressPack(match.Net, maddr, mmask);
        maddr = htonl(maddr);
        if(maddr < addr)
            break;
        if(maddr == addr)
        {
            if(match.Metric > route.Metric)
                break;
            if(match.Metric == route.Metric)
                ERROR(Exception::Route::Static::Duplicate, match.Net << '\n' << match.Metric);
        }
        ++id;
    }
    route.ID = id;
    AddRoute(route);
    StaticRouteItem recent(Holder.Get(id));
    recent.Net = route.Net;
    recent.Metric = route.Metric;
    if(IsBaseStaticRoute(recent))
        RefreshRecursion(recent.Net);
    LOGGER_NOTICE("Add static route item done.");
}

void StaticRouteControl::Set(StaticRouteItem& route)
{
    EnsureRouteID(route);
    StaticRouteItem recent(Find(route.ID));
    route.Net = recent.Net;
    route.Metric = recent.Metric;
    bool old = IsBaseStaticRoute(recent);
    SetRoute(route, recent);
    if(IsBaseStaticRoute(recent) != old)
        RefreshRecursion(recent.Net);
    LOGGER_NOTICE("Set static route gates done.");
}

void StaticRouteControl::Del(StaticRouteItem& route)
{
    EnsureRouteID(route);
    StaticRouteItem recent(Find(route.ID));
    String net;
    if(IsBaseStaticRoute(recent))
        net = recent.Net;
    DelRoute(recent);
    if(!net.empty())
        RefreshRecursion(net);
    LOGGER_NOTICE("Del static route item done.");
}

void StaticRouteControl::SetDescription(StaticRouteItem& route)
{
    EnsureRouteID(route);
    StaticRouteItem recent(Find(route.ID));
    recent.Description = route.Description;
    LOGGER_NOTICE("Set static route description done.");
}

void StaticRouteControl::Get(GetListItem<StaticRouteItem>& get)
{
    int start, end;
    get.Ready(Holder.GetCount(), start, end);
    for(int i = start; i < end; ++i)
    {
        StaticRouteItem route(get.Result.Append());
        StaticRouteItem recent(Find(i));
        RouteControl::Get(route, recent);
        route.Net = recent.Net;
        route.Metric = recent.Metric;
    }
}

namespace StaticRoute
{
#define EXECUTE_RPC(methodname,methodfunc)\
    void LINE_NAME(methodfunc)(Value& params, Value& result)\
    {\
        StaticRouteControl control;\
        StaticRouteItem route(params);\
        control.methodfunc(route);\
        (bool&)result = true;\
    }\
    DECLARE_RPC_METHOD(methodname,LINE_NAME(methodfunc),true,true)

    EXECUTE_RPC(FuncStaticRouteAdd, Add);
    EXECUTE_RPC(FuncStaticRouteDelete, Del);
    EXECUTE_RPC(FuncStaticRouteSet, Set);
    EXECUTE_RPC(FuncStaticRouteDescription, SetDescription);

    void ExecuteStaticRouteGetCount(Value& params, Value& result)
    {
        StaticRouteControl control;
        IntValue res(result);
        control.GetCount(res);
    }

    DECLARE_RPC_METHOD(FuncStaticRouteGetCount, ExecuteStaticRouteGetCount, true, true);

    void ExecuteStaticRouteGet(Value& params, Value& result)
    {
        StaticRouteControl control;
        GetListItem<StaticRouteItem> get(params);
        control.Get(get);
        result = get.Result.Data;
    }

    DECLARE_RPC_METHOD(FuncStaticRouteGet, ExecuteStaticRouteGet, true, true);

    void StaticRouteBeforeImport(Value& data, bool reload)
    {
        StaticRouteControl control;
        while(control.GetCount())
        {
            Value temp;
            StaticRouteItem route(temp);
            route.ID = control.GetCount() - 1;
            control.Del(route);
        }
    }

    void StaticRouteImport(Value& data, bool reload)
    {
        List<StaticRouteItem> list(data["StaticRoute"]);
        StaticRouteControl control;
        ENUM_LIST(StaticRouteItem, list, e)
        {
            StaticRouteItem route(*e);
            if(reload)
                NO_ERROR(control.Add(route));
            else
                control.Add(route);
        }
    }

    void StaticRouteExport(Value& data)
    {
        List<Model> result(data["StaticRoute"]);
        StaticRouteControl control;
        Value temp;
        GetListItem<StaticRouteItem> list(temp);
        list.All = true;
        control.Get(list);
        ENUM_LIST(StaticRouteItem, list.Result, e)
        {
            StaticRouteItem route(*e);
            route.ID.Data.clear();
            route.Status.Data.clear();
            ENUM_LIST(RouteItem::GateItem, route.Gates, g)
            {
                RouteItem::GateItem gate(*g);
                gate.Status.Data.clear();
            }
            result.Append().Data = route.Data;
        }
    }

    DECLARE_SERIALIZE(StaticRouteBeforeImport, StaticRouteImport, NULL, StaticRouteExport, 3);

};
