#ifndef __ROUTE_H__
#define __ROUTE_H__

#include "model/route.h"

class RouteControl
{
    protected:
        typedef RouteItem::GateItem GateItem;
    private:
        void TryAddRotue(RouteItem& route);
        void GenerateCMD(OStream& cmd, RouteItem& route, bool isadd);
        void GenerateCMDGate(OStream& cmd, GateItem& gate);
        void GenerateCmdPath(OStream& cmd, RouteItem& route);
        static void InitGate(RouteItem& target);
        void DoDelRoute(RouteItem& recent);
        void RefreshID();
        void RefreshGate(const StringCollection& net);
        void RefreshDev(const StringCollection& dev);
        static String GateToDev(const String& gate);
    protected:
        void RefreshRoute(RouteItem& recent);
        virtual void OnRefreshRoute(RouteItem& recent) {}
        virtual void OnRefreshRouteDone() {}
        virtual void GenerateCMDTarget(OStream& cmd, RouteItem& route) = 0;
        void AddRoute(RouteItem& target);
        void SetRoute(RouteItem& target, RouteItem& recent);
        void DelRoute(RouteItem& recent);
        void Get(RouteItem& route, RouteItem& recent);
    public:
        List<RouteItem> Holder;
    public:
        RouteControl(Value& holder);
        virtual ~RouteControl() {}
        Value& Find(int index);
        void GetCount(IntValue& result);
        int GetCount();
        static void RefreshAll(const String& dev);
        static void RefreshAll(const StringCollection& dev);
        static void RefreshRecursion(const String& net);
        static void RefreshRecursion(const StringCollection& net);
};

#endif // __ROUTE_H__
