#include <iostream>

#include "model/interface.h"

#include "control.h"

using namespace std;

namespace Bonding
{
    void GetInterface(const String& dev, Value& result)
    {
        CommandModel cmd;
        cmd.FuncName = FuncBondingGet;
        cmd.Password = true;
        BondingInterface request(cmd.Params);
        request.Dev = dev;
        Rpc::Call(cmd, result);
    }

    void Show(BondingInterface& bond)
    {
        cout << "Bonding: " << bond.Dev << " (" << (bond.Enabled ? "Up" : "Down") << ")";
        if(bond.Dhcp)
            cout << "\tDhcp";
        if(!bond.Carrier && bond.Enabled)
            cout << "\tNo carrier";
        cout << endl;
        if(bond.Description != "")
            cout << bond.Description << endl;
        if(!bond.Slaves.IsEmpty())
        {
            cout << "Slaves:";
            ENUM_LIST(EthernetInterfaceNameValue, bond.Slaves, e)
            {
                EthernetInterfaceNameValue eth(*e);
                cout << " " << eth;
            }
            cout << endl;
        }
        cout << "MTU: " << bond.MTU << "\tAddress: " << bond.CurrentAddress << "\tArp: " << bond.Arp.Str() << endl;
        ENUM_LIST(HostPackStringValue, bond.IP, e)
        {
            HostPackStringValue ip(*e);
            cout << "IP: " << ip << endl;
        }
        cout << "Mode: " << bond.Mode.Str() << endl;
        cout << "Check: " << bond.CheckMode.Str() << "\tFrequency:" << bond.Frequency << endl;
        if(bond.CheckMode == BondingInterface::CheckModeState::arp)
        {
            if(!bond.CheckIP.IsEmpty())
            {
                cout << "Check IP:" << endl;
                ENUM_LIST(HostStringValue, bond.CheckIP, e)
                {
                    HostStringValue ip(*e);
                    cout << " " << ip << endl;
                }
            }
        }
    }

    void ListAll(Control& control)
    {
        control.MustMatchOp("bonding");
        if(control.IsEnd() || control.MatchOp("list"))
        {
            CommandModel cmd;
            cmd.FuncName = FuncBondingGetAll;
            cmd.Password = true;
            Value result;
            Rpc::Call(cmd, result);
            ENUM_LIST(BondingInterface, List<BondingInterface>(result), e)
            {
                BondingInterface bond(*e);
                Show(bond);
            }
        } else
            control.NotMatch();
    }

    DECLARE_COMMAND(ListAll);

    void ListDev(Control& control)
    {
        control.MustMatchOp("bonding");
        String dev;
        control.MustMatchValue(dev);
        if(control.IsEnd() || control.MatchOp("list"))
        {
            CommandModel cmd;
            cmd.FuncName = FuncBondingGet;
            cmd.Password = true;
            BondingInterface request(cmd.Params);
            request.Dev = dev;
            Value res;
            Rpc::Call(cmd, res);
            BondingInterface result(res);
            Show(result);
        } else
            control.NotMatch();
    }

    DECLARE_COMMAND(ListDev);

    void MatchSetDev(String& dev, Control& control)
    {
        control.MustMatchOp("bonding");
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
        cmd.FuncName = FuncBondingAddress;
        cmd.Password = true;
        BondingInterface bond(cmd.Params);
        bond.Dev = dev;
        bond.Address = address;
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(Address);

    void Enabled(Control& control)
    {
        String dev;
        MatchSetDev(dev, control);
        CommandModel cmd;
        cmd.FuncName = FuncBondingEnabled;
        cmd.Password = true;
        BondingInterface bond(cmd.Params);
        if(control.MatchOp("up"))
            bond.Enabled = true;
        else if(control.MatchOp("down"))
            bond.Enabled = false;
        else
            control.NotMatch();
        bond.Dev = dev;
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(Enabled);

    void Dhcp(Control& control)
    {
        String dev;
        MatchSetDev(dev, control);
        control.MustMatchOp("dhcp");
        CommandModel cmd;
        cmd.FuncName = FuncBondingDhcp;
        cmd.Password = true;
        BondingInterface bond(cmd.Params);
        if(control.MatchOp("on"))
            bond.Dhcp = true;
        else if(control.MatchOp("off"))
            bond.Dhcp = false;
        else
            control.NotMatch();
        bond.Dev = dev;
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(Dhcp);

    void Arp(Control& control)
    {
        String dev;
        MatchSetDev(dev, control);
        control.MustMatchOp("arp");
        CommandModel cmd;
        cmd.FuncName = FuncBondingArp;
        cmd.Password = true;
        BondingInterface bond(cmd.Params);
        if(control.MatchOp("enable"))
            bond.Arp = BondingInterface::ArpState::Enabled;
        else if(control.MatchOp("disable"))
            bond.Arp = BondingInterface::ArpState::Disabled;
        else if(control.MatchOp("proxy"))
            bond.Arp = BondingInterface::ArpState::Proxy;
        else if(control.MatchOp("reply"))
            bond.Arp = BondingInterface::ArpState::ReplyOnly;
        else
            control.NotMatch();
        bond.Dev = dev;
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(Arp);

    void MTU(Control& control)
    {
        String dev;
        MatchSetDev(dev, control);
        control.MustMatchOp("mtu");
        int mtu;
        control.MustMatchValue(mtu);
        CommandModel cmd;
        cmd.FuncName = FuncBondingMTU;
        cmd.Password = true;
        BondingInterface bond(cmd.Params);
        bond.Dev = dev;
        bond.MTU = mtu;
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
        cmd.FuncName = FuncBondingIP;
        cmd.Password = true;
        BondingInterface bond(cmd.Params);
        bond.Dev = dev;
        bond.IP.Clear();
        if(action != 0)
        {
            Value res;
            GetInterface(dev, res);
            BondingInterface recent(res);
            bond.IP = recent.IP;
        }
        if(action != -1)
        {
            ENUM_STL(StringList, list, e)
            {
                HostPackStringValue ip(bond.IP.Append());
                ip = *e;
            }
        } else {
            ENUM_STL(StringList, list, e)
            {
                int index = 0;
                ENUM_LIST(HostPackStringValue, bond.IP, i)
                {
                    HostPackStringValue ip(*i);
                    if(ip == *e)
                        break;
                    ++index;
                }
                if(index < bond.IP.GetCount())
                    bond.IP.Delete(index);
                else
                    ERROR(Exception::Command::DelIP, dev << '\n' << *e);
            }
        }
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(IP);

    void Description(Control& control)
    {
        String dev;
        MatchSetDev(dev, control);
        control.MustMatchOp("description");
        String description;
        control.MustMatchValue(description);
        CommandModel cmd;
        cmd.FuncName = FuncBondingDescription;
        cmd.Password = true;
        BondingInterface bond(cmd.Params);
        bond.Dev = dev;
        bond.Description = description;
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(Description);

    void Add(Control& control)
    {
        control.MustMatchOp("bonding");
        control.MustMatchOp("add");
        String dev;
        control.MustMatchValue(dev);
        CommandModel cmd;
        cmd.FuncName = FuncBondingAdd;
        cmd.Password = true;
        BondingInterface bond(cmd.Params);
        bond.Dev = dev;
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(Add);

    void Del(Control& control)
    {
        control.MustMatchOp("bonding");
        control.MustMatchOp("del");
        String dev;
        control.MustMatchValue(dev);
        CommandModel cmd;
        cmd.FuncName = FuncBondingDelete;
        cmd.Password = true;
        BondingInterface bond(cmd.Params);
        bond.Dev = dev;
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(Del);

    void Slaves(Control& control)
    {
        String dev;
        MatchSetDev(dev, control);
        control.MustMatchOp("slaves");
        control.MatchOp("flush");
        StringList list;
        while(!control.IsEnd())
        {
            String data;
            control.MustMatchValue(data);
            list.push_back(data);
        }
        CommandModel cmd;
        cmd.FuncName = FuncBondingSetSlaves;
        cmd.Password = true;
        BondingInterface bond(cmd.Params);
        bond.Dev = dev;
        bond.Slaves.Clear();
        ENUM_STL(StringList, list, e)
        {
            EthernetInterfaceNameValue name(bond.Slaves.Append());
            name = *e;
        }
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(Slaves);

    void Check(Control& control)
    {
        String dev;
        MatchSetDev(dev, control);
        control.MustMatchOp("check");
        CommandModel cmd;
        cmd.FuncName = FuncBondingCheck;
        cmd.Password = true;
        BondingInterface bond(cmd.Params);
        bond.Dev = dev;
        while(true)
        {
            if(control.MatchOp("mode"))
            {
                if(control.MatchOp("miimon"))
                    bond.CheckMode = BondingInterface::CheckModeState::miimon;
                else if(control.MatchOp("arp"))
                    bond.CheckMode = BondingInterface::CheckModeState::arp;
                else
                    control.NotMatch();
            } else if(control.MatchOp("frequency"))
            {
                int frequency;
                control.MustMatchValue(frequency);
                bond.Frequency = frequency;
            } else
                break;
        }
        if(!control.IsEnd())
            control.NotMatch();
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(Check);

    void Mode(Control& control)
    {
        String dev;
        MatchSetDev(dev, control);
        control.MustMatchOp("mode");
        CommandModel cmd;
        cmd.FuncName = FuncBondingMode;
        cmd.Password = true;
        BondingInterface bond(cmd.Params);
        if(control.MatchOp("rr"))
            bond.Mode = BondingInterface::ModeState::rr;
        else if(control.MatchOp("backup"))
            bond.Mode = BondingInterface::ModeState::backup;
        else if(control.MatchOp("xor"))
            bond.Mode = BondingInterface::ModeState::bxor;
        else if(control.MatchOp("broadcast"))
            bond.Mode = BondingInterface::ModeState::broadcast;
        else if(control.MatchOp("802.3ad"))
            bond.Mode = BondingInterface::ModeState::dot3ad;
        else if(control.MatchOp("tlb"))
            bond.Mode = BondingInterface::ModeState::btlb;
        else if(control.MatchOp("alb"))
            bond.Mode = BondingInterface::ModeState::balb;
        else
            control.NotMatch();
        bond.Dev = dev;
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(Mode);

    void CheckIP(Control& control)
    {
        String dev;
        MatchSetDev(dev, control);
        control.MustMatchOp("check");
        control.MustMatchOp("ip");
        control.MatchOp("flush");
        StringList list;
        while(!control.IsEnd())
        {
            String data;
            control.MustMatchValue(data);
            list.push_back(data);
        }
        CommandModel cmd;
        cmd.FuncName = FuncBondingCheckIP;
        cmd.Password = true;
        BondingInterface bond(cmd.Params);
        bond.Dev = dev;
        bond.CheckIP.Clear();
        ENUM_STL(StringList, list, e)
        {
            HostStringValue ip(bond.CheckIP.Append());
            ip = *e;
        }
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(CheckIP);
};
