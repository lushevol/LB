#ifndef __BASE_H__
#define __BASE_H__

#include "model/model.h"

class Configure
{
    private:
        String FProcessPath;
        String FWorkPath;
        String FPassword;
        String FProcessName;
        Value FValue;
        bool FShutdown;
        int FRef;
        Configure();
        ~Configure();
        void InitParams();
        static void Encrypt(const String& data, String& res);
    public:
        static void IncCounter();
        static void DecCounter();
        static const char* GetConfPath();
        static const char* GetAuthPath();
        static void SetPassword(const String& password);
        static bool ComparePassword(const String& password);
        static void SetShutdown();
        static const bool GetShutdown();
        static const char* GetProcessPath();
        static const char* GetProcessName();
        static const char* GetWorkPath();
        static Value& GetValue();
};

typedef Configure* PConfigure;

class InitModel
{
    public:
        typedef void (*Func)();
    private:
        int FPriority;
        Func FInit;
        Func FUninit;
    public:
        static void Init();
        static void Uninit();
        InitModel(Func init, Func uninit, int priority);
        ~InitModel();
};

#endif // __BASE_H__
