#include "XmlRpcUtil.h"

#ifndef MAKEDEPEND
# include <ctype.h>
# include <iostream>
# include <stdarg.h>
# include <stdio.h>
# include <string.h>
# include <sstream>
#endif

#include "XmlRpc.h"

using namespace XmlRpc;


//#define USE_WINDOWS_DEBUG // To make the error and log messages go to VC++ debug output
#ifdef USE_WINDOWS_DEBUG
#define WIN32_LEAN_AND_MEAN
#include <windows.h>
#endif

// Version id
const char XmlRpc::XMLRPC_VERSION[] = "XMLRPC++ 0.7";

// Default log verbosity: 0 for no messages through 5 (writes everything)
int XmlRpcLogHandler::_verbosity = 0;

// Default log handler
static class DefaultLogHandler : public XmlRpcLogHandler {
    public:

        void log(int level, const char* msg) {
#ifndef __RELEASE__

#ifdef USE_WINDOWS_DEBUG
            if(level <= _verbosity) {
                OutputDebugString(msg);
                OutputDebugString("\n");
            }
#else
            if(level <= _verbosity) std::cout << msg << std::endl;
#endif

#endif
        }

} defaultLogHandler;

// Message log singleton
XmlRpcLogHandler* XmlRpcLogHandler::_logHandler = &defaultLogHandler;


// Default error handler
static class DefaultErrorHandler : public XmlRpcErrorHandler {
    public:

        void error(const char* msg) {
#ifndef __RELEASE__

#ifdef USE_WINDOWS_DEBUG
            OutputDebugString(msg);
            OutputDebugString("\n");
#else
            std::cerr << msg << std::endl;
#endif

#endif
        }
} defaultErrorHandler;


// Error handler singleton
XmlRpcErrorHandler* XmlRpcErrorHandler::_errorHandler = &defaultErrorHandler;


// Easy API for log verbosity
int XmlRpc::getVerbosity() {
    return XmlRpcLogHandler::getVerbosity();
}
void XmlRpc::setVerbosity(int level) {
    XmlRpcLogHandler::setVerbosity(level);
}



void XmlRpcUtil::log(int level, const char* fmt, ...)
{
    if(level <= XmlRpcLogHandler::getVerbosity())
    {
        va_list va;
        char buf[1024];
        va_start(va, fmt);
        vsnprintf(buf, sizeof(buf) - 1, fmt, va);
        buf[sizeof(buf)-1] = 0;
        XmlRpcLogHandler::getLogHandler()->log(level, buf);
    }
}


void XmlRpcUtil::error(const char* fmt, ...)
{
    va_list va;
    va_start(va, fmt);
    char buf[1024];
    vsnprintf(buf, sizeof(buf) - 1, fmt, va);
    buf[sizeof(buf)-1] = 0;
    XmlRpcErrorHandler::getErrorHandler()->error(buf);
}


// Replace xml-encoded entities with the raw text equivalents.

std::string
XmlRpcUtil::xmlDecode(const std::string& encoded)
{
    std::ostringstream res;
    for(std::string::size_type i = 0; i < encoded.size(); ++i)
    {
        if(encoded[i] == '&')
        {
            std::string::size_type pos = encoded.find(';', i);
            if(pos != std::string::npos)
            {
                if(encoded[i+1] == '#')
                {
                    std::string data = encoded.substr(i + 2, pos - i - 2);
                    if(data.empty())
                        break;
                    int ch;
                    if(data[0] == 'x')
                    {
                        data = '0' + data;
                        sscanf(data.c_str(), "%x", &ch);
                    } else {
                        sscanf(data.c_str(), "%d", &ch);
                    }
                    res << (char)ch;
                } else {
                    std::string name = encoded.substr(i + 1, pos - i - 1);
                    if(name == "lt")
                        res << '<';
                    else if(name == "gt")
                        res << '>';
                    else if(name == "amp")
                        res << '&';
                    else if(name == "quot")
                        res << '\"';
                    else if(name == "apos")
                        res << '\'';
                    else
                        break;
                }
                i = pos;
            } else
                break;
        } else
            res << encoded[i];
    }
    return res.str();
}


// Replace raw text with xml-encoded entities.

std::string
XmlRpcUtil::xmlEncode(const std::string& raw)
{
    std::ostringstream stream;
    for(std::string::size_type i = 0; i < raw.size(); ++i)
    {
        const unsigned char& ch = raw[i];
        if(ch < 32)
        {
            char buf[3];
            sprintf(buf, "%.2X", (unsigned int)ch);
            stream << "&#x" << buf << ";";
        } else {
            switch(ch)
            {
                case '<':
                    stream << "&lt;";
                    break;
                case '>':
                    stream << "&gt;";
                    break;
                case '&':
                    stream << "&amp;";
                    break;
                case '\"':
                    stream << "&quot;";
                    break;
                case '\'':
                    stream << "&apos;";
                    break;
                default:
                    stream << ch;
            }
        }
    }
    return stream.str();
}



