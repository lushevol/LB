#ifndef __DEBUG_H__
#define __DEBUG_H__

#ifdef __DEBUG__

#include <iostream>
#include <string>

class DebugOutput
{
    private:
        bool FShow;
    public:
        DebugOutput(const char* type, const char* file, int line);
        ~DebugOutput();
        DebugOutput& operator<<(const char* data);
        DebugOutput& operator<<(const std::string& data);
        DebugOutput& operator<<(const int data);
        static std::string OutputHex(const char* data);
        static std::string OutputHex(const std::string& data);
        static void UpdateConf();
        static void SetPath(const char* path);
        static bool IsShowType(const char* type);
};

#endif

#endif // __DEBUG_H__
