#ifndef __ROUTE_H__
#define __ROUTE_H__

#include "model/route.h"

#include "control.h"

namespace Route
{
    void MatchGate(RouteItem& route, Control& control);
};

#endif // __ROUTE_H__
