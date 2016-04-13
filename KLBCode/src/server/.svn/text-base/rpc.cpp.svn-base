#include <sstream>

#include <pthread.h>

#include "share/utility.h"
#include "share/include.h"
#include "model/model.h"
#include "licence/licence.h"

#include "rpc.h"
#include "base.h"
#include "logger.h"
#include "wdt.h"

using namespace std;
using namespace XmlRpc;

static PRpcServer server = NULL;

void RpcServer::IncCounter()
{
    if(!server)
        server = new RpcServer();
    ++server->FRef;
}

void RpcServer::DecCounter()
{
    if(server)
    {
        --server->FRef;
        if(server->FRef == 0)
        {
            delete server;
            server = NULL;
        }
    }
}

class OuterMethod: public XmlRpcServerMethod
{
    public:
        OuterMethod(const String& name)
            : XmlRpcServerMethod(name, NULL)
        {}
        virtual ~OuterMethod()
        {}
    protected:
        virtual void execute(Value& params, Value& result)
        {
            XmlRpcClient client("", INNER_RPC_PORT, true);
            PRINTF("Execute Outer Rpc: " << name());
            client.execute(name().c_str(), params, result);
            client.close();
            if(client.isFault())
                throw XmlRpcException(ExceptionModel(result).Message, ExceptionModel(result).Code);
        }
};

void RpcServer::AddMethod(RpcMethod* method)
{
    if(server)
        server->FPrivateServer.addMethod(method);
}

void RpcServer::RemoveMethod(RpcMethod* method)
{
    if(server)
        server->FPrivateServer.removeMethod(method);
}

RpcServer::RpcServer()
    : FPrivateServer(true)
    , FPublicServer(false)
{
    FRef = 0;
    Configure::IncCounter();
    srand((unsigned int)time(NULL));
}

RpcServer::~RpcServer()
{
    Configure::DecCounter();
}

void HandleShutdown(int signal)
{
    Configure::IncCounter();
    Configure::SetShutdown();
    Configure::DecCounter();
}

void* MainRpcServerThread(void* param)
{
    XmlRpcServer* server = (XmlRpcServer*)param;
    while(!Configure::GetShutdown())
    {
        if(server->isLocal())
        {
            WatchDog::Feed();
#ifdef __DEBUG__
            DEBUG_UPDATE_CONF();
#endif
        }
        server->work(1);
    }
    PRINTF((server->isLocal() ? "Private" : "Public") << " thread exit.....");
    pthread_exit(NULL);
}

void RpcServer::SetPublicServer()
{
    XmlRpcValue innerlist;
    FPrivateServer.listMethods(innerlist);
    innerlist.setSize(innerlist.size() - 1);
    ENUM_STL(XmlRpcValue::ValueArray, innerlist.getArray(), e)
    {
        RpcMethod* method = (RpcMethod*)FPrivateServer.findMethod(*e);
        if(method->IsOuter)
            FPublicServer.addMethod(new OuterMethod(method->name()));
    }
}

void RpcServer::ClearPulicServer()
{
    XmlRpcValue outerlist;
    FPublicServer.listMethods(outerlist);
    outerlist.setSize(outerlist.size() - 1);
    ENUM_STL(XmlRpcValue::ValueArray, outerlist.getArray(), e)
    {
        OuterMethod* method = (OuterMethod*)FPublicServer.findMethod(*e);
        FPublicServer.removeMethod(method);
        delete method;
    }
}

void RpcServer::DoRun()
{
#ifdef __DEBUG__
//        setVerbosity(5);
#endif
    if(FPublicServer.bindAndListen(RPC_PORT) && FPrivateServer.bindAndListen(INNER_RPC_PORT))
    {
        signal(SIGTERM, HandleShutdown);
        signal(SIGQUIT, HandleShutdown);
        signal(SIGINT, HandleShutdown);

        LOGGER_INFO("Starting...");
        try
        {
            LicenceManager::Clear();
            LicenceManager::Import();
            InitModel::Init();
            SetPublicServer();
            LOGGER_INFO("Running...");
            pthread_t private_t;
            if(!pthread_create(&private_t, NULL, MainRpcServerThread, &FPrivateServer))
            {
                pthread_t public_t;
                if(!pthread_create(&public_t, NULL, MainRpcServerThread, &FPublicServer))
                {
                    void* res;
                    pthread_join(private_t, &res);
                    pthread_join(public_t, &res);
                    PRINTF("wait thread exit done.....");
                } else {
                    Configure::SetShutdown();
                    void* res;
                    pthread_join(private_t, &res);
                }
            }
            FPrivateServer.shutdown();
            FPublicServer.shutdown();
            ClearPulicServer();
            InitModel::Uninit();
        } catch(ValueException& error)
        {
            cout << "Exception: " << error.getMessage() << " (code:" << error.getCode() << ")" << endl;
        }
        LOGGER_INFO("Stopped.");
    }
}

void RpcServer::Run()
{
    if(server)
        server->DoRun();
}

void RpcServer::InnerCall(const char* method, const Value* params)
{
    XmlRpcClient client("127.0.0.1", INNER_RPC_PORT, true);
    Value args, result;
    if(params != NULL)
        args = *params;
    client.execute(method, args, result);
    client.close();
}

RpcMethod::RpcMethod(const String& name, MethodFunc func, bool auth, bool outer)
    : XmlRpcServerMethod(name, NULL)
    , IsOuter(outer)
{
    RpcServer::IncCounter();
    RpcServer::AddMethod(this);
    FFunc = func;
    FAuth = auth;
}

RpcMethod::~RpcMethod()
{
    RpcServer::RemoveMethod(this);
    RpcServer::DecCounter();
}

void RpcMethod::ErrorInvalidPassword()
{
    ERROR(Exception::Server::Password, "");
}

void RpcMethod::CheckLicence()
{
    if(!LicenceManager::IsExist())
        ERROR(Exception::Server::Licence, "");
}

void RpcMethod::execute(Value& params, Value& result)
{
#ifdef __DEBUG__
    try
    {
#endif
        if(FAuth)
        {
            if(params.getType() != Value::TypeArray || params.size() < 1 || params.size() > 2)
                ERROR(Exception::Server::Params, "Request");
            PRINTF("Params: " << params.size() << " " << params[0].getType());
            if(!params[0].valid())
                ErrorInvalidPassword();
            if(!Configure::ComparePassword((const String&)params[0]))
                ErrorInvalidPassword();
            FFunc(params[1], result);
        } else {
            FFunc(params[0], result);
        }
#ifdef __DEBUG__
    } catch(ValueException& e)
    {
        LOGGER_NOTICE("Exception code: " << e.getCode() << " ( " << e.getMessage() << " )");
        throw e;
    }
#endif
}

