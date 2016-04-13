#ifndef __BIND_H_MODEL__
#define __BIND_H_MODEL__

#include "model.h"
#include "types.h"
#include "isp.h"

class BindItem: public Model
{
    public:
        class BaseRecord: public Model
        {
            public:
                typedef RangedIntValue<1, MaximumInteger, 86400> TTLValue;
            public:
                UnsignedValue ID;
                TTLValue TTL;
                BoolValue Enabled;
            public:
                BaseRecord(Value& item)
                    : Model(item)
                    , ID(item["ID"])
                    , TTL(item["TTL"])
                    , Enabled(item["Enabled"])
                {}
        };
        class ARecord: public BaseRecord
        {
            public:
                class ServerItem: public Model
                {
                    public:
                        ISPItem::IDValue ISP;
                        HostStringValue IP;
                    public:
                        ServerItem(Value& item)
                            : Model(item)
                            , ISP(item["ISP"])
                            , IP(item["IP"])
                        {}
                };
            public:
                DomainStringValue Name;
                BoolValue ReturnAll;
                List<DomainStringValue> Alias;
                List<ServerItem> Servers;
            public:
                ARecord(Value& item)
                    : BaseRecord(item)
                    , Name(item["Name"])
                    , ReturnAll(item["ReturnAll"])
                    , Alias(item["Alias"])
                    , Servers(item["Servers"])
                {}
        };
    public:
        RealPortValue Port;
        BoolValue Reverse;
        BoolValue Enabled;
        List<ARecord> A;
    public:
        BindItem(Value& bind)
            : Model(bind)
            , Port(bind["Port"])
            , Reverse(bind["Reverse"])
            , Enabled(bind["Enabled"])
            , A(bind["A"])
        {}
};

static const char* const FuncBindService        = "Bind.Service";
static const char* const FuncBindEnabled        = "Bind.Enabled";
static const char* const FuncBindARecordAdd     = "Bind.ARecord.Add";
static const char* const FuncBindARecordDel     = "Bind.ARecord.Delete";
static const char* const FuncBindARecordSet     = "Bind.ARecord.Set";
static const char* const FuncBindGetAll         = "Bind.Get.All";
static const char* const FuncBindGetService     = "Bind.Get.Service";
static const char* const FuncBindGetARecord     = "Bind.Get.ARecord";
static const char* const FuncBindGetIDByName    = "Bind.Get.IDByName";

#endif // __BIND_H_MODEL__
