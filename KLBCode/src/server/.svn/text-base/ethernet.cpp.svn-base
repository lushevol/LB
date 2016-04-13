#include <fstream>

#include "share/utility.h"

#include "rpc.h"
#include "serialize.h"
#include "base.h"
#include "logger.h"

#include "ethernet.h"

using namespace std;

EthernetInterfaceControl::EthernetInterfaceControl()
    : PhysicalInterfaceControl(Configure::GetValue()["Ethernet"])
    , Holder(PhysicalInterfaceControl::Holder.Data)
{
    if(!Holder.Valid())
    {
        StringList list;
        GenerateInterfaceList(list);
        ENUM_STL(StringList, list, e)
        {
            EthernetInterface interface(Holder.Append());
            InitInterface(*e, interface);
            Refresh(interface);
        }
    }
}

void EthernetInterfaceControl::GenerateInterfaceList(StringList& list)
{
    Exec exe("ls");
    exe << "/sys/class/net";
    exe.Execute();
    fdistream stream(exe.ReadOut());
    String dev;
    while(stream >> dev)
    {
        ifstream typestream(("/sys/class/net/" + dev + "/type").c_str());
        int type;
        if(typestream >> type && type == DevSystemType)
            list.push_back(dev);
    }
}

void EthernetInterfaceControl::InitInterface(const String& dev, EthernetInterface& interface)
{
    PhysicalInterfaceControl::InitInterface(dev, interface);
    interface.RealAddress = GetAddress(dev);
    interface.Master = "";
}

void EthernetInterfaceControl::GetLink(const String& dev, bool& detect, bool& duplex, int& speed)
{
    detect = true;
    duplex = true;
    speed = 1000;
    Exec exe("ethtool");
    exe << dev;
    exe.Execute();
    fdistream stream(exe.ReadOut());
    String line;
    while(getline(stream, line))
    {
        istringstream linestream(line);
        String name, key;
        linestream >> name >> key;
        if(name == "Speed:")
        {
            if(key == "10Mb/s")
                speed = 10;
            else if(key == "100Mb/s")
                speed = 100;
            else if(key == "10000Mb/s")
                speed = 10000;
            else
                speed = 1000;
        } else if(name == "Duplex:")
        {
            duplex = key != "Half" && key != "half";
        } else if(name == "Auto-negotiation:")
            detect = key == "on";
    }
    int status;
    exe.Close(status);
    if(status != 0)
        ERROR(Exception::Interface::NotFoundDev, dev);
}

bool EthernetInterfaceControl::ExecuteEthtool(Exec& exe)
{
    bool res = true;
    exe.Execute();
    fdistream stream(exe.ReadOut());
    String line;
    while(getline(stream, line))
    {
        PRINTF(line);
        if(line == "Cannot set new settings: Operation not supported")
        {
            res = false;
            break;
        }
    }
    exe.Close();
    return res;
}

void EthernetInterfaceControl::SetLink(const String& dev, bool detect, bool duplex, int speed)
{
    if(detect)
    {
        {
            Exec exe("ethtool");
            exe << "-s" << dev << "autoneg" << "on";
            if(!ExecuteEthtool(exe))
                ERROR(Exception::Interface::Ethernet::DetectOn, dev);
        }
    } else {
        {
            Exec exe("ethtool");
            exe << "-s" << dev << "autoneg" << "off";
            if(!ExecuteEthtool(exe))
                ERROR(Exception::Interface::Ethernet::DetectOff, dev);
        }
        {
            Exec exe("ethtool");
            exe << "-s" << dev << "speed" << speed << "duplex" << (duplex ? "full" : "half");
            if(!ExecuteEthtool(exe))
                ERROR(Exception::Interface::Ethernet::DuplexSpeed, dev);
        }
    }
}

void EthernetInterfaceControl::CheckMaster(EthernetInterface& interface)
{
    if(interface.Master != "")
        ERROR(Exception::Interface::Ethernet::Slave, interface.Dev << '\n' << interface.Master);
}

void EthernetInterfaceControl::CheckAdsl(EthernetInterface& interface)
{
    if(interface.Adsl != "")
        ERROR(Exception::Interface::Ethernet::Adsl, interface.Dev << '\n' << interface.Adsl);
}

void EthernetInterfaceControl::SetLink(EthernetInterface& target, EthernetInterface& recent)
{
    bool detect;
    bool duplex;
    int speed;
    GetLink(recent.Dev, detect, duplex, speed);
    detect = target.Detect.Valid() ? target.Detect : detect;
    duplex = target.FullDuplex.Valid() ? target.FullDuplex : duplex;
    speed = target.Speed.Valid() ? target.Speed : speed;
    if(!target.Detect.Valid() && !target.FullDuplex.Valid() && !target.Speed.Valid())
        ERROR(Exception::Server::Params, "Link");
    SetLink(target.Dev, detect, duplex, speed);
    recent.Detect = detect;
}

void EthernetInterfaceControl::Refresh(EthernetInterface& interface)
{
    if(interface.Master == "" && interface.Adsl == "")
    {
        PhysicalInterfaceControl::SetEnabled(interface.Dev, false);
        NO_ERROR(SetLink(interface, interface));
        PhysicalInterfaceControl::SetAddress(interface, interface);
        PhysicalInterfaceControl::SetMTU(interface, interface);
        PhysicalInterfaceControl::SetEnabled(interface, interface);
    } else {
        FlushIP(interface.Dev);
        interface.Gate = "";
        interface.Dns = "";
    }
}

void EthernetInterfaceControl::RefreshInterface(const String& dev)
{
    EthernetInterface interface(Find(dev));
    Refresh(interface);
}

void EthernetInterfaceControl::RefreshInterface()
{
    ENUM_LIST(EthernetInterface, Holder, e)
    {
        EthernetInterface interface(*e);
        Refresh(interface);
    }
}

void EthernetInterfaceControl::SetMTU(EthernetInterface& target)
{
    if(!target.Dev.Valid())
        ERROR(Exception::Server::Params, "Dev");
    EthernetInterface recent(Find(target.Dev));
    CheckMaster(recent);
    CheckAdsl(recent);
    PhysicalInterfaceControl::SetMTU(target, recent);
    LOGGER_NOTICE("Set " << recent.Dev << "'s mtu done.");
}

void EthernetInterfaceControl::SetArp(EthernetInterface& target)
{
    if(!target.Dev.Valid())
        ERROR(Exception::Server::Params, "Dev");
    EthernetInterface recent(Find(target.Dev));
    CheckMaster(recent);
    CheckAdsl(recent);
    PhysicalInterfaceControl::SetArp(target, recent);
    LOGGER_NOTICE("Set " << recent.Dev << "'s arp mode done.");
}

void EthernetInterfaceControl::SetEnabled(EthernetInterface& target)
{
    if(!target.Dev.Valid())
        ERROR(Exception::Server::Params, "Dev");
    EthernetInterface recent(Find(target.Dev));
    CheckMaster(recent);
    CheckAdsl(recent);
    PhysicalInterfaceControl::SetEnabled(target, recent);
    InterfaceRefresher::Refresh(recent.Dev);
    LOGGER_NOTICE("Set " << recent.Dev << "'s enabled done.");
}

void EthernetInterfaceControl::SetAddress(EthernetInterface& target)
{
    if(!target.Dev.Valid())
        ERROR(Exception::Server::Params, "Dev");
    EthernetInterface recent(Find(target.Dev));
    CheckMaster(recent);
    CheckAdsl(recent);
    PhysicalInterfaceControl::SetAddress(target, recent);
    InterfaceRefresher::Refresh(recent.Dev);
    LOGGER_NOTICE("Set " << recent.Dev << "'s address done.");
}

void EthernetInterfaceControl::SetIP(EthernetInterface& target)
{
    if(!target.Dev.Valid())
        ERROR(Exception::Server::Params, "Dev");
    EthernetInterface recent(Find(target.Dev));
    CheckMaster(recent);
    CheckAdsl(recent);
    PhysicalInterfaceControl::SetIP(target, recent);
    InterfaceRefresher::Refresh(recent.Dev);
    LOGGER_NOTICE("Set " << recent.Dev << "'s ip done.");
}

void EthernetInterfaceControl::SetDhcp(EthernetInterface& target)
{
    if(!target.Dev.Valid())
        ERROR(Exception::Server::Params, "Dev");
    EthernetInterface recent(Find(target.Dev));
    CheckMaster(recent);
    CheckAdsl(recent);
    PhysicalInterfaceControl::SetDhcp(target, recent);
    InterfaceRefresher::Refresh(recent.Dev);
    LOGGER_NOTICE("Set " << recent.Dev << "'s dhcp done.");
}

void EthernetInterfaceControl::SetLink(EthernetInterface& target)
{
    if(!target.Dev.Valid())
        ERROR(Exception::Server::Params, "Dev");
    EthernetInterface recent(Find(target.Dev));
    CheckMaster(recent);
    CheckAdsl(recent);
    SetLink(target, recent);
    LOGGER_NOTICE("Set " << recent.Dev << "'s link mode done.");
}

void EthernetInterfaceControl::Get(EthernetInterface& target)
{
    if(!target.Dev.Valid())
        ERROR(Exception::Server::Params, "Dev");
    EthernetInterface eth(Find(target.Dev));
    target.Adsl = eth.Adsl;
    target.Master = eth.Master;
    target.RealAddress = eth.RealAddress;
    target.Detect = eth.Detect;
    bool detect;
    bool duplex;
    int speed;
    GetLink(eth.Dev, detect, duplex, speed);
    target.Detect = detect;
    target.FullDuplex = duplex;
    target.Speed = speed;
    PhysicalInterfaceControl::Get(target, eth);
}

void EthernetInterfaceControl::GetAll(List<EthernetInterface>& list)
{
    list.Clear();
    ENUM_LIST(EthernetInterface, Holder, e)
    {
        EthernetInterface target(list.Append());
        EthernetInterface eth(*e);
        target.Adsl = eth.Adsl;
        target.Master = eth.Master;
        target.RealAddress = eth.RealAddress;
        target.Detect = eth.Detect;
        bool detect;
        bool duplex;
        int speed;
        GetLink(eth.Dev, detect, duplex, speed);
        target.Detect = detect;
        target.FullDuplex = duplex;
        target.Speed = speed;
        PhysicalInterfaceControl::Get(target, eth);
    }
}

void EthernetInterfaceControl::GetAllDev(StringList& list)
{
    ENUM_LIST(EthernetInterface, Holder, e)
    {
        list.push_back(EthernetInterface(*e).Dev);
    }
}

void EthernetInterfaceControl::SetDescription(EthernetInterface& target)
{
    if(!target.Dev.Valid())
        ERROR(Exception::Server::Params, "Dev");
    EthernetInterface recent(Find(target.Dev));
    recent.Description = target.Description;
    LOGGER_NOTICE("Set " << recent.Dev << "'s description done.");
}

namespace Ethernet
{
#define EXECUTE_RPC(methodname,methodfunc)\
    void LINE_NAME(methodfunc)(Value& params, Value& result)\
    {\
        EthernetInterfaceControl control;\
        EthernetInterface interface(params);\
        control.methodfunc(interface);\
        (bool&)result = true;\
    }\
    DECLARE_RPC_METHOD(methodname,LINE_NAME(methodfunc),true,true)

    EXECUTE_RPC(FuncEthernetEnabled, SetEnabled);
    EXECUTE_RPC(FuncEthernetMTU, SetMTU);
    EXECUTE_RPC(FuncEthernetAddress, SetAddress);
    EXECUTE_RPC(FuncEthernetIP, SetIP);
    EXECUTE_RPC(FuncEthernetArp, SetArp);
    EXECUTE_RPC(FuncEthernetDhcp, SetDhcp);
    EXECUTE_RPC(FuncEthernetLink, SetLink);
    EXECUTE_RPC(FuncEthernetDescription, SetDescription);

    void EthernetGet(Value& params, Value& result)
    {
        EthernetInterfaceControl control;
        EthernetInterface interface(params);
        control.Get(interface);
        result = params;
    }

    DECLARE_RPC_METHOD(FuncEthernetGet, EthernetGet, true, true)

    void EthernetGetAll(Value& params, Value& result)
    {
        EthernetInterfaceControl control;
        List<EthernetInterface> res(result);
        control.GetAll(res);
    }

    DECLARE_RPC_METHOD(FuncEthernetGetAll, EthernetGetAll, true, true);

    void EthernetBeforeImport(Value& data, bool reload)
    {
        StringList devlist;
        EthernetInterfaceControl control;
        control.GetAllDev(devlist);
        ENUM_STL(StringList, devlist, dev)
        {
            Value temp;
            EthernetInterface target(temp);
            target.Dev = *dev;
            target.Dhcp = false;
            control.SetDhcp(target);
        }
    }

    void EthernetImport(Value& data, bool reload)
    {
        List<EthernetInterface> list(data["Ethernet"]);
        EthernetInterfaceControl control;
        ENUM_LIST(EthernetInterface, list, e)
        {
            EthernetInterface eth(*e);
            if(reload)
            {
                NO_ERROR(control.SetLink(eth));
                NO_ERROR(control.SetMTU(eth));
                NO_ERROR(control.SetEnabled(eth));
                NO_ERROR(control.SetAddress(eth));
                NO_ERROR(control.SetIP(eth));
                NO_ERROR(control.SetDhcp(eth));
                NO_ERROR(control.SetArp(eth));
                NO_ERROR(control.SetDescription(eth));
            } else {
                NO_ERROR(control.SetLink(eth));
                control.SetMTU(eth);
                control.SetEnabled(eth);
                control.SetAddress(eth);
                control.SetIP(eth);
                control.SetDhcp(eth);
                control.SetArp(eth);
                control.SetDescription(eth);
            }
        }
    }

    void EthernetExport(Value& data)
    {
        List<EthernetInterface> list(data["Ethernet"]);
        EthernetInterfaceControl control;
        control.GetAll(list);
        ENUM_LIST(EthernetInterface, list, e)
        {
            EthernetInterface eth(*e);
            eth.Master.Data.clear();
            eth.Adsl.Data.clear();
            eth.RealAddress.Data.clear();
            eth.CurrentAddress.Data.clear();
            eth.Gate.Data.clear();
            eth.Dns.Data.clear();
            eth.Carrier.Data.clear();
        }
    }

    DECLARE_SERIALIZE(EthernetBeforeImport, EthernetImport, NULL, EthernetExport, 0);

    void InitEthernet()
    {
        Exec::System("rmmod bonding");
        EthernetInterfaceControl control;
        StringList list;
        control.GetAllDev(list);
        ENUM_STL(StringList, list, e)
        {
            Exec::System("tc qdisc del dev " + *e + " root");
        }
    }

    DECLARE_INIT(InitEthernet, NULL, 0);
};
