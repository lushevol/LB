#ifndef __NAT_H__
#define __NAT_H__

#include "model/nat.h"

class NatControl
{
    private:
        typedef NatItem::MatchItem MatchItem;
        typedef NatItem::ActionItem ActionItem;
        bool FSrc;
    private:
        static void CheckDev(const String& dev);
        static int GetRuleID(NatItem& nat);
        void CheckItemValid(NatItem& nat);
        static void GenerateActionIP(OStream& stream, ActionItem& action);
        static void GenerateActionPort(OStream& stream, ActionItem& action, const char* prefix);
        void GenerateChain(OStream& stream);
        void GenerateActionCmd(OStream& stream, ActionItem& action);
        void GenerateMatchCmd(OStream& stream, MatchItem& match);
        void RefreshID();
        void DoDelNat(NatItem& nat);
        void Refresh(NatItem& nat);
        void Refresh(const StringCollection& dev);
        void DoAddNat(NatItem& nat);
        void Copy(NatItem& target,NatItem& recent);
    public:
        List<NatItem> Holder;
    public:
        NatControl(Value& value, bool src);
        static void RefreshAll(const StringCollection& dev);
        static void RefreshAll(const String& dev);
        void GetCount(IntValue& result);
        int GetCount();
        void Add(NatItem& nat);
        void Insert(NatItem& nat);
        void Replace(NatItem& nat);
        void Get(GetListItem<NatItem>& get);
        void Del(NatItem& nat);
        void SetEnabled(NatItem& nat);
        void SetDescription(NatItem& nat);
};

class NatSrcControl: public NatControl
{
    public:
        NatSrcControl();
};

class NatDestControl: public NatControl
{
    public:
        NatDestControl();
};

#endif // __NAT_H__
