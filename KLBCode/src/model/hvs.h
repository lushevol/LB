#ifndef __HVS_H_INCLUDED__
#define __HVS_H_INCLUDED__

#include "model.h"
#include "types.h"
#include "interface.h"
#include "ipvs.h"

class HttpItem: public Model
{
    public:
        class GroupItem: public Model
        {
            public:
                class ServerItem: public Model
                {
                    public:
                        class TypeState: public State
                        {
                            public:
                                static const int Normal = 0;
                                static const int Down = 1;
                                static const int Backup = 2;
                                virtual const char* Get(int state) const
                                {
                                    switch(state)
                                    {
                                        case Normal:
                                            return "normal";
                                        case Down:
                                            return "down";
                                        case Backup:
                                            return "backup";
                                        default:
                                            return NULL;
                                    };
                                }
                                virtual int Default() const
                                {
                                    return Normal;
                                }
                        };
                        typedef StateValue<TypeState> TypeStateValue;
                        typedef RangedIntValue<1, 255> ShortValue;
                        typedef RangedIntValue<1, 0x7FFFFFFF, 10> TimeoutValue;

                    public:
                        UnsignedValue GroupID;
                        UnsignedValue ID;
                        HostStringValue IP;
                        RealPortValue Port;
                        ShortValue Weight;
                        ShortValue MaxFails;
                        TimeoutValue FailTimeout;
                        TypeStateValue Type;
                        AlnumStringValue SrunID;
                    public:
                        ServerItem(Value& server)
                            : Model(server)
                            , GroupID(server["GroupID"])
                            , ID(server["ID"])
                            , IP(server["IP"])
                            , Port(server["Port"])
                            , Weight(server["Weight"])
                            , MaxFails(server["MaxFails"])
                            , FailTimeout(server["FailTimeout"])
                            , Type(server["Type"])
                            , SrunID(server["SrunID"])
                        { }
                };

                class MethodState: public State
                {
                    public:
                        static const int RR = 0;
                        static const int WRR = 1;
                        static const int IPHash = 2;
                        static const int Fair = 3;
                        static const int URLHash = 4;
                        static const int Cookie = 5;
                        static const int CookieTomcat = 6;
                        static const int CookieResin = 7;
                        static const int ProxyIPHash = 8;
                        virtual const char* Get(int state) const
                        {
                            switch(state)
                            {
                                case RR:
                                    return "rr";
                                case WRR:
                                    return "wrr";
                                case IPHash:
                                    return "iphash";
                                case Fair:
                                    return "fair";
                                case URLHash:
                                    return "urlhash";
                                case Cookie:
                                    return "cookie";
                                case CookieTomcat:
                                    return "cookie-tomcate";
                                case CookieResin:
                                    return "cookie-resin";
                                case ProxyIPHash:
                                    return "proxy-iphash";
                                default:
                                    return NULL;
                            };
                        }
                        virtual int Default() const
                        {
                            return RR;
                        }
                };
                typedef StateValue<MethodState> MethodStateValue;

            public:
                UnsignedValue ID;
                AlnumStringValue Name;
                MethodStateValue Method;
                List<ServerItem> ServerList;
            public:
                GroupItem(Value& group)
                    : Model(group)
                    , ID(group["ID"])
                    , Name(group["Name"])
                    , Method(group["Method"])
                    , ServerList(group["ServerList"])
                { }
        };

        class ServiceItem: public Model
        {
            public:
                class LocationItem: public Model
                {
		    public:
			typedef RangedIntValue<0, 0x7FFFFFFF, 0> CacheValidValue;
                    public:
                        UnsignedValue ServiceID;
                        UnsignedValue ID;
                        NormalStringValue Match;
                        AlnumStringValue Group;
			CacheValidValue CacheValid;
                    public:
                        LocationItem(Value& location)
                            : Model(location)
                            , ServiceID(location["ServiceID"])
                            , ID(location["ID"])
                            , Match(location["Match"])
                            , Group(location["Group"])
                            , CacheValid(location["CacheValid"])
                        {}
                };

                typedef RangedIntValue<1, 0x7FFFFFFF, 60> TimeoutValue;
            public:
                UnsignedValue ID;
                DomainGroupStringValue Name;
                HostStringValue IP;
                RealPortValue Port;
                NormalStringValue Dev;
                BoolValue Status;
                BoolValue Ssl;
                TimeoutValue SslTimeout;
                BinaryValue Cert;
                BinaryValue Key;
                List<LocationItem> LocationList;
                HAStateValue HA;
                BoolValue HAStatus;
                BoolValue CookieEnabled;
                AlnumStringValue CookieName;
                TimeoutValue CookieExpire;
            public:
                ServiceItem(Value& service)
                    : Model(service)
                    , ID(service["ID"])
                    , Name(service["Name"])
                    , IP(service["IP"])
                    , Port(service["Port"])
                    , Dev(service["Dev"])
                    , Status(service["Status"])
                    , Ssl(service["Ssl"])
                    , SslTimeout(service["SslTimeout"])
                    , Cert(service["Cert"])
                    , Key(service["Key"])
                    , LocationList(service["LocationList"])
                    , HA(service["HA"])
                    , HAStatus(service["HAStatus"])
                    , CookieEnabled(service["CookieEnabled"])
                    , CookieName(service["CookieName"])
                    , CookieExpire(service["CookieExpire"])
                { }
        };
        typedef RangedIntValue<1, 64, 2> ProcessorValue;
        typedef RangedIntValue<64, 65536, 1024> ConnectionsValue;
        typedef RangedIntValue<1, 0x7FFFFFFF, 60> KeepaliveValue;
        typedef RangedIntValue<1, 0x7FFFFFFF, 1024> GzipLengthValue;
        typedef RangedIntValue<1, 0x7FFFFFFF, 10> CacheInactiveValue;
    public:
        BoolValue Enabled;
        BoolValue DenyNotMatch;
        ProcessorValue Processor;
        ConnectionsValue Connections;
        KeepaliveValue Keepalive;
        BoolValue Gzip;
        GzipLengthValue GzipLength;
	CacheInactiveValue CacheInactive;
        List<ServiceItem> ServiceList;
        List<GroupItem> GroupList;
        BoolValue Status;
    public:
        HttpItem(Value& http)
            : Model(http)
            , Enabled(http["Enabled"])
            , DenyNotMatch(http["DenyNotMatch"])
            , Processor(http["Processor"])
            , Connections(http["Connections"])
            , Keepalive(http["Keepalive"])
            , Gzip(http["Gzip"])
            , GzipLength(http["GzipLength"])
            , CacheInactive(http["CacheInactive"])
            , ServiceList(http["ServiceList"])
            , GroupList(http["GroupList"])
            , Status(http["Status"])
        { }
};

static const char* const FuncHttpGet                    = "Http.Get";
static const char* const FuncHttpSet                    = "Http.Set";
static const char* const FuncHttpServiceAdd             = "Http.Service.Add";
static const char* const FuncHttpServiceDel             = "Http.Service.Del";
static const char* const FuncHttpServiceSet             = "Http.Service.Set";
static const char* const FuncHttpServiceGet             = "Http.Service.Get";
static const char* const FuncHttpServiceList            = "Http.Service.List";
static const char* const FuncHttpLocationAdd            = "Http.Service.Location.Add";
static const char* const FuncHttpLocationDel            = "Http.Service.Location.Del";
static const char* const FuncHttpLocationSet            = "Http.Service.Location.Set";
static const char* const FuncHttpGroupAdd               = "Http.Group.Add";
static const char* const FuncHttpGroupDel               = "Http.Group.Del";
static const char* const FuncHttpGroupSet               = "Http.Group.Set";
static const char* const FuncHttpGroupGet               = "Http.Group.Get";
static const char* const FuncHttpGroupList              = "Http.Group.List";
static const char* const FuncHttpServerAdd              = "Http.Group.Server.Add";
static const char* const FuncHttpServerDel              = "Http.Group.Server.Del";
static const char* const FuncHttpServerSet              = "Http.Group.Server.Set";

namespace Exception
{
    namespace Http
    {
        static const int NameEmpty = 13001;
        static const int NameDuplicate = 13002;
        static const int AddressEmpty = 13003;
        static const int AddressDuplicate = 13004;
        static const int LocationMatchEmpty = 13005;
        static const int LocationGroupNotFound = 13006;
        static const int GroupNameEmpty = 13007;
        static const int GroupNameDuplicate = 13008;
        static const int ServerIPEmpty = 13009;
        static const int GroupUsed = 13010;
        static const int PortUsed = 13011;
    };
};

#endif // __HVS_H_INCLUDED__
