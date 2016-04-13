#include "share/include.h"
#include "share/utility.h"

#include "rpc.h"
#include "serialize.h"
#include "base.h"
#include "logger.h"

#include "network.h"
#include "blacklist.h"

BlackListControl::BlackListControl()
    : Holder(Configure::GetValue()["BlackList"])
{}

void BlackListControl::CheckID(BlackListItem& item)
{
    if(!item.ID.Valid())
        ERROR(Exception::Server::Params, "ID");
}

void BlackListControl::CheckMatchRule(BlackListItem& item)
{
    item.SrcNet.Valid();
    item.DestNet.Valid();
    item.ProtocolStr.Valid();
    item.Protocol.Valid();
    item.SrcPort.Valid();
    item.DestPort.Valid();
    if(item.ProtocolStr != "")
        item.Protocol = Network::GetProtocol(item.ProtocolStr);
    if(!Network::IsSupportPort(item.Protocol) && (item.SrcPort != 0 || item.DestPort != 0))
        ERROR(Exception::Network::SupportPort, item.Protocol);
}

void BlackListControl::CopyRule(BlackListItem& target, BlackListItem& recent)
{
    target.SrcNet = recent.SrcNet;
    target.DestNet = recent.DestNet;
    target.Protocol = recent.Protocol;
    target.ProtocolStr = recent.ProtocolStr;
    target.SrcPort = recent.SrcPort;
    target.DestPort = recent.DestPort;
}

void BlackListControl::InsertRule(BlackListItem& item)
{
    Exec exe("iptables");
    exe << "-t" << "mangle" << "-I" << "BLACKLIST" << item.ID + 1;
    if(item.SrcNet != "")
        exe << "-s" << item.SrcNet;
    if(item.DestNet != "")
        exe << "-d" << item.DestNet;
    if(item.Protocol != 0)
    {
        exe << "-p" << item.Protocol;
        if(item.SrcPort != 0)
            exe << "--sport" << item.SrcPort;
        if(item.DestPort != 0)
            exe << "--dport" << item.DestPort;
    } else {
        ASSERT(item.SrcPort == 0);
        ASSERT(item.DestPort == 0);
    }
    exe << "-j" << "DROP";
    exe.Execute();
}

void BlackListControl::DeleteRule(BlackListItem& item)
{
    Exec exe("iptables");
    exe << "-t" << "mangle" << "-D" << "BLACKLIST" << item.ID + 1;
    exe.Execute();
}

void BlackListControl::GetCount(IntValue& result)
{
    result = GetCount();
}

int BlackListControl::GetCount()
{
    return Holder.GetCount();
}

void BlackListControl::Add(BlackListItem& item)
{
    CheckMatchRule(item);
    BlackListItem recent(Holder.Append());
    recent.ID = Holder.GetCount() - 1;
    recent.Description = item.Description;
    CopyRule(recent, item);
    InsertRule(recent);
}

void BlackListControl::Set(BlackListItem& item)
{
    CheckID(item);
    BlackListItem recent(Holder.Get(item.ID));
    if(!item.SrcNet.Valid())
        item.SrcNet = recent.SrcNet;
    if(!item.DestNet.Valid())
        item.DestNet = recent.DestNet;
    if(!item.Protocol.Valid())
        item.Protocol = recent.Protocol;
    if(!item.ProtocolStr.Valid())
        item.ProtocolStr = recent.ProtocolStr;
    if(!item.SrcPort.Valid())
        item.SrcPort = recent.SrcPort;
    if(!item.DestPort.Valid())
        item.DestPort = recent.DestPort;
    CheckMatchRule(item);
    DeleteRule(recent);
    CopyRule(recent, item);
    InsertRule(recent);
}

void BlackListControl::Del(BlackListItem& item)
{
    CheckID(item);
    {
        BlackListItem recent(Holder.Get(item.ID));
        DeleteRule(recent);
    }
    Holder.Delete(item.ID);
    int count = 0;
    ENUM_LIST(BlackListItem, Holder, e)
    {
        e->ID = count++;
    }
}

void BlackListControl::Flush()
{
    Exec::System("iptables -t mangle -F BLACKLIST");
    Holder.Clear();
}

void BlackListControl::SetDescription(BlackListItem& item)
{
    CheckID(item);
    Holder.Get(item.ID).Description = item.Description;
}

void BlackListControl::Get(GetListItem<BlackListItem>& get)
{
    int start, end;
    get.Ready(Holder.GetCount(), start, end);
    for(int i = start; i < end; ++i)
    {
        BlackListItem target(get.Result.Append());
        BlackListItem recent(Holder.Get(i));
        target.ID = recent.ID;
        target.Description = recent.Description;
        CopyRule(target, recent);
    }
}

namespace BlackList
{

#define EXECUTE_RPC(methodname,methodfunc)\
    void LINE_NAME(methodfunc)(Value& params, Value& result)\
    {\
        RpcMethod::CheckLicence();\
        BlackListControl control;\
        BlackListItem item(params);\
        control.methodfunc(item);\
        (bool&)result = true;\
    }\
    DECLARE_RPC_METHOD(methodname,LINE_NAME(methodfunc),true,true)

    EXECUTE_RPC(FuncBlackListAdd, Add);
    EXECUTE_RPC(FuncBlackListDel, Del);
    EXECUTE_RPC(FuncBlackListSet, Set);
    EXECUTE_RPC(FuncBlackListDescription, SetDescription);

    void ExecuteBlackListGetCount(Value& params, Value& result)
    {
        RpcMethod::CheckLicence();
        BlackListControl control;
        IntValue res(result);
        control.GetCount(res);
    }

    DECLARE_RPC_METHOD(FuncBlackListGetCount, ExecuteBlackListGetCount, true, true);

    void ExecuteBlackListGet(Value& params, Value& result)
    {
        RpcMethod::CheckLicence();
        BlackListControl control;
        GetListItem<BlackListItem> get(params);
        control.Get(get);
        result = get.Result.Data;
    }

    DECLARE_RPC_METHOD(FuncBlackListGet, ExecuteBlackListGet, true, true);

    void BlackListBeforeImport(Value& data, bool reload)
    {
        BlackListControl().Flush();
    }

    void BlackListImport(Value& data, bool reload)
    {
        List<BlackListItem> list(data["BlackList"]);
        BlackListControl control;
        ENUM_LIST(BlackListItem, list, e)
        {
            if(reload)
                NO_ERROR(control.Add(*e));
            else
                control.Add(*e);
        }
    }

    void BlackListExport(Value& data)
    {
        List<Model> result(data["BlackList"]);
        BlackListControl control;
        Value temp;
        GetListItem<BlackListItem> list(temp);
        list.All = true;
        control.Get(list);
        ENUM_LIST(BlackListItem, list.Result, e)
        {
            e->ID.Data.clear();
            result.Append().Data = e->Data;
        }
    }

    DECLARE_SERIALIZE(BlackListBeforeImport, BlackListImport, NULL, BlackListExport, 2);

    void InitBlackList()
    {
        Exec::System("iptables -t mangle -N BLACKLIST");
        Exec::System("iptables -t mangle -F BLACKLIST");
        Exec::System("iptables -t mangle -I PREROUTING ! -s 127.0.0.0/8 ! -d 127.0.0.0/8 -j BLACKLIST");
        Exec::System("iptables -t mangle -I OUTPUT ! -s 127.0.0.0/8 ! -d 127.0.0.0/8 -j BLACKLIST");
    }

    DECLARE_INIT(InitBlackList, NULL, 1);
};
