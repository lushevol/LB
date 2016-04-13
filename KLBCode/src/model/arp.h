#ifndef __ARP_H_MODEL__
#define __ARP_H_MODEL__

#include "model.h"
#include "types.h"

class ArpItem: public Model
{
    public:
        class ArpState: public State
        {
            public:
                static const int Dynamic = 1;
                static const int Static = 2;
                static const int Invalid = 0;
                virtual const char* Get(int state) const
                {
                    switch(state)
                    {
                        case Dynamic:
                            return "Dynamic";
                        case Static:
                            return "Static";
                        case Invalid:
                            return "Invalid";
                        default:
                            return NULL;
                    };
                }
                virtual int Default() const
                {
                    return Invalid;
                }
        };
        typedef StateValue<ArpState> ArpStateValue;
    public:
        HostStringValue IP;
        PhysicalAddressStringValue MAC;
        NormalStringValue Dev;
        ArpStateValue Status;
    public:
        ArpItem(Value& item)
            : Model(item)
            , IP(item["IP"])
            , MAC(item["MAC"])
            , Dev(item["Dev"])
            , Status(item["Status"])
        {}
};

static const char* const FuncArpSet         = "Arp.Set";
static const char* const FuncArpDel         = "Arp.Delete";
static const char* const FuncArpGetDynamic  = "Arp.Get.Dynamic";
static const char* const FuncArpGetStatic   = "Arp.Get.Static";


#endif // __ARP_H_MODEL__
