#ifndef __INTERFACE_H_MODEL__
#define __INTERFACE_H_MODEL__

#include <string.h>

#include "model.h"
#include "types.h"

class PhysicalInterface: public Model
{
    public:
        class ArpState: public State
        {
            public:
                static const int Enabled = 0;
                static const int Disabled = 1;
                static const int ReplyOnly = 2;
                static const int Proxy = 3;
                virtual const char* Get(int state) const
                {
                    switch(state)
                    {
                        case Enabled:
                            return "Enabled";
                        case Disabled:
                            return "Disabled";
                        case ReplyOnly:
                            return "Reply only";
                        case Proxy:
                            return "Proxy";
                        default:
                            return NULL;
                    };
                }
                virtual int Default() const
                {
                    return Enabled;
                }
        };
        typedef StateValue<ArpState> ArpStateValue;
        typedef RangedIntValue<68, 1500, 1500> MTUValue;
    public:
        UnsignedValue ID;
        CommonStringValue Description;
        List<HostPackStringValue> IP;
        BoolValue Enabled;
        MTUValue MTU;
        PhysicalAddressStringValue Address;
        PhysicalAddressStringValue CurrentAddress;
        NormalStringValue Dev;
        BoolValue Dhcp;
        HostStringValue Gate;
        HostStringValue Dns;
        ArpStateValue Arp;
        BoolValue Carrier;
    public:
        PhysicalInterface(Value& interface)
            : Model(interface)
            , ID(interface["ID"])
            , Description(interface["Description"])
            , IP(interface["IP"])
            , Enabled(interface["Enabled"])
            , MTU(interface["MTU"])
            , Address(interface["Address"])
            , CurrentAddress(interface["CurrentAddress"])
            , Dev(interface["Dev"])
            , Dhcp(interface["Dhcp"])
            , Gate(interface["Gate"])
            , Dns(interface["Dns"])
            , Arp(interface["Arp"])
            , Carrier(interface["Carrier"])
        {}
};

class EthernetInterfaceName: public SpecialString
{
    public:
        virtual bool Get(const String& value, String& result)
        {
            if(value.empty())
            {
                result.clear();
                return true;
            }
            if(value.size() < 4)
                return false;
            if(strncmp(value.c_str(), "eth", 3) != 0)
                return false;
            for(String::size_type i = 3; i < value.size(); ++i)
                if(!isdigit(value[i]))
                    return false;
            result = value;
            return true;
        }
        virtual void Default(String& value)
        {
            value = "";
        }
};

typedef SpecialStringValue<EthernetInterfaceName> EthernetInterfaceNameValue;

class BondingInterfaceName: public SpecialString
{
    public:
        virtual bool Get(const String& value, String& result)
        {
            if(value.empty())
            {
                result.clear();
                return true;
            }
            if(value.size() < 5)
                return false;
            if(strncmp(value.c_str(), "bond", 4) != 0)
                return false;
            for(String::size_type i = 4; i < value.size(); ++i)
                if(!isdigit(value[i]))
                    return false;
            result = value;
            return true;
        }
        virtual void Default(String& value)
        {
            value = "";
        }
};

typedef SpecialStringValue<BondingInterfaceName> BondingInterfaceNameValue;

class EthernetInterface: public PhysicalInterface
{
    public:
        class SpeedState: public State
        {
            public:
                static const int Miiln = 10000;
                static const int Thousand = 1000;
                static const int Hundred = 100;
                static const int Ten = 10;
                virtual const char* Get(int state) const
                {
                    switch(state)
                    {
                        case Miiln:
                        case Thousand:
                        case Hundred:
                        case Ten:
                            return "";
                        default:
                            return NULL;
                    };
                }
                virtual int Default() const
                {
                    return Thousand;
                }
        };
        typedef StateValue<SpeedState> SpeedStateValue;
    public:
        EthernetInterfaceNameValue Dev;
        NormalStringValue Master;
        NormalStringValue Adsl;
        PhysicalAddressStringValue RealAddress;
        BoolValue Detect;
        BoolValue FullDuplex;
        SpeedStateValue Speed;
    public:
        EthernetInterface(Value& interface)
            : PhysicalInterface(interface)
            , Dev(PhysicalInterface::Dev.Data)
            , Master(interface["Master"])
            , Adsl(interface["Adsl"])
            , RealAddress(interface["RealAddress"])
            , Detect(interface["Detect"])
            , FullDuplex(interface["FullDuplex"])
            , Speed(interface["Speed"])
        {}
};

static const char* const FuncEthernetEnabled        = "Ethernet.Enabled";
static const char* const FuncEthernetMTU            = "Ethernet.MTU";
static const char* const FuncEthernetAddress        = "Ethernet.Address";
static const char* const FuncEthernetIP             = "Ethernet.IP";
static const char* const FuncEthernetArp            = "Ethernet.Arp";
static const char* const FuncEthernetDhcp           = "Ethernet.Dhcp";
static const char* const FuncEthernetLink           = "Ethernet.Link";
static const char* const FuncEthernetGet            = "Ethernet.Get";
static const char* const FuncEthernetGetAll         = "Ethernet.GetAll";
static const char* const FuncEthernetDescription    = "Ethernet.Description";

class BondingInterface: public PhysicalInterface
{
    public:
        class CheckModeState: public State
        {
            public:
                static const int miimon = 0;
                static const int arp = 1;
                virtual const char* Get(int state) const
                {
                    switch(state)
                    {
                        case miimon:
                            return "miimon";
                        case arp:
                            return "arp";
                        default:
                            return NULL;
                    };
                }
                virtual int Default() const
                {
                    return miimon;
                }
        };
        typedef StateValue<CheckModeState> CheckModeStateValue;
        class ModeState: public State
        {
            public:
                static const int rr = 0;
                static const int backup = 1;
                static const int bxor = 2;
                static const int broadcast = 3;
                static const int dot3ad = 4;
                static const int btlb = 5;
                static const int balb = 6;
                virtual const char* Get(int state) const
                {
                    switch(state)
                    {
                        case rr:
                            return "balance-rr";
                        case backup:
                            return "active-backup";
                        case bxor:
                            return "balance-xor";
                        case broadcast:
                            return "broadcast";
                        case dot3ad:
                            return "802.3ad";
                        case btlb:
                            return "balance-tlb";
                        case balb:
                            return "balance-alb";
                        default:
                            return NULL;
                    };
                }
                virtual int Default() const
                {
                    return rr;
                }
        };
        typedef StateValue<ModeState> ModeStateValue;
        typedef RangedIntValue<1, MaximumInteger, 100> FrequencyValue;
    public:
        BondingInterfaceNameValue Dev;
        List<EthernetInterfaceNameValue> Slaves;
        ModeStateValue Mode;
        CheckModeStateValue CheckMode;
        FrequencyValue Frequency;
        List<HostStringValue> CheckIP;
    public:
        BondingInterface(Value& interface)
            : PhysicalInterface(interface)
            , Dev(PhysicalInterface::Dev.Data)
            , Slaves(interface["Slaves"])
            , Mode(interface["Mode"])
            , CheckMode(interface["CheckMode"])
            , Frequency(interface["Frequency"])
            , CheckIP(interface["CheckIP"])
        {}
};

static const char* const FuncBondingEnabled         = "Bonding.Enabled";
static const char* const FuncBondingMTU             = "Bonding.MTU";
static const char* const FuncBondingAddress         = "Bonding.Address";
static const char* const FuncBondingIP              = "Bonding.IP";
static const char* const FuncBondingArp             = "Bonding.Arp";
static const char* const FuncBondingDhcp            = "Bonding.Dhcp";
static const char* const FuncBondingMode            = "Bonding.Mode";
static const char* const FuncBondingCheck           = "Bonding.Check";
static const char* const FuncBondingCheckIP         = "Bonding.CheckIP";
static const char* const FuncBondingAdd             = "Bonding.Add";
static const char* const FuncBondingDelete          = "Bonding.Delete";
static const char* const FuncBondingSetSlaves       = "Bonding.SetSlaves";
static const char* const FuncBondingGet             = "Bonding.Get";
static const char* const FuncBondingGetAll          = "Bonding.GetAll";
static const char* const FuncBondingDescription     = "Bonding.Description";

#endif // __INTERFACE_H_MODEL__
