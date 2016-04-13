#include <iostream>

#include "model/blacklist.h"

#include "tools.h"
#include "control.h"

using namespace std;

namespace BlackList
{
    void MatchBlackList(Control& control)
    {
        control.MustMatchOp("blacklist");
    }

    void ShowBlackList(Control& control)
    {
        MatchBlackList(control);
        if(!control.IsEnd() && !control.MatchOp("list"))
            control.NotMatch();
        CommandModel cmd;
        cmd.FuncName = FuncBlackListGet;
        cmd.Password = true;
        GetListItem<BlackListItem>(cmd.Params).All = true;
        Value result;
        Rpc::Call(cmd, result);
        List<BlackListItem> list(result);
        ENUM_LIST(BlackListItem, list, rule)
        {
            cout << "  " << rule->ID << ": ";
            ParseTools::ShowAny(rule->ProtocolStr, rule->Protocol);
            cout << " ";
            if(rule->SrcNet != "")
                cout << rule->SrcNet;
            else
                cout << "any";
            if(rule->Protocol && rule->SrcPort)
                cout << ":" << rule->SrcPort;
            cout << " --> ";
            if(rule->DestNet != "")
                cout << rule->DestNet;
            else
                cout << "any";
            if(rule->Protocol && rule->DestPort)
                cout << ":" << rule->DestPort;
            cout << "\t" << rule->Description;
            cout << endl;
        }
    }

    DECLARE_COMMAND(ShowBlackList);

    void Description(Control& control)
    {
        MatchBlackList(control);
        int id;
        control.MustMatchValue(id);
        control.MustMatchOp("description");
        String description;
        control.MustMatchValue(description);
        CommandModel cmd;
        cmd.FuncName = FuncBlackListDescription;
        cmd.Password = true;
        BlackListItem item(cmd.Params);
        item.ID = id;
        item.Description = description;
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(Description);

    void MatchRule(Control& control, BlackListItem& item)
    {
        control.MustMatchOp("match");
        if(control.MatchOp("any"))
        {
            item.SrcNet = "";
            item.DestNet = "";
            item.Protocol = 0;
            item.ProtocolStr = "";
            item.SrcPort = 0;
            item.DestPort = 0;
        } else {
            while(true)
            {
                if(control.MatchOp("from"))
                {
                    bool port = control.MatchOp("port");
                    if(!port)
                    {
                        String net;
                        control.MustMatchValue(net);
                        item.SrcNet = net;
                        port = control.MatchOp("port");
                    }
                    if(port)
                    {
                        int temp;
                        control.MustMatchValue(temp);
                        item.SrcPort = temp;
                    }
                } else if(control.MatchOp("to"))
                {
                    bool port = control.MatchOp("port");
                    if(!port)
                    {
                        String net;
                        control.MustMatchValue(net);
                        item.DestNet = net;
                        port = control.MatchOp("port");
                    }
                    if(port)
                    {
                        int temp;
                        control.MustMatchValue(temp);
                        item.DestPort = temp;
                    }
                } else if(control.MatchOp("protocol"))
                {
                    String temp;
                    control.MustMatchOpValue(temp);
                    item.ProtocolStr = temp;
                } else if(!control.IsEnd())
                    control.NotMatch();
                else
                    break;
            }
        }
    }

    void AddBlackList(Control& control)
    {
        MatchBlackList(control);
        control.MustMatchOp("add");
        CommandModel cmd;
        cmd.FuncName = FuncBlackListAdd;
        cmd.Password = true;
        BlackListItem item(cmd.Params);
        MatchRule(control, item);
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(AddBlackList);

    void SetBlackList(Control& control)
    {
        MatchBlackList(control);
        int id;
        control.MustMatchValue(id);
        CommandModel cmd;
        cmd.FuncName = FuncBlackListSet;
        cmd.Password = true;
        BlackListItem item(cmd.Params);
        MatchRule(control, item);
        item.ID = id;
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(SetBlackList);

    void DelBlackList(Control& control)
    {
        MatchBlackList(control);
        control.MustMatchOp("del");
        int id;
        control.MustMatchValue(id);
        CommandModel cmd;
        cmd.FuncName = FuncBlackListDel;
        cmd.Password = true;
        BlackListItem(cmd.Params).ID = id;
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(DelBlackList);
};
