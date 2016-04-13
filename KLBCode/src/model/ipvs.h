#ifndef __IPVS_H_MODEL__
#define __IPVS_H_MODEL__

#include "model.h"
#include "types.h"
#include "interface.h"

class HAState: public State
{
    public:
        static const int Local = 0;
        static const int Self = 1;
        static const int Other = 2;
        virtual const char* Get(int state) const
        {
            switch(state)
            {
                case Local:
                case Self:
                case Other:
                    return "";
                default:
                    return NULL;
            };
        }
        virtual int Default() const
        {
            return Local;
        }
};
typedef StateValue<HAState> HAStateValue;

class VirtualServiceItem: public Model
{
    public:
        class StatisticDataItem: public Model
        {
            public:
                Int64Value InPacket;
                Int64Value InPacketRate;
            public:
                StatisticDataItem(Value& item)
                    : Model(item)
                    , InPacket(item["InPacket"])
                    , InPacketRate(item["InPacketRate"])
                {}
                StatisticDataItem& operator=(StatisticDataItem& recent)
                {
                    InPacket = recent.InPacket;
                    InPacketRate = recent.InPacketRate;
                    return *this;
                }
        };
        class ScheduleState: public State
        {
            public:
                static const int rr = 0;
                static const int wrr = 1;
                static const int lc = 2;
                static const int wlc = 3;
                static const int lblc = 4;
                static const int lblcr = 5;
                static const int dh = 6;
                static const int sh = 7;
                static const int sed = 8;
                static const int nq = 9;
                virtual const char* Get(int state) const
                {
                    switch(state)
                    {
                        case rr:
                            return "rr";
                        case wrr:
                            return "wrr";
                        case lc:
                            return "lc";
                        case wlc:
                            return "wlc";
                        case lblc:
                            return "lblc";
                        case lblcr:
                            return "lblcr";
                        case dh:
                            return "dh";
                        case sh:
                            return "sh";
                        case sed:
                            return "sed";
                        case nq:
                            return "nq";
                        default:
                            return NULL;
                    };
                }
                virtual int Default() const
                {
                    return rr;
                }
        };
        typedef StateValue<ScheduleState> ScheduleStateValue;

        class PortItem: public Model
        {
            public:
                PortValue Min;
                PortValue Max;
            public:
                PortItem(Value& port)
                    : Model(port)
                    , Min(port["Min"])
                    , Max(port["Max"])
                {}
        };

        class ServerItem: public Model
        {
            public:
                class ActionState: public State
                {
                    public:
                        static const int Gate = 0;
                        static const int Masq = 1;
                        static const int Ipip = 2;
                        virtual const char* Get(int state) const
                        {
                            switch(state)
                            {
                                case Gate:
                                    return "gate";
                                case Masq:
                                    return "masq";
                                case Ipip:
                                    return "ipip";
                                default:
                                    return NULL;
                            };
                        }
                        virtual int Default() const
                        {
                            return Gate;
                        }
                };
                typedef StateValue<ActionState> ActionStateValue;

                typedef RangedIntValue<0, 65535, 1> WeightValue;
            public:
                UnsignedValue ID;
                NormalStringValue Name;
                WeightValue Weight;
                ActionStateValue Action;
                BoolValue Enabled;
                HostStringValue IP;
                PortValue MapPort;
                BoolValue Status;
                UnsignedValue Active;
                UnsignedValue InActive;
                StatisticDataItem Statistic;
            public:
                ServerItem(Value& server)
                    : Model(server)
                    , ID(server["ID"])
                    , Name(server["Name"])
                    , Weight(server["Weight"])
                    , Action(server["Action"])
                    , Enabled(server["Enabled"])
                    , IP(server["IP"])
                    , MapPort(server["MapPort"])
                    , Status(server["Status"])
                    , Active(server["Active"])
                    , InActive(server["InActive"])
                    , Statistic(server["Statistic"])
                {}
        };

        class MonitorItem: public Model
        {
            public:
                class TypeState: public State
                {
                    public:
                        static const int Off = 0;
                        static const int Ping = 1;
                        static const int Connect = 2;
                        virtual const char* Get(int state) const
                        {
                            switch(state)
                            {
                                case Off:
                                case Ping:
                                case Connect:
                                    return "";
                                default:
                                    return NULL;
                            };
                        }
                        virtual int Default() const
                        {
                            return Off;
                        }
                };
                typedef StateValue<TypeState> TypeStateValue;

                typedef RangedIntValue<1, 360> TimeValue;
                typedef RangedIntValue<1, 16> RetryValue;
                typedef RangedIntValue<1, 360000> DateValue;
            public:
                TimeValue Interval;
                TimeValue Timeout;
                RetryValue Retry;
                TypeStateValue Type;
                RealPortValue Port;
		BoolValue Enabled;
		CommonStringValue Mail;
		DateValue Date;
            public:
                MonitorItem(Value& monitor)
                    : Model(monitor)
                    , Interval(monitor["Interval"])
                    , Timeout(monitor["Timeout"])
                    , Retry(monitor["Retry"])
                    , Type(monitor["Type"])
                    , Port(monitor["Port"])
                    , Enabled(monitor["Enabled"])
                    , Mail(monitor["Mail"])
                    , Date(monitor["Date"])
                {}
        };

        class IPItem: public Model
        {
            public:
                HostStringValue IP;
                NormalStringValue Dev;
                BoolValue Status;
            public:
                IPItem(Value& item)
                    : Model(item)
                    , IP(item["IP"])
                    , Dev(item["Dev"])
                    , Status(item["Status"])
                {}
        };

        class TrafficItem: public Model
        {
            public:
                UnsignedValue Up;
                UnsignedValue Down;
            public:
                TrafficItem(Value& item)
                    : Model(item)
                    , Up(item["Up"])
                    , Down(item["Down"])
                {}
        };

        typedef RangedIntValue<0xF000, 0xFFFF> MarkValue;
        typedef RangedIntValue<60, 86400> PersistentTimeoutValue;
    public:
        UnsignedValue ID;
        NormalStringValue Name;
        CommonStringValue Description;
        List<IPItem> IP;
        MarkValue Mark;
        BoolValue Enabled;
        ScheduleStateValue Schedule;
        List<ServerItem> Servers;
        BoolValue Persistent;
        PersistentTimeoutValue PersistentTimeout;
        NetmaskStringValue PersistentNetmask;
        List<PortItem> TcpPorts;
        List<PortItem> UdpPorts;
        MonitorItem Monitor;
        TrafficItem Traffic;
        HAStateValue HA;
        BoolValue HAStatus;
        StatisticDataItem Statistic;
    public:
        VirtualServiceItem(Value& service)
            : Model(service)
            , ID(service["ID"])
            , Name(service["Name"])
            , Description(service["Description"])
            , IP(service["IP"])
            , Mark(service["Mark"])
            , Enabled(service["Enabled"])
            , Schedule(service["Schedule"])
            , Servers(service["Servers"])
            , Persistent(service["Persistent"])
            , PersistentTimeout(service["PersistentTimeout"])
            , PersistentNetmask(service["PersistentNetmask"])
            , TcpPorts(service["TcpPorts"])
            , UdpPorts(service["UdpPorts"])
            , Monitor(service["Monitor"])
            , Traffic(service["Traffic"])
            , HA(service["HA"])
            , HAStatus(service["HAStatus"])
            , Statistic(service["Statistic"])
        {}
};

static const char* const FuncVirtualServicesAdd             = "VirtualService.Add";
static const char* const FuncVirtualServicesDelete          = "VirtualService.Delete";
static const char* const FuncVirtualServicesEnabled         = "VirtualService.Enabled";
static const char* const FuncVirtualServicesAddress         = "VirtualService.Address";
static const char* const FuncVirtualServicesService         = "VirtualService.Service";
static const char* const FuncVirtualServicesTraffic         = "VirtualService.Traffic";
static const char* const FuncVirtualServicesAddServer       = "VirtualService.Server.Add";
static const char* const FuncVirtualServicesDelServer       = "VirtualService.Server.Delete";
static const char* const FuncVirtualServicesSetServer       = "VirtualService.Server.Set";
static const char* const FuncVirtualServicesEnabledServer   = "VirtualService.Server.Enabled";
static const char* const FuncVirtualServicesGetCount        = "VirtualService.GetCount";
static const char* const FuncVirtualServicesGet             = "VirtualService.Get";
static const char* const FuncVirtualServicesHA              = "VirtualService.HA";
static const char* const FuncVirtualServicesDescription     = "VirtualService.Description";
static const char* const FuncVirtualServicesNameToID        = "VirtualService.NameToID";

class StatisticItem: public Model
{
    public:
        typedef RangedIntValue<1, MaximumInteger> IntervalValue;
    public:
        IntValue From;
        IntValue To;
        IntervalValue Interval;
        StringValue Result;
    public:
        StatisticItem(Value& item)
            : Model(item)
            , From(item["From"])
            , To(item["To"])
            , Interval(item["Interval"])
            , Result(item["Result"])
        {}
};

class StatisticServiceItem: public StatisticItem
{
    public:
        UnsignedValue ID;
    public:
        StatisticServiceItem(Value& item)
            : StatisticItem(item)
            , ID(item["ID"])
        {}
};

class StatisticServerItem: public StatisticItem
{
    public:
        HostStringValue IP;
    public:
        StatisticServerItem(Value& item)
            : StatisticItem(item)
            , IP(item["IP"])
        {}
};

static const char* const FuncStatisticService   = "Statistic.Service";
static const char* const FuncStatisticServer    = "Statistic.Server";

class HAItem: public Model
{
    public:
        typedef RangedIntValue<10, 360000, 1000> TimeValue;
        typedef RangedIntValue<0, 255> SyncIDValue;
    public:
        TimeValue Keepalive;
        TimeValue Deadtime;
        TimeValue Warntime;
        TimeValue Initdead;
        BoolValue Autoback;
        RealPortValue Port;
        HostStringValue IP;
        NormalStringValue Dev;
        BoolValue DevStatus;
        HostnameStringValue Hostname;
        BoolValue Enabled;
        BoolValue Self;
        BoolValue Other;
        List<List<DomainStringValue> > Ping;
        SyncIDValue Sync;
	BoolValue Send;
	CommonStringValue Address;
    public:
        HAItem(Value& ha)
            : Model(ha)
            , Keepalive(ha["Keepalive"])
            , Deadtime(ha["Deadtime"])
            , Warntime(ha["Warntime"])
            , Initdead(ha["Initdead"])
            , Autoback(ha["Autoback"])
            , Port(ha["Port"])
            , IP(ha["IP"])
            , Dev(ha["Dev"])
            , DevStatus(ha["DevStatus"])
            , Hostname(ha["Hostname"])
            , Enabled(ha["Enabled"])
            , Self(ha["Self"])
            , Other(ha["Other"])
            , Ping(ha["Ping"])
            , Sync(ha["Sync"])
            , Send(ha["Send"])
            , Address(ha["Address"])
        {}
};

static const char* const FuncHASet      = "HA.Set";
static const char* const FuncHAGet      = "HA.Get";
static const char* const FuncHAEnabled  = "HA.Enabled";
static const char* const FuncHAChanged  = "HA.Changed";

#endif // __IPVS_H_MODEL__
