#ifndef __ETHERNET_H__
#define __ETHERNET_H__

#include "interface.h"

class EthernetInterfaceControl: public PhysicalInterfaceControl
{
    private:
        static bool ExecuteEthtool(Exec& exe);
        static void GenerateInterfaceList(StringList& list);
        static void InitInterface(const String& dev, EthernetInterface& interface);
        static void GetLink(const String& dev, bool& detect, bool& duplex, int& speed);
        static void SetLink(const String& dev, bool detect, bool duplex, int speed);
        static void CheckAdsl(EthernetInterface& interface);
        static void CheckMaster(EthernetInterface& interface);
    protected:
        static void SetLink(EthernetInterface& target, EthernetInterface& recent);
        static void Refresh(EthernetInterface& interface);
    public:
        List<EthernetInterface> Holder;
    public:
        EthernetInterfaceControl();
        void RefreshInterface(const String& dev);
        void RefreshInterface();
        void SetMTU(EthernetInterface& target);
        void SetEnabled(EthernetInterface& target);
        void SetAddress(EthernetInterface& target);
        void SetIP(EthernetInterface& target);
        void SetArp(EthernetInterface& target);
        void SetDhcp(EthernetInterface& target);
        void SetLink(EthernetInterface& target);
        void Get(EthernetInterface& target);
        void GetAll(List<EthernetInterface>& list);
        void SetDescription(EthernetInterface& target);
        void GetAllDev(StringList& list);
};

#endif // __ETHERNET_H__
