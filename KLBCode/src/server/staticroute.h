#ifndef __STATICROUTE_H__
#define __STATICROUTE_H__

#include "route.h"

class StaticRouteControl: public RouteControl
{
    private:
        static bool IsBaseStaticRoute(StaticRouteItem& route);
        void EnsureRouteID(StaticRouteItem& route);
    protected:
        virtual void GenerateCMDTarget(OStream& cmd, RouteItem& route);
    public:
        List<StaticRouteItem> Holder;
    public:
        StaticRouteControl();
        void Add(StaticRouteItem& route);
        void Set(StaticRouteItem& route);
        void Del(StaticRouteItem& route);
        void SetDescription(StaticRouteItem& route);
        void Get(GetListItem<StaticRouteItem>& get);
        void RefreshDefault();
};


#endif // __STATICROUTE_H__
