#include <map>
#include <fstream>
#include <sstream>

#include <pthread.h>

#include "include.h"
#include "debug.h"

#ifdef __DEBUG__

using namespace std;

namespace Debug
{

    static const char* DebugConf = "/etc/debug.conf";

    pthread_mutex_t Mutex = PTHREAD_MUTEX_INITIALIZER;

    void inline Lock()
    {
        pthread_mutex_lock(&Mutex);
    }

    void inline Unlock()
    {
        pthread_mutex_unlock(&Mutex);
    }

    typedef map<String, bool> DebugMap;

    static DebugMap TypeList;

    static const char* const Location = "Location";

    static const char* const Date = "Date";

};

using namespace Debug;

DebugOutput::DebugOutput(const char* type, const char* file, int line)
{
    Lock();
    FShow = true;
    DebugMap::iterator pos = TypeList.find(type);
    if(pos == TypeList.end())
        TypeList[type] = FShow;
    else
        FShow = pos->second;
    if(FShow)
    {
        if(TypeList.count(Location) == 0)
            TypeList[Location] = true;
        if(TypeList.count(Date) == 0)
            TypeList[Date] = true;
        if(TypeList[Date])
        {
            time_t now;
            time(&now);
            struct tm data;
            localtime_r(&now, &data);
            cout << data.tm_mon + 1 << "-" << data.tm_mday << " " << data.tm_hour << ":" << data.tm_min << ":" << data.tm_sec << "  ";
        }
        if(TypeList[Location])
        {
            cout << file << " at " << line << "  ";
        }
        if(TypeList[Date] || TypeList[Location])
            cout << endl;
    }
    Unlock();
}

DebugOutput::~DebugOutput()
{
    if(FShow)
        cout << endl;
}

bool DebugOutput::IsShowType(const char* type)
{
    bool ret = true;
    Lock();
    DebugMap::iterator pos = TypeList.find(type);
    if(pos == TypeList.end())
        TypeList[type] = ret;
    else
        ret = pos->second;
    Unlock();
    return ret;
}

DebugOutput& DebugOutput::operator<<(const char* data)
{
    if(FShow)
        cout << data;
    return *this;
}

DebugOutput& DebugOutput::operator<<(const std::string& data)
{
    if(FShow)
        cout << data;
    return *this;
}

DebugOutput& DebugOutput::operator<<(const int data)
{
    if(FShow)
        cout << data;
    return *this;
}

std::string DebugOutput::OutputHex(const char* data)
{
    ostringstream stream;
    while(*data)
    {
        stream << hex << ((unsigned int)(*(data++)) & 0xFF) << " ";
    }
    return stream.str();
}

std::string DebugOutput::OutputHex(const std::string& data)
{
    return OutputHex(data.c_str());
}

void DebugOutput::SetPath(const char* path)
{
    Lock();
    DebugConf = path;
    Unlock();
}

void DebugOutput::UpdateConf()
{
    Lock();
    {
        ifstream stream(DebugConf);
        if(stream)
        {
            String type, value;
            while(stream >> type >> value)
                TypeList[type] = value == "on";
            stream.close();
            if(TypeList.count(Location) == 0)
                TypeList[Location] = true;
            if(TypeList.count(Date) == 0)
                TypeList[Date] = true;
        }
    }
    {
        ofstream stream(DebugConf);
        if(stream)
        {
            ENUM_STL(DebugMap, TypeList, e)
            {
                stream << e->first << " " << (e->second ? "on" : "off") << endl;
            }
        }
    }
    Unlock();
}

#endif
