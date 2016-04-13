#include <sstream>
#include <string.h>

#include "libarg.h"

using namespace std;

ArgControl::ArgControl(int argc, char** argv)
    : FArgc(argc - 1)
    , FArgv(argv + 1)
{}

bool ArgControl::MatchOp(const char* op)
{
    if(IsEnd())
        return false;
    if(strcmp(op, *FArgv) != 0)
        return false;
    --FArgc;
    ++FArgv;
    return true;
}

bool ArgControl::MatchValue(String& value)
{
    if(IsEnd())
        return false;
    value = *FArgv;
    --FArgc;
    ++FArgv;
    return true;
}

bool ArgControl::MatchValue(uint32_t& value)
{
    if(IsEnd())
        return false;
    const char* temp = *FArgv;
    while(*temp)
    {
        if(!isdigit(*temp))
            return false;
        ++temp;
    }
    istringstream stream(*FArgv);
    if(!(stream >> value))
        return false;
    --FArgc;
    ++FArgv;
    return true;
}

void ArgControl::MustMatchOp(const char* op)
{
    if(!MatchOp(op))
        Error();
}

void ArgControl::MustMatchValue(String& value)
{
    if(!MatchValue(value))
        Error();
}

void ArgControl::MustMatchValue(uint32_t& value)
{
    if(!MatchValue(value))
        Error();
}

bool ArgControl::IsEnd()
{
    return FArgc == 0;
}

void ArgControl::Error()
{
    throw String("cmd params invalid.");
}
