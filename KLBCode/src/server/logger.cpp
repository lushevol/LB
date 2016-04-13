#include <string.h>
#include <fstream>
#include <iostream>

#include <syslog.h>

#include "share/utility.h"
#include "model/system.h"

#include "rpc.h"
#include "base.h"
#include "serialize.h"

#include "logger.h"

using namespace std;

void Logger::Write(Logger::Priority priority, const String& info)
{
    openlog("KLB", LOG_ODELAY, LOG_LOCAL6);
    int p;
    switch(priority)
    {
        case Warning:
            p = LOG_WARNING;
            cout << "Warning: ";
            break;
        case Error:
            p = LOG_ERR;
            cout << "Error:   ";
            break;
        case Notice:
            p = LOG_NOTICE;
            cout << "Notic:   ";
            break;
        default:
            p = LOG_INFO;
            cout << "Info:    ";
    };
    syslog(p, "%s", info.c_str());
    cout << info << endl;
    closelog();
}

Logger::Logger()
    : Item(Configure::GetValue()["Logger"])
{
    if(!Item.Enabled.Valid())
    {
        Item.Domain = "";
        Item.Enabled = false;
        Refresh();
    }
}

void Logger::Refresh()
{
    PRINTF("Refresh logger.....");
    static const char* path = "/etc/rsyslog.conf";
    Exec::System("service rsyslog stop");
    StringList buffer;
    {
        ifstream stream(path);
        if(stream)
        {
            String line;
            while(getline(stream, line))
            {
                if(strncmp(line.c_str(), "local6.*", 8) == 0)
                    continue;
                buffer.push_back(line);
            }
            stream.close();
        }
    }
    {
        ofstream stream(path);
        if(stream)
        {
            while(!buffer.empty())
            {
                stream << buffer.front() << endl;
                buffer.pop_front();
            }
            stream << "local6.*\t/var/log/klb.log" << endl;
            if(Item.Domain != "" && Item.Enabled)
                stream << "local6.*\t@" << Item.Domain << endl;
            stream.close();
        }
    }
    Exec::System("service rsyslog start");
}

void Logger::Get(LoggerItem& logger)
{
    logger.Enabled = Item.Enabled;
    logger.Domain = Item.Domain;
}

void Logger::Set(LoggerItem& logger)
{
    String domain = logger.Domain.Valid() ? logger.Domain : Item.Domain;
    bool enabled = logger.Enabled.Valid() ? logger.Enabled : Item.Enabled;
    if(Item.Domain != domain)
    {
        if(enabled != Item.Enabled)
        {
            Item.Domain = domain;
            Item.Enabled = enabled;
            Refresh();
        } else {
            Item.Domain = domain;
            if(enabled)
                Refresh();
        }
    } else if(enabled != Item.Enabled)
    {
        Item.Enabled = enabled;
        if(!domain.empty())
            Refresh();
    }
}

namespace Log
{
    void Get(Value& params, Value& result)
    {
        Logger log;
        LoggerItem res(result);
        log.Get(res);
    }

    DECLARE_RPC_METHOD(FuncLoggerGet, Get, true, true);

    void Set(Value& params, Value& result)
    {
        Logger log;
        LoggerItem item(params);
        log.Set(item);
        (bool&)result = true;
    }

    DECLARE_RPC_METHOD(FuncLoggerSet, Set, true, true);

    void Import(Value& data, bool reload)
    {
        LoggerItem item(data["Logger"]);
        Logger log;
        if(reload)
            NO_ERROR(log.Set(item));
        else
            log.Set(item);
    }

    void Export(Value& data)
    {
        LoggerItem item(data["Logger"]);
        Logger log;
        log.Get(item);
    }

    DECLARE_SERIALIZE(NULL, Import, NULL, Export, 10);

    void InitSyslog()
    {
        Logger();
    }

    DECLARE_INIT(InitSyslog, NULL, -0xFFFFFF);
}
