#ifndef __CONTROL_H__
#define __CONTROL_H__

#include <set>

#include "model/model.h"
#include "parser.h"

class CommandModel: public Model
{
    private:
        Value FData;
    public:
        StringValue FuncName;
        BoolValue Password;
        Value& Params;
    public:
        CommandModel()
            : Model(FData)
            , FuncName(FData["Name"])
            , Password(FData["Password"])
            , Params(FData["Params"])
        {}
};

class Control
{
    private:
        OpList FOP;
    public:
        Control(const OpList& ops);
        bool MatchOp(const char* op);
        bool MatchValue(String& result);
        bool MatchValue(int& result);
        bool MatchOpValue(String& result);
        bool MatchOpValue(int& result);
        void MustMatchOp(const char* op);
        void MustMatchValue(String& result);
        void MustMatchValue(int& result);
        void MustMatchOpValue(String& result);
        void MustMatchOpValue(int& result);
        bool IsEnd();
    public:
        void NotMatch();
};

typedef void (*CommandFunc)(Control& control);

class Command
{
    private:
        typedef std::set<Command*> CommandSet;
        static CommandSet* FSet;
        CommandFunc FFunc;
#ifdef __DEBUG__
        const char* FName;
#endif
    public:
#ifdef __DEBUG__
        Command(CommandFunc func, const char* name);
#else
        Command(CommandFunc func);
#endif
        ~Command();
        static void GenerateOps(const char* cmd, StringList& ops);
        static void Execute(const char* cmd);
};

class Rpc
{
    private:
        static String FPassword;
    public:
        static void Call(CommandModel& cmd, Value& result);
        static void CallNoResult(CommandModel& cmd);
        static void SetPassword(const String& password);
        static String Encrypt(const char* password);
        static void CheckServer();
};

#endif // __CONTROL_H__
