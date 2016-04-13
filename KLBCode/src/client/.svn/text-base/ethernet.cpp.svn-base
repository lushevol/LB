#include <iostream>
#include <sstream>

#include "model/interface.h"

#include "control.h"

using namespace std;

namespace Ethernet
{
    void GetInterface(const String& dev, Value& result)
    {
        CommandModel cmd;
        cmd.FuncName = FuncEthernetGet;
        cmd.Password = true;
        EthernetInterface request(cmd.Params);
        request.Dev = dev;
        Rpc::Call(cmd, result);
    }

    void Show(EthernetInterface& eth)
    {
        cout << "Ethernet: " << eth.Dev;
        if(eth.Master == "" && eth.Adsl == "")
        {
            cout << " (" << (eth.Enabled ? "Up" : "Down") << ")";
            if(eth.Dhcp)
                cout << "\tDhcp";
        } else if(eth.Master != "")
            cout << "\tMaster: " << eth.Master;
        else if(eth.Adsl != "")
            cout << "\tAdsl: " << eth.Adsl;
        if(eth.Enabled && !eth.Carrier)
            cout << "\tNo carrier";
        cout << endl;
        if(eth.Description != "")
            cout << eth.Description << endl;
        cout << "MTU: " << eth.MTU << "\tAddress: " << eth.CurrentAddress << "\tArp: " << eth.Arp.Str() << endl;
        cout << "Mode: " << (eth.Detect ? "Auto" : "Manual");
        if(!eth.Detect || (eth.Enabled && eth.Carrier))
            cout << "\tSpeed: " << eth.Speed << "Mb/s\t\tDuplex: " << (eth.FullDuplex ? "Full" : "Half");
        cout << endl;
        ENUM_LIST(HostPackStringValue, eth.IP, e)
        {
            HostPackStringValue ip(*e);
            cout << "IP: " << ip << endl;
        }
    }

    void ListAll(Control& control)
    {
        control.MustMatchOp("ethernet");
        if(control.IsEnd() || control.MatchOp("list"))
        {
            CommandModel cmd;
            cmd.FuncName = FuncEthernetGetAll;
            cmd.Password = true;
            Value result;
            Rpc::Call(cmd, result);
            ENUM_LIST(EthernetInterface, List<EthernetInterface>(result), e)
            {
                EthernetInterface eth(*e);
                Show(eth);
            }
        } else
            control.NotMatch();
    }

    DECLARE_COMMAND(ListAll);

    void ListDev(Control& control)
    {
        control.MustMatchOp("ethernet");
        String dev;
        control.MustMatchValue(dev);
        if(control.IsEnd() || control.MatchOp("list"))
        {
            CommandModel cmd;
            cmd.FuncName = FuncEthernetGet;
            cmd.Password = true;
            EthernetInterface request(cmd.Params);
            request.Dev = dev;
            Value res;
            Rpc::Call(cmd, res);
            EthernetInterface result(res);
            Show(result);
        } else
            control.NotMatch();
    }

    DECLARE_COMMAND(ListDev);

    void MatchSetDev(String& dev, Control& control)
    {
        control.MustMatchOp("ethernet");
        control.MustMatchValue(dev);
    }

    void Address(Control& control)
    {
        String dev;
        MatchSetDev(dev, control);
        control.MustMatchOp("address");
        String address;
        control.MustMatchValue(address);
        CommandModel cmd;
        cmd.FuncName = FuncEthernetAddress;
        cmd.Password = true;
        EthernetInterface eth(cmd.Params);
        eth.Address = address;
        eth.Dev = dev;
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(Address);

    void Enabled(Control& control)
    {
        String dev;
        MatchSetDev(dev, control);
        CommandModel cmd;
        cmd.FuncName = FuncEthernetEnabled;
        cmd.Password = true;
        EthernetInterface eth(cmd.Params);
        if(control.MatchOp("up"))
            eth.Enabled = true;
        else if(control.MatchOp("down"))
            eth.Enabled = false;
        else
            control.NotMatch();
        eth.Dev = dev;
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(Enabled);

    void Dhcp(Control& control)
    {
        String dev;
        MatchSetDev(dev, control);
        control.MustMatchOp("dhcp");
        CommandModel cmd;
        cmd.FuncName = FuncEthernetDhcp;
        cmd.Password = true;
        EthernetInterface eth(cmd.Params);
        if(control.MatchOp("on"))
            eth.Dhcp = true;
        else if(control.MatchOp("off"))
            eth.Dhcp = false;
        else
            control.NotMatch();
        eth.Dev = dev;
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(Dhcp);

    void Arp(Control& control)
    {
        String dev;
        MatchSetDev(dev, control);
        control.MustMatchOp("arp");
        CommandModel cmd;
        cmd.FuncName = FuncEthernetArp;
        cmd.Password = true;
        EthernetInterface eth(cmd.Params);
        if(control.MatchOp("enable"))
            eth.Arp = EthernetInterface::ArpState::Enabled;
        else if(control.MatchOp("disable"))
            eth.Arp = EthernetInterface::ArpState::Disabled;
        else if(control.MatchOp("proxy"))
            eth.Arp = EthernetInterface::ArpState::Proxy;
        else if(control.MatchOp("reply"))
            eth.Arp = EthernetInterface::ArpState::ReplyOnly;
        else
            control.NotMatch();
        eth.Dev = dev;
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(Arp);

    void MTU(Control& control)
    {
        String dev;
        MatchSetDev(dev, control);
        control.MustMatchOp("mtu");
        CommandModel cmd;
        cmd.FuncName = FuncEthernetMTU;
        cmd.Password = true;
        EthernetInterface eth(cmd.Params);
        int mtu;
        control.MustMatchValue(mtu);
        eth.Dev = dev;
        eth.MTU = mtu;
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(MTU);

    void IP(Control& control)
    {
        String dev;
        MatchSetDev(dev, control);
        control.MustMatchOp("ip");
        int action = 0;
        if(control.MatchOp("add"))
            action = 1;
        else if(control.MatchOp("del"))
            action = -1;
        else if(control.MatchOp("flush"))
        {
        }
        StringList list;
        while(!control.IsEnd())
        {
            String data;
            control.MustMatchValue(data);
            list.push_back(data);
        }
        CommandModel cmd;
        cmd.FuncName = FuncEthernetIP;
        cmd.Password = true;
        EthernetInterface eth(cmd.Params);
        eth.Dev = dev;
        eth.IP.Clear();
        if(action != 0)
        {
            Value res;
            GetInterface(dev, res);
            EthernetInterface recent(res);
            eth.IP = recent.IP;
        }
        if(action != -1)
        {
            ENUM_STL(StringList, list, e)
            {
                HostPackStringValue ip(eth.IP.Append());
                ip = *e;
            }
        } else {
            ENUM_STL(StringList, list, e)
            {
                int index = 0;
                ENUM_LIST(HostPackStringValue, eth.IP, i)
                {
                    HostPackStringValue ip(*i);
                    if(ip == *e)
                        break;
                    ++index;
                }
                if(index < eth.IP.GetCount())
                    eth.IP.Delete(index);
                else
                    ERROR(Exception::Command::DelIP, dev << '\n' << *e);
            }
        }
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(IP);

    void Link(Control& control)
    {
        String dev;
        MatchSetDev(dev, control);
        control.MustMatchOp("mode");
        CommandModel cmd;
        cmd.FuncName = FuncEthernetLink;
        cmd.Password = true;
        EthernetInterface eth(cmd.Params);
        eth.Dev = dev;
        if(control.MatchOp("auto"))
        {
            eth.Detect = true;
            Rpc::CallNoResult(cmd);
        } else {
            eth.Detect = false;
            if(control.MatchOp("full"))
                eth.FullDuplex = true;
            else if(control.MatchOp("half"))
                eth.FullDuplex = false;
            else
                control.NotMatch();
            if(control.MatchOp("10000"))
                eth.Speed = EthernetInterface::SpeedState::Miiln;
            else if(control.MatchOp("1000"))
                eth.Speed = EthernetInterface::SpeedState::Thousand;
            else if(control.MatchOp("100"))
                eth.Speed = EthernetInterface::SpeedState::Hundred;
            else if(control.MatchOp("10"))
                eth.Speed = EthernetInterface::SpeedState::Ten;
            else
                control.NotMatch();
            Rpc::CallNoResult(cmd);
        }
    }

    DECLARE_COMMAND(Link);

    void Description(Control& control)
    {
        String dev;
        MatchSetDev(dev, control);
        control.MustMatchOp("description");
        String description;
        control.MustMatchValue(description);
        CommandModel cmd;
        cmd.FuncName = FuncEthernetDescription;
        cmd.Password = true;
        EthernetInterface eth(cmd.Params);
        eth.Dev = dev;
        eth.Description = description;
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(Description);

};
