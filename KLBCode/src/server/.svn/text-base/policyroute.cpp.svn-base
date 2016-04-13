#include <sstream>
#include <set>

#include "share/utility.h"

#include "rpc.h"
#include "serialize.h"
#include "base.h"
#include "logger.h"

#include "policyroute.h"
#include "network.h"

using namespace std;

void PolicyRouteControl::GenerateCMDTarget(OStream& cmd, RouteItem& route)
{
    cmd << "default table " << PolicyRouteItem(route.Data).Mark;
}

PolicyRouteControl::PolicyRouteControl()
    : RouteControl(Configure::GetValue()["PolicyRoute"])
    , Holder(RouteControl::Holder.Data)
{}

void PolicyRouteControl::AddMangle(int id, int mark)
{
    ASSERT(mark <= 0x0000FFFF);
    String markstr = IntToString(mark);
    Exec::System("ip rule add fwmark " + markstr + " table " + markstr);
    Exec::System("ip rule add fwmark " + IntToString(mark << 16) + " table " + markstr);
    Exec::System("iptables -t mangle -N " + markstr);
    Exec::System("iptables -t mangle -F " + markstr);
    Exec::System("iptables -t mangle -A " + markstr + " -j MARK --set-mark " + IntToString((mark << 16) + mark));
    Exec::System("iptables -t mangle -A " + markstr + " -j MARK --set-mark 0");
    Exec::System("iptables -t mangle -I PROUTE_MATCH " + IntToString(id + 1) + " -m mark --mark 0 -j " + markstr);
}

void PolicyRouteControl::DelMangle(int mark)
{
    ASSERT(mark <= 0x0000FFFF);
    String markstr = IntToString(mark);
    Exec::System("iptables -t mangle -D PROUTE_MATCH -m mark --mark 0 -j " + markstr);
    Exec::System("iptables -t mangle -F " + markstr);
    Exec::System("iptables -t mangle -X " + markstr);
    Exec::System("ip rule del fwmark " + markstr + " table " + markstr);
    Exec::System("ip rule del fwmark " + IntToString(mark << 16) + " table " + markstr);
}

void PolicyRouteControl::AddRule(int mark, RuleItem& rule)
{
    ostringstream stream;
    stream << "iptables -t mangle -I " << mark << " " << (rule.ID + 2);
    if(rule.SrcNet != "")
        stream << " -s " << rule.SrcNet;
    if(rule.DestNet != "")
        stream << " -d " << rule.DestNet;
    if(rule.Protocol != 0)
        stream << " -p " << rule.Protocol;
    if(rule.SrcPort != 0)
        stream << " --sport " << rule.SrcPort;
    if(rule.DestPort != 0)
        stream << " --dport " << rule.DestPort;
    stream << " -j RETURN";
    Exec::System(stream.str());
}

void PolicyRouteControl::DelRule(int mark, int id)
{
    Exec::System("iptables -t mangle -D " + IntToString(mark) + " " + IntToString(id + 2));
}

void PolicyRouteControl::GenerateMark(PolicyRouteItem& route)
{
    if(Holder.GetCount() == route.Mark.Max - route.Mark.Min + 1)
        ERROR(Exception::Route::Policy::Count, "");
    IntCollection markset;
    ENUM_LIST(PolicyRouteItem, Holder, e)
    {
        PolicyRouteItem item(*e);
        markset.insert(item.Mark);
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

void PolicyRouteControl::Add(PolicyRouteItem& route)
{
    route.ID = Holder.GetCount();
    Insert(route);
}

void PolicyRouteControl::Insert(PolicyRouteItem& route)
{
    if(!route.ID.Valid())
        ERROR(Exception::Server::Params, "ID");
    if(route.ID > Holder.GetCount())
        ERROR(Exception::Types::RangedList, route.ID);
    GenerateMark(route);
    AddRoute(route);
    PolicyRouteItem recent(Holder.Get(route.ID));
    recent.Mark = route.Mark;
    AddMangle(route.ID, route.Mark);
    recent.Rules.Clear();
    LOGGER_NOTICE("Add policy route item done.");
}

void PolicyRouteControl::Set(PolicyRouteItem& route)
{
    if(!route.ID.Valid())
        ERROR(Exception::Server::Params, "ID");
    PolicyRouteItem recent(Find(route.ID));
    SetRoute(route, recent);
    LOGGER_NOTICE("Set policy route gates done.");
}

void PolicyRouteControl::Del(PolicyRouteItem& route)
{
    if(!route.ID.Valid())
        ERROR(Exception::Server::Params, "ID");
    PolicyRouteItem recent(Find(route.ID));
    DelMangle(recent.Mark);
    DelRoute(recent);
    LOGGER_NOTICE("Del policy route item done.");
}

void PolicyRouteControl::AddRule(PolicyRouteItem& route)
{
    if(!route.ID.Valid())
        ERROR(Exception::Server::Params, "ID");
    PolicyRouteItem recent(Find(route.ID));
    ENUM_LIST(RuleItem, route.Rules, e)
    {
        RuleItem rule(*e);
        rule.SrcNet.Valid();
        rule.DestNet.Valid();
        rule.ProtocolStr.Valid();
        rule.Protocol.Valid();
        rule.SrcPortStr.Valid();
        rule.SrcPort.Valid();
        rule.DestPortStr.Valid();
        rule.DestPort.Valid();
        if(rule.ProtocolStr != "")
            rule.Protocol = Network::GetProtocol(rule.ProtocolStr);
        if(rule.SrcPortStr != "")
            rule.SrcPort = Network::GetServiceByProtocol(rule.Protocol, rule.SrcPortStr);
        if(rule.DestPortStr != "")
            rule.DestPort = Network::GetServiceByProtocol(rule.Protocol, rule.DestPortStr);
        if(!Network::IsSupportPort(rule.Protocol) && (rule.SrcPort != 0 || rule.DestPort != 0))
            ERROR(Exception::Network::SupportPort, rule.Protocol);
    }
    ENUM_LIST(RuleItem, route.Rules, e)
    {
        RuleItem target(*e);
        RuleItem rule(recent.Rules.Append());
        rule.ID = recent.Rules.GetCount() - 1;
        rule.SrcNet = target.SrcNet;
        rule.DestNet = target.DestNet;
        rule.SrcPort = target.SrcPort;
        rule.DestPort = target.DestPort;
        rule.SrcPortStr = target.SrcPortStr;
        rule.DestPortStr = target.DestPortStr;
        rule.Protocol = target.Protocol;
        rule.ProtocolStr = target.ProtocolStr;
        AddRule(recent.Mark, rule);
    }
    LOGGER_NOTICE("Add policy route rule done.");
}

void PolicyRouteControl::DelRule(PolicyRouteItem& route)
{
    if(!route.ID.Valid())
        ERROR(Exception::Server::Params, "ID");
    PolicyRouteItem recent(Find(route.ID));
    IntCollection list;
    ENUM_LIST(RuleItem, route.Rules, e)
    {
        RuleItem rule(*e);
        if(!rule.ID.Valid())
            ERROR(Exception::Server::Params, "ID");
        if(list.count(rule.ID) == 0)
        {
            recent.Rules.Get(rule.ID);
            list.insert(rule.ID);
        }
    }
    ENUM_STL_R(IntCollection, list, e)
    {
        DelRule(recent.Mark, *e);
        recent.Rules.Delete(*e);
    }
    int count = 0;
    ENUM_LIST(RuleItem, recent.Rules, e)
    {
        (*e).ID = count++;
    }
    LOGGER_NOTICE("Del policy route rule done.");
}

void PolicyRouteControl::SetDescription(PolicyRouteItem& route)
{
    if(!route.ID.Valid())
        ERROR(Exception::Server::Params, "ID");
    PolicyRouteItem recent(Find(route.ID));
    recent.Description = route.Description;
    LOGGER_NOTICE("Set policy route item description done.");
}

void PolicyRouteControl::Get(GetListItem<PolicyRouteItem>& get)
{
    int start, end;
    get.Ready(Holder.GetCount(), start, end);
    for(int i = start; i < end; ++i)
    {
        PolicyRouteItem route(get.Result.Append());
        PolicyRouteItem recent(Find(i));
        RouteControl::Get(route, recent);
        route.Rules = recent.Rules;
    }
}

namespace PolicyRoute
{

#define EXECUTE_RPC(methodname,methodfunc)\
    void LINE_NAME(methodfunc)(Value& params, Value& result)\
    {\
        RpcMethod::CheckLicence();\
        PolicyRouteControl control;\
        PolicyRouteItem route(params);\
        control.methodfunc(route);\
        (bool&)result = true;\
    }\
    DECLARE_RPC_METHOD(methodname,LINE_NAME(methodfunc),true,true)

    EXECUTE_RPC(FuncPolicyRouteAdd, Add);
    EXECUTE_RPC(FuncPolicyRouteInsert, Insert);
    EXECUTE_RPC(FuncPolicyRouteDelete, Del);
    EXECUTE_RPC(FuncPolicyRouteSet, Set);
    EXECUTE_RPC(FuncPolicyRouteAddRule, AddRule);
    EXECUTE_RPC(FuncPolicyRouteDeleteRule, DelRule);
    EXECUTE_RPC(FuncPolicyRouteDescription, SetDescription);

    void ExecutePolicyRouteGetCount(Value& params, Value& result)
    {
        RpcMethod::CheckLicence();
        PolicyRouteControl control;
        IntValue res(result);
        control.GetCount(res);
    }

    DECLARE_RPC_METHOD(FuncPolicyRouteGetCount, ExecutePolicyRouteGetCount, true, true);

    void ExecutePolicyRouteGet(Value& params, Value& result)
    {
        RpcMethod::CheckLicence();
        PolicyRouteControl control;
        GetListItem<PolicyRouteItem> get(params);
        control.Get(get);
        result = get.Result.Data;
    }

    DECLARE_RPC_METHOD(FuncPolicyRouteGet, ExecutePolicyRouteGet, true, true);

    void PolicyRouteBeforeImport(Value& data, bool reload)
    {
        PolicyRouteControl control;
        while(control.GetCount())
        {
            Value temp;
            PolicyRouteItem route(temp);
            route.ID = control.GetCount() - 1;
            control.Del(route);
        }
    }

    void PolicyRouteImport(Value& data, bool reload)
    {
        List<PolicyRouteItem> list(data["PolicyRoute"]);
        PolicyRouteControl control;
        ENUM_LIST(PolicyRouteItem, list, e)
        {
            PolicyRouteItem route(*e);
            if(reload)
            {
                try
                {
                    control.Add(route);
                    NO_ERROR(control.AddRule(route));
                } catch(ValueException&)
                {
                }
            } else {
                control.Add(route);
                control.AddRule(route);
            }
        }
    }

    void PolicyRouteExport(Value& data)
    {
        List<Model> result(data["PolicyRoute"]);
        PolicyRouteControl control;
        Value temp;
        GetListItem<PolicyRouteItem> list(temp);
        list.All = true;
        control.Get(list);
        ENUM_LIST(PolicyRouteItem, list.Result, e)
        {
            PolicyRouteItem route(*e);
            route.ID.Data.clear();
            route.Status.Data.clear();
            route.Mark.Data.clear();
            ENUM_LIST(RouteItem::GateItem, route.Gates, g)
            {
                RouteItem::GateItem gate(*g);
                gate.Status.Data.clear();
            }
            ENUM_LIST(PolicyRouteItem::RuleItem, route.Rules, r)
            {
                PolicyRouteItem::RuleItem rule(*r);
                rule.ID.Data.clear();
            }
            result.Append().Data = route.Data;
        }
    }

    DECLARE_SERIALIZE(PolicyRouteBeforeImport, PolicyRouteImport, NULL, PolicyRouteExport, 3);

    void InitPolicyRouteMangle()
    {
        Exec::System("iptables -t mangle -N PROUTE");
        Exec::System("iptables -t mangle -A PREROUTING -m mark --mark 0 -j PROUTE");
        Exec::System("iptables -t mangle -N PROUTE_MATCH");
        //HIGH
        Exec::System("iptables -t mangle -N PROUTE_MATCH_HIGH");
        Exec::System("iptables -t mangle -A PROUTE_MATCH_HIGH -j PROUTE_MATCH");
        Exec::System("iptables -t mangle -A PROUTE_MATCH_HIGH -m mark --mark 0 -j CONNMARK --set-mark 0x00010000/0xFFFF0000");
        Exec::System("iptables -t mangle -A PROUTE_MATCH_HIGH -m mark ! --mark 0 -j CONNMARK --save-mark --mask 0xFFFF0000");
        Exec::System("iptables -t mangle -A PROUTE_MATCH_HIGH -j MARK --set-mark 0");
        Exec::System("iptables -t mangle -N PROUTE_HIGH");
        Exec::System("iptables -t mangle -A PROUTE -m state --state ESTABLISHED_REPLY -j PROUTE_HIGH");
        Exec::System("iptables -t mangle -A PROUTE_HIGH -m connmark --mark 0/0xFFFF0000 -j PROUTE_MATCH_HIGH");
        Exec::System("iptables -t mangle -A PROUTE_HIGH -j CONNMARK --restore-mark --mask 0xFFFF0000");
        Exec::System("iptables -t mangle -A PROUTE_HIGH -m mark --mark 0x00010000 -j MARK --set-mark 0");
        //Low
        Exec::System("iptables -t mangle -N PROUTE_MATCH_LOW");
        Exec::System("iptables -t mangle -A PROUTE_MATCH_LOW -j PROUTE_MATCH");
        Exec::System("iptables -t mangle -A PROUTE_MATCH_LOW -m mark --mark 0 -j CONNMARK --set-mark 0x00000001/0x0000FFFF");
        Exec::System("iptables -t mangle -A PROUTE_MATCH_LOW -m mark ! --mark 0 -j CONNMARK --save-mark --mask 0x0000FFFF");
        Exec::System("iptables -t mangle -A PROUTE_MATCH_LOW -j MARK --set-mark 0");
        Exec::System("iptables -t mangle -N PROUTE_LOW");
        Exec::System("iptables -t mangle -A PROUTE -m state --state NEW,RELATED,ESTABLISHED_ORIG -j PROUTE_LOW");
        Exec::System("iptables -t mangle -A PROUTE_LOW -m connmark --mark 0/0x0000FFFF -j PROUTE_MATCH_LOW");
        Exec::System("iptables -t mangle -A PROUTE_LOW -j CONNMARK --restore-mark --mask 0x0000FFFF");
        Exec::System("iptables -t mangle -A PROUTE_LOW -m mark --mark 0x00000001 -j MARK --set-mark 0");
    }

    DECLARE_INIT(InitPolicyRouteMangle, NULL, 6);

};
