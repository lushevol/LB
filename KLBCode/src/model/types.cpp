#include <stdio.h>
#include <string.h>
#include <sstream>

#include <arpa/inet.h>

#include "share/include.h"
#include "types.h"

static const String::size_type MaxLength = 2048;

using namespace std;

bool BaseAddressString::Get(const String& value, String& result)
{
    if(FInput == value)
    {
        result = FOutput;
        return true;
    }
    bool check = value.empty();
    if(!check)
        check = DoGet(value, result);
    else
        result = "";
    if(check)
    {
        FInput = value;
        FOutput = result;
    }
    return check;
}

void BaseAddressString::Default(String& value)
{
    value = "";
}

bool Address::StringToAddress(const String& str, uint32_t& addr)
{
    return inet_pton(AF_INET, str.c_str(), &addr) > 0;
}

bool Address::AddressToString(uint32_t addr, String& str)
{
    char buf[16];
    if(inet_ntop(AF_INET, &addr, buf, sizeof(buf)) == 0)
        return false;
    str = buf;
    return true;
}

bool Address::AddressToNetmask(uint32_t addr, int& netmask)
{
    int mask = 0;
    addr = htonl(addr);
    while(addr & 0x80000000)
    {
        ++mask;
        addr <<= 1;
    }
    if(addr)
        return false;
    netmask = mask;
    return true;
}

bool Address::IsNetmask(int netmask)
{
    return netmask >= 0 && netmask <= 32;
}

bool Address::NetmaskToAddress(int netmask, uint32_t& addr)
{
    if(!IsNetmask(netmask))
        return false;
    addr = 0;
    while(netmask--)
    {
        addr >>= 1;
        addr |= 0x80000000;
    }
    addr = htonl(addr);
    return true;
}

bool Address::StringToAddressPack(const String& str, uint32_t& addr, int& netmask)
{
    uint32_t address;
    int mask;
    String::size_type pos = str.find('/');
    if(pos == String::npos)
    {
        if(!StringToAddress(str, address))
            return false;
        mask = 32;
    } else {
        if(!StringToAddress(str.substr(0, pos), address))
            return false;
        String maskstr = str.substr(pos + 1, str.size() - pos - 1);
        ENUM_STL_CONST(String, maskstr, e)
        {
            if(!isdigit(*e))
                return false;
        }
        istringstream maskstream(maskstr);
        if(!(maskstream >> mask))
            return false;
    }
    addr = address;
    netmask = mask;
    return true;
}

bool Address::AddressPackToString(uint32_t addr, int netmask, String& str)
{
    ostringstream stream;
    String net;
    if(!AddressToString(addr, net))
        return false;
    if(!IsNetmask(netmask))
        return false;
    stream << net << '/' << netmask;
    str = stream.str();
    return true;
}

bool AddressString::DoGet(const String& value, String& result)
{
    uint32_t addr;
    if(!Address::StringToAddress(value, addr))
        return false;
    if(!Check(addr))
        return false;
    if(!Address::AddressToString(addr, result))
        return false;
    return true;
}

bool HostString::Check(uint32_t addr)
{
    return addr != 0 && htonl(addr) < 0xE0000000;
}

bool NetmaskString::Check(uint32_t addr)
{
    int netmask;
    return Address::AddressToNetmask(addr, netmask);
}

bool MulticastString::Check(uint32_t addr)
{
    return htonl(addr) >= 0xE0000000 && htonl(addr) < 0xF0000000;
}

bool AddressPackString::DoGet(const String& value, String& result)
{
    uint32_t addr;
    int netmask;
    if(!Address::StringToAddressPack((value == "default") ? "0.0.0.0/0" : value, addr, netmask))
        return false;
    if(!CheckAddress(addr, netmask))
        return false;
    if(!Address::AddressPackToString(addr, netmask, result))
        return false;
    return true;
}

bool HostPackString::CheckAddress(uint32_t addr, int netmask)
{
    if(addr == 0 || htonl(addr) >= 0xE0000000)
        return false;
    uint32_t mask;
    if(!Address::NetmaskToAddress(netmask, mask))
        return false;
    if(~mask != 0 && ((addr & ~mask) == 0 || (addr & ~mask) == ~mask))
        return false;
    return true;
}

bool NetPackString::CheckAddress(uint32_t addr, int netmask)
{
    if(htonl(addr) >= 0xE0000000)
        return false;
    uint32_t mask;
    if(!Address::NetmaskToAddress(netmask, mask))
        return false;
    if((addr & ~mask) != 0)
        return false;
    return true;
}

bool PhysicalAddress::StringToAddress(const String& mac, PhysicalAddress& address)
{
    static const unsigned int MAC_LEN = 6 * 3 - 1;
    if(mac.size() != MAC_LEN)
        return false;
    for(String::size_type i = 2; i < MAC_LEN; i += 3)
        if(mac[i] != ':')
            return false;
    for(String::size_type i = 0; i < MAC_LEN; i += 3)
    {
        if(!isxdigit(mac[i]) || !isxdigit(mac[i + 1]))
            return false;
    }
    uint8_t data[6];
    for(String::size_type pos = 0; pos < MAC_LEN; pos += 3)
    {
        unsigned int res;
        if(!sscanf(&mac[pos], "%2X", &res))
            return false;
        data[pos/3] = res;
    }
    memcpy(address.Data, data, sizeof(uint8_t) * 6);
    return true;
}

void PhysicalAddress::AddressToString(const PhysicalAddress& address, String& mac)
{
    ostringstream stream;
    char buf[3];
    for(int i = 0; i < 6; ++i)
    {
        sprintf(buf, "%.2X", address.Data[i]);
        if(i != 0)
            stream << ":";
        stream << buf;
    }
    mac = stream.str();
}

bool PhysicalAddress::IsEmpty() const
{
    for(int i = 0; i < 6; ++i)
        if(Data[i])
            return false;
    return true;
}

void PhysicalAddress::Clear()
{
    memset(Data, 0, sizeof(uint8_t) * 6);
}

bool PhysicalAddressString::DoGet(const String& value, String& result)
{
    PhysicalAddress address;
    if(!PhysicalAddress::StringToAddress(value, address))
        return false;
    PhysicalAddress::AddressToString(address, result);
    return true;
}

void EmptySpecialString::Default(String& value)
{
    value = "";
}

bool HostnameString::Get(const String& value, String& result)
{
    if(value.size() > MaxLength)
        return false;
    ENUM_STL_CONST(String, value, e)
    {
        if(!isalnum(*e) && *e != '-' && *e != '.')
            return false;
    }
    result = value;
    return true;
}

bool NormalString::Get(const String& value, String& result)
{
    if(value.size() > MaxLength)
        return false;
    ENUM_STL_CONST(String, value, e)
    {
        if(!isascii(*e) || iscntrl(*e))
            return false;
    }
    result = value;
    return true;
}

bool DomainString::Get(const String& value, String& result)
{
    if(value.size() > MaxLength)
        return false;
    enum
    {
        Start,
        Char,
        Point,
        Link
    } state = Start;
    ENUM_STL_CONST(String, value, e)
    {
        switch(state)
        {
            case Start:
                if(isalnum(*e))
                    state = Char;
                else
                    return false;
                break;
            case Char:
                if(!isalnum(*e))
                {
                    if(*e == '-')
                        state = Link;
                    else if(*e == '.')
                        state = Point;
                    else
                        return false;
                }
                break;
            case Point:
                if(isalnum(*e))
                    state = Char;
                else
                    return false;
                break;
            case Link:
                if(*e != '-')
                {
                    if(isalnum(*e))
                        state = Char;
                    else
                        return false;
                }
                break;
        }
    }
    if(state != Char && state != Start)
        return false;
    result = value;
    return true;
}

bool CommonString::Get(const String& value, String& result)
{
    if(value.size() > MaxLength)
        return false;
    ENUM_STL_CONST(String, value, e)
    {
        if(iscntrl(*e) && !isspace(*e))
            return false;
    }
    result = value;
    return true;
}

bool AlnumString::Get(const String& value, String& result)
{
    if(value.size() > MaxLength)
        return false;
    ENUM_STL_CONST(String, value, e)
    {
        if(!isalnum(*e))
            return false;
    }
    result = value;
    return true;
}

bool DomainGroupString::Get(const String& value, String& result)
{
    if(value.size() > MaxLength)
        return false;
    bool first = true;
    ostringstream res_stream;
    istringstream stream(value);
    String temp;
    while(stream >> temp)
    {
        String res;
        if(!DomainString::Get(temp, res))
            return false;
        if(!first)
            res_stream << " ";
        else
            first = false;
        res_stream << res;
    }
    result = res_stream.str();
    return true;
}
