#ifdef __DEBUG__

#include <iostream>
#include <fstream>
#include <map>
#include <deque>

#include "model/ipvs.h"
#include "model/system.h"
#include "model/hvs.h"

#include "control.h"

using namespace std;

namespace DebugCommand
{
    void MatchDebug(Control& control)
    {
        control.MustMatchOp("debug");
    }

    void ShowTime(Control& control)
    {
        tzset();
        MatchDebug(control);
        control.MustMatchOp("time");
        int now = time(NULL);
        cout << "Format:   Second\tMinute\t\tHour\tDay" << endl;
        cout << "UTC:   " << now << "\t" << now / 60 << "\t" << now / 60 / 60 << "\t" << now / 60 / 60 / 24 << endl;
        cout << "Local: " << (now - timezone) << "\t" << (now - timezone) / 60 << "\t" << (now - timezone) / 60 / 60 << "\t" << (now - timezone) / 60 / 60 / 24 << endl;
    }

    DECLARE_COMMAND(ShowTime);

    void ShowStatistic(Control& control)
    {
        MatchDebug(control);
        control.MustMatchOp("statistic");
        CommandModel cmd;
        cmd.Password = true;
        if(control.MatchOp("service"))
        {
            cmd.FuncName = FuncStatisticService;
            int id;
            control.MustMatchValue(id);
            StatisticServiceItem(cmd.Params).ID = id;
        } else if(control.MatchOp("server"))
        {
            cmd.FuncName = FuncStatisticServer;
            String ip;
            control.MustMatchValue(ip);
            StatisticServerItem(cmd.Params).IP = ip;
        } else
            control.NotMatch();
        StatisticItem item(cmd.Params);
        while(true)
        {
            if(control.MatchOp("from"))
            {
                int temp;
                control.MustMatchValue(temp);
                item.From = temp;
            } else if(control.MatchOp("to"))
            {
                int temp;
                control.MustMatchValue(temp);
                item.To = temp;
            } else if(control.MatchOp("interval"))
            {
                int temp;
                control.MustMatchOpValue(temp);
                item.Interval = temp;
            } else
                break;
        }
        Value res;
        Rpc::Call(cmd, res);
        StringValue result(res);
        cout << result << endl;
    }

    DECLARE_COMMAND(ShowStatistic);

    void ConfigExport(Control& control)
    {
        MatchDebug(control);
        control.MustMatchOp("configure");
        control.MustMatchOp("export");
        CommandModel cmd;
        cmd.FuncName = FuncConfigureExport;
        cmd.Password = true;
        Value res;
        Rpc::Call(cmd, res);
        ofstream stream("/tmp/klb.configure.test");
        if(stream)
        {
            stream << res << endl;
            stream.close();
        }
        cout << "Export Done." << endl;
    }

    DECLARE_COMMAND(ConfigExport);

    void ConfigImport(Control& control)
    {
        MatchDebug(control);
        control.MustMatchOp("configure");
        control.MustMatchOp("import");
        CommandModel cmd;
        cmd.FuncName = FuncConfigureImport;
        cmd.Password = true;
        StringValue data(cmd.Params);
        ifstream stream("/tmp/klb.configure.test");
        if(stream)
        {
            ostringstream strstream;
            strstream << stream.rdbuf();
            data = strstream.str();
        }
        Rpc::CallNoResult(cmd);
    }

    DECLARE_COMMAND(ConfigImport);

    void CertKeyImport(Control& control)
    {
        MatchDebug(control);
        control.MustMatchOp("http");
        control.MustMatchOp("importCATest");
        int id;
        {
            CommandModel cmd;
            cmd.FuncName = FuncHttpServiceSet;
            cmd.Password = true;
            HttpItem::ServiceItem service(cmd.Params);
            control.MustMatchValue(id);
            service.ID = id;
            system("dd if=/dev/urandom of=/tmp/src.ca bs=1K count=1");
            system("dd if=/dev/urandom of=/tmp/src.key bs=1K count=1");
            {
                ifstream stream("/tmp/src.ca");
                if(stream)
                {
                    ostringstream strstream;
                    strstream << stream.rdbuf();
                    service.Cert = strstream.str();
                }
            }
            {
                ifstream stream("/tmp/src.key");
                if(stream)
                {
                    ostringstream strstream;
                    strstream << stream.rdbuf();
                    service.Key = strstream.str();
                }
            }
            Rpc::CallNoResult(cmd);
        }
        {
            CommandModel cmd;
            cmd.FuncName = FuncHttpServiceGet;
            cmd.Password = true;
            HttpItem::ServiceItem request(cmd.Params);
            request.ID = id;
            Value result;
            Rpc::Call(cmd, result);
            HttpItem::ServiceItem service(result);
            {
                String data;
                ofstream stream("/tmp/dst.ca");
                service.Cert.ToString(data);
                stream << data;
            }
            {
                String data;
                ofstream stream("/tmp/dst.key");
                service.Key.ToString(data);
                stream << data;
            }
        }
        {
            system("diff /tmp/src.key /tmp/dst.key");
            system("diff /tmp/src.ca /tmp/dst.ca");
        }
    }

    DECLARE_COMMAND(CertKeyImport);
}

#endif
