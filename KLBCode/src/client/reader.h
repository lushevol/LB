#ifndef __READER_H__
#define __READER_H__

#include "model/model.h"

class Reader
{
    private:
        static void SetDisplayMode(bool enable);
        static char* CompletionPath(const char* text, int state);
        static void Display(char** matches, int len, int max);
        static void BindKey();
        static void UnbindKey();
    public:
        static void Init();
        static void ReadPassword(String& password);
        static void ReadNoTab(const char* prompt, String& result);
        static void Read(const char* prompt, String& result);
        static void ReadCommand(String& result);
};

#endif // __READER_H__
