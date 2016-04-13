#include <iostream>
#include <sstream>

#include "model/nat.h"

#include "control.h"
#include "tools.h"

using namespace std;

namespace Nat
{
    typedef NatItem::MatchItem MatchItem;
    typedef NatItem::ActionItem ActionItem;

    enum NatType
    {
        Src,
        Dest,
    };

    void MatchType(NatType& type, Control& control)
    {
        control.MustMatchOp("nat");
        if(control.MatchOp("src"))
            type = Src;
        else if(control.MatchOp("dest"))
            type = Dest;
        else
            control.NotMatch();
    }

    void Show(NatItem& item, NatType type)
    {
        cout << item.ID << ": " << (item.Enabled ? "Enabled" : "Disabled");
        if(!item.Status)
            cout << " (invalid)";
        cout << endl;
        if(item.Description != "")
            cout << item.Description << endl;
        cout << "prot: ";
        ParseTools::ShowAny(item.Match.ProtocolStr, item.Match.Protocol);
        cout << "  ";
        if(item.Match.SrcNet != "")
            cout << item.Match.SrcNet;
        else
            cout << "any";
        if(item.Match.Protocol && item.Match.SrcPort != 0)
        {
            cout << ":";
            ParseTools::ShowAny(item.Match.SrcPortStr, item.Match.SrcPort);
        }
        cout << " --> ";
        if(item.Match.DestNet != "")
            cout << item.Match.DestNet;
        else
            cout << "any";
        if(item.Match.Protocol && item.Match.DestPort != 0)
        {
            cout << ":";
            ParseTools::ShowAny(item.Match.DestPortStr, item.Match.DestPort);
        }
        if(item.Match.Dev != "")
            cout << ' ' << ((type == Src) ? "out: " : "in: ") << item.Match.Dev;
        cout << "  ";
        if(!item.Action.Except)
        {
            if(type == Src)
            {
                if(item.Action.Masquerade)
                    cout << "masquerade ";
                else {
                    cout << "to-source: " << item.Action.StartIP;
                    if(item.Action.EndIP != "")
                        cout << "-" << item.Action.EndIP;
                    cout << " ";
                }
            } else {
                cout << "to-destnation: " << item.Action.StartIP;
                if(item.Action.EndIP != "")
                    cout << "-" << item.Action.EndIP;
                cout << " ";
            }
            if(item.Action.StartPort != 0)
            {
                cout << "port: " << item.Action.StartPort;
                if(item.Action.EndPort != 0)
                    cout << "-" << item.Action.EndPort;
            }
        } else {
            cout << "exclude";
        }
        cout << endl;
    }

    void ListAll(Control& control)
    {
        NatType type = Src;
        MatchType(type, control);
        if(!control.IsEnd() && !control.MatchOp("list"))
            control.NotMatch();
        CommandModel cmd;
        switch(type)
        {
            case Src:
                cmd.FuncName = FuncNatSrcGet;
                break;
            case Dest:
                cmd.FuncName = FuncNatDestGet;
                break;
        };
        cmd.Password = true;
        GetListItem<NatItem> request(cmd.Params);
        request.All = true;
        Value res;
        Rpc::Call(cmd, res);
        ENUM_LIST(NatItem, List<NatItem>(res), e)
        {
            NatItem item(*e);
            Show(item, type);
        }
    }

    DECLARE_COMMAND(ListAll);

    void MatchRule(MatchItem& match, NatType type, Control& control)
    {
        String dev((type == Src) ? "out" : "in");
        control.MustMatchOp("match");
        if(!control.MatchOp("all"))
        {
            while(true)
            {
                if(control.MatchOp("from"))
                {
                    bool port = control.MatchOp("port");
                    if(!port)
                    {
                        String net;
                        control.MustMatchValue(net);
                        match.SrcNet = net;
                        port = control.MatchOp("port");
                    }
                    if(port)
                    {
                        String port;
                        control.MustMatchValue(port);
                        match.SrcPortStr = port;
                    }
                } else if(control.MatchOp("to"))
                {
                    bool port = control.MatchOp("port");
                    if(!port)
                    {
                        String net;
                        control.MustMatchValue(net);
                        match.DestNet = net;
                        port = control.MatchOp("port");
                    }
                    if(port)
                    {
                        String port;
                        control.MustMatchValue(port);
                        match.DestPortStr = port;
                    }
                } else if(control.MatchOp("protocol"))
                {
                    String temp;
                    control.MustMatchOpValue(temp);
                    match.ProtocolStr = temp;
                } else if(control.MatchOp(dev.c_str()))
                {
                    String temp;
                    control.MustMatchValue(temp);
                    match.Dev = temp;
                } else
                    break;
            }
        }
    }

    void MatchAction(NatItem& item, NatType type, Control& control)
    {
        if(control.MatchOp("exclude"))
        {
            item.Action.Except = true;
            return;
        }
        item.Action.Except = false;
        control.MustMatchOp("translate");
        bool masq = false;
        if(type == Src)
        {
            masq = control.MatchOp("masquerade");
            item.Action.Masquerade = masq;
        }
        if(!masq)
        {
            control.MustMatchOp("ip");
            String temp;
            control.MustMatchValue(temp);
            String start, end;
            ParseTools::SplitAddressRange(temp, start, end);
            item.Action.StartIP = start;
            item.Action.EndIP = end;
        }
        if(control.MatchOp("port"))
        {
            String temp;
            control.MustMatchValue(temp);
            int start, end;
            ParseTools::SplitPortRange(temp, start, end);
            item.Action.StartPort = start;
            item.Action.EndPort = end;
        }
    }

    void Add(Control& control)
    {
        NatType type = Src;
        MatchType(type, control);
        control.MustMatchOp("add");
        CommandModel cmd;
        cmd.Password = true;
        NatItem nat(cmd.Params);
        nat.Enabled = true;
        MatchRule(nat.Match, type, control);
        if(type == Src)
            cmd.FuncName = FuncNatSrcAdd;
        else
            cmd.FuncName = FuncNatDestAdd;
        MatchAction(nat, type, control);
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(Add);

    void Insert(Control& control)
    {
        NatType type = Src;
        MatchType(type, control);
        control.MustMatchOp("insert");
        int id;
        control.MustMatchValue(id);
        CommandModel cmd;
        cmd.Password = true;
        NatItem nat(cmd.Params);
        nat.ID = id;
        nat.Enabled = true;
        MatchRule(nat.Match, type, control);
        if(type == Src)
            cmd.FuncName = FuncNatSrcInsert;
        else
            cmd.FuncName = FuncNatDestInsert;
        MatchAction(nat, type, control);
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(Insert);

    void Replace(Control& control)
    {
        NatType type = Src;
        MatchType(type, control);
        control.MustMatchOp("replace");
        int id;
        control.MustMatchValue(id);
        CommandModel cmd;
        cmd.Password = true;
        NatItem nat(cmd.Params);
        nat.ID = id;
        nat.Enabled = true;
        MatchRule(nat.Match, type, control);
        if(type == Src)
            cmd.FuncName = FuncNatSrcReplace;
        else
            cmd.FuncName = FuncNatDestReplace;
        MatchAction(nat, type, control);
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(Replace);

    void Del(Control& control)
    {
        NatType type = Src;
        MatchType(type, control);
        control.MustMatchOp("del");
        int id;
        control.MustMatchValue(id);
        CommandModel cmd;
        cmd.Password = true;
        NatItem nat(cmd.Params);
        nat.ID = id;
        switch(type)
        {
            case Src:
                cmd.FuncName = FuncNatSrcDel;
                break;
            case Dest:
                cmd.FuncName = FuncNatDestDel;
                break;
        };
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(Del);

    void Enabled(Control& control)
    {
        NatType type = Src;
        MatchType(type, control);
        int id;
        control.MustMatchValue(id);
        bool enabled;
        if(control.MatchOp("enable"))
            enabled = true;
        else if(control.MatchOp("disable"))
            enabled = false;
        else
            control.NotMatch();
        CommandModel cmd;
        cmd.Password = true;
        NatItem nat(cmd.Params);
        nat.ID = id;
        nat.Enabled = enabled;
        switch(type)
        {
            case Src:
                cmd.FuncName = FuncNatSrcEnabled;
                break;
            case Dest:
                cmd.FuncName = FuncNatDestEnabled;
                break;
        };
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(Enabled);

    void Description(Control& control)
    {
        NatType type = Src;
        MatchType(type, control);
        int id;
        control.MustMatchValue(id);
        control.MustMatchOp("description");
        String temp;
        control.MustMatchValue(temp);
        CommandModel cmd;
        cmd.Password = true;
        NatItem nat(cmd.Params);
        nat.ID = id;
        nat.Description = temp;
        switch(type)
        {
            case Src:
                cmd.FuncName = FuncNatSrcDescription;
                break;
            case Dest:
                cmd.FuncName = FuncNatDestDescription;
                break;
        };
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(Description);
};
