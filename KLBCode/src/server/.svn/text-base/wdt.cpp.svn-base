#include <unistd.h>
#include <fcntl.h>
#include <sys/ioctl.h>
#include <linux/watchdog.h>

#include "share/utility.h"
#include "base.h"

#include "wdt.h"

namespace WatchDog
{
#if (defined __RELEASE__ || 0)
    static const char* const DogPath = "/dev/watchdog";
    static int Timeout = 60;
    static int Dog = -1;

    void Feed()
    {
        if(Dog != -1)
        {
            ioctl(Dog, WDIOC_KEEPALIVE, 0);
            DEBUG_PRINT("Watchdog", "Feed dog.....................................");
        }
    }

    void SetTimeout(int timeout)
    {
        if(Dog != -1)
            ioctl(Dog, WDIOC_SETTIMEOUT, &timeout);
    }

    void InitWatchdog()
    {
        struct stat buf;
        if(stat(DogPath, &buf) != 0)
        {
            if(Exec::System("modprobe softdog") != 0)
                return;
        }
        do
        {
            sched_yield();
            Dog = open(DogPath, O_RDWR);
        } while(Dog == -1);
        SetTimeout(Timeout);
        Feed();
    }

    void UninitWatchdog()
    {
        if(Dog != -1)
            close(Dog);
    }

    DECLARE_INIT(InitWatchdog, UninitWatchdog, 0xFFFFFF);

#else
    void Feed()
    {}

    void SetTimeout(int timeout)
    {}

#endif

};

