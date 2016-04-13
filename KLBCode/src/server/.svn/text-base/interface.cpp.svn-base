#include <sstream>
#include <fstream>
#include <iomanip>

#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <sys/ioctl.h>
#include <net/if.h>
#include <netinet/if_ether.h>

#include "share/utility.h"

#include "base.h"

#include "interface.h"
#include "staticroute.h"
#include "network.h"

using namespace std;

int PhysicalInterfaceControl::GetMTU(const String& dev)
{
    Exec exe("ip");
    exe << "link" << "show" << dev;
    exe.Execute();
    fdistream stream(exe.ReadOut());
    String id, name, flag, tag;
    int mtu;
    if(!(stream >> id >> name >> flag >> tag >> mtu && tag == "mtu"))
        ERROR(Exception::Interface::NotFoundDev, dev);
    return mtu;
}

void PhysicalInterfaceControl::SetMTU(const String& dev, int mtu)
{
    Exec exe("ip");
    exe << "link" << "set" << dev << "mtu" << mtu;
    exe.Execute();
    int status;
    exe.Close(status);
    if(status != 0)
        ERROR(Exception::Interface::MTU, dev << '\n' << mtu);
}

bool PhysicalInterfaceControl::GetCarrier(const String& dev)
{
    bool result = false;
    ifstream stream(("/sys/class/net/" + dev + "/carrier").c_str());
    if(stream)
    {
        int value = 0;
        try
        {
            stream >> value;
        } catch(ios_base::failure& e)
        {
        }
        result = value == 1;
        stream.close();
    } else
        ERROR(Exception::Interface::NotFoundDev, dev);
    return result;
}

bool PhysicalInterfaceControl::GetEnabled(const String& dev)
{
    bool result = false;
    ifstream stream(("/sys/class/net/" + dev + "/flags").c_str());
    if(stream)
    {
        int flags;
        stream >> hex >> flags;
        result = flags & 0x1;
        stream.close();
    } else
        ERROR(Exception::Interface::NotFoundDev, dev);
    return result;
}

void PhysicalInterfaceControl::SetEnabled(const String& dev, bool enabled)
{
    if(Exec::System("ip link set " + dev + ((enabled) ? " up" : " down")) != 0)
        ERROR((enabled ? Exception::Interface::Enabled : Exception::Interface::Disabled), dev);
}

String PhysicalInterfaceControl::GetAddress(const String& dev)
{
    String result;
    ifstream stream(("/sys/class/net/" + dev + "/address").c_str());
    if(stream)
    {
        stream >> result;
        stream.close();
    } else
        ERROR(Exception::Interface::NotFoundDev, dev);
    return result;
}

void PhysicalInterfaceControl::SetAddress(const String& dev, const String& address)
{
    if(address.empty())
        return;
    bool enabled = GetEnabled(dev);
    SetEnabled(dev, false);
    String old(GetAddress(dev));
    if(Exec::System("ip link set " + dev + " address " + address) != 0)
    {
        if(enabled)
            SetEnabled(dev, true);
        ERROR(Exception::Interface::Address, dev << '\n' << address);
    }
    try
    {
        SetEnabled(dev, true);
        if(!enabled)
            SetEnabled(dev, false);
    } catch(ValueException& e)
    {
        Exec::System("ip link set " + dev + " address " + old);
        if(enabled)
            SetEnabled(dev, true);
        ERROR(Exception::Interface::Address, dev << '\n' << address);
    }
}

void PhysicalInterfaceControl::GetIP(const String& dev, List<HostPackStringValue>& result)
{
    Exec ip("ip");
    ip << "address" << "show" << "dev" << dev;
    ip.Execute();
    fdistream lines(ip.ReadOut());
    String line;
    getline(lines, line);
    getline(lines, line);
    while(getline(lines, line))
    {
        String ip, prot;
        istringstream linestream(line);
        linestream >> prot >> ip;
        if(prot == "inet")
            result.Append() = ip;
    }
    int status;
    ip.Close(status);
    if(status != 0)
        ERROR(Exception::Interface::NotFoundDev, dev);
}

void PhysicalInterfaceControl::FlushIP(const String& dev)
{
    if(Exec::System("ip address flush dev " + dev) != 0)
        ERROR(Exception::Interface::IP::Flush, dev);
}

void PhysicalInterfaceControl::SetIP(const String& dev, List<HostPackStringValue>& result)
{
    FlushIP(dev);
    PRINTF("IP Count " << result.GetCount());
    ENUM_LIST(HostPackStringValue, result, e)
    {
        HostPackStringValue ip(*e);
        if(Exec::System("ip address add " + (const String&)ip + " brd + dev " + dev) != 0)
            ERROR(Exception::Interface::IP::Add, dev << '\n' << (const String&)ip);
    }
}

void PhysicalInterfaceControl::SetArp(const String& dev, int arp)
{
    ostringstream stream;
    stream << "echo ";
    switch(arp)
    {
        case PhysicalInterface::ArpState::Enabled:
            stream << "2";
            break;
        case PhysicalInterface::ArpState::ReplyOnly:
            stream << "3";
            break;
        case PhysicalInterface::ArpState::Proxy:
            stream << "1";
            break;
        case PhysicalInterface::ArpState::Disabled:
        default:
            stream << "8";
    }
    stream << " > /proc/sys/net/ipv4/conf/" << dev << "/"
           << ((arp == PhysicalInterface::ArpState::Proxy) ? "proxy_arp" : "arp_ignore");
    Exec::System("echo 0 > /proc/sys/net/ipv4/conf/" + dev + "/proxy_arp");
    Exec::System("echo 2 > /proc/sys/net/ipv4/conf/" + dev + "/arp_ignore");
    Exec::System("echo 1 > /proc/sys/net/ipv4/conf/" + dev + "/arp_announce");
    Exec::System(stream.str());
}

void PhysicalInterfaceControl::SetDhcp(const String& dev, String& gate, String& dns)
{
    static const char* const resolv = "/etc/resolv.conf";
    static const char* const predhclient = "/etc/reslov.conf.predhclient";
    SetEnabled(dev, true);
    Exec exe("dhclient");
    exe << dev << "-timeout" << "10";
    exe.Execute();
    exe.Close();
    Exec::System("killall dhclient");
    SetEnabled(dev, true);
    {
        dns = "";
        ifstream stream(resolv);
        if(stream)
        {
            String line;
            while(getline(stream, line))
            {
                istringstream linestream(line);
                String type, ip;
                if(linestream >> type >> ip && type == "nameserver")
                {
                    dns = ip;
                    break;
                }
            }
            stream.close();
            unlink(resolv);
            link(predhclient, resolv);
        }
    }
    {
        gate = "";
        Exec ip("ip");
        ip << "route" << "list" << "exact" << "0.0.0.0/0";
        ip.Execute();
        fdistream stream(ip.ReadOut());
        String line;
        if(getline(stream, line))
        {
            istringstream wordstream(line);
            String word;
            while(wordstream >> word)
            {
                if(word == "via")
                {
                    wordstream >> word;
                    gate = word;
                    break;
                }
            }
        }
        Exec::System("ip route del default metric 0");
    }
}

void PhysicalInterfaceControl::SetMTU(PhysicalInterface& target, PhysicalInterface& recent)
{
    if(!target.MTU.Valid())
        ERROR(Exception::Server::Params, "MTU");
    SetMTU(target.Dev, target.MTU);
    recent.MTU = target.MTU;
}

void PhysicalInterfaceControl::SetEnabled(PhysicalInterface& target, PhysicalInterface& recent)
{
    if(!target.Enabled.Valid())
        ERROR(Exception::Server::Params, "Enabled");
    SetEnabled(target.Dev, target.Enabled);
    recent.Enabled = target.Enabled;
    if(recent.Dhcp)
        SetDhcp(recent, recent);
    else
        SetIP(recent, recent);
}

void PhysicalInterfaceControl::SetAddress(PhysicalInterface& target, PhysicalInterface& recent)
{
    if(!target.Address.Valid())
        ERROR(Exception::Server::Params, "Address");
    try
    {
        SetAddress(target.Dev, target.Address);
    } catch(ValueException& e)
    {
        SetAddress(recent.Dev, recent.Address);
        throw e;
    }
    recent.Address = target.Address;
}

void PhysicalInterfaceControl::SetIP(PhysicalInterface& target, PhysicalInterface& recent)
{
    if(recent.Dhcp)
        ERROR(Exception::Interface::IP::Dhcp, recent.Dev);
    if(!target.IP.Valid())
        ERROR(Exception::Server::Params, "IP");
    StringCollection list;
    ENUM_LIST(HostPackStringValue, target.IP, e)
    {
        HostPackStringValue ip(*e);
        if(list.count(ip))
            ERROR(Exception::Interface::IP::Duplicate, recent.Dev << '\n' << ip);
        if(ip == "")
            ERROR(Exception::Server::Params, "IP");
        list.insert(ip);
    }
    if(recent.Enabled)
    {
        try
        {
            SetIP(target.Dev, target.IP);
        } catch(ValueException& e)
        {
            SetIP(recent.Dev, recent.IP);
            throw e;
        }
    } else {
        FlushIP(target.Dev);
        recent.Gate = "";
        recent.Dns = "";
    }
    recent.IP = target.IP;
    SetArp(recent, recent);
}

void PhysicalInterfaceControl::SetArp(PhysicalInterface& target, PhysicalInterface& recent)
{
    if(!target.Arp.Valid())
        ERROR(Exception::Server::Params, "ARP");
    if(recent.Enabled && !recent.IP.IsEmpty())
        SetArp(target.Dev, target.Arp);
    recent.Arp = target.Arp;
}

void PhysicalInterfaceControl::SetDhcp(PhysicalInterface& target, PhysicalInterface& recent)
{
    if(!target.Dhcp.Valid())
        ERROR(Exception::Server::Params, "DHCP");
    if(recent.Enabled)
    {
        if(target.Dhcp)
        {
            String gate, dns;
            recent.IP.Clear();
            SetDhcp(target.Dev, gate, dns);
            GetIP(target.Dev, recent.IP);
            if(!recent.IP.IsEmpty())
            {
                recent.Gate = gate;
                recent.Dns = dns;
            } else {
                recent.Gate = "";
                recent.Dns = "";
            }
            StaticRouteControl().RefreshDefault();
            DnsServerControl().Refresh();
        } else {
            if(recent.Dhcp)
            {
                recent.IP.Clear();
                recent.Gate = "";
                recent.Dns = "";
            }
        }
        SetArp(recent, recent);
    }
    recent.Dhcp = target.Dhcp;
}

void PhysicalInterfaceControl::InitInterface(const String& dev, PhysicalInterface& interface)
{
    interface.Dev = dev;
    interface.Description = "";
    interface.MTU = interface.MTU.Default;
    interface.IP.Clear();
    interface.Enabled = false;
    interface.Address = "";
    interface.Arp = interface.Arp.Type.Default();
    interface.Dhcp = false;
    interface.Gate = "";
    interface.Dns = "";
}

void PhysicalInterfaceControl::Get(PhysicalInterface& target, PhysicalInterface& recent)
{
    target.Dev = recent.Dev;
    target.Description = recent.Description;
    target.MTU = recent.MTU;
    target.IP = recent.IP;
    target.Enabled = recent.Enabled;
    target.Address = recent.Address;
    target.CurrentAddress = GetAddress(recent.Dev);
    target.Arp = recent.Arp;
    target.Dhcp = recent.Dhcp;
    target.Carrier = GetCarrier(recent.Dev);
}

PhysicalInterfaceControl::PhysicalInterfaceControl(Value& holder)
    : Holder(holder)
{}

Value& PhysicalInterfaceControl::Find(const String& dev)
{
    Value* result = NULL;
    int index = 0;
    ENUM_LIST(PhysicalInterface, Holder, e)
    {
        PhysicalInterface interface(*e);
        if(interface.Dev == dev)
        {
            result = &(*e).Data;
            break;
        }
        ++index;
    }
    if(!result)
        ERROR(Exception::Interface::NotFoundDev, dev);
    PhysicalInterface(*result).ID = index;
    return *result;
}

void PhysicalInterfaceControl::SendArp(const String& ip, const String& dev)
{
    uint32_t addr;
    Address::StringToAddress(ip, addr);

    static const unsigned int MAC_LEN = 6;
    static const unsigned int IP_LEN = sizeof(addr);
    static const unsigned int ARP_FRAME_TYPE = 0x0806;
    static const unsigned int ETHERNET_TYPE = 0x0001;
    static const unsigned int IP_PROTO_TYPE = 0x0800;
    static const unsigned int ARP_REQUEST = 0x0001;
    static const unsigned int ARP_RELAY = 0x0002;

    struct arp_packet
    {
        u_char target_hw_addr[MAC_LEN];
        u_char src_hw_addr[MAC_LEN];
        u_short frame_type;
        u_short hw_type;
        u_short prot_type;
        u_char hw_addr_size;
        u_char prot_addr_size;
        u_short op;
        u_char arp_sender_hw_addr[MAC_LEN];
        u_char arp_sender_ip_addr[IP_LEN];
        u_char arp_target_hw_addr[MAC_LEN];
        u_char arp_target_ip_addr[IP_LEN];
        u_char padding[18];
    } arp;

    int sock = socket(AF_INET, SOCK_PACKET, htons(ETH_P_RARP));

    struct ifreq interface;
    strcpy(interface.ifr_name, dev.c_str());
    ioctl(sock, SIOCGIFHWADDR, (char*)&interface);

#ifdef __DEBUG__
    char buf[256];
    sprintf(buf, "SendARP: %02x:%02x:%02x:%02x:%02x:%02x to %s\n",
            (unsigned char)interface.ifr_hwaddr.sa_data[0],
            (unsigned char)interface.ifr_hwaddr.sa_data[1],
            (unsigned char)interface.ifr_hwaddr.sa_data[2],
            (unsigned char)interface.ifr_hwaddr.sa_data[3],
            (unsigned char)interface.ifr_hwaddr.sa_data[4],
            (unsigned char)interface.ifr_hwaddr.sa_data[5],
            dev.c_str());
    DEBUG_PRINT("VirtualService", buf);
#endif

    arp.frame_type = htons(ARP_FRAME_TYPE);
    arp.hw_type = htons(ETHERNET_TYPE);
    arp.prot_type = htons(IP_PROTO_TYPE);
    arp.hw_addr_size = MAC_LEN;
    arp.prot_addr_size = IP_LEN;

    memset(arp.target_hw_addr, -1, MAC_LEN);
    memcpy(arp.src_hw_addr, interface.ifr_hwaddr.sa_data, MAC_LEN);
    memcpy(arp.arp_sender_hw_addr, interface.ifr_hwaddr.sa_data, MAC_LEN);

    memcpy(arp.arp_sender_ip_addr, &addr, IP_LEN);
    memcpy(arp.arp_target_ip_addr, &addr, IP_LEN);

    memset(arp.padding, 0, 18);

    struct sockaddr target;
    strcpy(target.sa_data, dev.c_str());

    arp.op = htons(ARP_REQUEST);
    memset(arp.arp_target_hw_addr, 0, MAC_LEN);
    sendto(sock, &arp, sizeof(arp), 0, &target, sizeof(target));

    arp.op = htons(ARP_RELAY);
    memcpy(arp.arp_target_hw_addr, interface.ifr_hwaddr.sa_data, MAC_LEN);
    sendto(sock, &arp, sizeof(arp), 0, &target, sizeof(target));

    close(sock);
}

typedef set<InterfaceRefresher*> InterfaceRefresherList;
typedef map<int, InterfaceRefresherList> InterfaceRefresherMap;

static InterfaceRefresherMap* FInterfaceRefresher = NULL;

void InterfaceRefresher::Refresh(const StringCollection& dev)
{
    PRINTF("Inteface Refresh..........");
    if(FInterfaceRefresher)
    {
        ENUM_STL(InterfaceRefresherMap, *FInterfaceRefresher, list)
        {
            ENUM_STL(InterfaceRefresherList, list->second, e)
            {
                if((*e)->FRefresh != NULL)
                    (*e)->FRefresh(dev);
            }
        }
    }
}

void InterfaceRefresher::Refresh(const String& dev)
{
    StringCollection devs;
    devs.insert(dev);
    Refresh(devs);
}

InterfaceRefresher::InterfaceRefresher(Func refresh, int priority)
{
    FRefresh = refresh;
    FPriority = priority;
    if(!FInterfaceRefresher)
        FInterfaceRefresher = new InterfaceRefresherMap();
    (*FInterfaceRefresher)[FPriority].insert(this);
}

InterfaceRefresher::~InterfaceRefresher()
{
    (*FInterfaceRefresher)[FPriority].erase(this);
    if((*FInterfaceRefresher)[FPriority].size() == 0)
        FInterfaceRefresher->erase(FPriority);
    if(FInterfaceRefresher->size() == 0)
    {
        delete FInterfaceRefresher;
        FInterfaceRefresher = NULL;
    }
}

namespace Interface
{

    void InitCfg()
    {
        Exec::System("killall -9 NetworkManager");
        Exec::System("rm -rf /etc/sysconfig/network-scripts/ifcfg-*");
    }

    DECLARE_INIT(InitCfg, NULL, -100);

    void InitLoopBack()
    {
        Exec::System("ip address flush dev lo");
        Exec::System("ip link set lo up");
        Exec::System("ip address add 127.0.0.1/8 brd + dev lo");
        Exec::System("echo 1 > /proc/sys/net/ipv4/conf/lo/arp_announce");
        Exec::System("echo 2 > /proc/sys/net/ipv4/conf/lo/arp_ignore");
        Exec::System("echo 1 > /proc/sys/net/ipv4/conf/all/arp_announce");
        Exec::System("echo 2 > /proc/sys/net/ipv4/conf/all/arp_ignore");
    }

    DECLARE_INIT(InitLoopBack, NULL, 3);

    void InitDhcp()
    {
        Exec::System("killall dhclient");
    }

    DECLARE_INIT(InitDhcp, NULL, 0);

}
