#ifndef __EXCEPTION_MSG_H__
#define __EXCEPTION_MSG_H__

#include "model/model.h"

namespace Exception
{
    String GetMessage(ValueException& e);
#ifdef __DEBUG__
    void GetMessageList();
#endif
}

#endif // __EXCEPTION_MSG_H__
