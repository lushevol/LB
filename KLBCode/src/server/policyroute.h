#ifndef __POLICYROUTE_H__
#define __POLICYROUTE_H__

#include "route.h"

class PolicyRouteControl: public RouteControl
{
    private:
        typedef PolicyRouteItem::RuleItem RuleItem;
        static void AddMangle(int id, int mark);
        static void DelMangle(int mark);
        void GenerateMark(PolicyRouteItem& route);
        static void AddRule(int mark, RuleItem& rule);
        static void DelRule(int mark, int id);
    protected:
        virtual void GenerateCMDTarget(OStream& cmd, RouteItem& route);
    public:
        List<PolicyRouteItem> Holder;
    public:
        PolicyRouteControl();
        void Add(PolicyRouteItem& route);
        void Insert(PolicyRouteItem& route);
        void Set(PolicyRouteItem& route);
        void Del(PolicyRouteItem& route);
        void AddRule(PolicyRouteItem& route);
        void DelRule(PolicyRouteItem& route);
        void SetDescription(PolicyRouteItem& route);
        void Get(GetListItem<PolicyRouteItem>& get);
};

#endif // __POLICYROUTE_H__
