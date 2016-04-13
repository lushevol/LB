#ifndef __SYSTEM_H_MODEL__
#define __SYSTEM_H_MODEL__

#include "model.h"
#include "types.h"

static const char* const FuncCheckServer = "Server.Check";

static const char* const FuncPasswordSet = "Password.Set";

static const char* const FuncConfigureImport    = "Configure.Import";
static const char* const FuncConfigureExport    = "Configure.Export";
static const char* const FuncConfigureSave      = "Configure.Save";
static const char* const FuncConfigureReload    = "Configure.Reload";

class MailItem: public Model
{
	public:
        BoolValue Enabled;
	CommonStringValue Address;
	CommonStringValue User;
	CommonStringValue Passwd;
	DomainStringValue Smtp;
	public:
	MailItem(Value& mail)
		: Model(mail)
		, Enabled(mail["Enabled"])
		, Address(mail["Address"])
		, User(mail["User"])
		, Passwd(mail["Passwd"])
		, Smtp(mail["Smtp"])
		{}
};

static const char* const FuncMailGet = "Mail.Get";
static const char* const FuncMailSet = "Mail.Set";

class LoggerItem: public Model
{
    public:
        BoolValue Enabled;
        DomainStringValue Domain;
    public:
        LoggerItem(Value& logger)
            : Model(logger)
            , Enabled(logger["Enabled"])
            , Domain(logger["Domain"])
        {}
};

static const char* const FuncLoggerGet = "Logger.Get";
static const char* const FuncLoggerSet = "Logger.Set";

class TimeItem: public Model
{
    public:
        typedef RangedIntValue < -14, 12 > ZoneValue;
    public:
        IntValue Tick;
        ZoneValue Zone;
    public:
        TimeItem(Value& time)
            : Model(time)
            , Tick(time["Tick"])
            , Zone(time["Zone"])
        {}
};

static const char* const FuncTimeGet = "Time.Get";
static const char* const FuncTimeSet = "Time.Set";

class CoreStatusItem: public Model
{
    public:
        List<NormalStringValue> Core;
        NormalStringValue Usage;
    public:
        CoreStatusItem(Value& cpu)
            : Model(cpu)
            , Core(cpu["Core"])
            , Usage(cpu["Usage"])
        {}
};

static const char* const FuncStatusCore = "Status.Core";

class MemoryStatusItem: public Model
{
    public:
        UnsignedValue Total;
        UnsignedValue Free;
    public:
        MemoryStatusItem(Value& memory)
            : Model(memory)
            , Total(memory["Total"])
            , Free(memory["Free"])
        {}
};

static const char* const FuncStatusMemory = "Status.Memory";

class DiskStatusItem: public Model
{
    public:
        UnsignedValue Total;
        UnsignedValue Free;
        UnsignedValue Used;
    public:
        DiskStatusItem(Value& disk)
            : Model(disk)
            , Total(disk["Total"])
            , Free(disk["Free"])
            , Used(disk["Used"])
        {}
};

static const char* const FuncStatusDisk = "Status.Disk";

static const char* const FuncUpdaterReset   = "Updater.Reset";
static const char* const FuncUpdaterUpload  = "Updater.Upload";
static const char* const FuncUpdaterStart   = "Updater.Start";

static const char* const FuncLicenceIsExist     = "Licence.IsExist";
static const char* const FuncLicenceGetMachine  = "Licence.Machine";
static const char* const FuncLicenceImport      = "Licence.Import";

static const char* const FuncSystemReboot   = "System.Reboot";
static const char* const FuncSystemShutdown   = "System.Shutdown";
#endif // __SYSTEM_H_MODEL__
