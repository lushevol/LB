#ifndef __BLACKLIST_MODEL__
#define __BLACKLIST_MODEL__

#include "model.h"
#include "types.h"

class BlackListItem: public Model
{
    public:
        UnsignedValue ID;
        CommonStringValue Description;
        NetPackStringValue SrcNet;
        NetPackStringValue DestNet;
        ProtocolValue Protocol;
        NormalStringValue ProtocolStr;
        PortValue SrcPort;
        PortValue DestPort;
    public:
        BlackListItem(Value& item)
            : Model(item)
            , ID(item["ID"])
            , Description(item["Description"])
            , SrcNet(item["SrcNet"])
            , DestNet(item["DestNet"])
            , Protocol(item["Protocol"])
            , ProtocolStr(item["ProtocolStr"])
            , SrcPort(item["SrcPort"])
            , DestPort(item["DestPort"])
        {}
};

static const char* const FuncBlackListGet           = "BlackList.Get";
static const char* const FuncBlackListGetCount      = "BlackList.GetCount";
static const char* const FuncBlackListDel           = "BlackList.Delete";
static const char* const FuncBlackListAdd           = "BlackList.Add";
static const char* const FuncBlackListSet           = "BlackList.Set";
static const char* const FuncBlackListDescription   = "BlackList.Description";

#endif // __BLACKLIST_MODEL__
