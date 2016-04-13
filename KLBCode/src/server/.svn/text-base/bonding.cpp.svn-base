#include <fstream>
#include <sstream>
#include <map>

#include "share/utility.h"

#include "rpc.h"
#include "serialize.h"
#include "base.h"
#include "logger.h"

#include "bonding.h"
#include "ethernet.h"

using namespace std;

BondingInterfaceControl::BondingInterfaceControl()
    : PhysicalInterfaceControl(Configure::GetValue()["Bonding"])
    , Holder(PhysicalInterfaceControl::Holder.Data)
{
    if(!Holder.Valid())
        Holder.Clear();
}

void BondingInterfaceControl::GetSlaves(const String& dev, StringList& list)
{
    ifstream stream(("/sys/class/net/" + dev + "/bonding/slaves").c_str());
    if(stream)
    {
        list.clear();
        String name;
        while(stream >> name)
            list.push_back(name);
        stream.close();
    }
    else
        ERROR(Exception::Interface::NotFoundDev, dev);
}

void BondingInterfaceControl::AddSlave(const String& dev, const String& slave)
{
    PhysicalInterfaceControl::SetEnabled(slave, false);
    if(Exec::System("echo +" + slave + " >>/sys/class/net/" + dev + "/bonding/slaves") != 0)
        ERROR(Exception::Interface::Bonding::Slave::Add, dev << '\n' << slave);
}

void BondingInterfaceControl::DelSlave(const String& dev, const String& slave)
{
    if(Exec::System("echo -" + slave + " >>/sys/class/net/" + dev + "/bonding/slaves") != 0)
        ERROR(Exception::Interface::Bonding::Slave::Del, dev << '\n' << slave);
}

void BondingInterfaceControl::AddCheckIP(const String& dev, const String& ip)
{
    if(Exec::System("echo +" + ip + " >>/sys/class/net/" + dev + "/bonding/arp_ip_target") != 0)
        ERROR(Exception::Interface::Bonding::CheckIP::Add, dev << '\n' << ip);
}

void BondingInterfaceControl::DelCheckIP(const String& dev, const String& ip)
{
    if(Exec::System("echo -" + ip + " >>/sys/class/net/" + dev + "/bonding/arp_ip_target") != 0)
        ERROR(Exception::Interface::Bonding::CheckIP::Del, dev << '\n' << ip);
}

void BondingInterfaceControl::GetCheckIPList(const String& dev, StringList& list)
{
    ifstream stream(("/sys/class/net/" + dev + "/bonding/arp_ip_target").c_str());
    if(stream)
    {
        list.clear();
        String name;
        while(stream >> name)
            list.push_back(name);
        stream.close();
    }
    else
        ERROR(Exception::Interface::NotFoundDev, dev);
}

void BondingInterfaceControl::CheckSupportMode(int mode, List<EthernetInterfaceNameValue>& eth)
{
    typedef pair<bool, pair<bool, bool> > Info;
    typedef map < String, Info> EthMap;
    static EthMap info;
#define DUPLEX(e) (e).first
#define SPEED(e) (e).second.first
#define MAC(e) (e).second.second
    ENUM_LIST(EthernetInterfaceNameValue, eth, i)
    {
        EthernetInterfaceNameValue dev(*i);
        EthMap::iterator pos = info.find(dev);
        if(pos == info.end())
        {
            PRINTF("Detect eth " << dev << " support.");
            Info& temp = info[dev];
            {
                DUPLEX(temp) = false;
                SPEED(temp) = false;
                Exec exe("ethtool");
                exe << dev;
                exe.Execute();
                fdistream stream(exe.ReadOut());
                String line;
                while(stream >> line)
                {
                    istringstream linestream(line);
                    String word;
                    if(linestream >> word)
                    {
                        if(word == "Duplex:")
                            DUPLEX(temp) = true;
                        else if(word == "Speed:")
                            SPEED(temp) = true;
                    }
                }
            }
            {
                String mac = PhysicalInterfaceControl::GetAddress(dev);
                bool enabled = PhysicalInterfaceControl::GetEnabled(dev);
                if(!enabled)
                    PhysicalInterfaceControl::SetEnabled(dev, true);
                MAC(temp) = Exec::System("ip link set " + (const String&)dev + " address " + mac) == 0;
                if(!enabled)
                    PhysicalInterfaceControl::SetEnabled(dev, false);
            }
            pos = info.find(dev);
            PRINTF("Detect eth done " << dev << " duplex " << DUPLEX(temp));
            PRINTF("Detect eth done " << dev << " speed " << SPEED(temp));
            PRINTF("Detect eth done " << dev << " mac " << MAC(temp));
        }
        switch(mode)
        {
            case BondingInterface::ModeState::dot3ad:
                if(!DUPLEX(pos->second) || !SPEED(pos->second))
                    ERROR(Exception::Interface::Bonding::Mode::Dot, dev);
                break;
            case BondingInterface::ModeState::btlb:
                if(!SPEED(pos->second))
                    ERROR(Exception::Interface::Bonding::Mode::TLB, dev);
                break;
            case BondingInterface::ModeState::balb:
                if(!SPEED(pos->second) || !MAC(pos->second))
                    ERROR(Exception::Interface::Bonding::Mode::ALB, dev);
                break;
            default:
                break;
        };
    };
#undef DUPLEX
#undef SPEED
#undef MAC
}

void BondingInterfaceControl::SetMode(const String& dev, int mode)
{
    bool enabled = GetEnabled(dev);
    if(enabled)
        PhysicalInterfaceControl::SetEnabled(dev, false);
    ostringstream stream;
    stream << "echo " << mode << " >> /sys/class/net/" << dev << "/bonding/mode";
    int status = Exec::System(stream.str());
    if(enabled)
        PhysicalInterfaceControl::SetEnabled(dev, true);
    if(status != 0)
        ERROR(Exception::Interface::Bonding::Mode::Set, dev);
}

void BondingInterfaceControl::SetLinkCheck(const String& dev, int mode, int frequency)
{
    if(frequency <= 0)
        ERROR(Exception::Server::Params, "Frequency");
    ostringstream cmd, zero;
    zero << "echo 0 >> /sys/class/net/" << dev << "/bonding/";
    cmd << "echo " << frequency << " >> /sys/class/net/" << dev << "/bonding/";
    if(mode == BondingInterface::CheckModeState::miimon)
    {
        cmd << "miimon";
        zero << "arp_interval";
    } else {
        cmd << "arp_interval";
        zero << "miimon";
    }
    if(Exec::System(zero.str()) != 0)
        ERROR(Exception::Interface::Bonding::LinkCheck, dev);
    if(Exec::System(cmd.str()) != 0)
        ERROR(Exception::Interface::Bonding::LinkCheck, dev);
}

void BondingInterfaceControl::AddBonding(const String& dev)
{
    if(Exec::System("echo +" + dev + " >>/sys/class/net/bonding_masters") != 0)
        ERROR(Exception::Interface::Bonding::Add, dev);
    do
    {
        sched_yield();
        Exec::System("ip link set " + dev + " up");
    } while(!GetEnabled(dev));
    Exec::System("ip link set " + dev + " down");
}

void BondingInterfaceControl::DelBonding(const String& dev)
{
    if(Exec::System("echo -" + dev + " >>/sys/class/net/bonding_masters") != 0)
        ERROR(Exception::Interface::Bonding::Del, dev);
}

void BondingInterfaceControl::SetAddress(BondingInterface& target, BondingInterface& recent)
{
    if(!recent.Slaves.IsEmpty())
    {
        ENUM_LIST(EthernetInterfaceNameValue, recent.Slaves, e)
        {
            PhysicalInterfaceControl::SetEnabled(EthernetInterfaceNameValue(*e), false);
        }
        try
        {
            PhysicalInterfaceControl::SetAddress(target, recent);
            ENUM_LIST(EthernetInterfaceNameValue, recent.Slaves, e)
            {
                PhysicalInterfaceControl::SetEnabled(EthernetInterfaceNameValue(*e), true);
            }
        } catch(ValueException& error)
        {
            ENUM_LIST(EthernetInterfaceNameValue, recent.Slaves, e)
            {
                PhysicalInterfaceControl::SetEnabled(EthernetInterfaceNameValue(*e), true);
            }
            throw error;
        }
    } else {
        if(target.Address.Valid() && target.Address == "")
            return;
        ERROR(Exception::Interface::Bonding::Slave::Empty, recent.Dev);
    }
}

void BondingInterfaceControl::SetMTU(BondingInterface& target)
{
    BondingInterface recent(Find(target.Dev));
    PhysicalInterfaceControl::SetMTU(target, recent);
    LOGGER_NOTICE("Set " << recent.Dev << "'s mtu done.");
}

void BondingInterfaceControl::SetArp(BondingInterface& target)
{
    BondingInterface recent(Find(target.Dev));
    PhysicalInterfaceControl::SetArp(target, recent);
    LOGGER_NOTICE("Set " << recent.Dev << "'s arp mode done.");
}

void BondingInterfaceControl::SetEnabled(BondingInterface& target)
{
    if(!target.Dev.Valid())
        ERROR(Exception::Server::Params, "Dev");
    BondingInterface recent(Find(target.Dev));
    PhysicalInterfaceControl::SetEnabled(target, recent);
    InterfaceRefresher::Refresh(recent.Dev);
    LOGGER_NOTICE("Set " << recent.Dev << "'s enabled done.");
}

void BondingInterfaceControl::SetAddress(BondingInterface& target)
{
    if(!target.Dev.Valid())
        ERROR(Exception::Server::Params, "Dev");
    BondingInterface recent(Find(target.Dev));
    SetAddress(target, recent);
    InterfaceRefresher::Refresh(recent.Dev);
    LOGGER_NOTICE("Set " << recent.Dev << "'s address done.");
}

void BondingInterfaceControl::SetIP(BondingInterface& target)
{
    if(!target.Dev.Valid())
        ERROR(Exception::Server::Params, "Dev");
    BondingInterface recent(Find(target.Dev));
    PhysicalInterfaceControl::SetIP(target, recent);
    InterfaceRefresher::Refresh(recent.Dev);
    LOGGER_NOTICE("Set " << recent.Dev << "'s ip done.");
}

void BondingInterfaceControl::SetDhcp(BondingInterface& target)
{
    if(!target.Dev.Valid())
        ERROR(Exception::Server::Params, "Dev");
    BondingInterface recent(Find(target.Dev));
    PhysicalInterfaceControl::SetDhcp(target, recent);
    InterfaceRefresher::Refresh(recent.Dev);
    LOGGER_NOTICE("Set " << recent.Dev << "'s dhcp done.");
}

void BondingInterfaceControl::SetSlaves(BondingInterface& target)
{
    if(!target.Dev.Valid())
        ERROR(Exception::Server::Params, "Dev");
    BondingInterface recent(Find(target.Dev));
    if(!target.Slaves.Valid())
        ERROR(Exception::Server::Params, "Slaves");
    StringCollection devlist;
    EthernetInterfaceControl control;
    ENUM_LIST(EthernetInterfaceNameValue, target.Slaves, e)
    {
        EthernetInterface eth(control.Find(*e));
        if(eth.Adsl != "")
            ERROR(Exception::Interface::Ethernet::Adsl, eth.Dev << '\n' << eth.Adsl);
        if(eth.Master != "" && eth.Master != recent.Dev)
            ERROR(Exception::Interface::Ethernet::Slave, eth.Dev << '\n' << eth.Master);
        if(devlist.count(eth.Dev) == 0)
            devlist.insert(eth.Dev);
    }
    CheckSupportMode(recent.Mode, target.Slaves);
    try
    {
        ENUM_LIST(EthernetInterfaceNameValue, recent.Slaves, e)
        {
            EthernetInterfaceNameValue ether(*e);
            DelSlave(target.Dev, ether);
        }
        ENUM_LIST(EthernetInterfaceNameValue, target.Slaves, e)
        {
            EthernetInterfaceNameValue ether(*e);
            AddSlave(target.Dev, ether);
        }
        StringCollection collection;
        ENUM_LIST(EthernetInterfaceNameValue, recent.Slaves, e)
        {
            EthernetInterfaceNameValue ether(*e);
            EthernetInterface interface(control.Find(ether));
            interface.Master = "";
            collection.insert(ether);
        }
        ENUM_LIST(EthernetInterfaceNameValue, target.Slaves, e)
        {
            EthernetInterfaceNameValue ether(*e);
            EthernetInterface interface(control.Find(ether));
            interface.Master = target.Dev;
            collection.insert(ether);
        }
        ENUM_STL(StringCollection, collection, e)
        {
            control.RefreshInterface(*e);
        }
        recent.Slaves = target.Slaves;
        if(recent.Slaves.IsEmpty())
            recent.Address = "";
        else
            SetAddress(recent, recent);
        collection.insert(recent.Dev);
        InterfaceRefresher::Refresh(collection);
    } catch(ValueException& error)
    {
        StringList list;
        GetSlaves(recent.Dev, list);
        StringCollection collection;
        ENUM_STL(StringList, list, e)
        {
            DelSlave(target.Dev, *e);
            collection.insert(*e);
        }
        ENUM_LIST(EthernetInterfaceNameValue, recent.Slaves, e)
        {
            EthernetInterfaceNameValue ether(*e);
            AddSlave(target.Dev, ether);
            collection.insert(ether);
        }
        ENUM_STL(StringCollection, collection, e)
        {
            control.RefreshInterface(*e);
        }
        collection.insert(recent.Dev);
        InterfaceRefresher::Refresh(collection);
        throw error;
    }
    LOGGER_NOTICE("Set " << recent.Dev << "'s slaves done.");
}

void BondingInterfaceControl::SetMode(BondingInterface& target)
{
    if(!target.Dev.Valid())
        ERROR(Exception::Server::Params, "Dev");
    BondingInterface recent(Find(target.Dev));
    if(!target.Mode.Valid())
        ERROR(Exception::Server::Params, "Mode");
    CheckSupportMode(target.Mode, recent.Slaves);
    try
    {
        SetMode(target.Dev, target.Mode);
    } catch(ValueException& e)
    {
        SetMode(recent.Dev, recent.Mode);
        throw e;
    }
    recent.Mode = target.Mode;
    LOGGER_NOTICE("Set " << recent.Dev << "'s bonding mode done.");
}

void BondingInterfaceControl::SetLinkCheck(BondingInterface& target)
{
    if(!target.Dev.Valid())
        ERROR(Exception::Server::Params, "Dev");
    BondingInterface recent(Find(target.Dev));
    if(!target.CheckMode.Valid() && !target.Frequency.Valid())
        ERROR(Exception::Server::Params, "CheckMode Frequency");
    int checkmode = target.CheckMode.Valid() ? target.CheckMode : recent.CheckMode;
    int frequency = target.Frequency.Valid() ? target.Frequency : recent.Frequency;
    try
    {
        SetLinkCheck(target.Dev, checkmode, frequency);
    } catch(ValueException& e)
    {
        SetLinkCheck(recent.Dev, recent.CheckMode, recent.Frequency);
        throw e;
    }
    recent.CheckMode = checkmode;
    recent.Frequency = frequency;
    LOGGER_NOTICE("Set " << recent.Dev << "'s check-mode done.");
}

void BondingInterfaceControl::SetLinkCheckIP(BondingInterface& target)
{
    if(!target.Dev.Valid())
        ERROR(Exception::Server::Params, "Dev");
    BondingInterface recent(Find(target.Dev));
    if(!target.CheckIP.Valid())
        ERROR(Exception::Server::Params, "CheckIP");
    try
    {
        ENUM_LIST(HostStringValue, recent.CheckIP, e)
        {
            HostStringValue ip(*e);
            DelCheckIP(target.Dev, ip);
        }
        ENUM_LIST(HostStringValue, target.CheckIP, e)
        {
            HostStringValue ip(*e);
            AddCheckIP(target.Dev, ip);
        }
    } catch(ValueException& error)
    {
        StringList list;
        GetCheckIPList(recent.Dev, list);
        ENUM_STL(StringList, list, e)
        {
            DelCheckIP(target.Dev, *e);
        }
        ENUM_LIST(HostStringValue, recent.CheckIP, e)
        {
            HostStringValue ip(*e);
            AddCheckIP(target.Dev, ip);
        }
        throw error;
    }
    recent.CheckIP = target.CheckIP;
    LOGGER_NOTICE("Set " << recent.Dev << "'s checkip done.");
}

void BondingInterfaceControl::AddBonding(BondingInterface& target)
{
    if(!target.Dev.Valid())
        ERROR(Exception::Server::Params, "Dev");
    try
    {
        Find(target.Dev);
        ERROR(Exception::Interface::ExistDev, target.Dev);
    } catch(ValueException& e)
    {
    }
    AddBonding(target.Dev);
    LOGGER_NOTICE("Add bonding interface: " << target.Dev);
    try
    {
        SetLinkCheck(target.Dev, BondingInterface::CheckModeState::miimon, target.Frequency.Default);
        PhysicalInterfaceControl::SetEnabled(target.Dev, false);
        PhysicalInterfaceControl::SetMTU(target.Dev, target.MTU.Max);
        BondingInterface recent(Holder.Append());
        InitInterface(target.Dev, recent);
        recent.Slaves.Clear();
        recent.CheckMode = recent.CheckMode.Type.Default();
        recent.Mode = recent.Mode.Type.Default();
        recent.CheckIP.Clear();
        recent.Frequency = target.Frequency.Default;
        recent.Description = target.Description;
        InterfaceRefresher::Refresh(recent.Dev);
    } catch(ValueException& error)
    {
        PRINTF(error.getMessage() << " " << error.getCode());
        DelBonding(target.Dev);
        throw error;
    }
}

void BondingInterfaceControl::DelBonding(BondingInterface& target)
{
    int id = -1;
    {
        if(!target.Dev.Valid())
            ERROR(Exception::Server::Params, "Dev");
        BondingInterface recent(Find(target.Dev));
        StringList list;
        try
        {
            ENUM_LIST_R(EthernetInterfaceNameValue, recent.Slaves, e)
            {
                EthernetInterfaceNameValue ether(*e);
                list.push_front(ether);
                DelSlave(recent.Dev, ether);
            }
            DelBonding(recent.Dev);
            StringCollection dev;
            EthernetInterfaceControl control;
            ENUM_LIST(EthernetInterfaceNameValue, recent.Slaves, e)
            {
                EthernetInterfaceNameValue ether(*e);
                EthernetInterface interface(control.Find(ether));
                interface.Master = "";
                control.RefreshInterface(ether);
                dev.insert(ether);
            }
            id = recent.ID;
            dev.insert(recent.Dev);
            InterfaceRefresher::Refresh(dev);
        } catch(ValueException& error)
        {
            ENUM_STL(StringList, list, e)
            {
                AddSlave(recent.Dev, *e);
            }
            throw error;
        }
    }
    Holder.Delete(id);
    LOGGER_NOTICE("Del bonding interface: " << target.Dev);
}

void BondingInterfaceControl::Get(BondingInterface& target)
{
    if(!target.Dev.Valid())
        ERROR(Exception::Server::Params, "Dev");
    BondingInterface bonding(Find(target.Dev));
    target.Slaves = bonding.Slaves;
    target.Mode = bonding.Mode;
    target.CheckMode = bonding.CheckMode;
    target.Frequency = bonding.Frequency;
    target.CheckIP = bonding.CheckIP;
    PhysicalInterfaceControl::Get(target, bonding);
}

void BondingInterfaceControl::GetAll(List<BondingInterface>& list)
{
    list.Clear();
    ENUM_LIST(BondingInterface, Holder, e)
    {
        BondingInterface target(list.Append());
        BondingInterface bonding(*e);
        target.Slaves = bonding.Slaves;
        target.Mode = bonding.Mode;
        target.CheckMode = bonding.CheckMode;
        target.Frequency = bonding.Frequency;
        target.CheckIP = bonding.CheckIP;
        PhysicalInterfaceControl::Get(target, bonding);
    }
}

void BondingInterfaceControl::SetDescription(BondingInterface& target)
{
    if(!target.Dev.Valid())
        ERROR(Exception::Server::Params, "Dev");
    BondingInterface recent(Find(target.Dev));
    recent.Description = target.Description;
    LOGGER_NOTICE("Set " << recent.Dev << "'s description done.");
}

namespace Bonding
{

#define EXECUTE_RPC(methodname,methodfunc)\
    void LINE_NAME(methodfunc)(Value& params, Value& result)\
    {\
        RpcMethod::CheckLicence();\
        BondingInterfaceControl control;\
        BondingInterface interface(params);\
        control.methodfunc(interface);\
        (bool&)result = true;\
    }\
    DECLARE_RPC_METHOD(methodname,LINE_NAME(methodfunc),true,true)

    EXECUTE_RPC(FuncBondingEnabled, SetEnabled);
    EXECUTE_RPC(FuncBondingMTU, SetMTU);
    EXECUTE_RPC(FuncBondingAddress, SetAddress);
    EXECUTE_RPC(FuncBondingIP, SetIP);
    EXECUTE_RPC(FuncBondingArp, SetArp);
    EXECUTE_RPC(FuncBondingDhcp, SetDhcp);
    EXECUTE_RPC(FuncBondingMode, SetMode);
    EXECUTE_RPC(FuncBondingCheck, SetLinkCheck);
    EXECUTE_RPC(FuncBondingCheckIP, SetLinkCheckIP);
    EXECUTE_RPC(FuncBondingAdd, AddBonding);
    EXECUTE_RPC(FuncBondingDelete, DelBonding);
    EXECUTE_RPC(FuncBondingSetSlaves, SetSlaves);
    EXECUTE_RPC(FuncBondingDescription, SetDescription);

    void BondingGet(Value& params, Value& result)
    {
        RpcMethod::CheckLicence();
        BondingInterfaceControl control;
        BondingInterface interface(params);
        control.Get(interface);
        result = params;
    }

    DECLARE_RPC_METHOD(FuncBondingGet, BondingGet, true, true);

    void BondingGetAll(Value& params, Value& result)
    {
        BondingInterfaceControl control;
        List<BondingInterface> res(result);
        control.GetAll(res);
    }

    DECLARE_RPC_METHOD(FuncBondingGetAll, BondingGetAll, true, true);

    void BondingBeforeImport(Value& data, bool reload)
    {
        BondingInterfaceControl control;
        StringList bonding;
        {
            Value temp;
            List<BondingInterface> list(temp);
            control.GetAll(list);
            ENUM_LIST(BondingInterface, list, e)
            {
                BondingInterface bond(*e);
                bonding.push_back(bond.Dev);
            }
        }
        ENUM_STL(StringList, bonding, e)
        {
            Value temp;
            BondingInterface bond(temp);
            bond.Dev = *e;
            control.DelBonding(bond);
        }
    }

    void BondingImport(Value& data, bool reload)
    {
        List<BondingInterface> list(data["Bonding"]);
        BondingInterfaceControl control;
        ENUM_LIST(BondingInterface, list, e)
        {
            BondingInterface bond(*e);
            if(reload)
            {
                NO_ERROR(control.AddBonding(bond));
                NO_ERROR(control.SetMode(bond));
                NO_ERROR(control.SetSlaves(bond));
                NO_ERROR(control.SetLinkCheck(bond));
                NO_ERROR(control.SetLinkCheckIP(bond));
                NO_ERROR(control.SetMTU(bond));
                NO_ERROR(control.SetEnabled(bond));
                NO_ERROR(control.SetAddress(bond));
                NO_ERROR(control.SetIP(bond));
                NO_ERROR(control.SetDhcp(bond));
                NO_ERROR(control.SetArp(bond));
                NO_ERROR(control.SetDescription(bond));
            } else {
                control.AddBonding(bond);
                control.SetMode(bond);
                control.SetSlaves(bond);
                control.SetLinkCheck(bond);
                control.SetLinkCheckIP(bond);
                control.SetMTU(bond);
                control.SetEnabled(bond);
                control.SetAddress(bond);
                control.SetIP(bond);
                control.SetDhcp(bond);
                control.SetArp(bond);
                control.SetDescription(bond);
            }
        }
    }

    void BondingExport(Value & data)
    {
        List<BondingInterface> list(data["Bonding"]);
        BondingInterfaceControl control;
        control.GetAll(list);
        ENUM_LIST(BondingInterface, list, e)
        {
            BondingInterface bond(*e);
            bond.CurrentAddress.Data.clear();
            bond.Gate.Data.clear();
            bond.Dns.Data.clear();
            bond.Carrier.Data.clear();
        }
    }

    DECLARE_SERIALIZE(BondingBeforeImport, BondingImport, NULL, BondingExport, 1);

    void InitBonding()
    {
        Exec::System("rmmod bonding");
        Exec::System("rm -f /etc/sysconfig/network-scripts/ifcfg-bond*");
        Exec::System("modprobe bonding max_bonds=0");
    }

    DECLARE_INIT(InitBonding, NULL, 1);

};
