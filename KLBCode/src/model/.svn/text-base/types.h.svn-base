#ifndef __TYPES_H__
#define __TYPES_H__

#include <stdint.h>

#include <netinet/in.h>

#include "model.h"

class Address
{
    public:
        static bool StringToAddress(const String& str, uint32_t& addr);
        static bool AddressToString(uint32_t addr, String& str);
        static bool AddressToNetmask(uint32_t addr, int& netmask);
        static bool IsNetmask(int netmask);
        static bool NetmaskToAddress(int netmask, uint32_t& addr);
        static bool StringToAddressPack(const String& str, uint32_t& addr, int& netmask);
        static bool AddressPackToString(uint32_t addr, int netmask, String& str);
};

typedef RangedIntValue<0, 255> ProtocolValue;
typedef RangedIntValue<0, 65535> PortValue;
typedef RangedIntValue<1, 65535> RealPortValue;
typedef RangedIntValue<0, 32> NetmaskValue;

class BaseAddressString: public SpecialString
{
    private:
        String FInput;
        String FOutput;
    protected:
        virtual bool DoGet(const String& value, String& result) = 0;
    public:
        virtual bool Get(const String& value, String& result);
        virtual void Default(String& value);
};

class AddressString: public BaseAddressString
{
    private:
        virtual bool DoGet(const String& value, String& result);
    protected:
        virtual bool Check(uint32_t addr) = 0;
};

class HostString: public AddressString
{
    protected:
        virtual bool Check(uint32_t addr);
};

typedef SpecialStringValue<HostString> HostStringValue;

class NetmaskString: public AddressString
{
    protected:
        virtual bool Check(uint32_t addr);
};

typedef SpecialStringValue<NetmaskString> NetmaskStringValue;

class MulticastString: public AddressString
{
    protected:
        virtual bool Check(uint32_t addr);
};

typedef SpecialStringValue<MulticastString> MulticastStringValue;

class AddressPackString: public BaseAddressString
{
    private:
        virtual bool DoGet(const String& value, String& result);
    protected:
        virtual bool CheckAddress(uint32_t addr, int netmask) = 0;
};

class HostPackString: public AddressPackString
{
    protected:
        virtual bool CheckAddress(uint32_t addr, int netmask);
};

typedef SpecialStringValue<HostPackString> HostPackStringValue;

class NetPackString: public AddressPackString
{
    protected:
        virtual bool CheckAddress(uint32_t addr, int netmask);
};

typedef SpecialStringValue<NetPackString> NetPackStringValue;

class PhysicalAddress
{
    public:
        uint8_t Data[6];
    public:
        static bool StringToAddress(const String& mac, PhysicalAddress& address);
        static void AddressToString(const PhysicalAddress& address, String& mac);
        bool IsEmpty() const;
        void Clear();
};

class PhysicalAddressString: public BaseAddressString
{
    protected:
        virtual bool DoGet(const String& value, String& result);
};

typedef SpecialStringValue<PhysicalAddressString> PhysicalAddressStringValue;

class EmptySpecialString: public SpecialString
{
    public:
        virtual void Default(String& value);
};

class HostnameString: public EmptySpecialString
{
    public:
        virtual bool Get(const String& value, String& result);
};

typedef SpecialStringValue<HostnameString> HostnameStringValue;

class NormalString: public EmptySpecialString
{
    public:
        virtual bool Get(const String& value, String& result);
};

typedef SpecialStringValue<NormalString> NormalStringValue;

class DomainString: public EmptySpecialString
{
    public:
        virtual bool Get(const String& value, String& result);
};

typedef SpecialStringValue<DomainString> DomainStringValue;

class CommonString: public EmptySpecialString
{
    public:
        virtual bool Get(const String& value, String& result);
};

typedef SpecialStringValue<CommonString> CommonStringValue;

class AlnumString: public EmptySpecialString
{
    public:
        virtual bool Get(const String& value, String& result);
};

typedef SpecialStringValue<AlnumString> AlnumStringValue;

class DomainGroupString: public DomainString
{
    public:
        virtual bool Get(const String& value, String& result);
};

typedef SpecialStringValue<DomainGroupString> DomainGroupStringValue;

#endif // __TYPES_H__
