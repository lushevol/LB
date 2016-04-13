#ifndef __RPC_H__
#define __RPC_H__

#include "rpc/XmlRpc.h"
#include "model/model.h"

class RpcMethod: public XmlRpc::XmlRpcServerMethod
{
    public:
        const bool IsOuter;
        typedef void (*MethodFunc)(Value& params, Value& result);
        RpcMethod(const String& name, MethodFunc func, bool auth = true, bool outer = false);
        virtual ~RpcMethod();
        static void ErrorInvalidPassword();
        static void CheckLicence();
    private:
        MethodFunc FFunc;
        bool FAuth;
        virtual void execute(Value& params, Value& result);
};

class RpcServer
{
    public:
        static void IncCounter();
        static void DecCounter();
        static void Run();
        static void AddMethod(RpcMethod* method);
        static void RemoveMethod(RpcMethod* method);
        static void InnerCall(const char* method, const Value* params = NULL);
    private:
        int FRef;
        RpcServer();
        ~RpcServer();
        XmlRpc::XmlRpcServer FPrivateServer;
        XmlRpc::XmlRpcServer FPublicServer;
        void DoRun();
        void SetPublicServer();
        void ClearPulicServer();
};

typedef RpcServer* PRpcServer;

#endif // __RPC_H__
