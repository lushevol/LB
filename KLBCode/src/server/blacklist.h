#ifndef __BLACKLIST_H__
#define __BLACKLIST_H__

#include "model/blacklist.h"

class BlackListControl
{
    private:
        static void CheckID(BlackListItem& item);
        static void CheckMatchRule(BlackListItem& item);
        static void CopyRule(BlackListItem& target, BlackListItem& recent);
        static void InsertRule(BlackListItem& item);
        static void DeleteRule(BlackListItem& item);
    public:
        List<BlackListItem> Holder;
    public:
        BlackListControl();
        void GetCount(IntValue& result);
        int GetCount();
        void Add(BlackListItem& item);
        void Set(BlackListItem& item);
        void Del(BlackListItem& item);
        void SetDescription(BlackListItem& item);
        void Get(GetListItem<BlackListItem>& get);
        void Flush();
};


#endif // __BLACKLIST_H__
