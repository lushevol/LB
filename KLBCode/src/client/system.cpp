#include <iostream>

#include <time.h>

#include "model/system.h"

#include "control.h"
#include "reader.h"

using namespace std;

namespace System
{
    void ConfigureSave(Control& control)
    {
        control.MustMatchOp("system");
        control.MustMatchOp("configure");
        control.MustMatchOp("save");
        CommandModel cmd;
        cmd.FuncName = FuncConfigureSave;
        cmd.Password = true;
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(ConfigureSave);

    void ConfigureReload(Control& control)
    {
        control.MustMatchOp("system");
        control.MustMatchOp("configure");
        control.MustMatchOp("reload");
        CommandModel cmd;
        cmd.FuncName = FuncConfigureReload;
        cmd.Password = true;
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(ConfigureReload);

    void StatusCore(Control& control)
    {
        control.MustMatchOp("system");
        control.MustMatchOp("status");
        control.MustMatchOp("cpu");
        CommandModel cmd;
        cmd.FuncName = FuncStatusCore;
        cmd.Password = true;
        Value res;
        Rpc::Call(cmd, res);
        CoreStatusItem item(res);
        int count = 0;
        ENUM_LIST(NormalStringValue, item.Core, e)
        {
            NormalStringValue hz(*e);
            cout << "Core " << ++count << ": " << hz << "MHz" << endl;
        }
        cout << "Usage: " << item.Usage << endl;
    }

    DECLARE_COMMAND(StatusCore);

    void StatusMemory(Control& control)
    {
        control.MustMatchOp("system");
        control.MustMatchOp("status");
        control.MustMatchOp("memory");
        CommandModel cmd;
        cmd.FuncName = FuncStatusMemory;
        cmd.Password = true;
        Value res;
        Rpc::Call(cmd, res);
        MemoryStatusItem item(res);
        cout << "Total: " << item.Total << "kB" << endl;
        cout << "Free:  " << item.Free << "kB" << endl;
    }

    DECLARE_COMMAND(StatusMemory);

    void StatusDisk(Control& control)
    {
        control.MustMatchOp("system");
        control.MustMatchOp("status");
        control.MustMatchOp("disk");
        CommandModel cmd;
        cmd.FuncName = FuncStatusDisk;
        cmd.Password = true;
        Value res;
        Rpc::Call(cmd, res);
        DiskStatusItem item(res);
        cout << "Total: " << item.Total << "MB" << endl;
        cout << "Free:  " << item.Free << "MB" << endl;
        cout << "Used:  " << item.Used << "MB" << endl;
    }

    DECLARE_COMMAND(StatusDisk);

    void GetTime(Control& control)
    {
        control.MustMatchOp("system");
        control.MustMatchOp("time");
        if(control.IsEnd() || control.MatchOp("get"))
        {
            CommandModel cmd;
            cmd.FuncName = FuncTimeGet;
            cmd.Password = true;
            Value res;
            Rpc::Call(cmd, res);
            TimeItem item(res);
            time_t timep = item.Tick + item.Zone * 3600;
            cout << "GMT " << item.Zone << " " << ctime(&timep);
        } else
            control.NotMatch();
    }

    DECLARE_COMMAND(GetTime);

    void SetTime(Control& control)
    {
        control.MustMatchOp("system");
        control.MustMatchOp("time");
        control.MustMatchOp("set");
        CommandModel cmd;
        cmd.FuncName = FuncTimeSet;
        cmd.Password = true;
        TimeItem item(cmd.Params);
        String temp;
        Reader::Read("year-month-day hour:minute:second : ", temp);
        if(!temp.empty())
        {
            tzset();
            struct tm now;
            if(strptime(temp.c_str(), "%Y-%m-%d %H:%M:%S", &now))
                item.Tick = mktime(&now) - timezone;
            else
                ERROR(Exception::Command::TimeFormat, "");
        } else
            ERROR(Exception::Command::TimeFormat, "");
        Reader::Read("GMT: ", temp);
        if(!temp.empty())
        {
            istringstream stream(temp.c_str());
            int zone;
            if(stream >> zone && zone >= -14 && zone <= 12)
                item.Zone = zone;
            else
                ERROR(Exception::Command::ZoneFormat, "");
        } else
            ERROR(Exception::Command::ZoneFormat, "");
        Rpc::CallNoResult(cmd);

    }

    DECLARE_COMMAND(SetTime);

    void SetPassword(Control& control)
    {
        control.MustMatchOp("system");
        control.MustMatchOp("password");
        if(control.IsEnd() || control.MatchOp("set"))
        {
            String password;
            cout << "new password:";
            Reader::ReadPassword(password);
            String again;
            cout << endl << "again:";
            Reader::ReadPassword(again);
            cout << endl;
            if(again != password)
                ERROR(Exception::Command::PasswordAgain, "");
            CommandModel cmd;
            cmd.FuncName = FuncPasswordSet;
            cmd.Password = true;
            CommonStringValue(cmd.Params) = Rpc::Encrypt(password.c_str());
            Rpc::CallNoResult(cmd);
            Rpc::SetPassword(password);
        } else
            control.NotMatch();
    }

    DECLARE_COMMAND(SetPassword);

    void GetMail(Control& control)
    {
        control.MustMatchOp("system");
        control.MustMatchOp("mail");
        if(control.IsEnd() || control.MatchOp("status"))
        {
            CommandModel cmd;
            cmd.FuncName = FuncMailGet;
            cmd.Password = true;
            Value result;
            Rpc::Call(cmd, result);
            MailItem item(result);
            cout << "Address: " << item.Address << endl; 
	    cout << "smtp: " << item.Smtp << endl;
            cout << "User: " << item.User  << endl;
	    cout << "Passwd: " << item.Passwd << endl;
            cout << "Status: " << (item.Enabled ? "Enabled" : "Disabled") << endl;
        } else
            control.NotMatch();
    }

    DECLARE_COMMAND(GetMail);

    void SetMail(Control& control)
    {
        control.MustMatchOp("system");
        control.MustMatchOp("mail");
        control.MustMatchOp("set");
        control.MustMatchOp("address");
        String address;
        control.MustMatchValue(address);
        control.MustMatchOp("smtp");
        String smtp;
        control.MustMatchValue(smtp);
        control.MustMatchOp("user");
        String user;
        control.MustMatchValue(user);
        control.MustMatchOp("passwd");
        String passwd;
        control.MustMatchValue(passwd);
        CommandModel cmd;
        cmd.FuncName = FuncMailSet;
        cmd.Password = true;
        MailItem item(cmd.Params);
        item.Address = address;
	item.Smtp = smtp;
	item.User = user;
	item.Passwd = passwd;
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(SetMail);

    void SetMailEnabled(Control& control)
    {
        control.MustMatchOp("system");
        control.MustMatchOp("mail");
        bool enabled = false;
        if(control.MatchOp("enable"))
            enabled = true;
        else if(control.MatchOp("disable"))
            enabled = false;
        else
            control.NotMatch();
        CommandModel cmd;
        cmd.FuncName = FuncMailSet;
        cmd.Password = true;
        MailItem item(cmd.Params);
        item.Enabled = enabled;
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(SetMailEnabled);

    void GetLogger(Control& control)
    {
        control.MustMatchOp("system");
        control.MustMatchOp("log");
        if(control.IsEnd() || control.MatchOp("status"))
        {
            CommandModel cmd;
            cmd.FuncName = FuncLoggerGet;
            cmd.Password = true;
            Value result;
            Rpc::Call(cmd, result);
            LoggerItem item(result);
            cout << "Domain: " << item.Domain << endl;
            cout << "Status: " << (item.Enabled ? "Enabled" : "Disabled") << endl;
        } else
            control.NotMatch();
    }

    DECLARE_COMMAND(GetLogger);

    void SetLoggerDomain(Control& control)
    {
        control.MustMatchOp("system");
        control.MustMatchOp("log");
        control.MustMatchOp("host");
        String temp;
        control.MustMatchValue(temp);
        CommandModel cmd;
        cmd.FuncName = FuncLoggerSet;
        cmd.Password = true;
        LoggerItem item(cmd.Params);
        item.Domain = temp;
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(SetLoggerDomain);

    void SetLoggerEnabled(Control& control)
    {
        control.MustMatchOp("system");
        control.MustMatchOp("log");
        bool enabled = false;
        if(control.MatchOp("enable"))
            enabled = true;
        else if(control.MatchOp("disable"))
            enabled = false;
        else
            control.NotMatch();
        CommandModel cmd;
        cmd.FuncName = FuncLoggerSet;
        cmd.Password = true;
        LoggerItem item(cmd.Params);
        item.Enabled = enabled;
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(SetLoggerEnabled);

    void LicenceIsExist(Control& control)
    {
        control.MustMatchOp("system");
        control.MustMatchOp("licence");
        if(!control.IsEnd())
            control.NotMatch();
        CommandModel cmd;
        cmd.FuncName = FuncLicenceIsExist;
        cmd.Password = true;
        Value result;
        Rpc::Call(cmd, result);
        if(BoolValue(result))
            cout << "Registered." << endl;
        else
            cout << "Not registered." << endl;
    }

    DECLARE_COMMAND(LicenceIsExist);

    void LicenceGetMachine(Control& control)
    {
        control.MustMatchOp("system");
        control.MustMatchOp("licence");
        control.MustMatchOp("machine");
        CommandModel cmd;
        cmd.FuncName = FuncLicenceGetMachine;
        cmd.Password = true;
        Value result;
        Rpc::Call(cmd, result);
        NormalStringValue info(result);
        cout << info << endl;
    }

    DECLARE_COMMAND(LicenceGetMachine);

    void LicenceRegister(Control& control)
    {
        control.MustMatchOp("system");
        control.MustMatchOp("licence");
        control.MustMatchOp("register");
        CommandModel cmd;
        cmd.FuncName = FuncLicenceImport;
        cmd.Password = true;
        String temp;
        control.MustMatchValue(temp);
        NormalStringValue(cmd.Params) = temp;
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(LicenceRegister);

    void SystemReboot(Control& control)
    {
        control.MustMatchOp("system");
        control.MustMatchOp("reboot");
        CommandModel cmd;
        cmd.FuncName = FuncSystemReboot;
        cmd.Password = true;
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(SystemReboot);

    void SystemShutdown(Control& control)
    {
        control.MustMatchOp("system");
        control.MustMatchOp("shutdown");
        CommandModel cmd;
        cmd.FuncName = FuncSystemShutdown;
        cmd.Password = true;
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(SystemShutdown);

};

