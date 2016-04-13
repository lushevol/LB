#include "share/utility.h"

#include "rpc.h"
#include "serialize.h"
#include "base.h"
#include "logger.h"

#include "isp.h"

using namespace std;

ISPControl::ISPControl()
    : Holder(Configure::GetValue()["ISP"])
{
    if(!Holder.Valid())
        Holder.Clear();
}

Value& ISPControl::Find(ISPItem& item)
{
    if(!item.ID.Valid())
        ERROR(Exception::Server::Params, "ID");
    Value* result = NULL;
    ENUM_LIST(ISPItem, Holder, e)
    {
        if(e->ID == item.ID)
        {
            result = &e->Data;
            break;
        }
    }
    if(!result)
        ERROR(Exception::ISP::NotFoundID, item.ID);
    return *result;
}

void ISPControl::GetName(List<ISPItem>& list)
{
    list.Clear();
    ENUM_LIST(ISPItem, Holder, e)
    {
        ISPItem target(list.Append());
        target.ID = e->ID;
        target.Name = e->Name;
    }
}

void ISPControl::GetAll(List<ISPItem>& list)
{
    list.Clear();
    ENUM_LIST(ISPItem, Holder, e)
    {
        ISPItem target(list.Append());
        target.ID = e->ID;
        target.Name = e->Name;
        target.Net = e->Net;
    }
}

void ISPControl::GetName(ISPItem& item)
{
    ISPItem recent(Find(item));
    item.Name = recent.Name;
}

void ISPControl::GetAll(ISPItem& item)
{
    ISPItem recent(Find(item));
    item.Name = recent.Name;
    item.Net = recent.Net;
}

void ISPControl::Set(ISPItem& item)
{
    ISPItem recent(Find(item));
    if(item.Name.Valid())
    {
        if(item.Name == "")
            ERROR(Exception::ISP::Name::Empty, "");
        ENUM_LIST(ISPItem, Holder, e)
        {
            if(e->ID != item.ID && e->Name == item.Name)
                ERROR(Exception::ISP::Name::Duplicate, item.Name);
        }
    }
    if(item.Net.Valid())
    {
        ENUM_LIST(NetPackStringValue, item.Net, e)
        {
            if(*e == "")
                ERROR(Exception::ISP::EmptyNet, "");
        }
    }
    if(item.Name.Valid())
        recent.Name = item.Name;
    if(item.Net.Valid())
    {
        recent.Net.Clear();
        ENUM_LIST(NetPackStringValue, item.Net, e)
        {
            recent.Net.Append() = *e;
        }
    }
    if(item.Name.Valid() || item.Net.Valid())
        ISPRefresher::Refresh();
}

void ISPControl::Add(ISPItem& item)
{
    if(Holder.GetCount() >= ISPItem::IDValue::Max - ISPItem::IDValue::Min)
        ERROR(Exception::ISP::Count, "");
    IntCollection idlist;
    ENUM_LIST(ISPItem, Holder, e)
    {
        idlist.insert(e->ID);
    }
    ASSERT(ISPItem::IDValue::Min >= 0);
    item.ID.Data.clear();
    for(int index = ISPItem::IDValue::Min + 1; index <= ISPItem::IDValue::Max; ++index)
    {
        if(idlist.count(index) == 0)
        {
            item.ID = index;
            break;
        }
    }
    ASSERT(item.ID.Valid());
    ASSERT(item.ID != 0);
    if(!item.Name.Valid())
        ERROR(Exception::Server::Params, "Name");
    if(item.Name == "")
        ERROR(Exception::ISP::Name::Empty, "");
    ENUM_LIST(ISPItem, Holder, e)
    {
        if(e->Name == item.Name)
            ERROR(Exception::ISP::Name::Duplicate, item.Name);
    }
    if(item.Net.Valid())
    {
        ENUM_LIST(NetPackStringValue, item.Net, e)
        {
            if(*e == "")
                ERROR(Exception::ISP::EmptyNet, "");
        }
    }
    ISPItem recent(Holder.Append());
    recent.ID = item.ID;
    recent.Name = item.Name;
    recent.Net.Clear();
    if(item.Net.Valid())
    {
        ENUM_LIST(NetPackStringValue, item.Net, e)
        {
            recent.Net.Append() = *e;
        }
    }
    ISPRefresher::Refresh();
}

void ISPControl::Del(ISPItem& item)
{
    if(!item.ID.Valid())
        ERROR(Exception::Server::Params, "ID");
    int target = -1;
    for(int id = 0; id < Holder.GetCount(); ++id)
    {
        ISPItem recent(Holder.Get(id));
        if(recent.ID == item.ID)
        {
            target = id;
            break;
        }
    }
    if(target != -1)
    {
        Holder.Delete(target);
        ISPRefresher::Refresh();
    } else
        ERROR(Exception::ISP::NotFoundID, item.ID);
}

typedef set<ISPRefresher*> ISPRefresherList;
typedef map<int, ISPRefresherList> ISPRefresherMap;

static ISPRefresherMap* FISPRefresher = NULL;

void ISPRefresher::Refresh()
{
    DEBUG_PRINT("ISP", "ISP Refresh..........");
    if(FISPRefresher)
    {
        ENUM_STL(ISPRefresherMap, *FISPRefresher, list)
        {
            ENUM_STL(ISPRefresherList, list->second, e)
            {
                if((*e)->FRefresh != NULL)
                    (*e)->FRefresh();
            }
        }
    }
}

ISPRefresher::ISPRefresher(Func refresh, int priority)
{
    FRefresh = refresh;
    FPriority = priority;
    if(!FISPRefresher)
        FISPRefresher = new ISPRefresherMap();
    (*FISPRefresher)[FPriority].insert(this);
}

ISPRefresher::~ISPRefresher()
{
    (*FISPRefresher)[FPriority].erase(this);
    if((*FISPRefresher)[FPriority].size() == 0)
        FISPRefresher->erase(FPriority);
    if(FISPRefresher->size() == 0)
    {
        delete FISPRefresher;
        FISPRefresher = NULL;
    }
}

namespace ISP
{

#define EXECUTE_RPC(methodname,methodfunc)\
    void LINE_NAME(methodfunc)(Value& params, Value& result)\
    {\
        RpcMethod::CheckLicence();\
        ISPControl control;\
        ISPItem item(params);\
        control.methodfunc(item);\
        (bool&)result = true;\
    }\
    DECLARE_RPC_METHOD(methodname,LINE_NAME(methodfunc),true,true)

    EXECUTE_RPC(FuncISPSet, Set);
    EXECUTE_RPC(FuncISPAdd, Add);
    EXECUTE_RPC(FuncISPDel, Del);

#define EXECUTE_RPC_GET(methodname,methodfunc,model)\
    void LINE_NAME(methodfunc)(Value& params, Value& result)\
    {\
        RpcMethod::CheckLicence();\
        ISPControl control;\
        model item(params);\
        control.methodfunc(item);\
        result = params;\
    }\
    DECLARE_RPC_METHOD(methodname,LINE_NAME(methodfunc),true,true)

    EXECUTE_RPC_GET(FuncISPGetListAll, GetAll, List<ISPItem>);
    EXECUTE_RPC_GET(FuncISPGetListName, GetName, List<ISPItem>);
    EXECUTE_RPC_GET(FuncISPGetItemAll, GetAll, ISPItem);
    EXECUTE_RPC_GET(FuncISPGetItemName, GetName, ISPItem);

    void ISPBeforeImport(Value& data, bool reload)
    {
        ISPControl control;
        control.Holder.Clear();
    }

    void ISPImport(Value& data, bool reload)
    {
        List<ISPItem> list(data["ISP"]);
        ISPControl control;
        if(reload)
        {
            try
            {
                ENUM_LIST(ISPItem, list, e)
                {
                    ISPItem isp(*e);
                    NO_ERROR(control.Add(isp));
                }
            } catch(ValueException&)
            {
            }
        } else {
            ENUM_LIST(ISPItem, list, e)
            {
                ISPItem isp(*e);
                control.Add(isp);
            }
        }
    }

    void ISPExport(Value& data)
    {
        List<ISPItem> result(data["ISP"]);
        ISPControl control;
        control.GetAll(result);
    }

    DECLARE_SERIALIZE(ISPBeforeImport, ISPImport, NULL, ISPExport, 9);

}
