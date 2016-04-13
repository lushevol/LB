#include <iostream>
#include <map>
#include <deque>

#include "model/bind.h"

#include "control.h"

using namespace std;

namespace Bind
{

    typedef BindItem::ARecord ARecord;
    typedef ARecord::ServerItem ServerItem;

    void MatchBind(Control& control)
    {
        control.MustMatchOp("dns");
    }

    void BindEnabled(Control& control)
    {
        MatchBind(control);
        CommandModel cmd;
        cmd.FuncName = FuncBindEnabled;
        cmd.Password = true;
        BindItem bind(cmd.Params);
        if(control.MatchOp("enable"))
            bind.Enabled = true;
        else if(control.MatchOp("disable"))
            bind.Enabled = false;
        else
            control.NotMatch();
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(BindEnabled);

    void BindSetPort(Control& control)
    {
        MatchBind(control);
        CommandModel cmd;
        cmd.FuncName = FuncBindService;
        cmd.Password = true;
        BindItem bind(cmd.Params);
        control.MustMatchOp("port");
        int temp;
        control.MustMatchValue(temp);
        bind.Port = temp;
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(BindSetPort);

    void BindReverseEnabled(Control& control)
    {
        MatchBind(control);
        control.MustMatchOp("reverse");
        CommandModel cmd;
        cmd.FuncName = FuncBindService;
        cmd.Password = true;
        BindItem bind(cmd.Params);
        if(control.MatchOp("enable"))
            bind.Reverse = true;
        else if(control.MatchOp("disable"))
            bind.Reverse = false;
        else
            control.NotMatch();
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(BindReverseEnabled);

    void ARecordAdd(Control& control)
    {
        MatchBind(control);
        control.MustMatchOp("domain");
        control.MustMatchOp("add");
        CommandModel cmd;
        cmd.FuncName = FuncBindARecordAdd;
        cmd.Password = true;
        ARecord record(cmd.Params);
        String temp;
        control.MustMatchValue(temp);
        record.Name = temp;
        record.Enabled = true;
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(ARecordAdd);

    int GetDomainID(const String& domain)
    {
        CommandModel cmd;
        cmd.FuncName = FuncBindGetIDByName;
        cmd.Password = true;
        List<ARecord> list(cmd.Params);
        list.Append().Name = domain;
        Value result;
        Rpc::Call(cmd, result);
        return List<ARecord>(result).Head().ID;
    }

    void MatchSetARecord(Control& control, String& domain)
    {
        MatchBind(control);
        control.MustMatchOp("domain");
        control.MustMatchOp("set");
        control.MustMatchValue(domain);
    }

    void ARecordSetNormal(Control& control)
    {
        String domain;
        MatchSetARecord(control, domain);
        CommandModel cmd;
        cmd.FuncName = FuncBindARecordSet;
        cmd.Password = true;
        ARecord record(cmd.Params);
        while(true)
        {
            if(control.MatchOp("all"))
            {
                record.ReturnAll = true;
            } else if(control.MatchOp("link"))
            {
                record.ReturnAll = false;
            } else if(control.MatchOp("ttl"))
            {
                int ttl;
                control.MustMatchValue(ttl);
                record.TTL = ttl;
            } else if(control.MatchOp("enable"))
            {
                record.Enabled = true;
            } else if(control.MatchOp("disable"))
            {
                record.Enabled = false;
            } else if(control.MatchOp("domain"))
            {
                String temp;
                control.MustMatchValue(temp);
                record.Name = temp;
            }
            break;
        }
        if(!control.IsEnd())
            control.NotMatch();
        record.ID = GetDomainID(domain);
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(ARecordSetNormal);

    void ARecordSetAlias(Control& control)
    {
        String domain;
        MatchSetARecord(control, domain);
        control.MustMatchOp("alias");
        CommandModel cmd;
        cmd.FuncName = FuncBindARecordSet;
        cmd.Password = true;
        ARecord record(cmd.Params);
        String temp;
        while(control.MatchValue(temp))
        {
            record.Alias.Append() = temp;
        }
        record.ID = GetDomainID(domain);
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(ARecordSetAlias);

    void ARecordSetIP(Control& control)
    {
        String domain;
        MatchSetARecord(control, domain);
        control.MustMatchOp("server");
        CommandModel cmd;
        cmd.FuncName = FuncBindARecordSet;
        cmd.Password = true;
        ARecord record(cmd.Params);
        String isp;
        typedef deque<String> StringList;
        typedef map<String, StringList> IPCollection;
        IPCollection data;
        while(true)
        {
            if(control.MatchOp("isp"))
            {
                control.MustMatchValue(isp);
            } else if(control.MatchOp("noisp"))
            {
                isp.clear();
            } else if(!control.IsEnd())
            {
                String ip;
                control.MustMatchValue(ip);
                data[isp].push_back(ip);
            } else
                break;
        }
        {
            map<String, int> idlist;
            {
                CommandModel request;
                request.FuncName = FuncISPGetListName;
                request.Password = true;
                List<ISPItem> list(request.Params);
                Value result;
                Rpc::Call(request, result);
                List<ISPItem> reslist(result);
                ENUM_LIST(ISPItem, reslist, r)
                {
                    idlist[r->Name] = r->ID;
                }
            }
            idlist[""] = 0;
            ENUM_STL(IPCollection, data, e)
            {
                int id = idlist[e->first];
                if(!e->first.empty() && id == 0)
                    ERROR(Exception::Command::ISPName, e->first);
                ENUM_STL(StringList, e->second, s)
                {
                    ServerItem server(record.Servers.Append());
                    server.ISP = id;
                    server.IP = *s;
                }
            }
        }
        record.ID = GetDomainID(domain);
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(ARecordSetIP);

    void ARecordDel(Control& control)
    {
        MatchBind(control);
        control.MustMatchOp("domain");
        control.MustMatchOp("del");
        CommandModel cmd;
        cmd.FuncName = FuncBindARecordDel;
        cmd.Password = true;
        ARecord record(cmd.Params);
        String temp;
        control.MustMatchValue(temp);
        record.Name = temp;
        record.Enabled = true;
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(ARecordDel);

    void Show(Control& control)
    {
        MatchBind(control);
        if(!control.MatchOp("show") && !control.IsEnd())
            control.NotMatch();
        CommandModel cmd;
        cmd.FuncName = FuncBindGetAll;
        cmd.Password = true;
        Value result;
        Rpc::Call(cmd, result);
        BindItem bind(result);
        cout << "Port: " << bind.Port << endl;
        cout << "Reverse: " << (bind.Reverse ? "yes" : "no") << endl;
        cout << "State: " << (bind.Enabled ? "enabled" : "disabled") << endl;
        if(!bind.A.IsEmpty())
            cout << "Record:" << endl;
        ENUM_LIST(ARecord, bind.A, a)
        {
            cout << "  " << a->Name << " :" << endl;
            cout << "    " << "TTL: " << a->TTL << endl;
            cout << "    " << "State: " << (a->Enabled ? "enabled" : "disabled") << endl;
            cout << "    " << "Answer: " << (a->ReturnAll ? "all" : "link") << endl;
            if(!a->Alias.IsEmpty())
                cout << "    Alias:" << endl;
            ENUM_LIST(DomainStringValue, a->Alias, alias)
            {
                cout << "      " << *alias << endl;
            }
            if(!a->Servers.IsEmpty())
            {
                map<int, String> idlist;
                {
                    CommandModel cmd;
                    cmd.FuncName = FuncISPGetListName;
                    cmd.Password = true;
                    Value result;
                    Rpc::Call(cmd, result);
                    ENUM_LIST(ISPItem, List<ISPItem>(result), e)
                    {
                        idlist[e->ID] = e->Name;
                    }
                }
                cout << "    Servers:" << endl;
                ENUM_LIST(ServerItem, a->Servers, s)
                {
                    cout << "      " << s->IP << " " << idlist[s->ISP] << endl;
                }
            }
        }
    }

    DECLARE_COMMAND(Show);

};
