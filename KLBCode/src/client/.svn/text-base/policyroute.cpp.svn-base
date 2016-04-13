#include <iostream>
#include <sstream>

#include "model/route.h"

#include "route.h"
#include "control.h"
#include "tools.h"

using namespace std;

namespace Policy
{

    typedef PolicyRouteItem::GateItem GateItem;
    typedef PolicyRouteItem::RuleItem RuleItem;

    void Show(PolicyRouteItem& route)
    {
        cout << route.ID << ": " << route.Description;
        cout << " " << route.GatePolicy.Str();
        if(!route.Status)
            cout << " (invalid)";
        cout << "   " << route.Description << endl;
        if(route.Rules.GetCount())
        {
            ENUM_LIST(RuleItem, route.Rules, e)
            {
                RuleItem rule(*e);
                cout << "  " << rule.ID << ": ";
                ParseTools::ShowAny(rule.ProtocolStr, rule.Protocol);
                cout << '\t';
                if(rule.SrcNet != "")
                    cout << rule.SrcNet;
                else
                    cout << "any";
                if(rule.Protocol && rule.SrcPort != 0)
                {
                    cout << ":";
                    ParseTools::ShowAny(rule.SrcPortStr, rule.SrcPort);
                }
                cout << "\t-->\t";
                if(rule.DestNet != "")
                    cout << rule.DestNet;
                else
                    cout << "any";
                if(rule.Protocol && rule.DestPort != 0)
                {
                    cout << ":";
                    ParseTools::ShowAny(rule.DestPortStr, rule.DestPort);
                }
                cout << endl;
            }
        } else
            cout << "  No rules" << endl;
        if(route.Gates.GetCount())
        {
            cout << "  Gate:";
            ENUM_LIST(GateItem, route.Gates, e)
            {
                GateItem gate(*e);
                cout << "\t";
                if(gate.IP != "" || gate.Auto)
                {
                    cout << "via ";
                    if(gate.Auto)
                    {
                        cout << "auto";
                        if(gate.IP != "")
                            cout << "(" << gate.IP << ")";
                    } else
                        cout << gate.IP;
                    cout << " ";
                }
                cout << "dev " << gate.Dev << " ";
                cout << "weight " << gate.Weight;
                if(!gate.Status)
                    cout << " (invalid)";
                cout << endl;
            }
        }
    }

    void MatchPolicyRoute(Control& control)
    {
        control.MustMatchOp("route");
        control.MustMatchOp("policy");
    }

    void ListAll(Control& control)
    {
        MatchPolicyRoute(control);
        if(!control.IsEnd() && !control.MatchOp("list"))
            control.NotMatch();
        CommandModel cmd;
        cmd.FuncName = FuncPolicyRouteGet;
        cmd.Password = true;
        GetListItem<PolicyRouteItem> request(cmd.Params);
        request.All = true;
        Value res;
        Rpc::Call(cmd, res);
        ENUM_LIST(PolicyRouteItem, List<PolicyRouteItem>(res), e)
        {
            PolicyRouteItem route(*e);
            Show(route);
        }
    }

    DECLARE_COMMAND(ListAll);

    void Description(Control& control)
    {
        MatchPolicyRoute(control);
        int id;
        control.MustMatchValue(id);
        control.MustMatchOp("description");
        String description;
        control.MustMatchValue(description);
        CommandModel cmd;
        cmd.FuncName = FuncPolicyRouteDescription;
        cmd.Password = true;
        PolicyRouteItem route(cmd.Params);
        route.ID = id;
        route.Description = description;
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(Description);

    void Del(Control& control)
    {
        MatchPolicyRoute(control);
        control.MustMatchOp("del");
        int id;
        control.MustMatchValue(id);
        CommandModel cmd;
        cmd.FuncName = FuncPolicyRouteDelete;
        cmd.Password = true;
        PolicyRouteItem route(cmd.Params);
        route.ID = id;
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(Del);

    void Add(Control& control)
    {
        MatchPolicyRoute(control);
        control.MustMatchOp("add");
        CommandModel cmd;
        cmd.FuncName = FuncPolicyRouteAdd;
        cmd.Password = true;
        PolicyRouteItem route(cmd.Params);
        Route::MatchGate(route, control);
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(Add);

    void Insert(Control& control)
    {
        MatchPolicyRoute(control);
        control.MustMatchOp("insert");
        int id;
        control.MustMatchValue(id);
        CommandModel cmd;
        cmd.FuncName = FuncPolicyRouteInsert;
        cmd.Password = true;
        PolicyRouteItem route(cmd.Params);
        route.ID = id;
        Route::MatchGate(route, control);
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(Insert);

    void SetGate(Control& control)
    {
        MatchPolicyRoute(control);
        int id;
        control.MustMatchValue(id);
        CommandModel cmd;
        cmd.FuncName = FuncPolicyRouteSet;
        cmd.Password = true;
        PolicyRouteItem route(cmd.Params);
        route.ID = id;
        Route::MatchGate(route, control);
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(SetGate);

    void AddRule(Control& control)
    {
        MatchPolicyRoute(control);
        int id;
        control.MustMatchValue(id);
        control.MustMatchOp("rule");
        control.MustMatchOp("add");
        CommandModel cmd;
        cmd.FuncName = FuncPolicyRouteAddRule;
        cmd.Password = true;
        PolicyRouteItem route(cmd.Params);
        route.ID = id;
        RuleItem rule(route.Rules.Append());
        if(!control.MatchOp("all"))
            while(true)
            {
                if(control.MatchOp("from"))
                {
                    bool port = control.MatchOp("port");
                    if(!port)
                    {
                        String net;
                        control.MustMatchValue(net);
                        rule.SrcNet = net;
                        port = control.MatchOp("port");
                    }
                    if(port)
                    {
                        String port;
                        control.MustMatchValue(port);
                        rule.SrcPortStr = port;
                    }
                } else if(control.MatchOp("to"))
                {
                    bool port = control.MatchOp("port");
                    if(!port)
                    {
                        String net;
                        control.MustMatchValue(net);
                        rule.DestNet = net;
                        port = control.MatchOp("port");
                    }
                    if(port)
                    {
                        String port;
                        control.MustMatchValue(port);
                        rule.DestPortStr = port;
                    }
                } else if(control.MatchOp("protocol"))
                {
                    String temp;
                    control.MustMatchOpValue(temp);
                    rule.ProtocolStr = temp;
                } else if(!control.IsEnd())
                    control.NotMatch();
                else
                    break;
            }
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(AddRule);


    void DelRule(Control& control)
    {
        MatchPolicyRoute(control);
        int id;
        control.MustMatchValue(id);
        control.MustMatchOp("rule");
        control.MustMatchOp("del");
        CommandModel cmd;
        cmd.FuncName = FuncPolicyRouteDeleteRule;
        cmd.Password = true;
        PolicyRouteItem route(cmd.Params);
        route.ID = id;
        do
        {
            int ruleid;
            control.MustMatchValue(ruleid);
            RuleItem rule(route.Rules.Append());
            rule.ID = ruleid;
        } while(!control.IsEnd());
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(DelRule);
};
