#ifndef __ARP_H__
#define __ARP_H__

#include "model/arp.h"

class ArpControl
{
    private:
        typedef ArpItem::ArpState ArpState;
    private:
        static bool CheckDev(const String& dev);
    public:
        List<ArpItem> Holder;
    public:
        ArpControl();
        void GetStatic(List<ArpItem>& list);
        void GetDynamic(List<ArpItem>& list);
        void Set(ArpItem& arp);
        void Del(ArpItem& arp);
        static void Refresh(const StringCollection& dev);
};

#endif // __ARP_H__
