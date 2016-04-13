#include <set>
#include <map>
#include <fstream>
#include <memory>

#include "model/system.h"

#include "logger.h"
#include "rpc.h"
#include "serialize.h"
#include "base.h"

using namespace std;

typedef set<SerializeModel*> SerializeModelList;
typedef map<int, SerializeModelList> SerializeModelMap;

static SerializeModelMap* FSerializeModel = NULL;

void SerializeModel::Import(Value& data, bool reload)
{
    if(FSerializeModel)
    {
#ifdef __DEBUG__
        try
        {
#endif
            PRINTF("FBeforeImport......." << data.getType());
            ENUM_STL_R(SerializeModelMap, *FSerializeModel, list)
            {
                ENUM_STL(SerializeModelList, list->second, e)
                {
                    if((*e)->FBeforeImport != NULL)
                    {
                        try
                        {
                            (*e)->FBeforeImport(data, reload);
                        } catch(ValueException& e)
                        {
                            if(!reload)
                                throw e;
                        }
                    }
                }
            }
            PRINTF("FImport.......");
            ENUM_STL(SerializeModelMap, *FSerializeModel, list)
            {
                ENUM_STL(SerializeModelList, list->second, e)
                {
                    if((*e)->FImport != NULL)
                        try
                        {
                            (*e)->FImport(data, reload);
                        } catch(ValueException& e)
                        {
                            if(!reload)
                                throw e;
                        }
                }
            }
            PRINTF("FAfterImport.......");
            ENUM_STL(SerializeModelMap, *FSerializeModel, list)
            {
                ENUM_STL(SerializeModelList, list->second, e)
                {
                    if((*e)->FAfterImport != NULL)
                        try
                        {
                            (*e)->FAfterImport(data, reload);
                        } catch(ValueException& e)
                        {
                            if(!reload)
                                throw e;
                        }
                }
            }
#ifdef __DEBUG__
        } catch(ValueException& e)
        {
            PRINTF("Serialize error: " << e.getMessage() << "     code:" << e.getCode());
            throw e;
        }
#endif

    }
}

void SerializeModel::Export(Value& data)
{
    if(FSerializeModel)
    {
        data.clear();
        ENUM_STL(SerializeModelMap, *FSerializeModel, list)
        {
            ENUM_STL(SerializeModelList, list->second, e)
            {
                if((*e)->FExport != NULL)
                    (*e)->FExport(data);
            }
        }
    }
}

SerializeModel::SerializeModel(ImportFunc before, ImportFunc in, ImportFunc after,  ExportFunc ex, int priority)
{
    FBeforeImport = before;
    FImport = in;
    FAfterImport = after;
    FExport = ex;
    FPriority = priority;
    if(!FSerializeModel)
        FSerializeModel = new SerializeModelMap();
    (*FSerializeModel)[FPriority].insert(this);
}

SerializeModel::~SerializeModel()
{
    (*FSerializeModel)[FPriority].erase(this);
    if((*FSerializeModel)[FPriority].size() == 0)
        FSerializeModel->erase(FPriority);
    if(FSerializeModel->size() == 0)
    {
        delete FSerializeModel;
        FSerializeModel = NULL;
    }
}

void LoadConfigure()
{
    ifstream conf(Configure::GetConfPath());
    if(conf)
    {
        ostringstream stream;
        stream << conf.rdbuf();
        Value data;
        data.fromXml(stream.str());
        conf.close();
        PRINTF("LoadData.................................................................................");
        SerializeModel::Import(data, true);
    }
}

DECLARE_INIT(LoadConfigure, NULL, MaximumInteger);

void ExecuteConfigureReload(Value& params, Value& result)
{
    LoadConfigure();
    LOGGER_INFO("Reload configure done.");
    (bool&)result = true;
}

DECLARE_RPC_METHOD(FuncConfigureReload, ExecuteConfigureReload, true, true);

void ExecuteConfigureSave(Value& params, Value& result)
{
    Value data;
    SerializeModel::Export(data);
    ofstream conf(Configure::GetConfPath());
    if(conf)
    {
        conf << data.toXml();
        conf.close();
    } else
        ERROR(Exception::System::SaveConfigure, "");
    LOGGER_INFO("Save configure done.");
    (bool&)result = true;
}

DECLARE_RPC_METHOD(FuncConfigureSave, ExecuteConfigureSave, true, true);

void ExecuteConfigureImport(Value& params, Value& result)
{
    RpcMethod::CheckLicence();
    try
    {
        (bool&)result = true;
        Value param;
        param.fromXml(params);
        SerializeModel::Import(param, false);
        LOGGER_INFO("Import configure done.");
    } catch(ValueException& e)
    {
        LOGGER_INFO("Import configure failed.(" << e.getCode() << ")(" << e.getMessage() << ")");
        LoadConfigure();
        ERROR(Exception::System::ImportConfigure, "");
    }
}

DECLARE_RPC_METHOD(FuncConfigureImport, ExecuteConfigureImport, true, true);

void ExecuteConfigureExport(Value& params, Value& result)
{
    RpcMethod::CheckLicence();
    Value temp;
    SerializeModel::Export(temp);
    auto_ptr<TiXmlDocument> doc(new TiXmlDocument());
    doc->Parse(temp.toXml().c_str());
    ostringstream stream;
    stream << (*doc);
    result = stream.str();
}

DECLARE_RPC_METHOD(FuncConfigureExport, ExecuteConfigureExport, true, true);


