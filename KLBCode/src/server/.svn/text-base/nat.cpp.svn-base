#include <sstream>

#include "share/include.h"
#include "share/utility.h"

#include "model/interface.h"

#include "rpc.h"
#include "base.h"
#include "serialize.h"
#include "logger.h"

#include "nat.h"
#include "network.h"
#include "ethernet.h"
#include "bonding.h"

using namespace std;

void NatControl::CheckDev(const String& dev)
{
    switch(Network::GetDevType(dev))
    {
        case Network::Ethernet:
            {
                EthernetInterfaceControl control;
                EthernetInterface eth(control.Find(dev));
                if(eth.Master != "")
                    ERROR(Exception::Interface::Ethernet::Slave, dev << '\n' << eth.Master);
                if(eth.Adsl != "")
                    ERROR(Exception::Interface::Ethernet::Adsl, dev << '\n' << eth.Adsl);
            }
            break;
        case Network::Bonding:
            {
                BondingInterfaceControl control;
                BondingInterface bond(control.Find(dev));
                if(bond.Slaves.IsEmpty())
                    ERROR(Exception::Interface::Bonding::Slave::Empty, dev);
            }
            break;
        case Network::Adsl:
            break;
        default:
            ERROR(Exception::Interface::NotFoundDev, dev);
    };
}

void NatControl::GenerateMatchCmd(OStream& stream,  MatchItem& match)
{
    if(match.SrcNet != "")
        stream << " -s " << match.SrcNet;
    if(match.DestNet != "")
        stream << " -d " << match.DestNet;
    if(match.Dev != "")
    {
        CheckDev(match.Dev);
        stream << (FSrc ? " -o " : " -i ") << match.Dev;
    }
    if(match.Protocol != 0)
        stream << " -p " << match.Protocol;
    if(Network::IsSupportPort(match.Protocol))
    {
        if(match.SrcPort != 0)
            stream << " --sport " << match.SrcPort;
        if(match.DestPort != 0)
            stream << " --dport " << match.DestPort;
    }
}

void NatControl::GenerateChain(OStream& stream)
{
    stream << (FSrc ? "SRC" : "DEST");
}

void NatControl::GenerateActionIP(OStream& stream, ActionItem& action)
{
    stream << action.StartIP;
    if(action.EndIP != "")
        stream << "-" << action.EndIP;
}

void NatControl::GenerateActionPort(OStream& stream, ActionItem& action, const char* prefix)
{
    if(action.StartPort != 0)
    {
        stream << prefix << action.StartPort;
        if(action.EndPort != 0)
            stream << "-" << action.EndPort;
    }
}

void NatControl::GenerateActionCmd(OStream& stream, ActionItem& action)
{
    if(action.Except)
        stream << " -j RETURN";
    else {
        if(FSrc)
        {
            if(action.Masquerade)
            {
                stream << " -j MASQUERADE";
                GenerateActionPort(stream, action, " --to-ports ");
            } else {
                stream << " -j SNAT --to-source ";
                GenerateActionIP(stream, action);
                GenerateActionPort(stream, action, ":");
            }
        } else {
            stream << " -j DNAT --to-destination ";
            GenerateActionIP(stream, action);
            GenerateActionPort(stream, action, ":");
        }
    }
}

void NatControl::CheckItemValid(NatItem& nat)
{
    MatchItem& match = nat.Match;
    if(match.ProtocolStr != "")
        match.Protocol = Network::GetProtocol(match.ProtocolStr);
    if(match.SrcPortStr != "")
        match.SrcPort = Network::GetServiceByProtocol(match.Protocol, match.SrcPortStr);
    if(match.DestPortStr != "")
        match.DestPort = Network::GetServiceByProtocol(match.Protocol, match.DestPortStr);
    if(!Network::IsSupportPort(match.Protocol) && (match.SrcPort != 0 || match.DestPort != 0))
        ERROR(Exception::Network::SupportPort, match.Protocol);
    ActionItem& action = nat.Action;
    if(!action.Except)
    {
        if(!FSrc || !action.Masquerade)
        {
            if(action.StartIP == "")
                ERROR(Exception::Nat::Map::IP, "");
        }
        if(action.StartPort == 0 && action.EndPort != 0)
            action.StartPort = action.EndPort;
        if(action.StartPort != 0)
        {
            if(Network::IsSupportPort(match.Protocol))
            {
                if(action.EndPort == 0)
                    action.EndPort = action.StartPort;
                if(action.EndPort < action.StartPort)
                    ERROR(Exception::Nat::Map::Port, action.StartPort << '\n' << action.EndPort);
            } else
                ERROR(Exception::Network::SupportPort, match.Protocol);
        }
    }
    if(nat.ID == 0)
        nat.RuleID = 1;
    else {
        NatItem before(Holder.Get(nat.ID - 1));
        nat.RuleID = GetRuleID(before);
    }
    try
    {
        if(match.Dev != "")
            CheckDev(match.Dev);
        nat.Status = true;
    } catch(ValueException& e)
    {
        nat.Status = false;
    }
}

int NatControl::GetRuleID(NatItem& nat)
{
    if(nat.Enabled && nat.Status)
        return nat.RuleID + 1;
    else
        return nat.RuleID;
}

void NatControl::RefreshID()
{
    int count = 0;
    int rule = 1;
    ENUM_LIST(NatItem, Holder, e)
    {
        NatItem nat(*e);
        nat.ID = count;
        nat.RuleID = rule;
        if(nat.Enabled && nat.Status)
            ++rule;
        ++count;
    }
}

void NatControl::DoAddNat(NatItem& nat)
{
    ostringstream stream;
    stream << "iptables -t nat -I ";
    GenerateChain(stream);
    stream << " " << nat.RuleID;
    GenerateMatchCmd(stream, nat.Match);
    GenerateActionCmd(stream, nat.Action);
    Exec::System(stream.str());
}

void NatControl::DoDelNat(NatItem& nat)
{
    ostringstream stream;
    stream << "iptables -t nat -D ";
    GenerateChain(stream);
    stream << " " << nat.RuleID;
    Exec::System(stream.str());
}

void NatControl::Copy(NatItem& target, NatItem& recent)
{
    target.Match.SrcNet = recent.Match.SrcNet;
    target.Match.DestNet = recent.Match.DestNet;
    target.Match.Protocol = recent.Match.Protocol;
    target.Match.ProtocolStr = recent.Match.ProtocolStr;
    target.Match.SrcPort = recent.Match.SrcPort;
    target.Match.SrcPortStr = recent.Match.SrcPortStr;
    target.Match.DestPort = recent.Match.DestPort;
    target.Match.DestPortStr = recent.Match.DestPortStr;
    target.Match.Dev = recent.Match.Dev;
    target.Description = recent.Description;
    target.Enabled = recent.Enabled;
    target.Status = recent.Status;
    target.ID = recent.ID;
    target.Action.Except = recent.Action.Except;
    if(!recent.Action.Except)
    {
        if(FSrc)
            target.Action.Masquerade = recent.Action.Masquerade;
        if(!FSrc || !recent.Action.Masquerade)
        {
            target.Action.StartIP = recent.Action.StartIP;
            target.Action.EndIP = recent.Action.EndIP;
        }
        target.Action.StartPort = recent.Action.StartPort;
        target.Action.EndPort = recent.Action.EndPort;
    }
}

void NatControl::Refresh(NatItem& nat)
{
    if(nat.Enabled)
    {
        bool old = nat.Status;
        if(nat.Status)
            DoDelNat(nat);
        try
        {
            nat.Status = true;
            DoAddNat(nat);
        } catch(ValueException& e)
        {
            nat.Status = false;
        }
        if(old != nat.Status)
            RefreshID();
    }
}

void NatControl::Refresh(const StringCollection& dev)
{
    ENUM_LIST(NatItem, Holder, e)
    {
        NatItem nat(*e);
        if(dev.count(nat.Match.Dev))
            Refresh(nat);
    }
}

void NatControl::RefreshAll(const StringCollection& dev)
{
    NatSrcControl().Refresh(dev);
    NatDestControl().Refresh(dev);
}

DECLARE_INTERFACE_REFRESH(NatControl::RefreshAll, 1);

void NatControl::RefreshAll(const String& dev)
{
    StringCollection list;
    list.insert(dev);
    RefreshAll(list);
}

void NatControl::GetCount(IntValue& result)
{
    result = Holder.GetCount();
}

int NatControl::GetCount()
{
    return Holder.GetCount();
}

void NatControl::Get(GetListItem<NatItem>& get)
{
    int start, end;
    get.Ready(Holder.GetCount(), start, end);
    for(int i = start; i < end; ++i)
    {
        NatItem nat(get.Result.Append());
        NatItem recent(Holder.Get(i));
        Copy(nat, recent);
    }
}

void NatControl::Add(NatItem& nat)
{
    nat.ID = Holder.GetCount();
    Insert(nat);
}

void NatControl::Insert(NatItem& nat)
{
    if(!nat.ID.Valid())
        ERROR(Exception::Server::Params, "ID");
    if(nat.ID > Holder.GetCount())
        ERROR(Exception::Types::RangedList, nat.ID);
    CheckItemValid(nat);
    if(nat.Enabled && nat.Status)
        DoAddNat(nat);
    Holder.Insert(nat.ID);
    NatItem recent(Holder.Get(nat.ID));
    Copy(recent, nat);
    RefreshID();
    LOGGER_NOTICE("Add nat item done.");
}

void NatControl::Replace(NatItem& nat)
{
    if(!nat.ID.Valid())
        ERROR(Exception::Server::Params, "ID");
    if(nat.ID >= Holder.GetCount())
        ERROR(Exception::Types::RangedList, nat.ID);
    CheckItemValid(nat);
    NatItem recent(Holder.Get(nat.ID));
    if(nat.Enabled && nat.Status)
    {
        DoAddNat(nat);
        recent.RuleID = recent.RuleID + 1;
    }
    if(recent.Enabled && recent.Status)
        DoDelNat(recent);
    Copy(recent, nat);
    RefreshID();
    LOGGER_NOTICE("replace nat item done.");
}

void NatControl::Del(NatItem& nat)
{
    if(!nat.ID.Valid())
        ERROR(Exception::Server::Params, "ID");
    {
        NatItem recent(Holder.Get(nat.ID));
        if(recent.Enabled && recent.Status)
            DoDelNat(recent);
    }
    Holder.Delete(nat.ID);
    RefreshID();
    LOGGER_NOTICE("Del nat item done.");
}

void NatControl::SetEnabled(NatItem& nat)
{
    if(!nat.ID.Valid())
        ERROR(Exception::Server::Params, "ID");
    if(!nat.Enabled.Valid())
        ERROR(Exception::Server::Params, "Enabled");
    NatItem recent(Holder.Get(nat.ID));
    if(nat.Enabled != recent.Enabled)
    {
        if(recent.Status)
        {
            if(recent.Enabled)
                DoDelNat(recent);
            else
                DoAddNat(recent);
            recent.Enabled = nat.Enabled;
            RefreshID();
        } else
            recent.Enabled = nat.Enabled;
    }
    LOGGER_NOTICE("Set nat item enabled done.");
}

void NatControl::SetDescription(NatItem& nat)
{
    if(!nat.ID.Valid())
        ERROR(Exception::Server::Params, "ID");
    NatItem recent(Holder.Get(nat.ID));
    recent.Description = nat.Description;
    LOGGER_NOTICE("Set nat item description done.");
}

NatControl::NatControl(Value& value, bool src)
    : FSrc(src)
    , Holder(value)
{}

NatSrcControl::NatSrcControl()
    : NatControl(Configure::GetValue()["NatSrc"], true)
{}

NatDestControl::NatDestControl()
    : NatControl(Configure::GetValue()["NatDest"], false)
{}

namespace Nat
{

#define EXECUTE_RPC(methodname,methodfunc,control)\
    void LINE_NAME(methodfunc)(Value& params, Value& result)\
    {\
        RpcMethod::CheckLicence();\
        control LINE_NAME(control);\
        NatItem LINE_NAME(model)(params);\
        LINE_NAME(control).methodfunc(LINE_NAME(model));\
        (bool&)result = true;\
    }\
    DECLARE_RPC_METHOD(methodname,LINE_NAME(methodfunc),true,true)

    EXECUTE_RPC(FuncNatSrcAdd, Add, NatSrcControl);
    EXECUTE_RPC(FuncNatSrcInsert, Insert, NatSrcControl);
    EXECUTE_RPC(FuncNatSrcReplace, Replace, NatSrcControl);
    EXECUTE_RPC(FuncNatSrcDel, Del, NatSrcControl);
    EXECUTE_RPC(FuncNatSrcEnabled, SetEnabled, NatSrcControl);
    EXECUTE_RPC(FuncNatSrcDescription, SetDescription, NatSrcControl);

    EXECUTE_RPC(FuncNatDestAdd, Add, NatDestControl);
    EXECUTE_RPC(FuncNatDestInsert, Insert, NatDestControl);
    EXECUTE_RPC(FuncNatDestReplace, Replace, NatDestControl);
    EXECUTE_RPC(FuncNatDestDel, Del, NatDestControl);
    EXECUTE_RPC(FuncNatDestEnabled, SetEnabled, NatDestControl);
    EXECUTE_RPC(FuncNatDestDescription, SetDescription, NatDestControl);

#define EXECUTE_RPC_GET(methodname,control)\
    void LINE_NAME(get)(Value& params, Value& result)\
    {\
        RpcMethod::CheckLicence();\
        control LINE_NAME(control);\
        GetListItem<NatItem> list(params);\
        LINE_NAME(control).Get(list);\
        result = list.Result.Data;\
    }\
    DECLARE_RPC_METHOD(methodname,LINE_NAME(get),true,true)

    EXECUTE_RPC_GET(FuncNatSrcGet, NatSrcControl);
    EXECUTE_RPC_GET(FuncNatDestGet, NatDestControl);

#define EXECUTE_RPC_GETCOUNT(methodname,control)\
    void LINE_NAME(getcount)(Value& params, Value& result)\
    {\
        RpcMethod::CheckLicence();\
        control LINE_NAME(control);\
        IntValue res(result);\
        LINE_NAME(control).GetCount(res);\
    }\
    DECLARE_RPC_METHOD(methodname,LINE_NAME(getcount),true,true)

    EXECUTE_RPC_GETCOUNT(FuncNatSrcGetCount, NatSrcControl);
    EXECUTE_RPC_GETCOUNT(FuncNatDestGetCount, NatDestControl);

    template<class Control>
    void NatBeforeImport(Value& data, bool reload)
    {
        Control control;
        while(control.GetCount())
        {
            Value temp;
            NatItem nat(temp);
            nat.ID = control.GetCount() - 1;
            control.Del(nat);
        }
    }

    static const char* const NatName[] = {
        "NatSrc",
        "NatDest"
    };

    template<class Control, int Index>
    void NatImport(Value& data, bool reload)
    {
        List<NatItem> list(data[NatName[Index]]);
        Control control;
        ENUM_LIST(NatItem, list, e)
        {
            NatItem nat(*e);
            if(reload)
                NO_ERROR(control.Add(nat));
            else
                control.Add(nat);
        }
    }

    template<class Control, int Index>
    void NatExport(Value& data)
    {
        List<Model> result(data[NatName[Index]]);
        Control control;
        Value temp;
        GetListItem<NatItem> list(temp);
        list.All = true;
        control.Get(list);
        ENUM_LIST(NatItem, list.Result, e)
        {
            NatItem nat(*e);
            nat.ID.Data.clear();
            nat.RuleID.Data.clear();
            nat.Status.Data.clear();
            result.Append().Data = nat.Data;
        }
    }

    SerializeModel LINE_NAME(nat)(
        NatBeforeImport<NatSrcControl>,
        NatImport < NatSrcControl, 0 > ,
        NULL,
        NatExport< NatSrcControl, 0 >,
        3);

    SerializeModel LINE_NAME(nat)(
        NatBeforeImport<NatDestControl>,
        NatImport < NatDestControl, 1 > ,
        NULL,
        NatExport< NatDestControl, 1 >,
        3);

    void InitNat()
    {
        Exec::System("iptables -t nat -N SRC");
        Exec::System("iptables -t nat -A POSTROUTING -j SRC");
        Exec::System("iptables -t nat -N DEST");
        Exec::System("iptables -t nat -A PREROUTING -j DEST");
    }

    DECLARE_INIT(InitNat, NULL, 1);

};
