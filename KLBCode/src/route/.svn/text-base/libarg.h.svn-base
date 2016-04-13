#ifndef __LIBARG_H__
#define __LIBARG_H__

#include <stdint.h>

#include "share/include.h"

class ArgControl
{
    public:
        ArgControl(int argc, char** argv);
        bool MatchOp(const char* op);
        bool MatchValue(String& value);
        bool MatchValue(uint32_t& value);
        void MustMatchOp(const char* op);
        void MustMatchValue(String& value);
        void MustMatchValue(uint32_t& value);
        bool IsEnd();
        void Error();
    private:
        int FArgc;
        char** FArgv;
};

#endif // __LIBARG_H__
