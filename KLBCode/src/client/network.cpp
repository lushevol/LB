#include <iostream>

#include "model/network.h"
#include "model/types.h"

#include "control.h"

using namespace std;

namespace Network
{
    void AntiDos(Control& control)
    {
        control.MustMatchOp("network");
        control.MustMatchOp("antidos");
        if(control.IsEnd() || control.MatchOp("status"))
        {
            CommandModel cmd;
            cmd.Password = true;
            cmd.FuncName = FuncAntiDosGet;
            Value result;
            Rpc::Call(cmd, result);
            BoolValue res(result);
            cout << "the antidos status is " << (res ? "running" : "stop") << endl;
        } else {
            bool enabled;
            if(control.MatchOp("enable"))
                enabled = true;
            else if(control.MatchOp("disable"))
                enabled = false;
            else
                control.NotMatch();
            CommandModel cmd;
            cmd.Password = true;
            cmd.FuncName = FuncAntiDosSet;
            BoolValue(cmd.Params) = enabled;
            Rpc::CallNoResult(cmd);
        }
    }

    DECLARE_COMMAND(AntiDos);

    void Hostname(Control& control)
    {
        control.MustMatchOp("network");
        control.MustMatchOp("hostname");
        if(control.IsEnd() || control.MatchOp("get"))
        {
            CommandModel cmd;
            cmd.Password = true;
            cmd.FuncName = FuncHostnameGet;
            Value result;
            Rpc::Call(cmd, result);
            HostnameStringValue res(result);
            cout << "hostname: " << res << endl;
        } else {
            control.MustMatchOp("set");
            String temp;
            control.MustMatchValue(temp);
            CommandModel cmd;
            cmd.Password = true;
            cmd.FuncName = FuncHostnameSet;
            HostnameStringValue(cmd.Params) = temp;
            Rpc::CallNoResult(cmd);
        }
    }

    DECLARE_COMMAND(Hostname);

    void DnsServer(Control& control)
    {
        control.MustMatchOp("network");
        control.MustMatchOp("dns");
        if(control.IsEnd() || control.MatchOp("list"))
        {
            CommandModel cmd;
            cmd.Password = true;
            cmd.FuncName = FuncDnsServerGet;
            Value result;
            Rpc::Call(cmd, result);
            ENUM_LIST(HostStringValue, List<HostStringValue>(result), e)
            {
                HostStringValue ip(*e);
                cout << "nameserver " << ip << endl;
            }
        } else {
            control.MustMatchOp("set");
            control.MatchOp("flush");
            CommandModel cmd;
            cmd.Password = true;
            cmd.FuncName = FuncDnsServerSet;
            List<HostStringValue> list(cmd.Params);
            while(true)
            {
                String temp;
                if(control.MatchValue(temp))
                {
                    HostStringValue ip(list.Append());
                    ip = temp;
                } else
                    break;
            }
            Rpc::CallNoResult(cmd);
        }
    }

    DECLARE_COMMAND(DnsServer);
};
