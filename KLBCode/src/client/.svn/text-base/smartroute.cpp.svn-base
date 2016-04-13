#include <iostream>
#include <sstream>

#include "model/route.h"

#include "route.h"
#include "control.h"
#include "tools.h"

using namespace std;

namespace Smart
{
    typedef SmartRouteItem::GateItem GateItem;
    typedef map<int, String> NameMap;

    void Show(SmartRouteItem& route, NameMap& list)
    {
        cout << route.ID << ": " << route.Description;
        cout << " " << route.GatePolicy.Str();
        if(!route.Status)
            cout << " (invalid)";
        cout << "   " << route.Description << endl;
        cout << "  ISP: " << list[route.ISP] << endl;
        cout << "  Link check: " << route.Mode.Str();
        if(route.Mode == 0)
            cout << endl;
        else
        {
            cout << " " << route.Ip;;
            if(route.Mode != 1)
                cout << " port=" << route.Port;
            cout << " frequency=" << route.Frequency << " timeout=" << route.Timeout << endl;
        }
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

    void MatchSmartRoute(Control& control)
    {
        control.MustMatchOp("route");
        control.MustMatchOp("smart");
    }

    void ListAll(Control& control)
    {
        MatchSmartRoute(control);
        if(!control.IsEnd() && !control.MatchOp("list"))
            control.NotMatch();
        CommandModel cmd;
        cmd.FuncName = FuncSmartRouteGet;
        cmd.Password = true;
        GetListItem<SmartRouteItem> request(cmd.Params);
        request.All = true;
        Value res;
        Rpc::Call(cmd, res);
        NameMap list;
        {
            CommandModel request;
            request.FuncName = FuncISPGetListName;
            request.Password = true;
            List<ISPItem>(request.Params);
            Value result;
            Rpc::Call(request, result);
            List<ISPItem> reslist(result);
            ENUM_LIST(ISPItem, reslist, r)
            {
                list[r->ID] = r->Name;
            }
            list[0] = "(Default)";
        }
        ENUM_LIST(SmartRouteItem, List<SmartRouteItem>(res), e)
        {
            SmartRouteItem route(*e);
            Show(route, list);
        }
    }

    DECLARE_COMMAND(ListAll);

    void Description(Control& control)
    {
        MatchSmartRoute(control);
        int id;
        control.MustMatchValue(id);
        control.MustMatchOp("description");
        String description;
        control.MustMatchValue(description);
        CommandModel cmd;
        cmd.FuncName = FuncSmartRouteDescription;
        cmd.Password = true;
        SmartRouteItem route(cmd.Params);
        route.ID = id;
        route.Description = description;
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(Description);

    void Check(Control& control)
    {
        MatchSmartRoute(control);
        CommandModel cmd;
        cmd.FuncName = FuncSmartRouteCheck;
        cmd.Password = true;
        SmartRouteItem route(cmd.Params);
        int id;
        control.MustMatchValue(id);
        route.ID = id;
        control.MustMatchOp("check");
        if(control.MatchOp("disable"))
            route.Mode = SmartRouteItem::CheckModeState::disable;
        else
        {
            if(control.MatchOp("ping"))
            {
                route.Mode = SmartRouteItem::CheckModeState::ping;
                String ip;
                control.MustMatchValue(ip);
                route.Ip = ip;
            }
            else if(control.MatchOp("tcp"))
            {
                route.Mode = SmartRouteItem::CheckModeState::tcp;
                String ip;
                control.MustMatchValue(ip);
                route.Ip = ip;
                control.MustMatchOp("port");
                int port;
                control.MustMatchValue(port);
                route.Port = port;
            }
            else if(control.MatchOp("udp"))
            {
                route.Mode = SmartRouteItem::CheckModeState::udp;
                String ip;
                control.MustMatchValue(ip);
                route.Ip = ip;
                control.MustMatchOp("port");
                int port;
                control.MustMatchValue(port);
                route.Port = port;
            }
            else
                control.NotMatch();
            if(control.MatchOp("frequency"))
            {
                int frequency;
                control.MustMatchValue(frequency);
                route.Frequency = frequency;
            } else
                route.Frequency = route.Frequency.Default;
            if(control.MatchOp("timeout"))
            {
                int timeout;
                control.MustMatchValue(timeout);
                route.Timeout = timeout;
            } else
                route.Timeout = route.Timeout.Default;
        }
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(Check);

    void Del(Control& control)
    {
        MatchSmartRoute(control);
        control.MustMatchOp("del");
        int id;
        control.MustMatchValue(id);
        CommandModel cmd;
        cmd.FuncName = FuncSmartRouteDelete;
        cmd.Password = true;
        SmartRouteItem route(cmd.Params);
        route.ID = id;
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(Del);

    void Add(Control& control)
    {
        MatchSmartRoute(control);
        control.MustMatchOp("add");
        CommandModel cmd;
        cmd.FuncName = FuncSmartRouteAdd;
        cmd.Password = true;
        SmartRouteItem route(cmd.Params);
        String isp;
        if(control.MatchOp("isp"))
            control.MustMatchValue(isp);
        else
            route.ISP = 0;
        Route::MatchGate(route, control);
        if(!isp.empty())
        {
            CommandModel request;
            request.FuncName = FuncISPGetListName;
            request.Password = true;
            List<ISPItem>(request.Params);
            Value result;
            Rpc::Call(request, result);
            List<ISPItem> reslist(result);
            ENUM_LIST(ISPItem, reslist, r)
            {
                if(r->Name == isp)
                    route.ISP = r->ID;
            }
            if(!route.ISP.Valid())
                ERROR(Exception::Command::ISPName, isp);
        }
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(Add);

    void SetGate(Control& control)
    {
        MatchSmartRoute(control);
        int id;
        control.MustMatchValue(id);
        CommandModel cmd;
        cmd.FuncName = FuncSmartRouteSet;
        cmd.Password = true;
        SmartRouteItem route(cmd.Params);
        route.ID = id;
        Route::MatchGate(route, control);
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(SetGate);

    void SetISP(Control& control)
    {
        MatchSmartRoute(control);
        int id;
        control.MustMatchValue(id);
        CommandModel cmd;
        cmd.FuncName = FuncSmartRouteSet;
        cmd.Password = true;
        SmartRouteItem route(cmd.Params);
        route.ID = id;
        String isp;
        if(control.MatchOp("isp"))
        {
            control.MustMatchValue(isp);
        } else if(!control.MatchOp("noisp"))
            control.NotMatch();
        if(!isp.empty())
        {
            CommandModel request;
            request.FuncName = FuncISPGetListName;
            request.Password = true;
            List<ISPItem>(request.Params);
            Value result;
            Rpc::Call(request, result);
            List<ISPItem> reslist(result);
            ENUM_LIST(ISPItem, reslist, r)
            {
                if(r->Name == isp)
                    route.ISP = r->ID;
            }
            if(!route.ISP.Valid())
                ERROR(Exception::Command::ISPName, isp);
        } else
            route.ISP = 0;
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(SetISP);
}
