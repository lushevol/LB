#include <sstream>
#include <fstream>
#include <set>
#include <map>

#include <sys/stat.h>
#include <fcntl.h>
#include <openssl/md5.h>
#include <linux/limits.h>

#include "share/utility.h"
#include "share/include.h"

#include "model/exception.h"

#include "logger.h"
#include "base.h"

using namespace std;

static PConfigure configure = NULL;

Configure::Configure()
{
    FRef = 0;
    FShutdown = false;
    InitParams();
}

Configure::~Configure()
{
}

void Configure::IncCounter()
{
    if(configure == NULL)
    {
        configure = new Configure();
    }
    ++configure->FRef;
}

void Configure::DecCounter()
{
    if(configure)
    {
        --configure->FRef;
        if(!configure->FRef)
        {
            delete configure;
            configure = NULL;
        }
    }
}

void Configure::SetShutdown()
{
    ASSERT(configure);
    configure->FShutdown = true;
}

const bool Configure::GetShutdown()
{
    ASSERT(configure);
    return configure->FShutdown;
}

const char* Configure::GetConfPath()
{
    return "/etc/klbmanager.cf";
}

const char* Configure::GetAuthPath()
{
    return "/etc/klbpasswd.cf";
}

void Configure::Encrypt(const String& data, String& res)
{
    static int size = 16;
    unsigned char buf[size];
    MD5((const unsigned char*)data.c_str(), data.size(), buf);
    res.clear();
    res.append((char*)buf, size);
}

void Configure::SetPassword(const String& password)
{
    ASSERT(configure);
    if(!password.empty())
    {
        Encrypt(password, configure->FPassword);
        int fd = open(GetAuthPath(), O_CREAT | O_WRONLY | O_TRUNC, 0600);
        if(fd != -1)
        {
            const char* ch = configure->FPassword.c_str();
            int size = configure->FPassword.size();
            PRINTF("New password size: " << size);
            PRINTSTRINGHEX(configure->FPassword);
            int res;
            while((res = write(fd, ch, size)) > 0)
            {
                ch += res;
                size -= res;
            }
            close(fd);
        }
    } else {
        configure->FPassword.clear();
        unlink(GetAuthPath());
    }
}

bool Configure::ComparePassword(const String& password)
{
    ASSERT(configure);
    PRINTF("compare password: " << password.size() << " ");
    PRINTSTRINGHEX(password);
    if(configure->FPassword.empty())
    {
        int fd = open(GetAuthPath(), O_RDONLY);
        if(fd != -1)
        {
            static const int size = 1024;
            char buf[size];
            int res;
            while((res = read(fd, buf, size)) > 0)
                configure->FPassword.append(buf, res);
            close(fd);
        }
    }
    String res;
    if(!password.empty())
        Encrypt(password, res);
    return res == configure->FPassword;
}

const char* Configure::GetProcessPath()
{
    ASSERT(configure);
    return configure->FProcessPath.c_str();
}

const char* Configure::GetWorkPath()
{
    ASSERT(configure);
    return configure->FWorkPath.c_str();
}

const char* Configure::GetProcessName()
{
    ASSERT(configure);
    return configure->FProcessName.c_str();
}

void Configure::InitParams()
{
    pid_t pid = getpid();
    ostringstream stream;
    stream << "/proc/" << pid << "/exe";
    char path[PATH_MAX+1];
    path[PATH_MAX] = 0;
    if(realpath(stream.str().c_str(), path))
    {
        struct stat info;
        stat(path, &info);
        ostringstream stream;
        stream << (int)info.st_ino << ":" << (int)info.st_size;
        FProcessPath = path;
        String::size_type pos = FProcessPath.find_last_of('/');
        FWorkPath = FProcessPath.substr(0, pos + 1);
        FProcessName = FProcessPath.substr(pos + 1, FProcessPath.size() - pos - 1);
    }
}

Value& Configure::GetValue()
{
    ASSERT(configure);
    return configure->FValue;
}

typedef set<InitModel*> InitModelList;
typedef map<int, InitModelList> InitModelMap;

static InitModelMap* FInitModel = NULL;

void InitModel::Init()
{
    if(FInitModel)
    {
        ENUM_STL(InitModelMap, *FInitModel, list)
        {
            ENUM_STL(InitModelList, list->second, e)
            {
                PRINTF("InitModel.....");
                if((*e)->FInit != NULL)
                    (*e)->FInit();
            }
        }
    }
}

void InitModel::Uninit()
{
    if(FInitModel)
    {
        ENUM_STL_R(InitModelMap, *FInitModel, list)
        {
            ENUM_STL(InitModelList, list->second, e)
            {
                if((*e)->FUninit != NULL)
                    (*e)->FUninit();
            }
        }
    }
}

InitModel::InitModel(Func init, Func uninit, int priority)
{
    FInit = init;
    FUninit = uninit;
    FPriority = priority;
    if(!FInitModel)
        FInitModel = new InitModelMap();
    (*FInitModel)[FPriority].insert(this);
}

InitModel::~InitModel()
{
    (*FInitModel)[FPriority].erase(this);
    if((*FInitModel)[FPriority].size() == 0)
        FInitModel->erase(FPriority);
    if(FInitModel->size() == 0)
    {
        delete FInitModel;
        FInitModel = NULL;
    }
}

namespace Base
{
    int System(const char* cmd)
    {
        int status = Exec::System(cmd);
        if(status == -1)
            ERROR(Exception::Server::SystemCheckFault, "Cannot execute fork.");
        return WEXITSTATUS(status);
    }

    void SystemCheck()
    {
#ifdef __DEBUG__
#define WARNING(code,expr...) LOGGER_WARNING(expr)
#else
#define WARNING(code,expr...) ERROR(code,expr)
#endif
        LOGGER_INFO("System check...");
        //root
        if(getuid() != 0)
            ERROR(Exception::Server::SystemCheckFault, "Must use root to run.");
        //iproute2
        if(System("ip") != 255)
            ERROR(Exception::Server::SystemCheckFault, "Cannot found ip command.");
        if(System("tc") != 0)
            ERROR(Exception::Server::SystemCheckFault, "Cannot found tc command.");
        //ethtool
        if(System("ethtool") != 1)
            ERROR(Exception::Server::SystemCheckFault, "Cannot found ethtool command.");
        //iptables
        if(System("ls /etc/init.d/iptables") != 0)
            ERROR(Exception::Server::SystemCheckFault, "Cannot found iptables script.");
        if(System("iptables") != 2)
            ERROR(Exception::Server::SystemCheckFault, "Cannot found iptables command.");
        //ipvsadm
        if(System("ipvsadm -") != 255)
            ERROR(Exception::Server::SystemCheckFault, "Cannot found ipvsadm command.");
        //ldirectord
        if(System("ldirectord -v") != 0)
            ERROR(Exception::Server::SystemCheckFault, "Cannot found ldirectord.");
        //heartbeat
        if(System("ls /etc/init.d/heartbeat") != 0)
            WARNING(Exception::Server::SystemCheckFault, "Cannot found heartbeat script.");
        //named
        if(System("ls /etc/init.d/named") != 0)
            WARNING(Exception::Server::SystemCheckFault, "Cannot found named script.");
        if(System(". /etc/sysconfig/named;echo $ROOTDIR|grep \"/\"") == 0)
            ERROR(Exception::Server::SystemCheckFault, "Do not use chroot in named.");
        //syslog
        if(System("ls /etc/init.d/rsyslog") != 0)
            WARNING(Exception::Server::SystemCheckFault, "Cannot found rsyslog script.");
        //IMQ
        if(System("cat /lib/modules/`uname -r`/modules.dep|grep imq.ko") != 0)
            WARNING(Exception::Server::SystemCheckFault, "Cannot found imq module.");
        if(System("cat /lib/modules/`uname -r`/modules.dep|grep IMQ.ko") != 0)
            WARNING(Exception::Server::SystemCheckFault, "Cannot found imq target module.");
        //SmartRouter
        if(System("cat /lib/modules/`uname -r`/modules.dep|grep ipt_MKDEV.ko") != 0)
            WARNING(Exception::Server::SystemCheckFault, "Cannot found ipt_MKDEV module.");
        if(System("klbroute") != 0)
            WARNING(Exception::Server::SystemCheckFault, "Cannot found klbroute.");
        //iptables state
        if(System("iptables -m state --state ESTABLISHED_ORIG -j ACCEPT -A NOTEXIST") != 1)
            WARNING(Exception::Server::SystemCheckFault, "Cannot found ESTABLISHED_ORIG match state.");
        if(System("iptables -m state --state ESTABLISHED_REPLY -j ACCEPT -A NOTEXIST") != 1)
            WARNING(Exception::Server::SystemCheckFault, "Cannot found ESTABLISHED_REPLY match state.");
        LOGGER_INFO("System check done.");
#undef WARNING
    }

    DECLARE_INIT(SystemCheck, NULL, -0xFFFFFF);
};
