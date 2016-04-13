#ifndef __SMARTROUTE_H__
#define __SMARTROUTE_H__

#include "route.h"

class SmartRouteControl: public RouteControl
{
    private:
        static void AddMangle(int mark);
        static void DelMangle(int mark);
        static String GetISPRulePath(int id);
        void EnsureUniqueISP(int isp, int route);
        void RefreshSmartModule();
        void GenerateMark(SmartRouteItem& route);
    protected:
        virtual void GenerateCMDTarget(OStream& cmd, RouteItem& route);
        virtual void OnRefreshRouteDone();
    public:
        List<SmartRouteItem> Holder;
    public:
        SmartRouteControl();
        void Add(SmartRouteItem& route);
        void Set(SmartRouteItem& route);
        void Del(SmartRouteItem& route);
        void SetDescription(SmartRouteItem& route);
        void SetCheck(SmartRouteItem& route);
        void Get(GetListItem<SmartRouteItem>& get);
        void RefreshDefaultRoute();
        static void RefreshISP();
};

#endif // __SMARTROUTE_H__
