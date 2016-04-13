#include <string.h>
#include <fstream>
#include <iostream>

#include "share/utility.h"
#include "model/system.h"

#include "rpc.h"
#include "base.h"
#include "serialize.h"

#include "system.h"

using namespace std;
void MailControl::Refresh()
{
	static const char *path = "/etc/mail.rc";
	StringList buffer;
	{
		ifstream stream(path);
		if(stream)
		{
			String line;
			while(getline(stream, line))
			{
				if(strncmp(line.c_str(), "set from", 8) == 0)
					break;
				buffer.push_back(line);
			}
			stream.close();
		}
	}

	{
		ofstream stream(path);
		if(stream)
		{
			while(!buffer.empty())
			{
				stream << buffer.front() << endl;
				buffer.pop_front();
			}
			if(Mail.Enabled)
			{
				stream << "set from=" << Mail.Address << " smtp=" << Mail.Smtp << endl;
				stream << "set smtp-auth-user=" << Mail.User << " smtp-auth-password=" << Mail.Passwd << " smtp-auth=login" << endl;
			}
			stream.close();
		}
	}		
}

MailControl::MailControl()
    : Mail(Configure::GetValue()["Mail"])
{
    if(!Mail.Enabled.Valid())
	Mail.Enabled = false;
}

void MailControl::Get(MailItem& item)
{
	item.Address = Mail.Address;
	item.User = Mail.User;
	item.Passwd = Mail.Passwd;
	item.Smtp = Mail.Smtp;
	item.Enabled = Mail.Enabled;
}

void MailControl::Set(MailItem& item)
{
	Mail.Enabled = item.Enabled.Valid() ? item.Enabled : Mail.Enabled;
	Mail.Address = item.Address.Valid() ? item.Address : Mail.Address;
	Mail.User = item.User.Valid() ? item.User : Mail.User;
	Mail.Passwd = item.Passwd.Valid() ? item.Passwd : Mail.Passwd;
	Mail.Smtp = item.Smtp.Valid() ? item.Smtp : Mail.Smtp;

	Refresh();
}

namespace mail
{
    void Get(Value& params, Value& result)
    {
        MailControl mail;
        MailItem res(result);
        mail.Get(res);
    }

    DECLARE_RPC_METHOD(FuncMailGet, Get, true, true);

    void Set(Value& params, Value& result)
    {
        MailControl mail;
        MailItem item(params);
        mail.Set(item);
        (bool&)result = true;
    }

    DECLARE_RPC_METHOD(FuncMailSet, Set, true, true);

    void Import(Value& data, bool reload)
    {
        MailItem item(data["Mail"]);
        MailControl mail;
        if(reload)
            NO_ERROR(mail.Set(item));
        else
            mail.Set(item);
    }

    void Export(Value& data)
    {
        MailItem item(data["Mail"]);
        MailControl mail;
        mail.Get(item);
    }

    DECLARE_SERIALIZE(NULL, Import, NULL, Export, 1);
}
