#include <iostream>

#include "model/model.h"

#include "share/version.h"

#include "control.h"
#include "exception.h"
#include "reader.h"

using namespace std;
int main(int argc, char* argv[])
{
#ifdef __DEBUG__
    if(argc == 2 && strcmp(argv[1], "-f") == 0)
    {
        Exception::GetMessageList();
        return 0;
    }
#endif
    DEBUG_CONF_PATH("/etc/klbclient.debug.conf");
    cout << "  KLB Client  Version " << VERSION_MAJOR << '.' << VERSION_MINOR << '.' << VERSION_REVISION << '.' << VERSION_BUILD  << endl;
    Reader::Init();
    bool run = true;
    while(run)
    {
        DEBUG_UPDATE_CONF();
        try
        {
            Rpc::CheckServer();
            String res;
            Reader::ReadCommand(res);
            Command::Execute(res.c_str());
        } catch(ValueException& e)
        {
            switch(e.getCode())
            {
                case Exception::Server::Password:
                    {
                        cout << "password:";
                        String res;
                        Reader::ReadPassword(res);
                        Rpc::SetPassword(res);
                        cout << endl;
                        try
                        {
                            Rpc::CheckServer();
                        } catch(ValueException& e)
                        {
                            cout << Exception::GetMessage(e) << endl;
                        }
                    }
                    break;
                default:
                    cout << Exception::GetMessage(e) << endl;
                    run = false;
                    break;
            }
        }
    }
    return 0;
}
