#ifndef __NAT_H_MODEL__
#define __NAT_H_MODEL__

#include "model.h"
#include "types.h"

class NatItem: public Model
{
    public:
        class MatchItem: public Model
        {
            public:
                NetPackStringValue SrcNet;
                NetPackStringValue DestNet;
                ProtocolValue Protocol;
                NormalStringValue ProtocolStr;
                PortValue SrcPort;
                NormalStringValue SrcPortStr;
                PortValue DestPort;
                NormalStringValue DestPortStr;
                NormalStringValue Dev;
            public:
                MatchItem(Value& value)
                    : Model(value)
                    , SrcNet(value["SrcNet"])
                    , DestNet(value["DestNet"])
                    , Protocol(value["Protocol"])
                    , ProtocolStr(value["ProtocolStr"])
                    , SrcPort(value["SrcPort"])
                    , SrcPortStr(value["SrcPortStr"])
                    , DestPort(value["DestPort"])
                    , DestPortStr(value["DestPortStr"])
                    , Dev(value["Dev"])
                {}
        };
        class ActionItem: public Model
        {
            public:
                BoolValue Except;
                BoolValue Masquerade;
                HostStringValue StartIP;
                HostStringValue EndIP;
                PortValue StartPort;
                PortValue EndPort;
            public:
                ActionItem(Value& value)
                    : Model(value)
                    , Except(value["Except"])
                    , Masquerade(value["Masquerade"])
                    , StartIP(value["StartIP"])
                    , EndIP(value["EndIP"])
                    , StartPort(value["StartPort"])
                    , EndPort(value["EndPort"])
                {}
        };
    public:
        UnsignedValue ID;
        UnsignedValue RuleID;
        BoolValue Enabled;
        BoolValue Status;
        CommonStringValue Description;
        MatchItem Match;
        ActionItem Action;
    public:
        NatItem(Value& value)
            : Model(value)
            , ID(value["ID"])
            , RuleID(value["RuleID"])
            , Enabled(value["Enabled"])
            , Status(value["Status"])
            , Description(value["Description"])
            , Match(value["Match"])
            , Action(value["Action"])
        {}
};

static const char* const FuncNatDestAdd         = "Nat.Dest.Add";
static const char* const FuncNatDestInsert      = "Nat.Dest.Insert";
static const char* const FuncNatDestDel         = "Nat.Dest.Delete";
static const char* const FuncNatDestReplace     = "Nat.Dest.Replace";
static const char* const FuncNatDestGet         = "Nat.Dest.Get";
static const char* const FuncNatDestGetCount    = "Nat.Dest.GetCount";
static const char* const FuncNatDestEnabled     = "Nat.Dest.Enabled";
static const char* const FuncNatDestDescription = "Nat.Dest.Description";

static const char* const FuncNatSrcAdd          = "Nat.Src.Add";
static const char* const FuncNatSrcInsert       = "Nat.Src.Insert";
static const char* const FuncNatSrcDel          = "Nat.Src.Delete";
static const char* const FuncNatSrcReplace      = "Nat.Src.Replace";
static const char* const FuncNatSrcGet          = "Nat.Src.Get";
static const char* const FuncNatSrcGetCount     = "Nat.Src.GetCount";
static const char* const FuncNatSrcEnabled      = "Nat.Src.Enabled";
static const char* const FuncNatSrcDescription  = "Nat.Src.Description";

#endif // __NAT_H_MODEL__
