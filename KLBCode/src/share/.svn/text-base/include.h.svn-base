#ifndef __INCLUDED_HEADER__
#define __INCLUDED_HEADER__

#define RPC_PORT 8888
#define INNER_RPC_PORT 0x07F9A8ED

#include <string>

#ifndef STRINGTYPE
#define STRINGTYPE
typedef std::string String;
#endif

#ifndef __RELEASE__

#ifndef __DEBUG__
#define __DEBUG__
#endif

#ifdef NDEBUG
#undef NDEBUG
#endif

#include <stdlib.h>
#include <assert.h>
#include "debug.h"

#define DEBUG_PRINT(type,param...)\
    do{\
        ::DebugOutput LINE_NAME(debugprint)(type,__FILE__,__LINE__);\
        LINE_NAME(debugprint)<<param;\
    }while(0)
#define DEBUG_HEX(param) (::DebugOutput::OutputHex(param))
#define DEBUG_CONF_PATH(path) do{::DebugOutput::SetPath(path);}while(0)
#define DEBUG_UPDATE_CONF() do{::DebugOutput::UpdateConf();}while(0)
#define DEBUG_TYPE(type) (::DebugOutput::IsShowType(type))

#define PRINTF(param...) DEBUG_PRINT("Any",param)
#define PRINTSTRINGHEX(str) PRINTF(DEBUG_HEX(str))

#define ASSERT(exp...) assert(exp)
#define VERIFY(exp...)\
    do\
    {\
        bool LINE_NAME(result)=(bool)(exp);\
        ASSERT(LINE_NAME(result));\
    }while(0)

#else

#ifdef __DEBUG__
#undef __DEBUG__
#endif

#ifndef NDEBUG
#define NDEBUG
#endif

#define DEBUG_PRINT(type,param...) do{}while(0)
#define DEBUG_HEX(param) (param)
#define DEBUG_CONF_PATH(path) do{}while(0)
#define DEBUG_UPDATE_CONF() do{}while(0)
#define DEBUG_TYPE(type) (0)

#define PRINTF(param...) do{}while(0)
#define PRINTSTRINGHEX(str) do{}while(0)

#define ASSERT(exp...) do{}while(0)
#define VERIFY(exp...) do{exp;}while(0)

#endif

#define LINE_NAME_DO_REAL(left,right) left##right
#define LINE_NAME_DO(left,right) LINE_NAME_DO_REAL(left,right)
#define LINE_NAME(name) LINE_NAME_DO(__##name##__,__LINE__)

//this is safe to remove item from list,set,map;DO NOT from vector deque
#define ENUM_STL_CONST(stltype,stlvar,iteratorvar)\
    for(\
            stltype::const_iterator LINE_NAME(next)=(stlvar).begin(),\
            iteratorvar=(LINE_NAME(next)==(stlvar).end())?LINE_NAME(next):LINE_NAME(next)++;\
            iteratorvar!=(stlvar).end();\
            iteratorvar=(LINE_NAME(next)==(stlvar).end())?LINE_NAME(next):LINE_NAME(next)++\
       )

//this is safe to remove item from list,set,map;DO NOT from vector deque
#define ENUM_STL(stltype,stlvar,iteratorvar)\
    for(\
            stltype::iterator LINE_NAME(next)=(stlvar).begin(),\
            iteratorvar=(LINE_NAME(next)==(stlvar).end())?LINE_NAME(next):LINE_NAME(next)++;\
            iteratorvar!=(stlvar).end();\
            iteratorvar=(LINE_NAME(next)==(stlvar).end())?LINE_NAME(next):LINE_NAME(next)++\
       )

//this is safe to remove item from list,set,map;DO NOT from vector deque
#define ENUM_STL_CONST_R(stltype,stlvar,iteratorvar)\
    for(\
            stltype::const_reverse_iterator LINE_NAME(next)=(stlvar).rbegin(),\
            iteratorvar=(LINE_NAME(next)==(stlvar).rend())?LINE_NAME(next):LINE_NAME(next)++;\
            iteratorvar!=(stlvar).rend();\
            iteratorvar=(LINE_NAME(next)==(stlvar).rend())?LINE_NAME(next):LINE_NAME(next)++\
       )

//this is safe to remove item from list,set,map;DO NOT from vector deque
#define ENUM_STL_R(stltype,stlvar,iteratorvar)\
    for(\
            stltype::reverse_iterator LINE_NAME(next)=(stlvar).rbegin(),\
            iteratorvar=(LINE_NAME(next)==(stlvar).rend())?LINE_NAME(next):LINE_NAME(next)++;\
            iteratorvar!=(stlvar).rend();\
            iteratorvar=(LINE_NAME(next)==(stlvar).rend())?LINE_NAME(next):LINE_NAME(next)++\
       )

#define ENUM_VALUES(index,values)\
    ENUM_STL(::ValueArray,(values).getArray(),index)

#define ENUM_LIST(elementtype,list,index)\
    ENUM_STL(::List< elementtype >,(list),index)

#define ENUM_VALUES_R(index,values)\
    ENUM_STL_R(::ValueArray,(values).getArray(),index)

#define ENUM_LIST_R(elementtype,list,index)\
    ENUM_STL_R(::List< elementtype >,(list),index)

#define DECLARE_RPC_METHOD(name,func,auth,outer)\
    static ::RpcMethod LINE_NAME(rpcmethod)(name,func,auth,outer);

#define DECLARE_INIT(init,uninit,priority)\
    static ::InitModel LINE_NAME(initmodel)(init,uninit,priority);

#define DECLARE_SERIALIZE(before,import,after,export,priority)\
    static ::SerializeModel LINE_NAME(serializemodel)(before,import,after,export,priority);

#define DECLARE_INTERFACE_REFRESH(refresh,priority)\
    static ::InterfaceRefresher LINE_NAME(interfacerefresh)(refresh,priority);

#define DECLARE_ISP_REFRESH(refresh,priority)\
    static ::ISPRefresher LINE_NAME(isprefresh)(refresh,priority);

#ifdef __DEBUG__
#define DECLARE_COMMAND(func)\
    static ::Command LINE_NAME(command)(func,#func);
#else
#define DECLARE_COMMAND(func)\
    static ::Command LINE_NAME(command)(func);
#endif

#define ERROR(code,expr...)\
    do\
    {\
        ::std::ostringstream LINE_NAME(error);\
        LINE_NAME(error)<<expr;\
        throw ::ValueException(LINE_NAME(error).str(),(code));\
    }while(0)

#define NO_ERROR(expr...)\
    do\
    {\
        try\
        {\
            expr;\
        }catch(::ValueException& e)\
        {\
        }\
    }while(0)

#define LOGGER(priority,expr...)\
    do\
    {\
        ::std::ostringstream LINE_NAME(logger);\
        LINE_NAME(logger)<<expr;\
        ::Logger::Write(priority,LINE_NAME(logger).str());\
        DEBUG_PRINT("Logger",LINE_NAME(logger).str());\
    }while(0)

#define LOGGER_INFO(expr...) LOGGER(::Logger::Info,expr)
#define LOGGER_ERROR(expr...) LOGGER(::Logger::Error,expr)
#define LOGGER_NOTICE(expr...) LOGGER(::Logger::Notice,expr)
#define LOGGER_WARNING(expr...) LOGGER(::Logger::Warning,expr)

#endif // __INCLUDED_HEADER__
