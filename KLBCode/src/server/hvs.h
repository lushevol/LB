#ifndef __HVS_H__
#define __HVS_H__

#include <map>
#include "model/hvs.h"

class HttpControl
{
    public:
        typedef HttpItem::ServiceItem ServiceItem;
        typedef HttpItem::GroupItem GroupItem;
        typedef HttpItem::ServiceItem::LocationItem LocationItem;
        typedef HttpItem::GroupItem::ServerItem ServerItem;
        typedef HttpItem::GroupItem::ServerItem::TypeState TypeState;
        typedef HttpItem::GroupItem::MethodState MethodState;
    private:
        typedef std::pair<String, String> AddressPair;
        typedef std::map<AddressPair, int> AddressCounter;
        static AddressCounter FAddressCounter;
    private:
        static bool GetHAStatus(int state);
        void UpdateNginx();
        static void AddIP(const String& ip, const String& dev);
        static void DelIP(const String& ip, const String& dev);
        static void AddIPCounter(const String& ip, const String& dev);
        static void DelIPCounter(const String& ip, const String& dev);
        void EnsureName(const String& name, int id);
        static void EnsurePort(int port);
        void CheckGroup(const String& group);
        void EnsureGroup(const String& group, int id);
        static bool CheckAddress(const AddressPair& address);
        static bool CheckIP(const String& ip, List<HostPackStringValue>& iplist);
    public:
        HttpItem Http;
    public:
        HttpControl();
        void Set(HttpItem& item);
        void Get(HttpItem& item);
        void ServiceAdd(ServiceItem& item);
        void ServiceDel(ServiceItem& item);
        void ServiceSet(ServiceItem& item);
        void ServiceGet(ServiceItem& item);
        void ServiceList(List<ServiceItem>& list);
        void ServiceListExport(List<ServiceItem>& list);
        void LocationAdd(LocationItem& item);
        void LocationDel(LocationItem& item);
        void LocationSet(LocationItem& item);
        void GroupAdd(GroupItem& item);
        void GroupDel(GroupItem& item);
        void GroupSet(GroupItem& item);
        void GroupGet(GroupItem& item);
        void GroupList(List<GroupItem>& list);
        void ServerAdd(ServerItem& item);
        void ServerDel(ServerItem& item);
        void ServerSet(ServerItem& item);
        void RefreshHA();
        static void RefreshAll(const StringCollection& devs);
};

#endif // __HVS_H__
