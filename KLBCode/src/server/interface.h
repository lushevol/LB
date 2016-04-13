#ifndef __INTERFACE_H__
#define __INTERFACE_H__

#include "model/interface.h"

class PhysicalInterfaceControl
{
    protected:
        static const int DevSystemType = 1;
        static int GetMTU(const String& dev);
        static bool GetEnabled(const String& dev);
        static bool GetCarrier(const String& dev);
        static String GetAddress(const String& dev);
        static void GetIP(const String& dev, List<HostPackStringValue>& result);
    protected:
        static void SetMTU(const String& dev, int mtu);
        static void SetEnabled(const String& dev, bool enabled);
        static void SetAddress(const String& dev, const String& address);
        static void FlushIP(const String& dev);
        static void SetIP(const String& dev, List<HostPackStringValue>& result);
        static void SetArp(const String& dev, int arp);
        static void SetDhcp(const String& dev, String& gate, String& dns);
    protected:
        static void SetMTU(PhysicalInterface& target, PhysicalInterface& recent);
        static void SetEnabled(PhysicalInterface& target, PhysicalInterface& recent);
        static void SetAddress(PhysicalInterface& target, PhysicalInterface& recent);
        static void SetIP(PhysicalInterface& target, PhysicalInterface& recent);
        static void SetArp(PhysicalInterface& target, PhysicalInterface& recent);
        static void SetDhcp(PhysicalInterface& target, PhysicalInterface& recent);
    protected:
        static void InitInterface(const String& dev, PhysicalInterface& interface);
        static void Get(PhysicalInterface& target, PhysicalInterface& recent);
    public:
        List<PhysicalInterface> Holder;
    public:
        PhysicalInterfaceControl(Value& holder);
        Value& Find(const String& dev);
        static void SendArp(const String& ip, const String& dev);
};

class InterfaceRefresher
{
    public:
        typedef void (*Func)(const StringCollection& dev);
    private:
        int FPriority;
        Func FRefresh;
    public:
        static void Refresh(const String& dev);
        static void Refresh(const StringCollection& dev);
        InterfaceRefresher(Func refresh, int priority);
        ~InterfaceRefresher();
};

#endif // __INTERFACE_H__
