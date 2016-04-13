#include <iostream>
#include <sstream>

#include "model/arp.h"

#include "control.h"
#include "tools.h"

using namespace std;

namespace Arp
{
    void MatchArp(Control& control)
    {
        control.MustMatchOp("arp");
    }

    void Show(ArpItem& arp)
    {
        cout << arp.IP << "\t" << arp.MAC << "\t" << arp.Dev << "\t" << arp.Status.Str() << endl;
    }

    void ListDynamic()
    {
        CommandModel cmd;
        cmd.Password = true;
        cmd.FuncName = FuncArpGetDynamic;
        Value result;
        Rpc::Call(cmd, result);
        ENUM_LIST(ArpItem, List<ArpItem>(result), e)
        {
            ArpItem arp(*e);
            Show(arp);
        }
    }

    void ListStatic()
    {
        CommandModel cmd;
        cmd.Password = true;
        cmd.FuncName = FuncArpGetStatic;
        Value result;
        Rpc::Call(cmd, result);
        ENUM_LIST(ArpItem, List<ArpItem>(result), e)
        {
            ArpItem arp(*e);
            Show(arp);
        }
    }

    void List(Control& control)
    {
        MatchArp(control);
        if(control.IsEnd())
        {
            ListDynamic();
            ListStatic();
        } else {
            control.MustMatchOp("list");
            if(control.IsEnd() || control.MatchOp("all"))
            {
                ListDynamic();
                ListStatic();
            } else if(control.MatchOp("dynamic")) {
                ListDynamic();
            } else if(control.MatchOp("static"))
            {
                ListStatic();
            } else
                control.NotMatch();
        }
    }

    DECLARE_COMMAND(List);

    void Set(Control& control)
    {
        MatchArp(control);
        control.MustMatchOp("set");
        CommandModel cmd;
        cmd.Password = true;
        cmd.FuncName = FuncArpSet;
        ArpItem arp(cmd.Params);
        while(true)
        {
            if(control.MatchOp("ip"))
            {
                String temp;
                control.MustMatchValue(temp);
                arp.IP = temp;
            } else if(control.MatchOp("mac"))
            {
                String temp;
                control.MustMatchValue(temp);
                arp.MAC = temp;
            } else if(control.MatchOp("dev"))
            {
                String temp;
                control.MustMatchValue(temp);
                arp.Dev = temp;
            } else
                break;
        }
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(Set);

    void Del(Control& control)
    {
        MatchArp(control);
        control.MustMatchOp("del");
        CommandModel cmd;
        cmd.Password = true;
        cmd.FuncName = FuncArpDel;
        ArpItem arp(cmd.Params);
        while(true)
        {
            if(control.MatchOp("ip"))
            {
                String temp;
                control.MustMatchValue(temp);
                arp.IP = temp;
            } else if(control.MatchOp("dev"))
            {
                String temp;
                control.MustMatchValue(temp);
                arp.Dev = temp;
            } else
                break;
        }
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(Del);

};
