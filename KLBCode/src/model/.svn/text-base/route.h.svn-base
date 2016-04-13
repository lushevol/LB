#ifndef __ROUTE_H_MODEL__
#define __ROUTE_H_MODEL__

#include "share/include.h"

#include "model.h"
#include "types.h"
#include "isp.h"

class RouteItem: public Model
{
    public:
        class GatePolicyState: public State
        {
            public:
                static const int rr = 0;
                static const int drr = 1;
                static const int random = 2;
                static const int wrandom = 3;
                virtual const char* Get(int state) const
                {
                    switch(state)
                    {
                        case rr:
                            return "rr";
                        case drr:
                            return "drr";
                        case random:
                            return "random";
                        case wrandom:
                            return "wrandom";
                        default:
                            return NULL;
                    };
                }
                virtual int Default() const
                {
                    return rr;
                }
        };
        typedef StateValue<GatePolicyState> GatePolicyStateValue;

        class GateItem: public Model
        {
            public:
                typedef RangedIntValue<1, 256> WeightValue;
            public:
                HostStringValue IP;
                NormalStringValue Dev;
                WeightValue Weight;
                BoolValue Auto;
                BoolValue Status;
            public:
                GateItem(Value& gate)
                    : Model(gate)
                    , IP(gate["IP"])
                    , Dev(gate["Dev"])
                    , Weight(gate["Weight"])
                    , Auto(gate["Auto"])
                    , Status(gate["Status"])
                {}
        };
    public:
        UnsignedValue ID;
        CommonStringValue Description;
        GatePolicyStateValue GatePolicy;
        List<GateItem> Gates;
        BoolValue Status;
        BoolValue Link;
    public:
        RouteItem(Value& route)
            : Model(route)
            , ID(route["ID"])
            , Description(route["Description"])
            , GatePolicy(route["GatePolicy"])
            , Gates(route["Gates"])
            , Status(route["Status"])
            , Link(route["Link"])
        {}
};

class StaticRouteItem: public RouteItem
{
    public:
        NetPackStringValue Net;
        UnsignedValue Metric;
    public:
        StaticRouteItem(Value& route)
            : RouteItem(route)
            , Net(route["Net"])
            , Metric(route["Metric"])
        {}
};

static const char* const FuncStaticRouteAdd         = "StaticRoute.Add";
static const char* const FuncStaticRouteDelete      = "StaticRoute.Delete";
static const char* const FuncStaticRouteSet         = "StaticRoute.Set";
static const char* const FuncStaticRouteGetCount    = "StaticRoute.GetCount";
static const char* const FuncStaticRouteGet         = "StaticRoute.Get";
static const char* const FuncStaticRouteDescription = "StaticRoute.Description";

class PolicyRouteItem: public RouteItem
{
    public:
        typedef RangedIntValue<50, 250> MarkValue;

        class RuleItem: public Model
        {
            public:
                UnsignedValue ID;
                NetPackStringValue SrcNet;
                NetPackStringValue DestNet;
                ProtocolValue Protocol;
                NormalStringValue ProtocolStr;
                PortValue SrcPort;
                NormalStringValue SrcPortStr;
                PortValue DestPort;
                NormalStringValue DestPortStr;
            public:
                RuleItem(Value& policy)
                    : Model(policy)
                    , ID(policy["ID"])
                    , SrcNet(policy["SrcNet"])
                    , DestNet(policy["DestNet"])
                    , Protocol(policy["Protocol"])
                    , ProtocolStr(policy["ProtocolStr"])
                    , SrcPort(policy["SrcPort"])
                    , SrcPortStr(policy["SrcPortStr"])
                    , DestPort(policy["DestPort"])
                    , DestPortStr(policy["DestPortStr"])
                {}
        };
    public:
        List<RuleItem> Rules;
        MarkValue Mark;
    public:
        PolicyRouteItem(Value& route)
            : RouteItem(route)
            , Rules(route["Rules"])
            , Mark(route["Mark"])
        {}
};

static const char* const FuncPolicyRouteAdd         = "PolicyRoute.Add";
static const char* const FuncPolicyRouteInsert      = "PolicyRoute.Insert";
static const char* const FuncPolicyRouteDelete      = "PolicyRoute.Delete";
static const char* const FuncPolicyRouteSet         = "PolicyRoute.Set";
static const char* const FuncPolicyRouteGetCount    = "PolicyRoute.GetCount";
static const char* const FuncPolicyRouteGet         = "PolicyRoute.Get";
static const char* const FuncPolicyRouteAddRule     = "PolicyRoute.AddRule";
static const char* const FuncPolicyRouteDeleteRule  = "PolicyRoute.DeleteRule";
static const char* const FuncPolicyRouteDescription = "PolicyRoute.Description";

class SmartRouteItem: public RouteItem
{
    public:
        typedef RangedIntValue<40, 49> MarkValue;
        class CheckModeState: public State
        {
            public:
                static const int disable = 0;
                static const int ping = 1;
                static const int tcp = 2;
                static const int udp = 3;
                virtual const char* Get(int state) const
                {
                    switch(state)
                    {
                        case disable:
                            return "disable";
                        case ping:
                            return "ping";
                        case tcp:
                            return "tcp";
                        case udp:
                            return "udp";
                        default:
                            return NULL;
                    };
                }
                virtual int Default() const
                {
                    return disable;
                }
        };
        typedef StateValue<CheckModeState> CheckModeStateValue;
        typedef RangedIntValue<1, MaximumInteger, 5> FrequencyValue;
        typedef RangedIntValue<1, MaximumInteger, 3> TimeoutValue;
    public:
        MarkValue Mark;
        ISPItem::IDValue ISP;
        CheckModeStateValue Mode;
        HostStringValue Ip;
        PortValue Port;
        FrequencyValue Frequency;
        TimeoutValue Timeout;
    public:
        SmartRouteItem(Value& route)
            : RouteItem(route)
            , Mark(route["Mark"])
            , ISP(route["ISP"])
            , Mode(route["Mode"])
            , Ip(route["Ip"])
            , Port(route["Port"])
            , Frequency(route["Frequcency"])
            , Timeout(route["Timeout"])
        {}
};

static const char* const FuncSmartRouteAdd          = "SmartRoute.Add";
static const char* const FuncSmartRouteDelete       = "SmartRoute.Delete";
static const char* const FuncSmartRouteSet          = "SmartRoute.Set";
static const char* const FuncSmartRouteGetCount     = "SmartRoute.GetCount";
static const char* const FuncSmartRouteGet          = "SmartRoute.Get";
static const char* const FuncSmartRouteDescription  = "SmartRoute.Description";
static const char* const FuncSmartRouteCheck        = "SmartRoute.Check";

#endif // __ROUTE_H_MODEL__
