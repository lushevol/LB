#include <stdio.h>
#include <stdlib.h>
#include <fstream>
#include <sstream>
#include <vector>
#include <cstring>
#include <unistd.h>
#include <arpa/inet.h>
#include <sys/socket.h>
#include <linux/mkdev.h>
#include "share/include.h"

#include "libstatic.h"
using namespace std;

struct Item
{
    uint32_t Start;
    uint32_t End;
};

typedef vector<Item> ItemList;
typedef vector< vector<Item> > VItemList;

static ItemList Collection;
static VItemList VCollection;
static vector<uint32_t> vec_mark;
static int sockfd = 0;

static void AddRange(uint32_t start, uint32_t end)
{
    Item temp;
    temp.Start = start;
    temp.End = end;
    Collection.push_back(temp);
    int size = Collection.size();
    int index = size - 1;
    while(index > 0)
    {
        Item& left = Collection[index-1];
        Item& right = Collection[index];
        if(left.End < start)
        {
            break;
        } else if(left.Start > end)
        {
            temp = left;
            left = right;
            right = temp;
            --index;
        } else {
            left.Start = left.Start < start ? left.Start : start;
            left.End = left.End > end ? left.End : end;
            --index;
            --size;
        }
    }
    Collection.resize(size);
}

static bool get_ip_value(uint32_t&min, uint32_t &max, const String &ip_str)
{
	uint32_t ip;
	int mask;
	uint32_t net_mask = 0xFFFFFFFF;
	char str[50];
	char *comma;


	if(ip_str.size() >= 50)
		return false;

	strcpy(str, ip_str.c_str());
	if((comma = strchr(str, '-')) != NULL)
	{
		*comma = 0;
		ip = inet_addr(str);
		if(ip == INADDR_NONE)
			return false;
		ip = ntohl(ip);
		min = ip;
		ip = inet_addr(comma+1);
		if(ip == INADDR_NONE)
			return false;
		ip = ntohl(ip);
		max = ip;
	}
	else if((comma = strchr(str, '/')) != NULL)
	{
		mask = atoi(comma+1);
		if(mask <0 || mask >32)
			return false;
		*comma = 0;
		ip = inet_addr(str);
		if(ip == INADDR_NONE)
			return false;
		mask = 32 - mask;
		net_mask >>= mask;
		net_mask <<= mask;
		ip = ntohl(ip);
		ip &= net_mask;
		min = ip;
		ip |= ~net_mask;
		max = ip;

	}
	else
		return false;

	return true;
}

bool LoadISP(const char* path, uint32_t mark)
{
    ifstream stream(path);
    if(stream)
    {
        String line;
        uint32_t start = 0, end = 0;
        Collection.resize(0);
        VCollection.push_back(Collection);
	vec_mark.push_back(mark);
        while(getline(stream, line))
        {
            if(!get_ip_value(start, end, line))
                continue;
            AddRange(start, end);
        }
        VCollection[VCollection.size()-1] = Collection;
        stream.close();
        return true;
    } else
        return false;
}


#ifdef __DEBUG__

void ShowISP()
{
    ENUM_STL(ItemList, Collection, e)
    {
        cout << "From: " << e->Start << " To: " << e->End << endl;
    }
}

#endif






bool InitRoute()
{
    if(sockfd)
        return false;
    sockfd = socket(AF_INET, SOCK_RAW, IPPROTO_RAW);
    if(sockfd < 0)
        return false;
    return true;
}

bool UninitRoute()
{
    if(!sockfd)
        return false;
    close(sockfd);
    sockfd = 0;
    return true;
}

bool StartRoute(int isp_num)
{
    if(!sockfd)
        return false;
    if(setsockopt(sockfd, IPPROTO_IP, IP_MARK_SO_SET_START, &isp_num, sizeof(isp_num)))
        return false;
    return true;
}

bool EndRoute()
{
    if(!sockfd)
        return false;
    if(setsockopt(sockfd, IPPROTO_IP, IP_MARK_SO_SET_STOP, NULL, 0))
        return false;
    return true;
}

bool SetIspMem(void)
{
    if(!sockfd)
        return false;
    int isp_num = VCollection.size();
    int num = 0;

    while(num != isp_num)
    {
        Collection = VCollection[num];
        int size = Collection.size();
        int len = (size+1)*sizeof(struct Item);//include mem head store mark_value and num_mem

        uint32_t *data = new uint32_t[len];
	data[0] = vec_mark[num];
	data[1] = size * 2;
        for(int i=0, j =2; i!=size; ++i)
        {
            data[j++] = Collection[i].Start;
            data[j++] = Collection[i].End;
        }

        if(setsockopt(sockfd, IPPROTO_IP, IP_MARK_SO_SET_ISPMEM, data, len))
        {
            delete [] data;
            return false;
        }
        delete [] data;
	++num;
    }

    return true;
}

bool InsertMark(uint32_t mark, int flag)
{
    if(!sockfd)
        return false;
    if(flag == VALID)
    {
        if(setsockopt(sockfd, IPPROTO_IP, IP_MARK_SO_SET_VALID, &mark, sizeof(uint32_t)))
            return false;
    }
    else
    {
        if(setsockopt(sockfd, IPPROTO_IP, IP_MARK_SO_SET_INVALID, &mark, sizeof(uint32_t)))
            return false;
    }

    return true;
}

bool ChangeCheck(uint32_t ip, uint32_t mark)
{
    uint32_t msg[2];
    msg[0] = ip;
    msg[1] = mark;
    if(!sockfd)
        return false;
    if(setsockopt(sockfd, IPPROTO_IP, IP_MARK_SO_SET_CHECK, &msg, sizeof(uint32_t)*2))
        return false;
    return true;
}
