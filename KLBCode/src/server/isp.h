#ifndef __ISP_H__
#define __ISP_H__

#include "model/isp.h"

class ISPControl
{
    public:
        List<ISPItem> Holder;
    public:
        ISPControl();
        Value& Find(ISPItem& item);
        void GetName(List<ISPItem>& list);
        void GetAll(List<ISPItem>& list);
        void GetName(ISPItem& item);
        void GetAll(ISPItem& item);
        void Set(ISPItem& item);
        void Add(ISPItem& item);
        void Del(ISPItem& item);
};

class ISPRefresher
{
    public:
        typedef void (*Func)();
    private:
        int FPriority;
        Func FRefresh;
    public:
        static void Refresh();
        ISPRefresher(Func refresh, int priority);
        ~ISPRefresher();
};

#endif // __ISP_H__
