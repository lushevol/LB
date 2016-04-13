#ifndef UTILITY_H
#define UTILITY_H

#include <stdio.h>
#include <stdlib.h>
#include <vector>
#include <sstream>
#include <string>
#include <string.h>

#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <pthread.h>

#ifndef STRINGTYPE
#define STRINGTYPE
typedef std::string String;
#endif

#ifndef PROINTERTYPE
#define PROINTERTYPE
typedef void* Pointer;
#endif

#ifdef __DEBUG__
#include "include.h"
#endif

namespace std
{
    class fdoutbuf : public streambuf
    {
        public:
            fdoutbuf(int _fd): fd(_fd) {};
        protected:
            int fd;
            virtual int_type overflow(int_type c)
            {
                if(c != EOF)
                {
                    char z = c;
                    if(write(fd, &z, 1) != 1)
                        return EOF;
                }
                return c;
            };
            virtual streamsize xsputn(const char* s, streamsize num)
            {
                return write(fd, s, num);
            };
    };

    class fdostream : public ostream
    {
        public:
            fdostream(int fd) : ostream(0), buf(fd)
            {
                rdbuf(&buf);
            };
        protected:
            fdoutbuf buf;
    };

    class fdinbuf : public streambuf
    {
        public:
            fdinbuf(int _fd) : fd(_fd)
            {
                setg(buffer + pbSize,
                     buffer + pbSize,
                     buffer + pbSize);
            };
        protected:
            static const int pbSize = 4;
            static const int bufSize = 1024;
            char buffer[bufSize+pbSize];
            int fd;
            virtual int_type underflow()
            {
                if(gptr() < egptr())
                    return traits_type::to_int_type(*gptr());
                int numPutback;
                numPutback = gptr() - eback();
                if(numPutback > pbSize)
                    numPutback = pbSize;
                memmove(buffer + (pbSize - numPutback),
                        gptr() - numPutback,
                        numPutback);
                int num;
                num = read(fd, buffer + pbSize, bufSize);
                if(num <= 0)
                    return EOF;
                setg(buffer + (pbSize - numPutback),
                     buffer + pbSize,
                     buffer + pbSize + num);
                return traits_type::to_int_type(*gptr());
            };
    };

    class fdistream : public istream
    {
        public:
            fdistream(int fd) : istream(0), buf(fd)
            {
                rdbuf(&buf);
            };
        protected:
            fdinbuf buf;
    };
}

class Exec
{
    private:
        static const int ClosedFD = -1;
        String FApp;
        typedef std::vector<String> Args;
        Args FArgs;
        int FIn;
        int FOut;
        pid_t FPID;
        void Init(const String& app)
        {
            FIn = ClosedFD;
            FOut = ClosedFD;
            FPID = 0;
            FApp = app;
        };
        static void* ReadProcess(void* data)
        {
            int fd = *(int*)data;
            char buf[65536];
            while(read(fd, &buf, sizeof(buf)) > 0)
            {
            }
            pthread_exit(0);
        }
    public:
        Exec(const String& app)
        {
            Init(app);
        }
        Exec(const char* const app)
        {
            if(app)
                Init(String(app));
        }
        Exec() {}
        ~Exec()
        {
            Close();
        }
        void SetApp(const String& app)
        {
            FApp = app;
        }
        void SetApp(const char* const app)
        {
            if(app)
                SetApp(String(app));
        }
        void Clear()
        {
            Close();
            FArgs.clear();
        }
        bool Close(int& status)
        {
            bool res;
            pthread_t tid;
            if(FPID != 0)
            {
                pthread_create(&tid, NULL, &ReadProcess, &FIn);
                waitpid(FPID, &status, 0);
                FPID = 0;
                res = true;
            } else
                res = false;
            if(FIn != ClosedFD)
            {
                close(FIn);
                FIn = ClosedFD;
            }
            if(FOut != ClosedFD)
            {
                close(FOut);
                FOut = ClosedFD;
            }
            if(res)
            {
                void* temp;
                pthread_cancel(tid);
                pthread_join(tid, &temp);
            }
            return res;
        }
        bool Close()
        {
            int status;
            return Close(status);
        }
        void AddArg(const char* const arg)
        {
            if(arg)
                AddArg(String(arg));
        }
        void AddArg(const String& arg)
        {
            FArgs.push_back(arg);
        }
        void Execute(bool back = false)
        {
            Close();
            if(FApp.empty())
                return;
            int read_pipes[2], write_pipes[2];
            if(pipe(read_pipes))
                return;
            if(pipe(write_pipes))
                goto clean_read;
            FPID = fork();
            if(FPID == -1)
                goto clean_write;
            if(FPID == 0)
            {
                close(2);
                close(1);
                close(0);
                dup2(read_pipes[1], 2);
                dup2(read_pipes[1], 1);
                close(read_pipes[0]);
                dup2(write_pipes[0], 0);
                close(write_pipes[1]);
                for(long i = 3; i < sysconf(_SC_OPEN_MAX); i++)
                    if(i != write_pipes[0] && i != read_pipes[1])
                        close(i);
                char* arg[FArgs.size()+2];
                arg[0] = (char*)FApp.c_str();
                for(Args::size_type i = 0; i != FArgs.size(); ++i)
                    arg[i+1] = (char*)FArgs[i].c_str();
                arg[FArgs.size()+1] = NULL;
                if(back)
                    daemon(0, 0);
                execvp(FApp.c_str(), arg);
                exit(EXIT_FAILURE);
            } else {
                FIn = read_pipes[0];
                close(read_pipes[1]);
                FOut = write_pipes[1];
                close(write_pipes[0]);
#ifdef __DEBUG__
                std::ostringstream stream;
                stream << "Execute(" << FIn << "," << FOut << "):  " << FApp;
                for(Args::size_type i = 0; i != FArgs.size(); ++i)
                    stream << ' ' << FArgs[i];
                DEBUG_PRINT("Execute", stream.str());
#endif
            }
            return;
clean_write:
            close(write_pipes[0]);
            close(write_pipes[1]);
clean_read:
            close(read_pipes[0]);
            close(read_pipes[1]);
        }
        int WriteIn()
        {
            return FOut;
        }
        int ReadOut()
        {
            return FIn;
        }
        Exec& operator<<(const String& value)
        {
            AddArg(value);
            return *this;
        }
        Exec& operator<<(const char* const value)
        {
            AddArg(value);
            return *this;
        }
        Exec& operator<<(const int value)
        {
            std::ostringstream stream;
            stream << value;
            AddArg(stream.str());
            return *this;
        }
        Exec& operator<<(const unsigned int value)
        {
            std::ostringstream stream;
            stream << value;
            AddArg(stream.str());
            return *this;
        }
        static int System(const String& cmd)
        {
#ifdef __DEBUG__
            DEBUG_PRINT("Execute", "Execute: " << cmd);
#endif
            int pid = fork();
            if(pid == -1)
                return -1;
            if(pid == 0)
            {
#ifdef __DEBUG__
                for(long i = 3; i < sysconf(_SC_OPEN_MAX); i++)
                    close(i);
#else
                for(long i = 1; i < sysconf(_SC_OPEN_MAX); i++)
                    close(i);
                int fd = open("/dev/null", O_WRONLY);
                dup2(fd, 2);
                dup2(fd, 1);
#endif
                close(0);
                int status = system(cmd.c_str());
                exit(WEXITSTATUS(status));
#ifndef __DEBUG__
                close(fd);
#endif
                return 0;
            } else {
                int status = 0;
                waitpid(pid, &status, 0);
                return status;
            }
        }
};

inline String RandomString()
{
    std::ostringstream stream;
    for(int i = 0; i < 16; ++i)
    {
        unsigned char ch = random();
        unsigned char high = (0x40 | (ch & 0x0F)) + 1;
        unsigned char low = (0x40 | ((ch >> 4) & 0x0F)) + 1;
        stream << high << low;
    }
    return stream.str();
}

inline String IntToString(int value)
{
    std::ostringstream stream;
    stream << value;
    return stream.str();
}

#endif

