#include <iostream>

#include "model/adsl.h"

#include "control.h"
#include "tools.h"
#include "reader.h"

using namespace std;

namespace Adsl
{
    typedef AdslItem::DialState DialState;

    void Show(AdslItem& adsl)
    {
        cout << "Adsl: " << adsl.Dev << " in " << adsl.Ethernet << " ";
        switch(adsl.Status)
        {
            case DialState::Stop:
                cout << " stopped";
                break;
            case DialState::Connected:
                cout << " conntected";
                break;
            case DialState::Dial:
                cout << " dialing...";
                break;
            default:
                cout << "(invalid)";
        }
        if(adsl.Description != "")
            cout << endl << adsl.Description;
        cout << endl;
        cout << " User:    " << adsl.User << endl;
        cout << " MTU:     " << adsl.MTU << endl;
        cout << " Timeout: " << adsl.Timeout << endl;
        if(adsl.Status == DialState::Connected)
        {
            cout << " IP:      " << adsl.IP << endl;
            cout << " Dns:     " << adsl.Dns << endl;
            cout << " Gate:    " << adsl.Gate << endl;
            int day = adsl.Time / 3600 / 24;
            int hour = (adsl.Time % (3600 * 24)) / 3600;
            int min = (adsl.Time % 3600) / 60;
            int second = (adsl.Time % 60);
            cout << " Time:    " << day << " day " << hour << " hour " << min << " min " << second << " second" << endl;
            cout << " RX:      " << adsl.RX << endl;
            cout << " TX:      " << adsl.TX << endl;
        }
    }

    void MatchAdsl(Control& control)
    {
        control.MustMatchOp("adsl");
    }

    void ListAll(Control& control)
    {
        MatchAdsl(control);
        if(control.IsEnd() || control.MatchOp("list"))
        {
            CommandModel cmd;
            cmd.FuncName = FuncAdslGetAll;
            cmd.Password = true;
            Value result;
            Rpc::Call(cmd, result);
            ENUM_LIST(AdslItem,List<AdslItem>(result),e)
            {
                AdslItem adsl(*e);
                Show(adsl);
            }
        } else
            control.NotMatch();
    }

    DECLARE_COMMAND(ListAll);

    void ListDev(Control& control)
    {
        MatchAdsl(control);
        String dev;
        control.MustMatchValue(dev);
        if(control.IsEnd() || control.MatchOp("list"))
        {
            CommandModel cmd;
            cmd.FuncName = FuncAdslGet;
            cmd.Password = true;
            AdslItem request(cmd.Params);
            request.Dev = dev;
            Value res;
            Rpc::Call(cmd, res);
            AdslItem result(res);
            Show(result);
        } else
            control.NotMatch();
    }

    DECLARE_COMMAND(ListDev);

    void Add(Control& control)
    {
        MatchAdsl(control);
        control.MustMatchOp("add");
        String ppp;
        control.MustMatchValue(ppp);
        control.MustMatchOp("ethernet");
        String eth;
        control.MustMatchValue(eth);
        CommandModel cmd;
        cmd.FuncName = FuncAdslAdd;
        cmd.Password = true;
        AdslItem adsl(cmd.Params);
        adsl.Dev = ppp;
        adsl.Ethernet = eth;
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(Add);

    void Del(Control& control)
    {
        MatchAdsl(control);
        control.MustMatchOp("del");
        String ppp;
        control.MustMatchValue(ppp);
        CommandModel cmd;
        cmd.FuncName = FuncAdslDel;
        cmd.Password = true;
        AdslItem adsl(cmd.Params);
        adsl.Dev = ppp;
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(Del);

    void Dial(Control& control)
    {
        MatchAdsl(control);
        String ppp;
        control.MustMatchValue(ppp);
        control.MustMatchOp("dial");
        CommandModel cmd;
        cmd.FuncName = FuncAdslDial;
        cmd.Password = true;
        AdslItem adsl(cmd.Params);
        adsl.Dev = ppp;
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(Dial);

    void Stop(Control& control)
    {
        MatchAdsl(control);
        String ppp;
        control.MustMatchValue(ppp);
        control.MustMatchOp("hangup");
        CommandModel cmd;
        cmd.FuncName = FuncAdslStop;
        cmd.Password = true;
        AdslItem adsl(cmd.Params);
        adsl.Dev = ppp;
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(Stop);

    void Description(Control& control)
    {
        MatchAdsl(control);
        String ppp;
        control.MustMatchValue(ppp);
        control.MustMatchOp("description");
        String description;
        control.MustMatchValue(description);
        CommandModel cmd;
        cmd.FuncName = FuncAdslDescription;
        cmd.Password = true;
        AdslItem adsl(cmd.Params);
        adsl.Dev = ppp;
        adsl.Description = description;
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(Description);

    void Set(Control& control)
    {
        MatchAdsl(control);
        String ppp;
        control.MustMatchValue(ppp);
        CommandModel cmd;
        cmd.FuncName = FuncAdslSet;
        cmd.Password = true;
        AdslItem adsl(cmd.Params);
        while(true)
        {
            if(control.MatchOp("mtu"))
            {
                int mtu;
                control.MustMatchValue(mtu);
                adsl.MTU = mtu;
            } else if(control.MatchOp("timeout"))
            {
                int timeout;
                control.MustMatchValue(timeout);
                adsl.Timeout = timeout;
            } else if(control.MatchOp("user"))
            {
                String temp;
                control.MustMatchValue(temp);
                adsl.User = temp;
            } else
                break;
        }
        if(!adsl.MTU.Valid() && !adsl.Timeout.Valid() && !adsl.User.Valid())
            control.NotMatch();
        adsl.Dev = ppp;
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(Set);

    void Password(Control& control)
    {
        MatchAdsl(control);
        String ppp;
        control.MustMatchValue(ppp);
        control.MustMatchOp("password");
        String password;
        cout << "new password:";
        Reader::ReadPassword(password);
        String again;
        cout << endl << "again:";
        Reader::ReadPassword(again);
        cout << endl;
        if(again != password)
            ERROR(Exception::Command::PasswordAgain, "");
        CommandModel cmd;
        cmd.FuncName = FuncAdslSet;
        cmd.Password = true;
        AdslItem adsl(cmd.Params);
        adsl.Dev = ppp;
        adsl.Password = password;
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(Password);
};
