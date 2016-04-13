#include <sstream>

#include "route.h"
#include "tools.h"

using namespace std;

namespace Route
{
    typedef RouteItem::GateItem GateItem;

    void MatchGate(RouteItem& route, Control& control)
    {
        while(control.MatchOp("nexthop"))
        {
            GateItem gate(route.Gates.Append());
            while(true)
            {
                if(control.MatchOp("via"))
                {
                    if(control.MatchOp("auto"))
                    {
                        gate.IP = "";
                        gate.Auto = true;
                    } else {
                        String ip;
                        control.MustMatchValue(ip);
                        gate.Auto = false;
                        gate.IP = ip;
                    }
                } else if(control.MatchOp("dev"))
                {
                    String dev;
                    control.MustMatchValue(dev);
                    gate.Dev = dev;
                } else if(control.MatchOp("weight"))
                {
                    int weight;
                    control.MustMatchValue(weight);
                    gate.Weight = weight;
                } else
                    break;
            }
        }
        if(!control.IsEnd())
            control.NotMatch();
    }
};
