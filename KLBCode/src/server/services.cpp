#include <map>
#include <set>
#include <sstream>
#include <fstream>

#include "share/include.h"
#include "share/utility.h"

#include "rpc.h"
#include "serialize.h"
#include "base.h"
#include "logger.h"

#include "services.h"
#include "hvs.h"
#include "ha.h"
#include "network.h"
#include "ethernet.h"
#include "bonding.h"
#include "recorder.h"

using namespace std;

VirtualServiceControl::AddressCounter VirtualServiceControl::FAddressCounter;
VirtualServiceControl::MangleCounter VirtualServiceControl::FMangleCounter;

void VirtualServiceControl::AddMangleCounter(const String& ip)
{
    MangleCounter::iterator pos = FMangleCounter.find(ip);
    if(pos == FMangleCounter.end())
    {
        FMangleCounter.insert(MangleCounter::value_type(ip, 1));
        Exec::System("iptables -t mangle -N IN_" + ip);
        Exec::System("iptables -t mangle -F IN_" + ip);
        Exec::System("iptables -t mangle -I SERVICES_IN -d " + ip + "/32 -j IN_" + ip);
        Exec::System("iptables -t mangle -N OUT_" + ip);
        Exec::System("iptables -t mangle -F OUT_" + ip);
        Exec::System("iptables -t mangle -I SERVICES_OUT -s " + ip + "/32 -j OUT_" + ip);
        Exec::System("iptables -t filter -A INPUT -d " + ip + "/32 -m mark --mark 0 ! -p icmp -j REJECT");
    } else
        ++(pos->second);
}

void VirtualServiceControl::DelMangleCounter(const String& ip)
{
    MangleCounter::iterator pos = FMangleCounter.find(ip);
    if(--(pos->second) == 0)
    {
        FMangleCounter.erase(pos);
        Exec::System("iptables -t filter -D INPUT -d " + ip + "/32 -m mark --mark 0 ! -p icmp -j REJECT");
        Exec::System("iptables -t mangle -D SERVICES_IN -d " + ip + "/32 -j IN_" + ip);
        Exec::System("iptables -t mangle -F IN_" + ip);
        Exec::System("iptables -t mangle -X IN_" + ip);
        Exec::System("iptables -t mangle -D SERVICES_OUT -s " + ip + "/32 -j OUT_" + ip);
        Exec::System("iptables -t mangle -F OUT_" + ip);
        Exec::System("iptables -t mangle -X OUT_" + ip);
    }
}

bool VirtualServiceControl::CheckIP(const String& ip, List<HostPackStringValue>& iplist)
{
    if(iplist.IsEmpty())
        return false;
    uint32_t ipaddr;
    VERIFY(Address::StringToAddress(ip, ipaddr));
    ENUM_LIST(HostPackStringValue, iplist, e)
    {
        HostPackStringValue ippack(*e);
        uint32_t packaddr, maskaddr;
        int mask;
        VERIFY(Address::StringToAddressPack(ippack, packaddr, mask));
        VERIFY(Address::NetmaskToAddress(mask, maskaddr));
        if(ipaddr == packaddr || ((ipaddr & maskaddr) != (packaddr & maskaddr)))
            return false;
    }
    return true;
}

bool VirtualServiceControl::CheckAddress(const AddressPair& address)
{
    bool result = false;
    switch(Network::GetDevType(address.second))
    {
        case Network::Ethernet:
            {
                EthernetInterfaceControl control;
                EthernetInterface eth(control.Find(address.second));
                if(eth.Master == "" && eth.Adsl == "" && !eth.Dhcp && eth.Enabled)
                    result = CheckIP(address.first, eth.IP);
            }
            break;
        case Network::Bonding:
            {
                BondingInterfaceControl control;
                BondingInterface bond(control.Find(address.second));
                if(!bond.Dhcp && !bond.Slaves.IsEmpty() && bond.Enabled)
                    result = CheckIP(address.first, bond.IP);
            }
            break;
        default:
            break;
    };
    return result;
}

void VirtualServiceControl::DoAddIP(const String& ip, const String& dev)
{
    Exec::System("ip address add " + ip + "/32 dev " + dev + " label " + dev + ":vs");
    PhysicalInterfaceControl::SendArp(ip, dev);
}

void VirtualServiceControl::DoRemoveIP(const String& ip, const String& dev)
{
    Exec::System("ip address del " + ip + "/32 dev " + dev + " label " + dev + ":vs");
}

bool VirtualServiceControl::AddIPCounter(const String& ip, const String& dev)
{
    AddressPair address(ip, dev);
    AddressCounter::iterator pos = FAddressCounter.find(address);
    if(pos == FAddressCounter.end())
    {
        FAddressCounter[address] = 1;
        return true;
    } else {
        ++(pos->second);
        return false;
    }
}

bool VirtualServiceControl::DelIPCounter(const String& ip, const String& dev)
{
    AddressCounter::iterator pos = FAddressCounter.find(AddressPair::pair(ip, dev));
    ASSERT(pos != FAddressCounter.end());
    if(--(pos->second) == 0)
    {
        FAddressCounter.erase(pos);
        return true;
    } else
        return false;
}

VirtualServiceControl::VirtualServiceControl()
    : Holder(Configure::GetValue()["Services"])
{}

void VirtualServiceControl::GeneratePortSet(List<PortItem>& ports, PortSet& set)
{
    set.reset(false);
    ENUM_LIST(PortItem, ports, e)
    {
        PortItem port(*e);
        if(port.Min == 0)
        {
            ERROR(Exception::IPVS::Service::Ports, port.Min << '\n' << port.Max);
        } else {
            if(port.Max >= port.Min)
            {
                for(int i = port.Min; i <= port.Max; ++i)
                    set.set(i, true);
            } else if(port.Max == 0)
                set.set(port.Min, true);
            else
                ERROR(Exception::IPVS::Service::Ports, port.Min << '\n' << port.Max);
        }
    }
}

void VirtualServiceControl::GeneratePortList(const PortSet& set, List<PortItem>& ports)
{
    ports.Clear();
    for(int left = 1; left < PortCount; ++left)
    {
        if(set[left])
        {
            int right = left + 1;
            while(right < PortCount && set[right])
                ++right;
            PortItem port(ports.Append());
            port.Min = left;
            port.Max = right - 1;
            left = right;
        }
    }
}

void VirtualServiceControl::GenerateIPSet(List<IPItem>& IP, StringCollection& set)
{
    set.clear();
    ENUM_LIST(IPItem, IP, e)
    {
        IPItem item(*e);
        if(item.IP == "")
            ERROR(Exception::IPVS::Service::IP::Empty, "");
        set.insert(item.IP);
    }
}

bool VirtualServiceControl::CrossCheck(StringCollection& a, StringCollection& b)
{
    StringCollection& left = (a.size() > b.size()) ? a : b;
    StringCollection& right = (a.size() <= b.size()) ? a : b;
    ENUM_STL(StringCollection, right, e)
    {
        if(left.count(*e))
            return true;
    }
    return false;
}

bool VirtualServiceControl::CrossCheck(PortSet& a, PortSet& b)
{
    return (a & b).any();
}

String VirtualServiceControl::GenerateConfPath(int mark)
{
    ostringstream stream;
    stream << "/tmp/klb-director-" << mark << ".conf";
    return stream.str();
}

void VirtualServiceControl::GenerateConf(const String& path, VirtualServiceItem& service)
{
    ofstream stream(path.c_str());
    if(stream)
    {
        ServiceType type = CheckSupportPortMap(service);
        stream << "checktimeout=" << service.Monitor.Timeout << endl;
        stream << "checkinterval=" << service.Monitor.Interval << endl;
        stream << "failurecount=" << service.Monitor.Retry << endl;
        stream << "autoreload=no" << endl;
        stream << "quiescent=no" << endl;
	if(service.Monitor.Enabled)
	{
        	stream << "emailalert=" << service.Monitor.Mail << endl;
        	stream << "emailalertfreq=" << service.Monitor.Date << endl;
        	stream << "emailalertstatus=all" << endl;
	}
        switch(type)
        {
            case TCP:
                stream << "virtual=" << (const String&)(service.IP.Head().IP) << ":" << service.TcpPorts.Head().Min << endl;
                break;
            case UDP:
                stream << "virtual=" << (const String&)(service.IP.Head().IP) << ":" << service.UdpPorts.Head().Min << endl;
                break;
            default:
                ASSERT(type == Mark);
                stream << "virtual=" << service.Mark << endl;
        };
        ENUM_LIST(ServerItem, service.Servers, e)
        {
            ServerItem server(*e);
            if(server.Enabled)
            {
                stream << '\t'
                       << "real=" << server.IP;
                if(type != Mark)
                {
                    stream << ':';
                    if(server.Action == ActionState::Masq && server.MapPort != 0)
                        stream << server.MapPort;
                    else {
                        switch(type)
                        {
                            case TCP:
                                stream << service.TcpPorts.Head().Min;
                                break;
                            case UDP:
                                stream << service.UdpPorts.Head().Min;
                                break;
                            default:
                                ASSERT(false);
                        }
                    }
                }
                stream << ' ' << server.Action.Str()
                       << ' ' << server.Weight
                       << endl;
            }
        }
        stream << '\t' << "scheduler=" << service.Schedule.Str() << endl;
        switch(type)
        {
            case TCP:
                stream << '\t' << "protocol=tcp" << endl;
                break;
            case UDP:
                stream << '\t' << "protocol=udp" << endl;
                break;
            default:
                ASSERT(type == Mark);
                stream << '\t' << "protocol=fwm" << endl;
        };
        switch(service.Monitor.Type)
        {
            case TypeState::Connect:
                stream << '\t' << "checktype=connect" << endl;
                stream << '\t' << "checkport=" << service.Monitor.Port << endl;
                break;
            case TypeState::Ping:
                stream << '\t' << "checktype=ping" << endl;
                break;
            default:
                stream << '\t' << "checktype=on" << endl;
        }
        if(service.Persistent)
        {
            stream << '\t' << "persistent=" << service.PersistentTimeout << endl;
            stream << '\t' << "netmask=" << service.PersistentNetmask << endl;
        }
        stream.close();
    }
}

void VirtualServiceControl::StartService(VirtualServiceItem& recent)
{
    String path(GenerateConfPath(recent.Mark));
    GenerateConf(path, recent);
    String status = "ldirectord " + path + " status";
    do
    {
        Exec exe("ldirectord");
        exe << path << "start";
        exe.Execute();
        exe.Close();
        if(!Exec::System(status))
            break;
        else
            sched_yield();
    } while(true);
}

void VirtualServiceControl::StopService(VirtualServiceItem& recent)
{
    String path(GenerateConfPath(recent.Mark));
    String stop = "ldirectord " + path + " stop";
    String status = "ldirectord " + path + " status";
    do
    {
        Exec::System(stop);
        sched_yield();
        if(Exec::System(status))
            break;
    } while(true);
    unlink(path.c_str());
}

void VirtualServiceControl::ReloadService(VirtualServiceItem& recent)
{
    String path(GenerateConfPath(recent.Mark));
    GenerateConf(path, recent);
    Exec::System("ldirectord " + path + " reload");
}

void VirtualServiceControl::GenerateMatchPortCmd(List<PortItem>& ports, String& cmd)
{
    ostringstream stream;
    bool first = true;
    ENUM_LIST(PortItem, ports, e)
    {
        if(first)
            first = false;
        else
            stream << ",";
        PortItem port(*e);
        stream << port.Min;
        if(port.Max > port.Min)
            stream << ":" << port.Max;
    }
    if(first)
        cmd = "";
    else
        cmd = stream.str();
}

void VirtualServiceControl::ChangeMangle(const String& ip, const String& tcp, const String& udp, int mark, bool add)
{
    if(add)
        AddMangleCounter(ip);
    if(!tcp.empty())
    {
        {
            ostringstream stream;
            stream << "iptables -t mangle " << (add ? "-I" : "-D") << " IN_" << ip << " -p tcp -m mark --mark 0 -m multiport --dport " << tcp << " -j IN_" << mark;
            Exec::System(stream.str());
        }
        {
            ostringstream stream;
            stream << "iptables -t mangle " << (add ? "-I" : "-D") << " OUT_" << ip << " -p tcp -m mark --mark 0 -m multiport --sport " << tcp << " -j OUT_" << mark;
            Exec::System(stream.str());
        }
    }
    if(!udp.empty())
    {
        {
            ostringstream stream;
            stream << "iptables -t mangle " << (add ? "-I" : "-D") << " IN_" << ip << " -p udp -m mark --mark 0 -m multiport --dport " << udp << " -j IN_" << mark;
            Exec::System(stream.str());
        }
        {
            ostringstream stream;
            stream << "iptables -t mangle " << (add ? "-I" : "-D") << " OUT_" << ip << " -p udp -m mark --mark 0 -m multiport --sport " << udp << " -j OUT_" << mark;
            Exec::System(stream.str());
        }
    }
    if(!add)
        DelMangleCounter(ip);
}

void VirtualServiceControl::ChangeMarkTarget(int mark, bool add)
{
    String str(IntToString(mark));
    if(add)
    {
        Exec::System("iptables -t mangle -N IN_" + str);
        Exec::System("iptables -t mangle -F IN_" + str);
        Exec::System("iptables -t mangle -A IN_" + str + " -j MARK --set-mark " + str);
        Exec::System("iptables -t mangle -A IN_" + str + " -j IMQ --todev 0");
        Exec::System("iptables -t mangle -A IN_" + str + " -j ACCEPT");
        Exec::System("iptables -t mangle -N OUT_" + str);
        Exec::System("iptables -t mangle -F OUT_" + str);
        Exec::System("iptables -t mangle -A OUT_" + str + " -j MARK --set-mark " + str);
        Exec::System("iptables -t mangle -A OUT_" + str + " -j IMQ --todev 1");
        Exec::System("iptables -t mangle -A OUT_" + str + " -j ACCEPT");
    } else {
        Exec::System("iptables -t mangle -F IN_" + str);
        Exec::System("iptables -t mangle -X IN_" + str);
        Exec::System("iptables -t mangle -F OUT_" + str);
        Exec::System("iptables -t mangle -X OUT_" + str);
    }
}

void VirtualServiceControl::ChangeAddress(VirtualServiceItem& recent, bool add)
{
    String tcp;
    GenerateMatchPortCmd(recent.TcpPorts, tcp);
    String udp;
    GenerateMatchPortCmd(recent.UdpPorts, udp);
    {
        ostringstream stream;
        stream << "ip rule " << (add ? "add" : "del") << " fwmark " << recent.Mark << " table 1";
        Exec::System(stream.str());
    }
    if(tcp.empty() && udp.empty())
        return;
    ENUM_LIST(IPItem, recent.IP, e)
    {
        IPItem item(*e);
        if(add && item.Dev != "")
            if(AddIPCounter(item.IP, item.Dev) && item.Status)
                DoAddIP(item.IP, item.Dev);
        if(item.Status)
            ChangeMangle(item.IP, tcp, udp, recent.Mark, add);
        if(!add && item.Dev != "")
            if(DelIPCounter(item.IP, item.Dev) && item.Status)
                DoRemoveIP(item.IP, item.Dev);
    }
}

void VirtualServiceControl::GenerateMark(VirtualServiceItem& service)
{
    if(service.Mark.Valid())
    {
        ENUM_LIST(VirtualServiceItem, Holder, e)
        {
            VirtualServiceItem recent(*e);
            if(recent.Mark == service.Mark)
                ERROR(Exception::IPVS::Service::DuplicateMark, service.Mark);
        }
    } else {
        if(Holder.GetCount() >= service.Mark.Max - service.Mark.Min + 1)
            ERROR(Exception::IPVS::Service::Count, "");
        IntCollection marks;
        ENUM_LIST(VirtualServiceItem, Holder, e)
        {
            VirtualServiceItem recent(*e);
            marks.insert(recent.Mark);
        }
        for(int mark = service.Mark.Min; mark <= service.Mark.Max; ++mark)
            if(marks.count(mark) == 0)
            {
                service.Mark = mark;
                break;
            }
    }
}

void VirtualServiceControl::GenerateServerStatus(List<VirtualServiceItem>& services)
{
    ENUM_LIST(VirtualServiceItem, services, e)
    {
        VirtualServiceItem service(*e);
        ENUM_LIST(ServerItem, service.Servers, s)
        {
            ServerItem server(*s);
            server.Status = false;
            server.Active = 0;
            server.InActive = 0;
        }
    }
    Exec exe("ipvsadm");
    exe << "-ln";
    exe.Execute();
    fdistream stream(exe.ReadOut());
    String line;
    getline(stream, line);
    getline(stream, line);
    getline(stream, line);
    String tag;
    Value* target = NULL;
    while(stream >> tag)
    {
        ASSERT(tag == "FWM" || tag == "->" || tag == "TCP" || tag == "UDP");
        if(tag == "FWM")
        {
            target = NULL;
            int mark;
            VERIFY(stream >> mark);
            ENUM_LIST(VirtualServiceItem, services, e)
            {
                if(e->Mark == mark)
                {
                    target = &(e->Data);
                    break;
                }
            }
        } else if(tag == "TCP" || tag == "UDP")
        {
            target = NULL;
            String address;
            DEBUG_PRINT("VirtualService", "Try to find: " << tag << " " << address << "  vs");
            VERIFY(stream >> address);
            String::size_type pos = address.find(':');
            ASSERT(pos != address.npos);
            String ip = address.substr(0, pos);
            String port = address.substr(pos + 1, address.size() - pos - 1);
            ENUM_LIST(VirtualServiceItem, services, e)
            {
                if(CheckSupportPortMap(*e) != Mark && e->IP.Head().IP == ip)
                {
                    if((tag == "TCP" && IntToString(e->TcpPorts.Head().Min) == port) | (tag == "UDP" && IntToString(e->UdpPorts.Head().Min) == port))
                    {
                        target = &(e->Data);
                        DEBUG_PRINT("VirtualService", "Oh yeah,  found..................");
                        break;
                    }
                }
            }
        } else if(target)
        {
            String ip;
            VERIFY(stream >> ip);
            ip = ip.substr(0, ip.find(':'));
            VirtualServiceItem service(*target);
            ENUM_LIST(ServerItem, service.Servers, e)
            {
                ServerItem server(*e);
                if(server.IP == ip)
                {
                    String temp;
                    int weight, active, inactive;
                    VERIFY(stream >> temp >> weight >> active >> inactive);
                    server.Status = weight > 0;
                    server.Active = active;
                    server.InActive = inactive;
                    break;
                }
            }
        }
        getline(stream, line);
    }
}

void VirtualServiceControl::SetTraffic(int mark, int up, int down)
{
    String markstr(IntToString(mark));
    static const int range = VirtualServiceItem::MarkValue::Max - VirtualServiceItem::MarkValue::Min + 1;
    static const int delta = VirtualServiceItem::MarkValue::Min - 1;
    ASSERT(VirtualServiceItem::MarkValue::Min - delta > 0);
    ASSERT(VirtualServiceItem::MarkValue::Max - delta + range <= 9999);
    String id(IntToString(mark - delta));
    String qid(IntToString(mark - delta + range));
    Exec::System("tc filter del dev imq0 parent 1: pref " + markstr);
    Exec::System("tc class del dev imq0 parent 1: classid 1:" + id);
    if(up != 0)
    {
        Exec::System("tc class add dev imq0 parent 1: classid 1:" + id + " htb rate " + IntToString(up) + "kbit");
        Exec::System("tc qdisc add dev imq0 parent 1:" + id + " handle " + qid + ": sfq");
        Exec::System("tc filter add dev imq0 parent 1: pref " + markstr + " protocol ip handle " + markstr + " fw flowid 1:" + id);
    }
    Exec::System("tc filter del dev imq1 parent 1: pref " + markstr);
    Exec::System("tc class del dev imq1 parent 1: classid 1:" + id);
    if(down != 0)
    {
        Exec::System("tc class add dev imq1 parent 1: classid 1:" + id + " htb rate " + IntToString(down) + "kbit");
        Exec::System("tc qdisc add dev imq1 parent 1:" + id + " handle " + qid + ": sfq");
        Exec::System("tc filter add dev imq1 parent 1: pref " + markstr + " protocol ip handle " + markstr + " fw flowid 1:" + id);
    }
}

void VirtualServiceControl::EnsureServiceID(VirtualServiceItem& service)
{
    if(!service.ID.Valid() && !service.Name.Valid())
        ERROR(Exception::Server::Params, "ID Name");
    if(service.ID.Valid())
        return;
    String name = service.Name;
    if(name.empty())
        ERROR(Exception::IPVS::Service::Name::Empty, "");
    bool all = true;
    ENUM_STL_CONST(String, name, e)
    {
        if(!isdigit(*e))
        {
            all = false;
            break;
        }
    }
    if(all)
    {
        istringstream stream(name);
        int id = -1;
        if(!(stream >> id))
            ERROR(Exception::IPVS::Service::Name::Invalid, name);
        service.ID = id;
        service.Name = Holder.Get(id).Name;
    } else {
        ENUM_LIST(VirtualServiceItem, Holder, e)
        {
            VirtualServiceItem recent(*e);
            if(recent.Name == name)
            {
                service.ID = recent.ID;
                break;
            }
        }
        if(!service.ID.Valid())
            ERROR(Exception::IPVS::Service::Name::NotFound, name);
    }
}

bool VirtualServiceControl::CheckName(const String& value)
{
    if(value.empty())
        return true;
    bool all = true;
    ENUM_STL_CONST(String, value, e)
    {
        if(!isdigit(*e))
            all = false;
        if(!isalnum(*e) && *e != '_' && *e != '-')
            return false;
    }
    if(all)
        return false;
    return true;
}

void VirtualServiceControl::CheckServiceName(const String& value, int id)
{
    if(!CheckName(value))
        ERROR(Exception::IPVS::Service::Name::Invalid, value);
    ENUM_LIST(VirtualServiceItem, Holder, e)
    {
        VirtualServiceItem recent(*e);
        if(recent.ID != id && recent.Name == value)
            ERROR(Exception::IPVS::Service::Name::Duplicate, value);
    }
}

void VirtualServiceControl::EnsureServerID(VirtualServiceItem& recent, List<ServerItem>& list)
{
    typedef map<String, int> IDMap;
    IDMap idmap;
    ENUM_LIST(ServerItem, recent.Servers, e)
    {
        ServerItem server(*e);
        if(server.Name != "")
            idmap.insert(IDMap::value_type(server.Name, server.ID));
    }
    ENUM_LIST(ServerItem, list, e)
    {
        ServerItem server(*e);
        if(!server.ID.Valid())
        {
            if(!server.Name.Valid())
                ERROR(Exception::Server::Params, "Name");
            String name = server.Name;
            if(name.empty())
                ERROR(Exception::IPVS::Server::Name::Empty, "");
            bool all = true;
            ENUM_STL_CONST(String, name, e)
            {
                if(!isdigit(*e))
                {
                    all = false;
                    break;
                }
            }
            if(all)
            {
                istringstream stream(name);
                int id = -1;
                if(!(stream >> id))
                    ERROR(Exception::IPVS::Server::Name::Invalid, name);
                server.ID = id;
                server.Name = recent.Servers.Get(id).Name;
            } else {
                IDMap::iterator pos = idmap.find(server.Name);
                if(pos == idmap.end())
                    ERROR(Exception::IPVS::Server::Name::NotFound, server.Name);
                else
                    server.ID = pos->second;
            }
        }
        recent.Servers.Get(server.ID);
    }
}

void VirtualServiceControl::ZeroStatistic(VirtualServiceItem& service)
{
    service.Statistic.InPacket = 0;
    service.Statistic.InPacketRate = 0;
    ENUM_LIST(ServerItem, service.Servers, server)
    {
        ZeroStatistic(*server);
    }
}

void VirtualServiceControl::ZeroStatistic(ServerItem& server)
{
    server.Statistic.InPacket = 0;
    server.Statistic.InPacketRate = 0;
}

void VirtualServiceControl::ZeroStatistic()
{
    ENUM_LIST(VirtualServiceItem, Holder, service)
    {
        ZeroStatistic(*service);
    }
    Exec::System("ipvsadm -Z");
}

VirtualServiceControl::ServiceType VirtualServiceControl::CheckSupportPortMap(VirtualServiceItem& service)
{
    if(service.IP.GetCount() != 1)
        return Mark;
    if(service.TcpPorts.GetCount() + service.UdpPorts.GetCount() != 1)
        return Mark;
    if(service.TcpPorts.GetCount() == 1)
    {
        ASSERT(service.TcpPorts.Head().Min > 0);
        ASSERT(service.TcpPorts.Head().Max > 0);
        if(service.TcpPorts.Head().Min == service.TcpPorts.Head().Max)
            return TCP;
    } else {
        ASSERT(service.UdpPorts.GetCount() == 1);
        ASSERT(service.UdpPorts.Head().Min > 0);
        ASSERT(service.UdpPorts.Head().Max > 0);
        if(service.UdpPorts.Head().Min == service.UdpPorts.Head().Max)
            return UDP;
    }
    return Mark;
}

void VirtualServiceControl::Add(VirtualServiceItem& service)
{
    GenerateMark(service);
    CheckServiceName(service.Name, -1);
    VirtualServiceItem recent(Holder.Append());
    recent.ID = Holder.GetCount() - 1;
    service.ID = recent.ID;
    recent.Description = service.Description;
    recent.Name = service.Name;
    recent.Mark = service.Mark;
    recent.IP.Clear();
    recent.Enabled = false;
    recent.Schedule = ScheduleState::rr;
    recent.TcpPorts.Clear();
    recent.UdpPorts.Clear();
    recent.Servers.Clear();
    recent.Persistent = false;
    recent.PersistentTimeout = recent.PersistentTimeout.Default;
    recent.PersistentNetmask = "255.255.255.255";
    recent.Monitor.Type = TypeState::Off;
    recent.Monitor.Interval = 10;
    recent.Monitor.Timeout = 5;
    recent.Monitor.Retry = 1;
    recent.Monitor.Port = 65535;
    recent.Monitor.Enabled = false;
    recent.Monitor.Mail = "";
    recent.Monitor.Date = 36000;
    recent.Traffic.Up = 0;
    recent.Traffic.Down = 0;
    recent.HA = HAState::Local;
    recent.HAStatus = true;
    ZeroStatistic(recent);
    ChangeMarkTarget(recent.Mark, true);
    LOGGER_NOTICE("Add virtual service done.");
}

void VirtualServiceControl::Del(VirtualServiceItem& service)
{
    StringCollection devs;
    EnsureServiceID(service);
    {
        VirtualServiceItem recent(Holder.Get(service.ID));
        if(recent.Enabled)
        {
            ChangeAddress(recent, false);
            StopService(recent);
        }
        SetTraffic(recent.Mark, 0, 0);
        ChangeMarkTarget(recent.Mark, false);
        ENUM_LIST(IPItem, recent.IP, e)
        {
            if(e->Dev != "" && !devs.count(e->Dev))
                devs.insert(e->Dev);
        }
    }
    Holder.Delete(service.ID);
    int count = 0;
    ENUM_LIST(VirtualServiceItem, Holder, e)
    {
        VirtualServiceItem(*e).ID = count++;
    }
    LOGGER_NOTICE("Del virtual service done.");
    ZeroStatistic();
    HttpControl::RefreshAll(devs);
}

void VirtualServiceControl::SetEnabled(VirtualServiceItem& service)
{
    EnsureServiceID(service);
    if(!service.Enabled.Valid())
        ERROR(Exception::Server::Params, "Enabled");
    VirtualServiceItem recent(Holder.Get(service.ID));
    if(service.Enabled != recent.Enabled)
    {
        if(recent.Enabled)
        {
            if(recent.HAStatus)
                ChangeAddress(recent, false);
            StopService(recent);
        } else {
            StartService(recent);
            if(recent.HAStatus)
                ChangeAddress(recent, true);
        }
        recent.Enabled = service.Enabled;
    }
    LOGGER_NOTICE("Set virtual service " << (recent.Enabled ? "enabled" : "disabled") << ".");
    ZeroStatistic();
}

void VirtualServiceControl::SetAddress(VirtualServiceItem& service)
{
    EnsureServiceID(service);
    if(!service.IP.Valid() && !service.TcpPorts.Valid() && !service.UdpPorts.Valid())
        ERROR(Exception::Server::Params, "IP TcpPorts UdpPorts");
    VirtualServiceItem target(Holder.Get(service.ID));
    if(!service.IP.Valid())
        service.IP = target.IP;
    else {
        StringCollection list;
        ENUM_LIST(IPItem, service.IP, i)
        {
            IPItem item(*i);
            if(item.IP == "")
                ERROR(Exception::IPVS::Service::IP::Empty, "");
            if(list.count(item.IP))
                ERROR(Exception::IPVS::Service::IP::Duplicate, item.IP);
            list.insert(item.IP);
            item.Dev.Valid();
        }
    }
    if(!service.TcpPorts.Valid())
        service.TcpPorts = target.TcpPorts;
    if(!service.UdpPorts.Valid())
        service.UdpPorts = target.UdpPorts;
    StringCollection ip;
    GenerateIPSet(service.IP, ip);
    PortSet tcp, udp;
    GeneratePortSet(service.TcpPorts, tcp);
    GeneratePortSet(service.UdpPorts, udp);
    ENUM_LIST(VirtualServiceItem, Holder, e)
    {
        VirtualServiceItem recent(*e);
        if(recent.ID != service.ID)
        {
            StringCollection recentip;
            GenerateIPSet(recent.IP, recentip);
            PortSet recenttcp, recentudp;
            GeneratePortSet(recent.TcpPorts, recenttcp);
            GeneratePortSet(recent.UdpPorts, recentudp);
            if(CrossCheck(recentip, ip) && (CrossCheck(recenttcp, tcp) || CrossCheck(recentudp, udp)))
                ERROR(Exception::IPVS::Service::Address, "");
        }
    }
    bool mapport = CheckSupportPortMap(target) != Mark;
    if(target.Enabled && target.HAStatus)
        ChangeAddress(target, false);
    StringCollection devs;
    ENUM_LIST(IPItem, target.IP, e)
    {
        if(e->Dev != "" && !devs.count(e->Dev))
            devs.insert(e->Dev);
    }
    target.IP.Clear();
    ENUM_LIST(IPItem, service.IP, e)
    {
        IPItem item(*e);
        IPItem recent(target.IP.Append());
        recent.IP = item.IP;
        recent.Dev = item.Dev;
        recent.Status = recent.Dev == "" ? true : CheckAddress(AddressPair::pair(recent.IP, recent.Dev));
        if(recent.Dev != "" && !devs.count(recent.Dev))
            devs.insert(recent.Dev);
    }
    HttpControl::RefreshAll(devs);
    GeneratePortList(tcp, target.TcpPorts);
    GeneratePortList(udp, target.UdpPorts);
    if(mapport || CheckSupportPortMap(target) != Mark)
    {
        if(target.Enabled)
            ReloadService(target);
    }
    if(target.Enabled && target.HAStatus)
        ChangeAddress(target, true);
    LOGGER_NOTICE("Set virtual service's address done.");
    ZeroStatistic();
}

void VirtualServiceControl::SetService(VirtualServiceItem& service)
{
    EnsureServiceID(service);
    VirtualServiceItem recent(Holder.Get(service.ID));
    int schedule = service.Schedule.Valid() ? service.Schedule : recent.Schedule;
    bool persistent = service.Persistent.Valid() ? service.Persistent : recent.Persistent;
    int persistenttimeout = service.PersistentTimeout.Valid() ? service.PersistentTimeout : recent.PersistentTimeout;
    String mask = service.PersistentNetmask.Valid() ? service.PersistentNetmask : recent.PersistentNetmask;
    int interval = service.Monitor.Interval.Valid() ? service.Monitor.Interval : recent.Monitor.Interval;
    int timeout = service.Monitor.Timeout.Valid() ? service.Monitor.Timeout : recent.Monitor.Timeout;
    int retry = service.Monitor.Retry.Valid() ? service.Monitor.Retry : recent.Monitor.Retry;
    int type = service.Monitor.Type.Valid() ? service.Monitor.Type : recent.Monitor.Type;
    int port = service.Monitor.Port.Valid() ? service.Monitor.Port : recent.Monitor.Port;
    bool enable = service.Monitor.Enabled.Valid() ? service.Monitor.Enabled : recent.Monitor.Enabled;
    String mail = service.Monitor.Mail.Valid() ? service.Monitor.Mail : recent.Monitor.Mail;
    int date = service.Monitor.Date.Valid() ? service.Monitor.Date : recent.Monitor.Date;
    if(recent.Enabled)
        StopService(recent);
    recent.Schedule = schedule;
    recent.Persistent = persistent;
    recent.PersistentNetmask = mask;
    recent.PersistentTimeout = persistenttimeout;
    recent.Monitor.Interval = interval;
    recent.Monitor.Timeout = timeout;
    recent.Monitor.Retry = retry;
    recent.Monitor.Type = type;
    recent.Monitor.Port = port;
    recent.Monitor.Mail = mail;
    recent.Monitor.Enabled = enable;
    recent.Monitor.Date = date;
    if(recent.Enabled)
        StartService(recent);
    LOGGER_NOTICE("Set virtual service's options done.");
    ZeroStatistic();
}

void VirtualServiceControl::SetTraffic(VirtualServiceItem& service)
{
    EnsureServiceID(service);
    VirtualServiceItem recent(Holder.Get(service.ID));
    int up = service.Traffic.Up.Valid() ? service.Traffic.Up : recent.Traffic.Up;
    int down = service.Traffic.Down.Valid() ? service.Traffic.Down : recent.Traffic.Down;
    SetTraffic(recent.Mark, up, down);
    recent.Traffic.Up = up;
    recent.Traffic.Down = down;
    LOGGER_NOTICE("Set virtual service's traffic done.");
}

void VirtualServiceControl::AddServer(VirtualServiceItem& service)
{
    EnsureServiceID(service);
    if(service.Servers.IsEmpty())
        return;
    VirtualServiceItem recent(Holder.Get(service.ID));
    {
        StringCollection iplist;
        StringCollection namelist;
        ENUM_LIST(ServerItem, recent.Servers, e)
        {
            ServerItem server(*e);
            iplist.insert(server.IP);
            if(server.Name != "")
                namelist.insert(server.Name);
        }
        ENUM_LIST(ServerItem, service.Servers, e)
        {
            ServerItem server(*e);
            if(server.Name != "")
            {
                if(namelist.count(server.Name))
                    ERROR(Exception::IPVS::Server::Name::Duplicate, server.Name);
                if(!CheckName(server.Name))
                    ERROR(Exception::IPVS::Server::Name::Invalid, server.Name);
                namelist.insert(server.Name);
            }
            if(server.IP == "")
                ERROR(Exception::IPVS::Server::IP::Empty, "");
            if(iplist.count(server.IP))
                ERROR(Exception::IPVS::Server::IP::Duplicate, server.IP);
            else
                iplist.insert(server.IP);
        }
    }
    ENUM_LIST(ServerItem, service.Servers, e)
    {
        ServerItem server(*e);
        ServerItem target(recent.Servers.Append());
        target.ID = recent.Servers.GetCount() - 1;
        target.Name = server.Name;
        target.IP = server.IP;
        target.Action = server.Action;
        target.Weight = server.Weight;
        target.MapPort = server.MapPort;
        ZeroStatistic(target);
        if(server.Enabled.Valid())
            target.Enabled = server.Enabled;
        else
            target.Enabled = true;
    }
    if(recent.Enabled)
        ReloadService(recent);
    LOGGER_NOTICE("Add real servers done.");
    ZeroStatistic();
}

void VirtualServiceControl::SetServer(VirtualServiceItem& service)
{
    EnsureServiceID(service);
    if(service.Servers.IsEmpty())
        return;
    VirtualServiceItem recent(Holder.Get(service.ID));
    EnsureServerID(recent, service.Servers);
    {
        IntCollection idlist;
        ENUM_LIST(ServerItem, service.Servers, server)
        {
            if(idlist.count(server->ID))
                ERROR(Exception::IPVS::Server::DuplicateID, server->ID);
            idlist.insert(server->ID);
        }
    }
    typedef map<Value*, Value*> ValueMap;
    ValueMap list;
    ENUM_LIST(ServerItem, service.Servers, e)
    {
        list[&(recent.Servers.Get(e->ID).Data)] = &(e->Data);
    }
    {
        StringCollection iplist;
        StringCollection namelist;
        ENUM_LIST(ServerItem, recent.Servers, e)
        {
            ServerItem server(*e);
            if(list.count(&(server.Data)) == 0)
            {
                iplist.insert(server.IP);
                if(server.Name != "")
                    namelist.insert(server.Name);
            }
        }
        ENUM_STL(ValueMap, list, e)
        {
            ServerItem recentserver(*(e->first));
            ServerItem targetserver(*(e->second));
            String name = targetserver.Name.Valid() ? targetserver.Name : recentserver.Name;
            if(!name.empty())
            {
                if(namelist.count(name))
                    ERROR(Exception::IPVS::Server::Name::Duplicate, name);
                if(!CheckName(name))
                    ERROR(Exception::IPVS::Server::Name::Invalid, name);
                namelist.insert(name);
            }
            String ip = targetserver.IP.Valid() ? targetserver.IP : recentserver.IP;
            if(ip.empty())
                ERROR(Exception::IPVS::Server::IP::Empty, "");
            if(iplist.count(ip))
                ERROR(Exception::IPVS::Server::IP::Duplicate, ip);
            else
                iplist.insert(ip);
        }
    }
    {
        ENUM_STL(ValueMap, list, e)
        {
            ServerItem recentserver(*(e->first));
            ServerItem targetserver(*(e->second));
            if(targetserver.Name.Valid())
                recentserver.Name = targetserver.Name;
            if(targetserver.IP.Valid())
                recentserver.IP = targetserver.IP;
            if(targetserver.Action.Valid())
                recentserver.Action = targetserver.Action;
            if(targetserver.Weight.Valid())
                recentserver.Weight = targetserver.Weight;
            if(targetserver.Enabled.Valid())
                recentserver.Enabled = targetserver.Enabled;
            if(targetserver.MapPort.Valid())
                recentserver.MapPort = targetserver.MapPort;
        }
        if(recent.Enabled)
            ReloadService(recent);
        LOGGER_NOTICE("Set real servers done.");
    }
    ZeroStatistic();
}

void VirtualServiceControl::DelServer(VirtualServiceItem& service)
{
    EnsureServiceID(service);
    if(service.Servers.IsEmpty())
        return;
    VirtualServiceItem recent(Holder.Get(service.ID));
    EnsureServerID(recent, service.Servers);
    IntCollection idlist;
    ENUM_LIST(ServerItem, service.Servers, server)
    {
        if(idlist.count(server->ID))
            ERROR(Exception::IPVS::Server::DuplicateID, server->ID);
        idlist.insert(server->ID);
    }
    ENUM_STL_R(IntCollection, idlist, e)
    {
        recent.Servers.Delete(*e);
    }
    int count = 0;
    ENUM_LIST(ServerItem, recent.Servers, e)
    {
        ServerItem(*e).ID = count++;
    }
    if(recent.Enabled)
        ReloadService(recent);
    LOGGER_NOTICE("Del real servers done.");
    ZeroStatistic();
}

void VirtualServiceControl::SetServerEnabled(VirtualServiceItem& service)
{
    EnsureServiceID(service);
    if(service.Servers.IsEmpty())
        return;
    VirtualServiceItem recent(Holder.Get(service.ID));
    EnsureServerID(recent, service.Servers);
    ENUM_LIST(ServerItem, service.Servers, server)
    {
        if(!server->Enabled.Valid())
            ERROR(Exception::Server::Params, "Enabled");
    }
    ENUM_LIST(ServerItem, service.Servers, server)
    {
        recent.Servers.Get(server->ID).Enabled = server->Enabled;
    }
    if(recent.Enabled)
        ReloadService(recent);
    LOGGER_NOTICE("Set real servers enabled option done.");
    ZeroStatistic();
}

int VirtualServiceControl::GetCount()
{
    return Holder.GetCount();
}

void VirtualServiceControl::GetCount(IntValue& result)
{
    result = Holder.GetCount();
}

void VirtualServiceControl::Copy(VirtualServiceItem& target, VirtualServiceItem& recent)
{
    target.ID = recent.ID;
    target.Name = recent.Name;
    target.Description = recent.Description;
    target.IP = recent.IP;
    target.Enabled = recent.Enabled;
    target.Schedule = recent.Schedule;
    target.Servers = recent.Servers;
    target.Mark = recent.Mark;
    target.Persistent = recent.Persistent;
    target.PersistentTimeout = recent.PersistentTimeout;
    target.PersistentNetmask = recent.PersistentNetmask;
    target.TcpPorts = recent.TcpPorts;
    target.UdpPorts = recent.UdpPorts;
    target.Monitor.Interval = recent.Monitor.Interval;
    target.Monitor.Timeout = recent.Monitor.Timeout;
    target.Monitor.Retry = recent.Monitor.Retry;
    target.Monitor.Type = recent.Monitor.Type;
    target.Monitor.Port = recent.Monitor.Port;
    target.Monitor.Enabled = recent.Monitor.Enabled;
    target.Monitor.Mail = recent.Monitor.Mail;
    target.Monitor.Date = recent.Monitor.Date;
    target.Traffic.Up = recent.Traffic.Up;
    target.Traffic.Down = recent.Traffic.Down;
    target.HA = recent.HA;
    target.HAStatus = recent.HAStatus;
    target.Statistic = recent.Statistic;
}

void VirtualServiceControl::GetStatic(VirtualServiceItem& service)
{
    EnsureServiceID(service);
    VirtualServiceItem recent(Holder.Get(service.ID));
    Copy(service, recent);
}

void VirtualServiceControl::Get(GetListItem<VirtualServiceItem>& list)
{
    int start, end;
    list.Ready(Holder.GetCount(), start, end);
    for(int i = start; i < end; ++i)
    {
        VirtualServiceItem service(list.Result.Append());
        VirtualServiceItem recent(Holder.Get(i));
        Copy(service, recent);
    }
    GenerateServerStatus(list.Result);
}

void VirtualServiceControl::GetIDByName(VirtualServiceItem& service)
{
    service.ID.Data.clear();
    EnsureServiceID(service);
    ENUM_LIST(ServerItem, service.Servers, e)
    {
        ServerItem server(*e);
        server.ID.Data.clear();
    }
    VirtualServiceItem recent(Holder.Get(service.ID));
    EnsureServerID(recent, service.Servers);
}

void VirtualServiceControl::SetDescription(VirtualServiceItem& service)
{
    EnsureServiceID(service);
    VirtualServiceItem recent(Holder.Get(service.ID));
    if(service.Name.Valid())
    {
        CheckServiceName(service.Name, recent.ID);
        recent.Name = service.Name;
        LOGGER_NOTICE("Set virtual service name done.");
    }
    if(service.Description.Valid())
    {
        recent.Description = service.Description;
        LOGGER_NOTICE("Set virtual service description done.");
    }
}

void VirtualServiceControl::SetHA(VirtualServiceItem& service)
{
    EnsureServiceID(service);
    if(!service.HA.Valid())
        ERROR(Exception::Server::Params, "HA");
    VirtualServiceItem recent(Holder.Get(service.ID));
    if(recent.HA != service.HA)
    {
        bool status = true;
        if(service.HA == HAState::Self)
            status = HAControl().HA.Self;
        else if(service.HA == HAState::Other)
            status = HAControl().HA.Other;
        recent.HA = service.HA;
        if(recent.HAStatus != status && recent.Enabled)
        {
            if(status)
                ChangeAddress(recent, true);
            else
                ChangeAddress(recent, false);
        }
        recent.HAStatus = status;
    }
}

void VirtualServiceControl::RefreshHA()
{
    ENUM_LIST(VirtualServiceItem, Holder, e)
    {
        VirtualServiceItem recent(*e);
        bool status = true;
        if(recent.HA == HAState::Self)
            status = HAControl().HA.Self;
        else if(recent.HA == HAState::Other)
            status = HAControl().HA.Other;
        if(status != recent.HAStatus && recent.Enabled)
        {
            if(status)
                ChangeAddress(recent, true);
            else
                ChangeAddress(recent, false);
        }
        recent.HAStatus = status;
    }
}

void VirtualServiceControl::RefreshAll(const StringCollection& dev)
{
    VirtualServiceControl control;
    ENUM_LIST(VirtualServiceItem, control.Holder, s)
    {
        VirtualServiceItem service(*s);
        ENUM_LIST(IPItem, service.IP, i)
        {
            IPItem item(*i);
            if(item.Dev != "" && dev.count(item.Dev))
            {
                bool status = item.Status;
                item.Status = CheckAddress(AddressPair::pair(item.IP, item.Dev));
                if(service.Enabled && service.HAStatus && (!service.TcpPorts.IsEmpty() || !service.UdpPorts.IsEmpty()))
                {
                    if(status != item.Status)
                    {
                        String tcp;
                        GenerateMatchPortCmd(service.TcpPorts, tcp);
                        String udp;
                        GenerateMatchPortCmd(service.UdpPorts, udp);
                        if(item.Status)
                            ChangeMangle(item.IP, tcp, udp, service.Mark, true);
                        else
                            ChangeMangle(item.IP, tcp, udp, service.Mark, false);
                    }
                    if(item.Status)
                        DoAddIP(item.IP, item.Dev);
                }
            }
        }
    }
}

namespace Services
{
    DECLARE_INTERFACE_REFRESH(VirtualServiceControl::RefreshAll, 4);

#define EXECUTE_RPC_USE_SYNC(methodname,methodfunc,sync)\
    void LINE_NAME(methodfunc)(Value& params, Value& result)\
    {\
        RpcMethod::CheckLicence();\
        VirtualServiceControl control;\
        VirtualServiceItem service(params);\
        control.methodfunc(service);\
        if(sync)\
            Recorder::Sync();\
        (bool&)result = true;\
    }\
    DECLARE_RPC_METHOD(methodname,LINE_NAME(methodfunc),true,true)

#define EXECUTE_RPC(methodname,methodfunc) \
    EXECUTE_RPC_USE_SYNC(methodname,methodfunc,false)
#define EXECUTE_RPC_SYNC(methodname,methodfunc) \
    EXECUTE_RPC_USE_SYNC(methodname,methodfunc,true)

    EXECUTE_RPC_SYNC(FuncVirtualServicesAdd, Add);
    EXECUTE_RPC_SYNC(FuncVirtualServicesDelete, Del);
    EXECUTE_RPC(FuncVirtualServicesEnabled, SetEnabled);
    EXECUTE_RPC(FuncVirtualServicesAddress, SetAddress);
    EXECUTE_RPC(FuncVirtualServicesService, SetService);
    EXECUTE_RPC(FuncVirtualServicesTraffic, SetTraffic);
    EXECUTE_RPC_SYNC(FuncVirtualServicesAddServer, AddServer);
    EXECUTE_RPC_SYNC(FuncVirtualServicesDelServer, DelServer);
    EXECUTE_RPC_SYNC(FuncVirtualServicesSetServer, SetServer);
    EXECUTE_RPC(FuncVirtualServicesEnabledServer, SetServerEnabled);
    EXECUTE_RPC(FuncVirtualServicesHA, SetHA);
    EXECUTE_RPC(FuncVirtualServicesDescription, SetDescription);

    void ExecuteVirtualServicesGetCount(Value& params, Value& result)
    {
        RpcMethod::CheckLicence();
        VirtualServiceControl control;
        IntValue res(result);
        control.GetCount(res);
    }

    DECLARE_RPC_METHOD(FuncVirtualServicesGetCount, ExecuteVirtualServicesGetCount, true, true);

    void ExecuteVirtualServicesGet(Value& params, Value& result)
    {
        RpcMethod::CheckLicence();
        VirtualServiceControl control;
        GetListItem<VirtualServiceItem> get(params);
        control.Get(get);
        result = get.Result.Data;
    }

    DECLARE_RPC_METHOD(FuncVirtualServicesGet, ExecuteVirtualServicesGet, true, true);

    void ExecuteVirtualServicesGetIDByName(Value& params, Value& result)
    {
        RpcMethod::CheckLicence();
        VirtualServiceControl control;
        VirtualServiceItem item(params);
        control.GetIDByName(item);
        result = params;
    }

    DECLARE_RPC_METHOD(FuncVirtualServicesNameToID, ExecuteVirtualServicesGetIDByName, true, true);

    void VirtualServicesBeforeImport(Value& data, bool reload)
    {
        VirtualServiceControl control;
        while(control.GetCount())
        {
            Value temp;
            VirtualServiceItem service(temp);
            service.ID = control.GetCount() - 1;
            control.Del(service);
        }
    }

    void VirtualServicesImport(Value& data, bool reload)
    {
        List<VirtualServiceItem> list(data["VirtualServices"]);
        VirtualServiceControl control;
        ENUM_LIST(VirtualServiceItem, list, e)
        {
            VirtualServiceItem service(*e);
            if(reload)
            {
                NO_ERROR(control.Add(service));
                NO_ERROR(control.SetAddress(service));
                NO_ERROR(control.SetService(service));
                NO_ERROR(control.AddServer(service));
                NO_ERROR(control.SetHA(service));
                NO_ERROR(control.SetTraffic(service));
                NO_ERROR(control.SetEnabled(service));
            } else {
                control.Add(service);
                control.SetAddress(service);
                control.SetService(service);
                control.AddServer(service);
                control.SetHA(service);
                control.SetTraffic(service);
                control.SetEnabled(service);
            }
        }
    }

    void VirtualServicesExport(Value& data)
    {
        List<Model> result(data["VirtualServices"]);
        VirtualServiceControl control;
        Value temp;
        GetListItem<VirtualServiceItem> list(temp);
        list.All = true;
        control.Get(list);
        ENUM_LIST(VirtualServiceItem, list.Result, e)
        {
            VirtualServiceItem service(*e);
            service.ID.Data.clear();
            service.HAStatus.Data.clear();
            ENUM_LIST(VirtualServiceItem::IPItem, service.IP, e)
            {
                VirtualServiceItem::IPItem item(*e);
                item.Status.Data.clear();
            }
            ENUM_LIST(VirtualServiceItem::ServerItem, service.Servers, e)
            {
                VirtualServiceItem::ServerItem server(*e);
                server.Status.Data.clear();
                server.Active.Data.clear();
                server.InActive.Data.clear();
            }
            result.Append().Data = service.Data;
        }
    }

    DECLARE_SERIALIZE(VirtualServicesBeforeImport, VirtualServicesImport, NULL, VirtualServicesExport, 5);

    void InitServices()
    {
        Exec::System("killall -9 ldirectord");
        Exec::System("ipvsadm -C");
        Exec::System("iptables -t mangle -N SERVICES_IN");
        Exec::System("iptables -t mangle -A PREROUTING -m mark --mark 0 -j SERVICES_IN");
        Exec::System("iptables -t mangle -N SERVICES_OUT");
        Exec::System("iptables -t mangle -A POSTROUTING -m mark --mark 0 -j SERVICES_OUT");
    }

    DECLARE_INIT(InitServices, NULL, 2);

    void InitVirtualIPRoute()
    {
        Exec::System("ip route flush table 1");
        Exec::System("ip route add local default dev lo table 1");
    }

    DECLARE_INIT(InitVirtualIPRoute, NULL, 4);

    void InitTrafficControl()
    {
        Exec::System("rmmod imq");
        Exec::System("modprobe imq numdevs=2");
        Exec::System("ip link set imq0 up");//上行
        Exec::System("tc qdisc add dev imq0 root handle 1: htb");
        Exec::System("ip link set imq1 up");//下行
        Exec::System("tc qdisc add dev imq1 root handle 1: htb");
    }

    DECLARE_INIT(InitTrafficControl, NULL, 1);

};
