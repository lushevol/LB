#include <iostream>

#include "model/route.h"

#include "control.h"
#include "route.h"
#include "tools.h"

using namespace std;

namespace StaticRoute
{

    typedef StaticRouteItem::GateItem GateItem;

    void Show(StaticRouteItem& route)
    {
        cout << route.ID << ": " << route.Net;
        if(route.Metric != 0)
            cout << " metric " << route.Metric;
        cout << " " << route.GatePolicy.Str();
        if(!route.Status)
            cout << " (invalid)";
        cout << "   " << route.Description << endl;
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

    void MatchStaticRoute(Control& control)
    {
        control.MustMatchOp("route");
        control.MustMatchOp("static");
    }

    void ListAll(Control& control)
    {
        MatchStaticRoute(control);
        if(!control.IsEnd() && !control.MatchOp("list"))
            control.NotMatch();
        CommandModel cmd;
        cmd.FuncName = FuncStaticRouteGet;
        cmd.Password = true;
        GetListItem<StaticRouteItem> request(cmd.Params);
        request.All = true;
        Value res;
        Rpc::Call(cmd, res);
        ENUM_LIST(StaticRouteItem, List<StaticRouteItem>(res), e)
        {
            StaticRouteItem route(*e);
            Show(route);
        }
    }

    DECLARE_COMMAND(ListAll);

    StringCollection keywords;

    void EnsureKeywords()
    {
        if(keywords.empty())
        {
            keywords.insert("add");
            keywords.insert("del");
            keywords.insert("id");
            keywords.insert("list");
        }
    }

    void MatchIndex(CommandModel& cmd, Control& control)
    {
        StaticRouteItem route(cmd.Params);
        if(control.MatchOp("id"))
        {
            int id;
            control.MustMatchValue(id);
            route.ID = id;
        } else {
            String temp;
            control.MustMatchValue(temp);
            EnsureKeywords();
            if(keywords.count(temp))
                control.NotMatch();
            route.Net = temp;
            if(control.MatchOp("metric"))
            {
                int metric;
                control.MustMatchValue(metric);
                route.Metric = metric;
            }
        }
    }

    void Description(Control& control)
    {
        MatchStaticRoute(control);
        CommandModel cmd;
        MatchIndex(cmd, control);
        control.MustMatchOp("description");
        String description;
        control.MustMatchValue(description);
        cmd.FuncName = FuncStaticRouteDescription;
        cmd.Password = true;
        StaticRouteItem route(cmd.Params);
        route.Description = description;
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(Description);

    void Del(Control& control)
    {
        MatchStaticRoute(control);
        control.MustMatchOp("del");
        CommandModel cmd;
        MatchIndex(cmd, control);
        cmd.FuncName = FuncStaticRouteDelete;
        cmd.Password = true;
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(Del);

    void Add(Control& control)
    {
        MatchStaticRoute(control);
        control.MustMatchOp("add");
        String net;
        control.MustMatchValue(net);
        CommandModel cmd;
        cmd.FuncName = FuncStaticRouteAdd;
        cmd.Password = true;
        StaticRouteItem route(cmd.Params);
        route.Net = net;
        if(control.MatchOp("metric"))
        {
            int metric;
            control.MustMatchValue(metric);
            route.Metric = metric;
        }
        Route::MatchGate(route, control);
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(Add);

    void Set(Control& control)
    {
        MatchStaticRoute(control);
        CommandModel cmd;
        MatchIndex(cmd, control);
        cmd.FuncName = FuncStaticRouteSet;
        cmd.Password = true;
        StaticRouteItem route(cmd.Params);
        Route::MatchGate(route, control);
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(Set);

};
