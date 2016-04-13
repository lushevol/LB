#ifndef __LOGGER_H__
#define __LOGGER_H__

#include <sstream>

#include "model/types.h"
#include "model/system.h"

class Logger
{
    public:
        typedef enum
        {
            Warning,
            Error,
            Notice,
            Info
        } Priority;
    public:
        LoggerItem Item;
    private:
        void Refresh();
    public:
        static void Write(Priority priority, const String& info);
        Logger();
        void Get(LoggerItem& logger);
        void Set(LoggerItem& logger);
};

#endif // __LOGGER_H__
