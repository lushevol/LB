#ifndef __BONDING_H__
#define __BONDING_H__

#include "interface.h"

class BondingInterfaceControl: public PhysicalInterfaceControl
{
    private:
        static void GetSlaves(const String& dev, StringList& list);
        static void AddSlave(const String& dev, const String& slave);
        static void DelSlave(const String& dev, const String& slave);
        static void AddCheckIP(const String& dev, const String& ip);
        static void DelCheckIP(const String& dev, const String& ip);
        static void GetCheckIPList(const String& dev, StringList& list);
        static void SetMode(const String& dev, int mode);
        static void SetLinkCheck(const String& dev, int mode, int frequency);
        static void AddBonding(const String& dev);
        static void DelBonding(const String& dev);
        static void CheckSupportMode(int mode, List<EthernetInterfaceNameValue>& eth);
    protected:
        static void SetAddress(BondingInterface& target, BondingInterface& recent);
    public:
        List<BondingInterface> Holder;
    public:
        BondingInterfaceControl();
        void SetMTU(BondingInterface& target);
        void SetEnabled(BondingInterface& target);
        void SetAddress(BondingInterface& target);
        void SetIP(BondingInterface& target);
        void SetArp(BondingInterface& target);
        void SetDhcp(BondingInterface& target);
        void SetSlaves(BondingInterface& target);
        void SetMode(BondingInterface& target);
        void SetLinkCheck(BondingInterface& target);
        void SetLinkCheckIP(BondingInterface& target);
        void AddBonding(BondingInterface& target);
        void DelBonding(BondingInterface& target);
        void Get(BondingInterface& target);
        void GetAll(List<BondingInterface>& list);
        void SetDescription(BondingInterface& target);
};

#endif // __BONDING_H__
