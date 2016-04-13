#include <iostream>
#include <vector>
#include <sstream>
#include <signal.h>
#include <string.h>
#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>

#include "share/include.h"

#include "libstatic.h"
#include "libarg.h"

using namespace std;

static int current_isp_num = 0;
static uint32_t Default = 0;

void HandleShutdown(int sig)
{
        EndRoute();
        UninitRoute();
        kill(getpid(), SIGKILL);
}

int main(int argc, char* argv[])
{
    vector<uint32_t> vec_mark;
    vector<int> vec_mode;
    vector<String> vec_ip;
    vector<int> vec_port;
    vector<int> vec_frequency;
    vector<int> vec_timeout;
    if(argc == 1)
        return 0;
    signal(SIGTERM, HandleShutdown);
    signal(SIGQUIT, HandleShutdown);
    signal(SIGINT, HandleShutdown);
    try
    {
        ArgControl control(argc, argv);
        do
        {
            if(control.MatchOp("static"))
            {
                uint32_t mark;
                control.MustMatchValue(mark);
                String path;
                control.MustMatchValue(path);
                LoadISP(path.c_str(), mark);
                vec_mark.push_back(mark);
                ++current_isp_num;
            } else if(control.MatchOp("check"))
            {
                uint32_t mode;
                control.MustMatchValue(mode);
                vec_mode.push_back(mode);
                String ip;
                control.MustMatchValue(ip);
                vec_ip.push_back(ip);
                uint32_t port;
                control.MustMatchValue(port);
                vec_port.push_back(port);
                uint32_t frequency;
                control.MustMatchValue(frequency);
                vec_frequency.push_back(frequency);
                uint32_t timeout;
                control.MustMatchValue(timeout);
                vec_timeout.push_back(timeout);
            } else if(control.MatchOp("default"))
            {
                control.MustMatchValue(Default);
            } else if(!control.IsEnd())
            {
                control.Error();
            } else
                break;
        } while(true);
#ifdef __DEBUG__
        ShowISP();
#endif
        if(!InitRoute())
            throw String("Init route failed.");

        if(!SetIspMem())
        {
            UninitRoute();
            throw String("Set isp mem failed.");
        }

        if(!StartRoute(current_isp_num))
        {
            UninitRoute();
            throw String("Start route failed.");
        }

	char cmd[64];
	vector<int> mark_state(current_isp_num, 0);
	int count = 0;
        int wait = 0;
        int max_f = vec_frequency[0];
        int max_t = vec_timeout[0];
        for(int i=0; i<current_isp_num-1; ++i)
        {
		if(vec_frequency[i]<vec_frequency[i+1])
			max_f = vec_frequency[i+1];
		if(vec_timeout[i]<vec_timeout[i+1])
			max_t = vec_timeout[i+1];
        }
	wait = max_f - max_t;
	if(wait < 1)
		wait = 1;
	if(max_t < 1)
		max_t = 60;
        while(1)
        {
            for(int i=0; i<current_isp_num; ++i)
		{
			memset(cmd, 0 , 64);
			if(vec_mode[i] == 0)
                        {
				continue;//type disable, don't check  gate.
				sprintf(cmd, "ping_gate 0x%x", vec_mark[i]);
                        }
			else if(vec_mode[i] == 1)
                        {
                                ChangeCheck(inet_addr(vec_ip[i].c_str()), vec_mark[i]);
				sprintf(cmd, "ping %s -c 1 -W %d", vec_ip[i].c_str(), vec_timeout[i]);
                        }
			else if(vec_mode[i] == 2)
                        {
                                ChangeCheck(inet_addr(vec_ip[i].c_str()), vec_mark[i]);
				sprintf(cmd, "/usr/lib/nagios/plugins/check_tcp -H %s -p %d -W %d", vec_ip[i].c_str(), vec_port[i], vec_timeout[i]);
                        }
			else if(vec_mode[i] == 3)
                        {
                                ChangeCheck(inet_addr(vec_ip[i].c_str()), vec_mark[i]);
				sprintf(cmd, "/usr/lib/nagios/plugins/check_udp -H %s -p %d -W %d", vec_ip[i].c_str(), vec_port[i], vec_timeout[i]);
                        }
			else
				break;
			int ret = system(cmd);
			if(ret)
				ret = system(cmd);

			if(ret)
			{
				if(!mark_state[i])
				{
					if(!InsertMark(vec_mark[i], INVALID))
					{
					    UninitRoute();
						throw String("setsockopt error.\n");
					}
					mark_state[i] = 1;
					++count;
				}
			}
			else
			{
				if(mark_state[i])
				{
					if(!InsertMark(vec_mark[i], VALID))
					{
					    UninitRoute();
						throw String("setsockopt error.\n");
					}
					mark_state[i] = 0;
					--count;
				}
			}

		}
		if(count)
			sleep(wait);
		else
			sleep(max_t);
        }

        EndRoute();
        UninitRoute();
        return 0;
    } catch(String& error)
    {
        cout << error << endl;
        return -1;
    }
}
