#include <iostream>

#include "model/ipvs.h"

#include "control.h"

using namespace std;

namespace HA
{

    void HAGet(Control& control)
    {
        control.MustMatchOp("ha");
        if(control.IsEnd() || control.MatchOp("show"))
        {
            CommandModel cmd;
            cmd.FuncName = FuncHAGet;
            cmd.Password = true;
            Value res;
            Rpc::Call(cmd, res);
            HAItem ha(res);
            cout << "Sendmail: " << (ha.Send ? "on" : "off") << "  Sendto: " << ha.Address << endl;
            cout << "Keepalive: " << ha.Keepalive << "ms" << endl;
            cout << "Warntime: " << ha.Warntime << "ms" << endl;
            cout << "Deadtime: " << ha.Deadtime << "ms" << endl;
            cout << "Initdead: " << ha.Initdead << "ms" << endl;
            cout << "Autoback: " << (ha.Autoback ? "yes" : "no") << endl;
            cout << "Other: ";
            if(ha.Hostname != "")
                cout << ha.Hostname;
            else
                cout << "(no hostname)";
            cout << " " ;
            if(ha.IP != "")
                cout << ha.IP;
            else
                cout << "(no ip)";
            cout << endl;
            cout << "Heartport: " << ha.Port << " ";
            if(ha.Dev != "")
                cout << ha.Dev;
            else
                cout << "(no dev)";
            if(!ha.DevStatus)
                cout << "(invalid)";
            cout << endl;
            cout << "Enabled: " << (ha.Enabled ? "yes" : "no") << endl;
            if(!ha.Ping.IsEmpty())
            {
                cout << "Ping:";
                int count = 0;
                ENUM_LIST(List<DomainStringValue>, ha.Ping, e)
                {
                    cout << "\tGroup" << count++ << ":";
                    ENUM_LIST(DomainStringValue, *e, p)
                    {
                        DomainStringValue ping(*p);
                        cout << " " << ping;
                    }
                    cout << endl;
                }
            }
            if(ha.Sync)
            {
                cout << "Sync ID: " << ha.Sync << endl;
            }
            cout << "SelfStatus: " << (ha.Self ? "Running" : "Backup") << endl;
            cout << "OtherStatus: " << (ha.Other ? "Running" : "Backup") << endl;
        } else
            control.NotMatch();
    }

    DECLARE_COMMAND(HAGet);

    void HAEnabled(Control& control)
    {
        control.MustMatchOp("ha");
        CommandModel cmd;
        cmd.FuncName = FuncHAEnabled;
        cmd.Password = true;
        HAItem ha(cmd.Params);
        if(control.MatchOp("enable"))
            ha.Enabled = true;
        else if(control.MatchOp("disable"))
            ha.Enabled = false;
        else
            control.NotMatch();
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(HAEnabled);

    void HASet(Control& control)
    {
        control.MustMatchOp("ha");
        CommandModel cmd;
        cmd.FuncName = FuncHASet;
        cmd.Password = true;
        HAItem ha(cmd.Params);
        while(true)
        {
            if(control.MatchOp("mail"))
	    {
		if(control.MatchOp("on"))
			ha.Send = true;
		if(control.MatchOp("off"))
			ha.Send = false;
		if(control.MatchOp("to"))
		{
			String mail;
			control.MustMatchValue(mail);
			ha.Address = mail;
		}
	    } else if(control.MatchOp("keepalive"))
            {
                int temp;
                control.MustMatchValue(temp);
                ha.Keepalive = temp;
            } else if(control.MatchOp("deadtime"))
            {
                int temp;
                control.MustMatchValue(temp);
                ha.Deadtime = temp;
            } else if(control.MatchOp("warntime"))
            {
                int temp;
                control.MustMatchValue(temp);
                ha.Warntime = temp;
            } else if(control.MatchOp("initdead"))
            {
                int temp;
                control.MustMatchValue(temp);
                ha.Initdead = temp;
            } else if(control.MatchOp("autoback"))
            {
                if(control.MatchOp("yes"))
                    ha.Autoback = true;
                else if(control.MatchOp("no"))
                    ha.Autoback = false;
                else
                    control.NotMatch();
            } else if(control.MatchOp("port"))
            {
                int temp;
                control.MustMatchValue(temp);
                ha.Port = temp;
            } else if(control.MatchOp("ip"))
            {
                String temp;
                control.MustMatchValue(temp);
                ha.IP = temp;
            } else if(control.MatchOp("hostname"))
            {
                String temp;
                control.MustMatchValue(temp);
                ha.Hostname = temp;
            } else if(control.MatchOp("dev"))
            {
                String temp;
                control.MustMatchValue(temp);
                ha.Dev = temp;
            } else if(control.MatchOp("sync"))
            {
                if(control.MatchOp("off"))
                    ha.Sync = 0;
                else {
                    int id;
                    control.MustMatchValue(id);
                    ha.Sync = id;
                }
            } else if(!control.IsEnd())
                control.NotMatch();
            else
                break;
        }
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(HASet);

    void HAPing(Control& control)
    {
        control.MustMatchOp("ha");
        control.MustMatchOp("ping");
        CommandModel cmd;
        cmd.FuncName = FuncHASet;
        cmd.Password = true;
        HAItem ha(cmd.Params);
        ha.Ping.Clear();
        if(!control.MatchOp("flush"))
        {
            while(true)
            {
                if(control.MatchOp("group"))
                {
                    ha.Ping.Append();
                } else {
                    if(control.IsEnd())
                        break;
                    ASSERT(!ha.Ping.IsEmpty());
                    String temp;
                    control.MustMatchValue(temp);
                    DomainStringValue(ha.Ping.Last().Append()) = temp;
                }
            }
        }
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(HAPing);
};
