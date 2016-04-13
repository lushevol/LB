#ifndef __HA_H__
#define __HA_H__

#include "model/ipvs.h"

class HAControl
{
    private:
        void GenerateConf();
        void StartHA();
        void StopHA();
        void RefreshDevStatus();
    public:
        HAItem HA;
    public:
        HAControl();
        void Get(HAItem& item);
        void Set(HAItem& item);
        void SetEnabled(HAItem& item);
        void Refresh();
        static void StatusChanged(const String& target, bool start);
        static void Refresh(const StringCollection& dev);
};

#endif // __HA_H__
