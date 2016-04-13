#include <iostream>

#include "model/hvs.h"

#include "control.h"

using namespace std;

namespace Http
{
    typedef HttpItem::GroupItem GroupItem;
    typedef HttpItem::ServiceItem ServiceItem;
    typedef GroupItem::ServerItem ServerItem;
    typedef ServiceItem::LocationItem LocationItem;
    typedef GroupItem::MethodState MethodState;
    typedef ServerItem::TypeState TypeState;

    void HttpGet(Control& control)
    {
        control.MustMatchOp("http");
        if(!control.IsEnd() && !control.MatchOp("get"))
            control.NotMatch();
        CommandModel cmd;
        cmd.FuncName = FuncHttpGet;
        cmd.Password = true;
        Value res;
        Rpc::Call(cmd, res);
        HttpItem http(res);
        cout << "Enabled: " << (http.Enabled ? "yes" : "no") << endl;
        cout << "Processor: " << http.Processor << endl;
        cout << "Connections: " << http.Connections << endl;
        cout << "Keepalive: " << http.Keepalive << "s" << endl;
        cout << "Gzip: " << (http.Gzip ? "yes" : "no") << endl;
        cout << "GzipLength: " << http.GzipLength << endl;
        cout << "CacheInactive: " << http.CacheInactive << "m" << endl;
        cout << "DenyNotMatch: " << (http.DenyNotMatch ? "on" : "off") << endl;
        cout << "Status: " << (http.Status ? "ok" : "error") << endl;
    }

    DECLARE_COMMAND(HttpGet);

    void HttpSet(Control& control)
    {
        control.MustMatchOp("http");
        CommandModel cmd;
        cmd.FuncName = FuncHttpSet;
        cmd.Password = true;
        HttpItem http(cmd.Params);
        if(control.MatchOp("keepalive"))
        {
            int temp;
            control.MustMatchValue(temp);
            http.Keepalive = temp;
        } else if(control.MatchOp("connections"))
        {
            int temp;
            control.MustMatchValue(temp);
            http.Connections = temp;
        } else if(control.MatchOp("processor"))
        {
            int temp;
            control.MustMatchValue(temp);
            http.Processor = temp;
        } else if(control.MatchOp("enabled"))
        {
            http.Enabled = true;
        } else if(control.MatchOp("disabled"))
        {
            http.Enabled = false;
        } else if(control.MatchOp("gzip"))
        {
            if(control.MatchOp("on"))
                http.Gzip = true;
            else if(control.MatchOp("off"))
                http.Gzip = false;
            else if(control.MatchOp("length")) {
                int temp;
                control.MustMatchValue(temp);
                http.GzipLength = temp;
            } else
                control.NotMatch();
        } else if(control.MatchOp("cacheinactive"))
        {
            int temp;
            control.MustMatchValue(temp);
            http.CacheInactive = temp;
        } else if(control.MatchOp("denynotmatch"))
        {
            if(control.MatchOp("on"))
                http.DenyNotMatch = true;
            else if(control.MatchOp("off"))
                http.DenyNotMatch = false;
            else
                control.NotMatch();
        } else
            control.NotMatch();
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(HttpSet);

    void HttpServiceAdd(Control& control)
    {
        control.MustMatchOp("http");
        control.MustMatchOp("service");
        control.MustMatchOp("add");
        CommandModel cmd;
        cmd.FuncName = FuncHttpServiceAdd;
        cmd.Password = true;
        ServiceItem service(cmd.Params);
        String temp;
        control.MustMatchOp("name");
        control.MustMatchValue(temp);
        service.Name = temp;
        control.MustMatchOp("ip");
        control.MustMatchValue(temp);
        service.IP = temp;
        control.MustMatchOp("dev");
        control.MustMatchValue(temp);
        service.Dev = temp;
        control.MustMatchOp("port");
        int port;
        control.MustMatchValue(port);
        service.Port = port;
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(HttpServiceAdd);

    void HttpServiceDel(Control& control)
    {
        control.MustMatchOp("http");
        control.MustMatchOp("service");
        control.MustMatchOp("del");
        CommandModel cmd;
        cmd.FuncName = FuncHttpServiceDel;
        cmd.Password = true;
        ServiceItem service(cmd.Params);
        int id;
        control.MustMatchValue(id);
        service.ID = id;
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(HttpServiceDel);

    void HttpServiceMatch(Control& control)
    {
        control.MustMatchOp("http");
        control.MustMatchOp("service");
    }

    void HttpServiceMatchID(Control& control, ServiceItem& service)
    {
        HttpServiceMatch(control);
        int id;
        control.MustMatchValue(id);
        service.ID = id;
    }

    void HttpServiceSet(Control& control)
    {
        CommandModel cmd;
        cmd.FuncName = FuncHttpServiceSet;
        cmd.Password = true;
        ServiceItem service(cmd.Params);
        HttpServiceMatchID(control, service);
        if(control.MatchOp("name"))
        {
            String temp;
            control.MustMatchValue(temp);
            service.Name = temp;
        } else if(control.MatchOp("ip"))
        {
            String temp;
            control.MustMatchValue(temp);
            service.IP = temp;
        } else if(control.MatchOp("dev"))
        {
            String temp;
            control.MustMatchValue(temp);
            service.Dev = temp;
        } else if(control.MatchOp("port"))
        {
            int temp;
            control.MustMatchValue(temp);
            service.Port = temp;
        } else if(control.MatchOp("ssl"))
        {
            if(control.MatchOp("on"))
                service.Ssl = true;
            else if(control.MatchOp("off"))
                service.Ssl = false;
            else
                control.NotMatch();
        } else if(control.MatchOp("timeout"))
        {
            int temp;
            control.MustMatchValue(temp);
            service.SslTimeout = temp;
        } else if(control.MatchOp("ha"))
        {
            if(control.MatchOp("local"))
                service.HA = HAState::Local;
            else if(control.MatchOp("self"))
                service.HA = HAState::Self;
            else if(control.MatchOp("other"))
                service.HA = HAState::Other;
            else
                control.NotMatch();
        } else if(control.MatchOp("cookie"))
        {
            if(control.MatchOp("enabled"))
                service.CookieEnabled = true;
            else if(control.MatchOp("disabled"))
                service.CookieEnabled = false;
            else if(control.MatchOp("timeout"))
            {
                int temp;
                control.MustMatchValue(temp);
                service.CookieExpire = temp;
            } else if(control.MatchOp("name"))
            {
                String temp;
                control.MustMatchValue(temp);
                service.CookieName = temp;
            } else
                control.NotMatch();
        } else
            control.NotMatch();
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(HttpServiceSet);

    void HttpServiceShow(ServiceItem& service)
    {
        cout << "ID: " << service.ID << " Name: " << service.Name << endl;
        cout << "IP: " << service.IP << " Port: " << service.Port << " Dev: " << service.Dev << (service.Status ? "" : " (invalid) ") << endl;
        cout << "Ssl: " << (service.Ssl ? "enabled" : "disabled") << " SslTimeout: " << service.SslTimeout << endl;
        if(service.CookieEnabled)
            cout << "Cookie: " << service.CookieName << " Timeout: " << service.CookieExpire << "s" << endl;
        ENUM_LIST(LocationItem, service.LocationList, e)
        {
            LocationItem location(*e);
            cout << "  Location: " << location.ID << " Match:" << location.Match << " Group: " << location.Group << " CacheValid: " << location.CacheValid << endl;
        }
    }

    void HttpServiceGet(Control& control)
    {
        CommandModel cmd;
        cmd.FuncName = FuncHttpServiceGet;
        cmd.Password = true;
        ServiceItem request(cmd.Params);
        HttpServiceMatchID(control, request);
        if(!control.IsEnd() && !control.MatchOp("get"))
            control.NotMatch();
        Value res;
        Rpc::Call(cmd, res);
        ServiceItem service(res);
        HttpServiceShow(service);
    }

    DECLARE_COMMAND(HttpServiceGet);

    void HttpServiceList(Control& control)
    {
        HttpServiceMatch(control);
        if(!control.IsEnd() && !control.MatchOp("list"))
            control.NotMatch();
        CommandModel cmd;
        cmd.FuncName = FuncHttpServiceList;
        cmd.Password = true;
        Value res;
        Rpc::Call(cmd, res);
        List<ServiceItem> list(res);
        ENUM_LIST(ServiceItem, list, e)
        {
            cout << "-------------------------------------" << endl;
            HttpServiceShow(*e);
        }
    }

    DECLARE_COMMAND(HttpServiceList);

    void HttpLocationMatch(Control& control, LocationItem& location)
    {
        HttpServiceMatch(control);
        int id;
        control.MustMatchValue(id);
        location.ServiceID = id;
        control.MustMatchOp("location");
    }

    void HttpLocationAdd(Control& control)
    {
        CommandModel cmd;
        cmd.FuncName = FuncHttpLocationAdd;
        cmd.Password = true;
        LocationItem location(cmd.Params);
        HttpLocationMatch(control, location);
        if(control.MatchOp("insert"))
        {
            int id;
            control.MustMatchValue(id);
            location.ID = id;
        } else if(control.MatchOp("add"))
        {
        } else
            control.NotMatch();
        control.MustMatchOp("match");
        String temp;
        control.MustMatchValue(temp);
        location.Match = temp;
        control.MustMatchOp("group");
        control.MustMatchValue(temp);
        location.Group = temp;
	int time;
	control.MustMatchOp("cachevalid");
        control.MustMatchValue(time);
        location.CacheValid = time;
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(HttpLocationAdd);

    void HttpLocationDel(Control& control)
    {
        CommandModel cmd;
        cmd.FuncName = FuncHttpLocationDel;
        cmd.Password = true;
        LocationItem location(cmd.Params);
        HttpLocationMatch(control, location);
        control.MustMatchOp("del");
        int id;
        control.MustMatchValue(id);
        location.ID = id;
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(HttpLocationDel);

    void HttpGroupMatch(Control& control)
    {
        control.MustMatchOp("http");
        control.MustMatchOp("group");
    }

    void HttpGroupMatchMethod(Control& control, GroupItem& group)
    {
        if(control.MatchOp("rr"))
            group.Method = MethodState::RR;
        else if(control.MatchOp("wrr"))
            group.Method = MethodState::WRR;
        else if(control.MatchOp("iphash"))
            group.Method = MethodState::IPHash;
        else if(control.MatchOp("fair"))
            group.Method = MethodState::Fair;
        else if(control.MatchOp("urlhash"))
            group.Method = MethodState::URLHash;
        else if(control.MatchOp("cookie"))
            group.Method = MethodState::Cookie;
        else if(control.MatchOp("cookie-tomcat"))
            group.Method = MethodState::CookieTomcat;
        else if(control.MatchOp("cookie-resin"))
            group.Method = MethodState::CookieResin;
        else if(control.MatchOp("proxy-iphash"))
            group.Method = MethodState::ProxyIPHash;
        else
            control.NotMatch();
    }

    void HttpGroupAdd(Control& control)
    {
        HttpGroupMatch(control);
        CommandModel cmd;
        cmd.FuncName = FuncHttpGroupAdd;
        cmd.Password = true;
        GroupItem group(cmd.Params);
        control.MustMatchOp("add");
        control.MustMatchOp("name");
        String temp;
        control.MustMatchValue(temp);
        group.Name = temp;
        control.MustMatchOp("method");
        HttpGroupMatchMethod(control, group);
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(HttpGroupAdd);

    void HttpGroupDel(Control& control)
    {
        HttpGroupMatch(control);
        control.MustMatchOp("del");
        CommandModel cmd;
        cmd.FuncName = FuncHttpGroupDel;
        cmd.Password = true;
        GroupItem group(cmd.Params);
        int id;
        control.MustMatchValue(id);
        group.ID = id;
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(HttpGroupDel);

    void HttpGroupSet(Control& control)
    {
        HttpGroupMatch(control);
        CommandModel cmd;
        cmd.FuncName = FuncHttpGroupSet;
        cmd.Password = true;
        GroupItem group(cmd.Params);
        int id;
        control.MustMatchValue(id);
        group.ID = id;
        if(control.MatchOp("name"))
        {
            String temp;
            control.MustMatchValue(temp);
            group.Name = temp;
        } else if(control.MatchOp("method"))
        {
            HttpGroupMatchMethod(control, group);
        } else
            control.NotMatch();
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(HttpGroupSet);

    void HttpGroupShow(GroupItem& group)
    {
        cout << "ID: " << group.ID << " Name: " << group.Name << endl;
        cout << "Method: " << group.Method.Str() << endl;
        ENUM_LIST(ServerItem, group.ServerList, e)
        {
            ServerItem server(*e);
            cout << " ServerID: " << server.ID << " " << server.IP << ":" << server.Port << " Weight: " << server.Weight << " "
                 << server.Type.Str() << " Fail: " << server.MaxFails << " Timeout: " << server.FailTimeout << "s" << " SrunID: " << server.SrunID << endl;
        }
    }

    void HttpGroupGet(Control& control)
    {
        HttpGroupMatch(control);
        CommandModel cmd;
        cmd.FuncName = FuncHttpGroupGet;
        cmd.Password = true;
        GroupItem request(cmd.Params);
        int id;
        control.MustMatchValue(id);
        request.ID = id;
        if(!control.IsEnd() && !control.MatchOp("get"))
            control.NotMatch();
        Value result;
        Rpc::Call(cmd, result);
        GroupItem group(result);
        HttpGroupShow(group);
    }

    DECLARE_COMMAND(HttpGroupGet);

    void HttpGroupList(Control& control)
    {
        HttpGroupMatch(control);
        CommandModel cmd;
        cmd.FuncName = FuncHttpGroupList;
        cmd.Password = true;
        if(!control.IsEnd() && !control.MatchOp("list"))
            control.NotMatch();
        Value result;
        Rpc::Call(cmd, result);
        List<GroupItem> list(result);
        ENUM_LIST(GroupItem, list, e)
        {
            cout << "---------------------------" << endl;
            HttpGroupShow(*e);
        }
    }

    DECLARE_COMMAND(HttpGroupList);

    void HttpServerMatch(Control& control, ServerItem& server)
    {
        HttpGroupMatch(control);
        int id;
        control.MustMatchValue(id);
        server.GroupID = id;
        control.MustMatchOp("server");
    }

    void HttpServerAdd(Control& control)
    {
        CommandModel cmd;
        cmd.FuncName = FuncHttpServerAdd;
        cmd.Password = true;
        ServerItem server(cmd.Params);
        HttpServerMatch(control, server);
        control.MustMatchOp("add");
        control.MustMatchOp("ip");
        String temp;
        control.MustMatchValue(temp);
        server.IP = temp;
        int itemp;
        control.MustMatchOp("port");
        control.MustMatchValue(itemp);
        server.Port = itemp;
        server.Weight = 1;
        server.FailTimeout = 10;
        server.Type = TypeState::Normal;
        server.MaxFails = 5;
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(HttpServerAdd);

    void HttpServerDel(Control& control)
    {
        CommandModel cmd;
        cmd.FuncName = FuncHttpServerDel;
        cmd.Password = true;
        ServerItem server(cmd.Params);
        HttpServerMatch(control, server);
        control.MustMatchOp("del");
        int id;
        control.MustMatchValue(id);
        server.ID = id;
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(HttpServerDel);

    void HttpServerSet(Control& control)
    {
        CommandModel cmd;
        cmd.FuncName = FuncHttpServerSet;
        cmd.Password = true;
        ServerItem server(cmd.Params);
        HttpServerMatch(control, server);
        int itemp;
        String temp;
        control.MustMatchValue(itemp);
        server.ID = itemp;
        if(control.MatchOp("ip"))
        {
            control.MustMatchValue(temp);
            server.IP = temp;
        } else if(control.MatchOp("port"))
        {
            control.MustMatchValue(itemp);
            server.Port = itemp;
        } else if(control.MatchOp("weight"))
        {
            control.MustMatchValue(itemp);
            server.Weight = itemp;
        } else if(control.MatchOp("type"))
        {
            if(control.MatchOp("backup"))
                server.Type = TypeState::Backup;
            else if(control.MatchOp("down"))
                server.Type = TypeState::Down;
            else if(control.MatchOp("normal"))
                server.Type = TypeState::Normal;
            else
                control.NotMatch();
        } else if(control.MatchOp("fail"))
        {
            control.MustMatchValue(itemp);
            server.MaxFails = itemp;
        } else if(control.MatchOp("timeout"))
        {
            control.MustMatchValue(itemp);
            server.FailTimeout = itemp;
        } else if(control.MatchOp("srunid"))
        {
            control.MustMatchValue(temp);
            server.SrunID = temp;
        } else
            control.NotMatch();
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(HttpServerSet);
}
