#ifndef __PARSETOOLS_H__
#define __PARSETOOLS_H__

#include "model/model.h"

#include "control.h"

namespace ParseTools
{
    void ShowAny(const String& str, int value);
    void SplitAddressRange(const String& range, String& start, String& end);
    void SplitPortRange(const String& range, int& start, int& end);
};


#endif // __PARSETOOLS_H__
