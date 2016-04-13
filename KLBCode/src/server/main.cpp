#include <sys/types.h>
#include <sys/stat.h>
#include <sys/file.h>
#include <fcntl.h>
#include <unistd.h>
#include <iostream>

#include "share/utility.h"

#include "share/version.h"

#include "rpc.h"
#include "base.h"

#include "adsl.h"
#include "ha.h"

#ifdef __DEBUG__
#include "model/types.h"
#include <sstream>
#include <fstream>
#include <set>
#include "cipher/cipher.h"
#include "model/ipvs.h"
#include "isp.h"
#include "bind.h"
#include "licence/licence.h"
#endif

using namespace std;

void Version()
{
    cout << "Version " << VERSION_MAJOR << '.' << VERSION_MINOR << '.' << VERSION_REVISION << '.' << VERSION_BUILD << endl;
}

void RunServer(bool isdaemon)
{
    int fd = open("/tmp/klbmanager.lock", O_CREAT, 0600);
    if(flock(fd, LOCK_EX | LOCK_NB) == 0)
    {
        if(isdaemon)
            daemon(0, 0);
        DEBUG_CONF_PATH("/etc/klbmanager.debug.conf");
        RpcServer::IncCounter();
        RpcServer::Run();
        RpcServer::DecCounter();
        flock(fd, LOCK_UN);
    } else
        cout << "Already running." << endl;
    close(fd);
}

void Help()
{
    cout << "Usage: klbmanager -d   Run." << endl
         << "       klbmanager      Run with foregound." << endl
#ifdef __DEBUG__
         << "       klbmanager -r   Register klbmanager." << endl
#endif
         << "       klbmanager -v   Show version." << endl;
}

int main(int argc, char* argv[])
{
#ifdef __DEBUG__
    ostringstream stream;
    for(int i = 2; i < argc; ++i)
        stream << argv[i] << " ";
    PRINTF("klbmanager call........ " << stream.str());
#endif
    switch(argc)
    {
        case 1:
            RunServer(false);
            break;
        case 2://-d  -v
            {
                String op(argv[1]);
                if(op == "-d")
                    RunServer(true);
#ifdef __DEBUG__
                else if(op == "-r")
                {
                    LicenceManager::LicenceData data;
                    LicenceManager::Clear(data);
                    data.Valid = true;
                    String machine;
                    LicenceManager::GetMachine(machine);
                    String code;
                    LicenceManager::Generate(data, machine, code);
                    VERIFY(LicenceManager::Import(code));
                }
#endif
                else if(op == "-v")
                    Version();
                else
                    Help();
            }
            break;
        case 4:
            {
                String op(argv[1]);
                if(op == "-h")
                {
                    PRINTF(".......................................HA");
                    HAControl::StatusChanged(String(argv[2]), String(argv[3]) == "start");
                } else
                    Help();
            }
            break;
        case 7:
            {
                String op(argv[1]);
                if(op == "-i")
                {
                    PRINTF(".......................................Adsl");
                    AdslControl::StatusChanged(String(argv[2]), String(argv[3]), String(argv[4]), String(argv[5]), String(argv[6]) == "OK");
                } else
                    Help();
            }
            break;
        default:
            Help();
    }
    return 0;
}


