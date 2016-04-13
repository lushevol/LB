#ifndef __SERVICES_H__
#define __SERVICES_H__

#include <bitset>
#include <map>

#include "model/ipvs.h"

class VirtualServiceControl
{
    public:
        enum ServiceType
        {
            Mark,
            TCP,
            UDP
        };
    private:
        static const int PortCount = 65536;
        typedef std::bitset<PortCount> PortSet;
        typedef std::map<String, int> MangleCounter;
        typedef std::pair<String, String> AddressPair;
        typedef std::map<AddressPair, int> AddressCounter;
        typedef VirtualServiceItem::PortItem PortItem;
        typedef VirtualServiceItem::ScheduleState ScheduleState;
        typedef VirtualServiceItem::MonitorItem::TypeState TypeState;
        typedef VirtualServiceItem::ServerItem ServerItem;
        typedef VirtualServiceItem::ServerItem::ActionState ActionState;
        typedef VirtualServiceItem::IPItem IPItem;
    private:
        static AddressCounter FAddressCounter;
        static MangleCounter FMangleCounter;
        static bool CheckIP(const String& ip, List<HostPackStringValue>& iplist);
        static bool CheckAddress(const AddressPair& address);
        static void DoAddIP(const String& ip, const String& dev);
        static void DoRemoveIP(const String& ip, const String& dev);
        static bool AddIPCounter(const String& ip, const String& dev);
        static bool DelIPCounter(const String& ip, const String& dev);
        static void AddMangleCounter(const String& ip);
        static void DelMangleCounter(const String& ip);
        static void GeneratePortSet(List<PortItem>& ports, PortSet& set);
        static void GeneratePortList(const PortSet& set, List<PortItem>& ports);
        static void GenerateMatchPortCmd(List<PortItem>& ports, String& cmd);
        static void GenerateIPSet(List<IPItem>& IP, StringCollection& set);
        static String GenerateConfPath(int mark);
        static void GenerateConf(const String& path, VirtualServiceItem& service);
        static bool CrossCheck(StringCollection& a, StringCollection& b);
        static bool CrossCheck(PortSet& a, PortSet& b);
        static void StartService(VirtualServiceItem& recent);
        static void StopService(VirtualServiceItem& recent);
        static void ReloadService(VirtualServiceItem& recent);
        static void ChangeMangle(const String& ip, const String& tcp, const String& udp, int mark, bool add);
        static void ChangeMarkTarget(int mark, bool add);
        static void ChangeAddress(VirtualServiceItem& recent, bool add);
        static void GenerateServerStatus(List<VirtualServiceItem>& services);
        static bool CheckName(const String& value);
        static void SetTraffic(int mark, int up, int down);
        static void Copy(VirtualServiceItem& target, VirtualServiceItem& recent);
        void EnsureServiceID(VirtualServiceItem& service);
        void CheckServiceName(const String& value, int id);
        static void EnsureServerID(VirtualServiceItem& recent, List<ServerItem>& list);
        void GenerateMark(VirtualServiceItem& service);
        static void ZeroStatistic(VirtualServiceItem& service);
        static void ZeroStatistic(ServerItem& server);
        void ZeroStatistic();
    public:
        List<VirtualServiceItem> Holder;
    public:
        VirtualServiceControl();
        void Add(VirtualServiceItem& service);
        void Del(VirtualServiceItem& service);
        void SetEnabled(VirtualServiceItem& service);
        void SetAddress(VirtualServiceItem& service);
        void SetService(VirtualServiceItem& service);
        void SetTraffic(VirtualServiceItem& service);
        void SetHA(VirtualServiceItem& service);
        void AddServer(VirtualServiceItem& service);
        void SetServer(VirtualServiceItem& service);
        void DelServer(VirtualServiceItem& service);
        void SetServerEnabled(VirtualServiceItem& service);
        int GetCount();
        void GetCount(IntValue& result);
        void GetIDByName(VirtualServiceItem& service);
        void Get(GetListItem<VirtualServiceItem>& list);
        void GetStatic(VirtualServiceItem& service);
        void SetDescription(VirtualServiceItem& service);
        void RefreshHA();
        static void RefreshAll(const StringCollection& dev);
        static ServiceType CheckSupportPortMap(VirtualServiceItem& service);
};

#endif // __SERVICES_H__
