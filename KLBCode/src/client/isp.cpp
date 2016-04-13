#include <iostream>

#include "model/isp.h"

#include "control.h"

using namespace std;

namespace ISP
{
    typedef map<String, int> IDMap;

    void GetIDMap(IDMap& result)
    {
        result.clear();
        CommandModel cmd;
        cmd.FuncName = FuncISPGetListName;
        cmd.Password = true;
        Value res;
        Rpc::Call(cmd, res);
        List<ISPItem> list(res);
        ENUM_LIST(ISPItem, list, e)
        {
            result[e->Name] = e->ID;
        }
    }

    void GetIDSet(IntCollection& result)
    {
        result.clear();
        CommandModel cmd;
        cmd.FuncName = FuncISPGetListName;
        cmd.Password = true;
        Value res;
        Rpc::Call(cmd, res);
        List<ISPItem> list(res);
        ENUM_LIST(ISPItem, list, e)
        {
            result.insert(e->ID);
        }
    }

    void Show(Control &control)
    {
        control.MustMatchOp("isp");
        if(!control.IsEnd() && !control.MatchOp("show"))
            control.NotMatch();
        bool all = control.MatchOp("all");
        CommandModel cmd;
        cmd.FuncName = all ? FuncISPGetListAll : FuncISPGetListName;
        cmd.Password = true;
        Value result;
        Rpc::Call(cmd, result);
        List<ISPItem> reslist(result);
        ENUM_LIST(ISPItem, reslist, r)
        {
            cout << "Name:  " << r->Name << endl;
            if(all)
            {
                ENUM_LIST(NetPackStringValue, r->Net, net)
                {
                    cout << "  " << *net << endl;
                }
            }
        }
    }

    DECLARE_COMMAND(Show);

    void ISPSetName(Control &control)
    {
        control.MustMatchOp("isp");
        control.MustMatchOp("set");
        CommandModel cmd;
        cmd.FuncName = FuncISPSet;
        cmd.Password = true;
        ISPItem isp(cmd.Params);
        String name;
        control.MustMatchValue(name);
        control.MustMatchOp("name");
        String newname;
        control.MustMatchValue(newname);
        IDMap idlist;
        GetIDMap(idlist);
        isp.ID = idlist[name];
        isp.Name = newname;
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(ISPSetName);

    void ISPSetNet(Control &control)
    {
        control.MustMatchOp("isp");
        control.MustMatchOp("set");
        CommandModel cmd;
        cmd.FuncName = FuncISPSet;
        cmd.Password = true;
        ISPItem isp(cmd.Params);
        String name;
        control.MustMatchValue(name);
        control.MustMatchOp("net");
        String net;
        while(control.MatchValue(net))
        {
            isp.Net.Append() = net;
        }
        IDMap idlist;
        GetIDMap(idlist);
        isp.ID = idlist[name];
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(ISPSetNet);

    void ISPAdd(Control &control)
    {
        control.MustMatchOp("isp");
        control.MustMatchOp("add");
        CommandModel cmd;
        cmd.FuncName = FuncISPAdd;
        cmd.Password = true;
        ISPItem isp(cmd.Params);
        String name;
        control.MustMatchValue(name);
        isp.Name = name;
        control.MustMatchOp("net");
        String net;
        while(control.MatchValue(net))
        {
            isp.Net.Append() = net;
        }
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(ISPAdd);

    void ISPDel(Control &control)
    {
        control.MustMatchOp("isp");
        control.MustMatchOp("del");
        CommandModel cmd;
        cmd.FuncName = FuncISPDel;
        cmd.Password = true;
        ISPItem isp(cmd.Params);
        String name;
        control.MustMatchValue(name);
        IDMap idlist;
        GetIDMap(idlist);
        isp.ID = idlist[name];
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(ISPDel);
}
