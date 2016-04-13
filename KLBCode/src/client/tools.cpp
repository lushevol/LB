#include <iostream>
#include <sstream>

#include "tools.h"

using namespace std;

namespace ParseTools
{
    void ShowAny(const String& str, int value)
    {
        if(value == 0)
            cout << "any";
        else if(str == "")
            cout << value;
        else
            cout << str;
    }

    void SplitAddressRange(const String& range, String& start, String& end)
    {
        String::size_type pos = range.find('-');
        if(pos != String::npos)
        {
            start = range.substr(0, pos);
            end = range.substr(pos + 1, range.size() - pos - 1);
        } else {
            start = range;
            end = "";
        }
    }

    void SplitPortRange(const String& range, int& start, int& end)
    {
        String a, b;
        SplitAddressRange(range, a, b);
        {
            istringstream stream(a);
            if(!(stream >> start))
                ERROR(Exception::Command::IntValue, a);
        }
        {
            if(b != "")
            {
                istringstream stream(b);
                if(!(stream >> end))
                    ERROR(Exception::Command::IntValue, b);
            } else
                end = 0;
        }
    }

};
