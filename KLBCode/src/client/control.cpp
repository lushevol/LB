#include <string.h>
#include <sstream>
#include <iostream>

#include "rpc/XmlRpc.h"

#include "model/system.h"

#include "control.h"
#include "exception.h"

using namespace std;
using namespace XmlRpc;

String Rpc::FPassword;

String Rpc::Encrypt(const char* password)
{
    if(!password || *password == '\0')
        return String();
    unsigned long long value = 0;
    const char* ch = password;
    while(*ch != '\0')
    {
        value = value * 31 + (const unsigned char) * ch;
        ++ch;
    }
    char buf[sizeof(value)*4];
    for(unsigned int i = 0; i < sizeof(value) * 2; ++i)
    {
        buf[i*2] = 0xC2 | ((value & 0x8) >> 3);
        buf[i*2+1] = 0x80 | ((value & 0x7) << 3);
        value >>= 4;
    }
    return String(buf, sizeof(value) * 4);
}

void Rpc::SetPassword(const String& password)
{
    FPassword = Encrypt(password.c_str());
}

void Rpc::CheckServer()
{
    CommandModel cmd;
    cmd.FuncName = FuncCheckServer;
    cmd.Password = true;
    Value result;
    Rpc::Call(cmd, result);
    if(result != FuncCheckServer)
        ERROR(Exception::Server::Version, "the server version is invalid.");
}

void Rpc::Call(CommandModel& cmd, Value& result)
{
    XmlRpcClient client("127.0.0.1", RPC_PORT);
    Value args;
    if(cmd.Password)
    {
        args[0] = FPassword;
        args[1] = cmd.Params;
    } else
        args[0] = cmd.Params;
    if(!client.execute(((const String&)cmd.FuncName).c_str(), args, result))
        ERROR(Exception::Command::Connect, "");
    client.close();
    if(client.isFault())
    {
        ExceptionModel error(result);
        ERROR(error.Code, error.Message);
    }
}

void Rpc::CallNoResult(CommandModel& cmd)
{
    Value result;
    Call(cmd, result);
    cout << "Done." << endl;
}

Control::Control(const OpList& ops)
    : FOP(ops)
{}

bool Control::MatchOp(const char* op)
{
    if(FOP.empty())
        return false;
    if(FOP.front().first != op || !FOP.front().second)
        return false;
    FOP.pop_front();
    return true;
}

bool Control::MatchValue(String& result)
{
    if(FOP.empty())
        return false;
    if(FOP.front().second)
        return false;
    result = FOP.front().first;
    FOP.pop_front();
    return true;
}

bool Control::MatchValue(int& result)
{
    if(FOP.empty())
        return false;
    if(FOP.front().second)
        return false;
    istringstream stream(FOP.front().first);
    if(!(stream >> result))
        ERROR(Exception::Command::IntValue, FOP.front().first);
    FOP.pop_front();
    return true;
}

bool Control::MatchOpValue(String& result)
{
    if(FOP.empty())
        return false;
    result = FOP.front().first;
    FOP.pop_front();
    return true;
}

bool Control::MatchOpValue(int& result)
{
    if(FOP.empty())
        return false;
    istringstream stream(FOP.front().first);
    if(!(stream >> result))
        ERROR(Exception::Command::IntValue, FOP.front().first);
    FOP.pop_front();
    return true;
}

void Control::MustMatchOp(const char* op)
{
    if(!MatchOp(op))
        NotMatch();
}

void Control::MustMatchValue(String& result)
{
    if(!MatchValue(result))
        NotMatch();
}

void Control::MustMatchValue(int& result)
{
    if(!MatchValue(result))
        NotMatch();
}

void Control::MustMatchOpValue(String& result)
{
    if(!MatchOpValue(result))
        NotMatch();
}

void Control::MustMatchOpValue(int& result)
{
    if(!MatchOpValue(result))
        NotMatch();
}

void Control::NotMatch()
{
    ERROR(Exception::Command::NotMatch, "");
}

bool Control::IsEnd()
{
    return FOP.empty();
}

Command::CommandSet* Command::FSet;

#ifdef __DEBUG__
Command::Command(CommandFunc func, const char* name)
#else
Command::Command(CommandFunc func)
#endif
    : FFunc(func)
#ifdef __DEBUG__
    , FName(name)
#endif
{
    if(!FSet)
        FSet = new CommandSet();
    FSet->insert(this);
}

Command::~Command()
{
    ASSERT(FSet);
    FSet->erase(this);
    if(FSet->empty())
        delete FSet;
}

void Command::GenerateOps(const char* cmd, StringList& ops)
{
    enum {
        None,
        Read,
        LeftQuot,
        RightQuot,
        AddParam
    } state = None;
    const unsigned char* start = (const unsigned char*)cmd;
    const unsigned char* end = start + strlen(cmd);
    ostringstream* stream = NULL;
    while(start <= end)
    {
        switch(state)
        {
            case None:
                if(*start <= ' ')
                {
                    ++start;
                } else if(*start == '"')
                {
                    ++start;
                    stream = new ostringstream;
                    state = LeftQuot;
                } else
                {
                    stream = new ostringstream;
                    state = Read;
                }
                break;
            case Read:
                if(*start <= ' ')
                {
                    state = AddParam;
                } else {
                    (*stream) << *start;
                    ++start;
                }
                break;
            case LeftQuot:
                if(*start < ' ')
                {
                    state = AddParam;
                } else if(*start == '"') {
                    ++start;
                    state = RightQuot;
                } else {
                    (*stream) << *start;
                    ++start;
                }
                break;
            case RightQuot:
                if(*start == '"')
                {
                    (*stream) << '"';
                    ++start;
                    state = LeftQuot;
                } else
                    state = AddParam;
                break;
            case AddParam:
                String res = stream->str();
                ops.push_back(res);
                delete stream;
                stream = NULL;
                state = None;
                break;
        }
    }
    if(stream)
        delete stream;
}

void Command::Execute(const char* cmd)
{
    try
    {
        if(FSet)
        {
            StringList ops;
            GenerateOps(cmd, ops);
            if(ops.empty())
                return;
            OpList list;
            Parser::Instance.ParseCmd(ops, list);
            ENUM_STL(CommandSet, *FSet, e)
            {
                Control control(list);
                try
                {
                    (*e)->FFunc(control);
                    PRINTF("Execute: " << (*e)->FName);
                    return;
                } catch(ValueException& error)
                {
                    switch(error.getCode())
                    {
                        case Exception::Command::NotMatch:
                            break;
                        default:
                            PRINTF("Execute: " << (*e)->FName);
                            throw error;
                    }
                }
            }
        }
        ERROR(Exception::Command::NotMatch, "");
    } catch(ValueException& e)
    {
        switch(e.getCode())
        {
            case Exception::Command::Connect:
            case Exception::Server::Password:
            case Exception::Server::Params:
                throw e;
            default:
                cout << Exception::GetMessage(e) << endl;
        }
    }
}
