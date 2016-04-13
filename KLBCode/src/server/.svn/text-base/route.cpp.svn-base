#include <sstream>

#include "share/include.h"
#include "share/utility.h"

#include "base.h"

#include "network.h"
#include "interface.h"
#include "ethernet.h"
#include "bonding.h"
#include "route.h"
#include "staticroute.h"
#include "policyroute.h"
#include "smartroute.h"
#include "adsl.h"

using namespace std;

void RouteControl::GenerateCMDGate(OStream& cmd, GateItem& gate)
{
    if(gate.Status)
    {
        cmd << " nexthop";
        if(gate.IP != "")
            cmd << " via " << gate.IP;
        cmd << " dev " << gate.Dev << " weight " << gate.Weight;
    }
}

void RouteControl::GenerateCmdPath(OStream& cmd, RouteItem& route)
{
    if(route.Link)
        cmd << " scope link";
    else
        cmd << " scope global";
    cmd << " proto static ";
}

void RouteControl::GenerateCMD(OStream& cmd, RouteItem& route, bool isadd)
{
    cmd << "ip route " << (isadd ? "add" : "replace") << " ";
    GenerateCMDTarget(cmd, route);
    GenerateCmdPath(cmd, route);
    ENUM_LIST(GateItem, route.Gates, e)
    {
        GateItem gate(*e);
        GenerateCMDGate(cmd, gate);
    }
}

void RouteControl::RefreshRoute(RouteItem& recent)
{
    DoDelRoute(recent);
    TryAddRotue(recent);
    OnRefreshRoute(recent);
}

void RouteControl::TryAddRotue(RouteItem& route)
{
    route.Status = false;
    ENUM_LIST(GateItem, route.Gates, e)
    {
        GateItem(*e).Status = false;
    }
    bool exist = false;
    ENUM_LIST(GateItem, route.Gates, e)
    {
        GateItem gate(*e);
        switch(Network::GetDevType(gate.Dev))
        {
            case Network::Ethernet:
                {
                    EthernetInterfaceControl control;
                    EthernetInterface eth(control.Find(gate.Dev));
                    if(eth.Master != "" || !eth.Enabled || eth.Adsl != "")
                        continue;
                    if(gate.Auto)
                    {
                        if(!eth.Dhcp)
                            continue;
                        if(eth.Gate == "")
                            continue;
                        gate.IP = eth.Gate;
                    }
                }
                break;
            case Network::Bonding:
                {
                    BondingInterfaceControl control;
                    BondingInterface bond(control.Find(gate.Dev));
                    if(!bond.Enabled || bond.Slaves.IsEmpty())
                        continue;
                    if(gate.Auto)
                    {
                        if(!bond.Dhcp)
                            continue;
                        if(bond.Gate == "")
                            continue;
                        gate.IP = bond.Gate;
                    }
                }
                break;
            case Network::Adsl:
                {
                    AdslControl control;
                    AdslItem adsl(control.Find(gate.Dev));
                    if(adsl.Status != AdslItem::DialState::Connected)
                        continue;
                    if(gate.Auto)
                    {
                        if(adsl.Gate == "")
                            continue;
                        gate.IP = adsl.Gate;
                    }
                }
                break;
            default:
                continue;
        };
        gate.Status = true;
        ostringstream cmd;
        GenerateCMD(cmd, route, !exist);
        if(Exec::System(cmd.str()) != 0)
            gate.Status = false;
        else
            exist = true;
    }
    route.Status = exist;
}

void RouteControl::RefreshDev(const StringCollection& dev)
{
    typedef deque<Value*> RouteList;
    RouteList base;
    RouteList other;
    ENUM_LIST(RouteItem, Holder, e)
    {
        RouteItem recent(*e);
        bool ismatch = false;
        ENUM_LIST(GateItem, recent.Gates, g)
        {
            GateItem gate(*g);
            if(dev.count(gate.Dev))
            {
                ismatch = true;
                break;
            }
        }
        if(ismatch)
        {
            if(recent.Link)
                base.push_back(&(*e).Data);
            else
                other.push_back(&(*e).Data);
        }
    }
    ENUM_STL(RouteList, base, e)
    {
        RouteItem recent(**e);
        RefreshRoute(recent);
    }
    ENUM_STL(RouteList, other, e)
    {
        RouteItem recent(**e);
        RefreshRoute(recent);
    }
    OnRefreshRouteDone();
}

void RouteControl::RefreshGate(const StringCollection& net)
{
    typedef pair<uint32_t, uint32_t> Pack;
    typedef deque<Pack> PackList;
    PackList list;
    ENUM_STL_CONST(StringCollection, net, e)
    {
        Pack pack;
        int mask;
        VERIFY(Address::StringToAddressPack(*e, pack.first, mask));
        VERIFY(Address::NetmaskToAddress(mask, pack.second));
        list.push_back(pack);
    }
    ENUM_LIST(RouteItem, Holder, r)
    {
        bool match = false;
        RouteItem route(*r);
        ENUM_LIST(GateItem, route.Gates, g)
        {
            GateItem gate(*g);
            if(!gate.Auto && gate.IP != "")
            {
                uint32_t ip;
                VERIFY(Address::StringToAddress(gate.IP, ip));
                ENUM_STL_CONST(PackList, list, e)
                {
                    if((ip & e->second) == e->first)
                    {
                        PRINTF("MatchGate: " << gate.IP);
                        match = true;
                        break;
                    }
                }
                if(match)
                    break;
            }
        }
        if(match)
            RefreshRoute(route);
    }
    OnRefreshRouteDone();
}

String RouteControl::GateToDev(const String& gate)
{
    if(Exec::System("ip route add default via " + gate + " table 251") != 0)
    {
        StaticRouteControl control;
        ENUM_LIST(StaticRouteItem, control.Holder, e)
        {
            StaticRouteItem route(*e);
            if(route.Link && !route.Gates.IsEmpty())
            {
                uint32_t net, mask;
                int netmask;
                VERIFY(Address::StringToAddressPack(route.Net, net, netmask));
                VERIFY(Address::NetmaskToAddress(netmask, mask));
                uint32_t ip;
                VERIFY(Address::StringToAddress(gate, ip));
                if((ip & mask) == net)
                    return GateItem(route.Gates.Head()).Dev;
            }
        }
        return "";
    }
    Exec exe("ip");
    exe << "route" << "show" << "table" << "251";
    exe.Execute();
    fdistream stream(exe.ReadOut());
    String dev;
    while(stream >> dev)
        {};
    Exec::System("ip route flush table 251");
    if(Network::GetDevType(dev) == Network::Unknown)
        return "";
    return dev;
}

void RouteControl::InitGate(RouteItem& target)
{
    bool link = true;
    ENUM_LIST(GateItem, target.Gates, e)
    {
        GateItem gate(*e);
        if(!gate.Auto.Valid())
            gate.Auto = false;
        if(!gate.Weight.Valid())
            gate.Weight = 1;
        if(!gate.IP.Valid())
            gate.IP = "";
        if(gate.Dev == "" && !gate.Auto && gate.IP != "")
        {
            gate.Dev = GateToDev(gate.IP);
            if(gate.Dev == "")
                ERROR(Exception::Route::Gate::DetectDev, gate.IP);
        }
        if(gate.Dev == "")
            ERROR(Exception::Route::Gate::EmptyDev, "");
        if(gate.Auto || gate.IP != "")
            link = false;
    }
    target.Link = link;
}

void RouteControl::AddRoute(RouteItem& target)
{
    InitGate(target);
    TryAddRotue(target);
    Holder.Insert(target.ID);
    RouteItem recent(Holder.Get(target.ID));
    recent.Gates = target.Gates;
    recent.GatePolicy = target.GatePolicy.Valid() ? target.GatePolicy : recent.GatePolicy.Type.Default();
    recent.Status = target.Status;
    recent.Description = target.Description;
    RefreshID();
}

void RouteControl::SetRoute(RouteItem& target, RouteItem& recent)
{
    if(!target.Gates.Valid())
        target.Gates = recent.Gates;
    if(!target.GatePolicy.Valid())
        target.GatePolicy = recent.GatePolicy;
    InitGate(target);
    if(recent.Status)
        DoDelRoute(recent);
    TryAddRotue(target);
    recent.Gates = target.Gates;
    recent.GatePolicy = target.GatePolicy;
    recent.Status = target.Status;
}

void RouteControl::DoDelRoute(RouteItem& recent)
{
    ostringstream del;
    del << "ip route del ";
    GenerateCMDTarget(del, recent);
    del << " proto static";
    Exec::System(del.str());
}

void RouteControl::DelRoute(RouteItem& recent)
{
    if(recent.Status)
        DoDelRoute(recent);
    Holder.Delete(recent.ID);
    RefreshID();
}

void RouteControl::RefreshID()
{
    int count = 0;
    ENUM_LIST(RouteItem, Holder, e)
    {
        RouteItem route(*e);
        route.ID = count++;
    }
}

RouteControl::RouteControl(Value& holder)
    : Holder(holder)
{
    if(!Holder.Valid())
        Holder.Clear();
}

Value& RouteControl::Find(int index)
{
    RouteItem route(Holder.Get(index));
    route.ID = index;
    return route.Data;
}

int RouteControl::GetCount()
{
    return Holder.GetCount();
}

void RouteControl::GetCount(IntValue& result)
{
    result = Holder.GetCount();
}

void RouteControl::Get(RouteItem& route, RouteItem& recent)
{
    route.ID = recent.ID;
    route.Gates = recent.Gates;
    route.GatePolicy = recent.GatePolicy;
    route.Status = recent.Status;
    route.Description = recent.Description;
}

void RouteControl::RefreshAll(const String& dev)
{
    StringCollection devlist;
    devlist.insert(dev);
    RefreshAll(devlist);
}

void RouteControl::RefreshAll(const StringCollection& dev)
{
    StaticRouteControl().RefreshDev(dev);
    PolicyRouteControl().RefreshDev(dev);
    SmartRouteControl().RefreshDev(dev);
}

void RouteControl::RefreshRecursion(const String& net)
{
    StringCollection netlist;
    netlist.insert(net);
    RefreshRecursion(netlist);
}

void RouteControl::RefreshRecursion(const StringCollection& net)
{
    StaticRouteControl().RefreshGate(net);
    PolicyRouteControl().RefreshGate(net);
    SmartRouteControl().RefreshGate(net);
}

namespace Route
{

    DECLARE_INTERFACE_REFRESH(RouteControl::RefreshAll, 0);

    void InitRoute()
    {
        Exec::System("echo 1 >> /proc/sys/net/ipv4/ip_forward");
        Exec::System("ip rule flush");
        Exec::System("ip route flush table 255");
        Exec::System("ip route flush table 254");
        Exec::System("ip route flush table 253");
        Exec::System("ip route flush table 252");
        Exec::System("ip rule add pref 32767 table 252");//smartroute
        Exec::System("ip rule add pref 32766 table 253");
        Exec::System("ip rule add pref 32765 table 254");
    }

    DECLARE_INIT(InitRoute, NULL, 0);

};
