#ifndef __SYSTEM_H__
#define __SYSTEM_H__

#include "share/utility.h"

#include "model/model.h"
#include "model/system.h"

class MailControl
{
	public:
		MailItem Mail;
	public:
		MailControl();
		void Get(MailItem& item);
		void Set(MailItem& item);
	private:
		void Refresh();
};

class TimeControl
{
    public:
        void Set(TimeItem& time);
        void Get(TimeItem& time);
};

class PasswordControl
{
    public:
        void Set(CommonStringValue& password);
};

class CoreStatusControl
{
    private:
        static double FUsage;
        static unsigned long int FThread;
        static void ReadStat(unsigned int& idel, unsigned& total);
        static Pointer ReadStatThread(Pointer data);
    public:
        void Get(CoreStatusItem& item);
        static void Init();
        static void Uninit();
};

class MemoryStatusControl
{
    public:
        void Get(MemoryStatusItem& item);
};

class DiskStatusControl
{
    public:
        void Get(DiskStatusItem& item);
};

class UpdaterControl
{
    private:
        static const char* FPath;
        static const char* FTempFile;
    public:
        void Reset();
        void Upload(BinaryValue& data);
        void Update();
};

class LicenceControl
{
    public:
        void IsExist(BoolValue& exist);
        void GetMachine(NormalStringValue& code);
        void Import(NormalStringValue& code);
};

class SystemControl
{
    public:
	void Reboot();
	void Shutdown();
};

#endif // __SYSTEM_H__
