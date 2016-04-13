#include <iostream>
#include <sstream>

#include "model/ipvs.h"

#include "control.h"
#include "tools.h"

using namespace std;

namespace IPVS
{
    typedef VirtualServiceItem::ServerItem ServerItem;
    typedef VirtualServiceItem::IPItem IPItem;
    typedef VirtualServiceItem::ScheduleState ScheduleState;
    typedef VirtualServiceItem::PortItem PortItem;
    typedef VirtualServiceItem::ServerItem::ActionState ActionState;
    typedef VirtualServiceItem::MonitorItem::TypeState MonitorState;

    int GetServiceID(const String& name)
    {
        CommandModel cmd;
        cmd.FuncName = FuncVirtualServicesNameToID;
        cmd.Password = true;
        VirtualServiceItem(cmd.Params).Name = name;
        Value res;
        Rpc::Call(cmd, res);
        return VirtualServiceItem(res).ID;
    }

    void GetService(const String& name, Value& result)
    {
        CommandModel cmd;
        cmd.FuncName = FuncVirtualServicesGet;
        cmd.Password = true;
        GetListItem<VirtualServiceItem> request(cmd.Params);
        request.Start = GetServiceID(name);
        request.Count = 1;
        Value res;
        Rpc::Call(cmd, res);
        result.clear();
        ENUM_LIST(VirtualServiceItem, List<VirtualServiceItem>(res), e)
        {
            result = (*e).Data;
            break;
        }
    }

    void MatchService(Control& control)
    {
        control.MustMatchOp("vs");
    }

    void ShowPorts(List<PortItem>& list)
    {
        bool first = true;
        ENUM_LIST(PortItem, list, e)
        {
            if(first)
                first = false;
            else
                cout << ",";
            PortItem port(*e);
            cout << port.Min;
            if(port.Min < port.Max)
                cout << "-" << port.Max;
        }
    }

    void Show(VirtualServiceItem& item)
    {
        cout << item.ID << ": " << item.Name << " " << (item.Enabled ? "Enabled" : "Disabled");
        if(item.Enabled && item.HAStatus)
            cout << " Running";
        cout << " ";
        switch(item.HA)
        {
            case HAState::Self:
                cout << "Self";
                break;
            case HAState::Other:
                cout << "Other";
                break;
            default:
                cout << "Local";
        }
        cout << endl;
        if(item.Description != "")
            cout << item.Description << endl;
        if(!item.IP.IsEmpty())
        {
            cout << " IP:";
            ENUM_LIST(IPItem, item.IP, e)
            {
                IPItem ip(*e);
                cout << "\t" << ip.IP;
                if(ip.Dev != "")
                    cout << "/" << ip.Dev;
                if(!ip.Status)
                    cout << "(invalid)";
                cout << endl;
            }
        }
        if(!item.TcpPorts.IsEmpty())
        {
            cout << " Tcp:";
            ShowPorts(item.TcpPorts);
        }
        if(!item.UdpPorts.IsEmpty())
        {
            cout << " Udp:";
            ShowPorts(item.UdpPorts);
        }
        cout << " Schedule:" << item.Schedule.Str();
        if(item.Persistent)
            cout << " Persistent:" << item.PersistentTimeout << " Netmask:" << item.PersistentNetmask;
        cout << endl;
        bool mapport = item.IP.GetCount() == 1 && item.TcpPorts.GetCount() + item.UdpPorts.GetCount() == 1;
        if(mapport && item.TcpPorts.GetCount() == 1)
            mapport = item.TcpPorts.Head().Min == item.TcpPorts.Head().Max;
        if(mapport && item.UdpPorts.GetCount() == 1)
            mapport = item.UdpPorts.Head().Min == item.UdpPorts.Head().Max;
        if(!item.Servers.IsEmpty())
        {
            cout << " Servers:" << endl;
            ENUM_LIST(ServerItem, item.Servers, e)
            {
                ServerItem server(*e);
                cout << "\t" << server.IP;
                if(mapport && server.Action == ActionState::Masq)
                    cout << ":" << server.MapPort;
                cout << "\t" << "Weight: " << server.Weight << "\t";
                switch(server.Action)
                {
                    case ActionState::Gate:
                        cout << "Route";
                        break;
                    case ActionState::Ipip:
                        cout << "Tunnel";
                        break;
                    default:
                        cout << "Masq";
                }
                if(server.Name != "")
                    cout << "\t" << server.Name;
                if(server.Enabled)
                {
                    cout << "\tEnabled";
                    if(!server.Status && item.Enabled)
                        cout << " (Invalid)";
                } else {
                    cout << "\tDisabled";
                }
                cout << endl;
            }
        }
        if(item.Monitor.Type != MonitorState::Off)
        {
            cout << " Monitor: ";
            switch(item.Monitor.Type)
            {
                case MonitorState::Connect:
                    cout << "connect";
                    break;
                default:
                    cout << "ping";
            }
            cout << " Interval:" << item.Monitor.Interval;
            cout << " Timeout:" << item.Monitor.Timeout;
            cout << " Retry:" << item.Monitor.Retry;
            if(item.Monitor.Type == MonitorState::Connect)
                cout << " Port:" << item.Monitor.Port;
            cout << endl;
	    if(item.Monitor.Enabled)
                cout << "Emailalert:on";
            else
                cout << "Emailalert:off";
            cout << " MailTo:" << item.Monitor.Mail;
            cout << " MailFreq:" << item.Monitor.Date << endl;
        }
        if(item.Traffic.Up != 0 || item.Traffic.Down != 0)
        {
            cout << " Traffic UP: ";
            if(item.Traffic.Up != 0)
                cout << item.Traffic.Up << "kbps";
            else
                cout << "No limit";
            cout << " DOWN: ";
            if(item.Traffic.Down != 0)
                cout << item.Traffic.Down << "kbps";
            else
                cout << "No limit";
            cout << endl;
        }
    }

    void ListServiceAll(Control& control)
    {
        MatchService(control);
        if(control.IsEnd() || control.MatchOp("list"))
        {
            CommandModel cmd;
            cmd.FuncName = FuncVirtualServicesGet;
            cmd.Password = true;
            GetListItem<VirtualServiceItem> request(cmd.Params);
            request.All = true;
            Value res;
            Rpc::Call(cmd, res);
            ENUM_LIST(VirtualServiceItem, List<VirtualServiceItem>(res), e)
            {
                VirtualServiceItem service(*e);
                Show(service);
            }
        } else
            control.NotMatch();
    }

    DECLARE_COMMAND(ListServiceAll);

    void Add(Control& control)
    {
        MatchService(control);
        control.MustMatchOp("add");
        CommandModel cmd;
        cmd.FuncName = FuncVirtualServicesAdd;
        cmd.Password = true;
        String name;
        if(control.MatchValue(name))
            VirtualServiceItem(cmd.Params).Name = name;
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(Add);

    void Del(Control& control)
    {
        MatchService(control);
        control.MustMatchOp("del");
        String name;
        control.MustMatchValue(name);
        CommandModel cmd;
        cmd.FuncName = FuncVirtualServicesDelete;
        cmd.Password = true;
        VirtualServiceItem item(cmd.Params);
        item.Name = name;
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(Del);

    void MatchServiceName(String& name, Control& control)
    {
        MatchService(control);
        control.MustMatchValue(name);
    }

    void ListService(Control& control)
    {
        String name;
        MatchServiceName(name, control);
        if(control.IsEnd() || control.MatchOp("list"))
        {
            Value res;
            GetService(name, res);
            VirtualServiceItem item(res);
            Show(item);
        } else
            control.NotMatch();
    }

    DECLARE_COMMAND(ListService);

    void Enabled(Control& control)
    {
        String name;
        MatchServiceName(name, control);
        CommandModel cmd;
        cmd.FuncName = FuncVirtualServicesEnabled;
        cmd.Password = true;
        VirtualServiceItem item(cmd.Params);
        item.Name = name;
        if(control.MatchOp("enable"))
            item.Enabled = true;
        else if(control.MatchOp("disable"))
            item.Enabled = false;
        else
            control.NotMatch();
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(Enabled);

    void Description(Control& control)
    {
        String name;
        MatchServiceName(name, control);
        control.MustMatchOp("description");
        String description;
        control.MustMatchValue(description);
        CommandModel cmd;
        cmd.FuncName = FuncVirtualServicesDescription;
        cmd.Password = true;
        VirtualServiceItem item(cmd.Params);
        item.Name = name;
        item.Description = description;
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(Description);

    void Name(Control& control)
    {
        String name;
        MatchServiceName(name, control);
        control.MustMatchOp("name");
        String newname;
        control.MustMatchValue(newname);
        CommandModel cmd;
        cmd.FuncName = FuncVirtualServicesDescription;
        cmd.Password = true;
        VirtualServiceItem item(cmd.Params);
        {
            CommandModel request;
            request.FuncName = FuncVirtualServicesNameToID;
            request.Password = true;
            VirtualServiceItem(request.Params).Name = name;
            Value res;
            Rpc::Call(request, res);
            item.ID = VirtualServiceItem(res).ID;
        }
        item.Name = newname;
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(Name);

    void HA(Control& control)
    {
        String name;
        MatchServiceName(name, control);
        control.MustMatchOp("ha");
        CommandModel cmd;
        cmd.FuncName = FuncVirtualServicesHA;
        cmd.Password = true;
        VirtualServiceItem item(cmd.Params);
        item.Name = name;
        if(control.MatchOp("local"))
            item.HA = HAState::Local;
        else if(control.MatchOp("self"))
            item.HA = HAState::Self;
        else if(control.MatchOp("other"))
            item.HA = HAState::Other;
        else
            control.NotMatch();
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(HA);

    void ParsePort(List<PortItem>& list, const String& str)
    {
        list.Clear();
        ostringstream stream;
        ENUM_STL_CONST(String, str, e)
        {
            if(*e == ',' || *e == '-')
                stream << " " << *e << " ";
            else
                stream << *e;
        }
        istringstream portstream(stream.str());
        String word;
        if(!(portstream >> word))
            ERROR(Exception::IPVS::Service::Ports, "the ports is empty.");
        list.Append();
        enum
        {
            Div,
            Min,
            Range,
            Max
        } state = Div;
        do
        {
            PortItem item(list.Last());
            switch(state)
            {
                case Div:
                    {
                        istringstream stream(word);
                        int min;
                        if(!(stream >> min))
                            ERROR(Exception::IPVS::Service::Ports, "the port min value is invalid.");
                        item.Min = min;
                        state = Min;
                    }
                    break;
                case Min:
                    if(word == "-")
                        state = Range;
                    else if(word == ",")
                    {
                        state = Div;
                        list.Append();
                    } else
                        ERROR(Exception::IPVS::Service::Ports, "the port op is unknown.");
                    break;
                case Range:
                    {
                        istringstream stream(word);
                        int max;
                        if(!(stream >> max))
                            ERROR(Exception::IPVS::Service::Ports, "the port max value is invalid.");
                        item.Max = max;
                        state = Max;
                    }
                    break;
                case Max:
                    if(word == ",")
                    {
                        state = Div;
                        list.Append();
                    } else
                        ERROR(Exception::IPVS::Service::Ports, "the port op is unknown.");
                    break;
            }
        }
        while(portstream >> word);
    }

    void Address(Control& control)
    {
        String name;
        MatchServiceName(name, control);
        control.MustMatchOp("address");
        CommandModel cmd;
        cmd.FuncName = FuncVirtualServicesAddress;
        cmd.Password = true;
        VirtualServiceItem item(cmd.Params);
        item.Name = name;
        while(true)
        {
            if(control.MatchOp("ip"))
            {
                if(control.MatchOp("flush"))
                {
                    item.IP.Clear();
                } else {
                    IPItem ip(item.IP.Append());
                    String temp;
                    control.MustMatchValue(temp);
                    ip.IP = temp;
                    if(control.MatchOp("dev"))
                    {
                        control.MustMatchValue(temp);
                        ip.Dev = temp;
                    }
                }
            } else if(control.MatchOp("tcp"))
            {
                if(control.MatchOp("flush"))
                {
                    item.TcpPorts.Clear();
                } else {
                    String temp;
                    control.MustMatchValue(temp);
                    ParsePort(item.TcpPorts, temp);
                }
            } else if(control.MatchOp("udp"))
            {
                if(control.MatchOp("flush"))
                {
                    item.UdpPorts.Clear();
                } else {
                    String temp;
                    control.MustMatchValue(temp);
                    ParsePort(item.UdpPorts, temp);
                }
            } else
                break;
        }
        if(!control.IsEnd())
            control.NotMatch();
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(Address);

    void Service(Control& control)
    {
        String name;
        MatchServiceName(name, control);
        CommandModel cmd;
        cmd.FuncName = FuncVirtualServicesService;
        cmd.Password = true;
        VirtualServiceItem item(cmd.Params);
        item.Name = name;
        while(true)
        {
            if(control.MatchOp("schedule"))
            {
                if(control.MatchOp("rr"))
                    item.Schedule = ScheduleState::rr;
                else if(control.MatchOp("wrr"))
                    item.Schedule = ScheduleState::wrr;
                else if(control.MatchOp("lc"))
                    item.Schedule = ScheduleState::lc;
                else if(control.MatchOp("wlc"))
                    item.Schedule = ScheduleState::wlc;
                else if(control.MatchOp("lblc"))
                    item.Schedule = ScheduleState::lblc;
                else if(control.MatchOp("lblcr"))
                    item.Schedule = ScheduleState::lblcr;
                else if(control.MatchOp("dh"))
                    item.Schedule = ScheduleState::dh;
                else if(control.MatchOp("sh"))
                    item.Schedule = ScheduleState::sh;
                else if(control.MatchOp("sed"))
                    item.Schedule = ScheduleState::sed;
                else if(control.MatchOp("nq"))
                    item.Schedule = ScheduleState::nq;
                else
                    control.NotMatch();
            } else if(control.MatchOp("persistent"))
            {
                if(control.MatchOp("off"))
                {
                    item.Persistent = false;
                } else {
                    item.Persistent = true;
                    int time;
                    control.MustMatchValue(time);
                    item.PersistentTimeout = time;
                    if(control.MatchOp("netmask"))
                    {
                        String mask;
                        control.MustMatchValue(mask);
                        item.PersistentNetmask = mask;
                    }
                }
            } else if(control.MatchOp("monitor"))
            {
                while(true)
                {
                    if(control.MatchOp("interval"))
                    {
                        int interval;
                        control.MustMatchValue(interval);
                        item.Monitor.Interval = interval;
                    } else if(control.MatchOp("timeout"))
                    {
                        int timeout;
                        control.MustMatchValue(timeout);
                        item.Monitor.Timeout = timeout;
                    } else if(control.MatchOp("retry"))
                    {
                        int retry;
                        control.MustMatchValue(retry);
                        item.Monitor.Retry = retry;
                    } else if(control.MatchOp("type"))
                    {
                        if(control.MatchOp("off"))
                            item.Monitor.Type = MonitorState::Off;
                        else if(control.MatchOp("ping"))
                            item.Monitor.Type = MonitorState::Ping;
                        else if(control.MatchOp("connect"))
                            item.Monitor.Type = MonitorState::Connect;
                        else
                            control.NotMatch();
                    } else if(control.MatchOp("port"))
                    {
                        int port;
                        control.MustMatchValue(port);
                        item.Monitor.Port = port;
                    } else if(control.MatchOp("mail"))
		    {
			if(control.MatchOp("on"))
				item.Monitor.Enabled = true;
			if(control.MatchOp("off"))
				item.Monitor.Enabled = false;
			if(control.MatchOp("to"))
			{
				String mail;
                        	control.MustMatchValue(mail);
                        	item.Monitor.Mail = mail;
			}
			if(control.MatchOp("alertfreq"))
			{
				int date;
                        	control.MustMatchValue(date);
                        	item.Monitor.Date = date;
			}
                    } else
                        break;
                }
            } else if(!control.IsEnd())
                control.NotMatch();
            else
                break;
        }
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(Service);

    void Traffic(Control& control)
    {
        String name;
        MatchServiceName(name, control);
        control.MustMatchOp("traffic");
        CommandModel cmd;
        cmd.FuncName = FuncVirtualServicesTraffic;
        cmd.Password = true;
        VirtualServiceItem item(cmd.Params);
        item.Name = name;
        while(true)
        {
            if(control.MatchOp("up"))
            {
                int temp;
                control.MustMatchValue(temp);
                item.Traffic.Up = temp;
            } else if(control.MatchOp("down"))
            {
                int temp;
                control.MustMatchValue(temp);
                item.Traffic.Down = temp;
            } else
                break;
        }
        if(!control.IsEnd())
            control.NotMatch();
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(Traffic);

    void AddServer(Control& control)
    {
        String name;
        MatchServiceName(name, control);
        control.MustMatchOp("server");
        control.MustMatchOp("add");
        CommandModel cmd;
        cmd.FuncName = FuncVirtualServicesAddServer;
        cmd.Password = true;
        VirtualServiceItem item(cmd.Params);
        item.Name = name;
        ServerItem server(item.Servers.Append());
        while(true)
        {
            if(control.MatchOp("ip"))
            {
                String temp;
                control.MustMatchValue(temp);
                server.IP = temp;
            } else if(control.MatchOp("name"))
            {
                String temp;
                control.MustMatchValue(temp);
                server.Name = temp;
            } else if(control.MatchOp("action"))
            {
                if(control.MatchOp("route"))
                    server.Action = ActionState::Gate;
                else if(control.MatchOp("tunnel"))
                    server.Action = ActionState::Ipip;
                else if(control.MatchOp("masq"))
                    server.Action = ActionState::Masq;
                else
                    control.NotMatch();
            } else if(control.MatchOp("weight"))
            {
                int temp;
                control.MustMatchValue(temp);
                server.Weight = temp;
            } else if(control.MatchOp("mapport"))
            {
                int temp;
                control.MustMatchValue(temp);
                server.MapPort = temp;
            } else
                break;
        }
        if(control.MatchOp("enable"))
            server.Enabled = true;
        else if(control.MatchOp("disable"))
            server.Enabled = false;
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(AddServer);

    int GetServerCount(const String& name)
    {
        Value res;
        GetService(name, res);
        return VirtualServiceItem(res).Servers.GetCount();
    }

    void DelServer(Control& control)
    {
        String name;
        MatchServiceName(name, control);
        control.MustMatchOp("server");
        CommandModel cmd;
        cmd.FuncName = FuncVirtualServicesDelServer;
        cmd.Password = true;
        VirtualServiceItem item(cmd.Params);
        item.Name = name;
        if(control.MatchOp("flush"))
        {
            int count = GetServerCount(name);
            for(int i = 0; i < count; ++i)
            {
                ServerItem(item.Servers.Append()).ID = i;
            }
        } else if(control.MatchOp("del"))
        {
            String temp;
            control.MustMatchValue(temp);
            ServerItem(item.Servers.Append()).Name = temp;
        } else
            control.NotMatch();
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(DelServer);

    void MatchServerName(String& name, Control& control)
    {
        control.MustMatchOp("server");
        control.MustMatchValue(name);
    }

    void EnabledServer(Control& control)
    {
        String name;
        MatchServiceName(name, control);
        String servername;
        MatchServerName(servername, control);
        CommandModel cmd;
        cmd.FuncName = FuncVirtualServicesEnabledServer;
        cmd.Password = true;
        VirtualServiceItem item(cmd.Params);
        item.Name = name;
        ServerItem server(item.Servers.Append());
        server.Name = servername;
        if(control.MatchOp("enable"))
            server.Enabled = true;
        else if(control.MatchOp("disable"))
            server.Enabled = false;
        else
            control.NotMatch();
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(EnabledServer);

    void SetServer(Control& control)
    {
        String name;
        MatchServiceName(name, control);
        String servername;
        MatchServerName(servername, control);
        CommandModel cmd;
        cmd.FuncName = FuncVirtualServicesSetServer;
        cmd.Password = true;
        VirtualServiceItem item(cmd.Params);
        item.Name = name;
        ServerItem server(item.Servers.Append());
        while(true)
        {
            if(control.MatchOp("ip"))
            {
                String temp;
                control.MustMatchValue(temp);
                server.IP = temp;
            } else if(control.MatchOp("name"))
            {
                String temp;
                control.MustMatchValue(temp);
                server.Name = temp;
            } else if(control.MatchOp("action"))
            {
                if(control.MatchOp("route"))
                    server.Action = ActionState::Gate;
                else if(control.MatchOp("tunnel"))
                    server.Action = ActionState::Ipip;
                else if(control.MatchOp("masq"))
                    server.Action = ActionState::Masq;
                else
                    control.NotMatch();
            } else if(control.MatchOp("weight"))
            {
                int temp;
                control.MustMatchValue(temp);
                server.Weight = temp;
            } else if(control.MatchOp("mapport"))
            {
                int temp;
                control.MustMatchValue(temp);
                server.MapPort = temp;
            } else
                break;
        }
        if(!control.IsEnd())
            control.NotMatch();
        {
            CommandModel cmd;
            cmd.FuncName = FuncVirtualServicesNameToID;
            cmd.Password = true;
            VirtualServiceItem get(cmd.Params);
            get.Name = name;
            get.Servers.Append().Name = servername;
            Value result;
            Rpc::Call(cmd, result);
            server.ID = VirtualServiceItem(result).Servers.Head().ID;
        }
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(SetServer);
};
