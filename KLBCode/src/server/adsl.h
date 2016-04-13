#ifndef __ADSL_H__
#define __ADSL_H__

#include "model/adsl.h"

class AdslControl
{
    private:
        typedef AdslItem::DialState DialState;
        static void Copy(AdslItem& target, AdslItem& source);
        static void CheckDevValid(AdslItem& adsl);
        static void SetAdsl(AdslItem& adsl);
        static void DelAdsl(AdslItem& adsl);
        static void StartDial(AdslItem& adsl);
        static void StopDial(AdslItem& adsl);
        void RefreshID();
    public:
        List<AdslItem> Holder;
    public:
        AdslControl();
        Value& Find(const String& dev);
        void GetAll(List<AdslItem>& list);
        void Get(AdslItem& adsl);
        void Add(AdslItem& adsl);
        void Set(AdslItem& adsl);
        void Del(AdslItem& adsl);
        void Dial(AdslItem& adsl);
        void Stop(AdslItem& adsl);
        static void StatusChanged(const String& dev, const String& ip, const String& gate, const String& dns, bool ok);
        void SetDescription(AdslItem& adsl);
};

#endif // __ADSL_H__
