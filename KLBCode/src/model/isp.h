#ifndef __ISP_H_MODEL__
#define __ISP_H_MODEL__

#include "model.h"
#include "types.h"

class ISPItem: public Model
{
    public:
        typedef RangedIntValue<0, 128> IDValue;
    public:
        IDValue ID;
        CommonStringValue Name;
        List<NetPackStringValue> Net;
    public:
        ISPItem(Value& item)
            : Model(item)
            , ID(item["ID"])
            , Name(item["Name"])
            , Net(item["Net"])
        {}
};

static const char* const FuncISPGetListAll      = "ISP.Get.List.All";
static const char* const FuncISPGetListName     = "ISP.Get.List.Name";
static const char* const FuncISPGetItemAll      = "ISP.Get.Item.All";
static const char* const FuncISPGetItemName     = "ISP.Get.Item.Name";
static const char* const FuncISPSet             = "ISP.Set";
static const char* const FuncISPAdd             = "ISP.Add";
static const char* const FuncISPDel             = "ISP.Delete";

#endif // __ISP_H_MODEL__
