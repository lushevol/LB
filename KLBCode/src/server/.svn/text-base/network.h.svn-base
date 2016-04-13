#ifndef __NETWORK_H__
#define __NETWORK_H__

#include "model/model.h"
#include "model/types.h"

class Network
{
    public:
        enum InterfaceType
        {
            Unknown,
            Ethernet,
            Bonding,
            Adsl
        };
        static InterfaceType GetDevType(const String& dev);
        static int GetProtocol(const String& name);
        static int GetServiceByProtocol(int protocol, const String& service);
        static bool IsSupportPort(int protocol);
};

class DnsServerControl
{
    public:
        List<HostStringValue> DnsList;
    public:
        DnsServerControl();
        void Get(List<HostStringValue>& dns);
        void Set(List<HostStringValue>& dns);
        void Refresh();
};

class HostnameControl
{
    private:
        HostnameStringValue FName;
        static void SetName(const String& name);
    public:
        HostnameControl();
        void Get(HostnameStringValue& name);
        void Set(HostnameStringValue& name);
        const String& Name();
};

class AntiDosControl
{
    private:
        BoolValue FAnti;
    public:
        AntiDosControl();
        void Get(BoolValue& anti);
        void Set(BoolValue& anti);
        const bool& Anti();
};

#endif // __NETWORK_H__
