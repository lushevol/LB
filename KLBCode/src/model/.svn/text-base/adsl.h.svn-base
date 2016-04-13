#ifndef __ADSL_H_MODEL__
#define __ADSL_H_MODEL__

#include "model.h"
#include "types.h"
#include "interface.h"

class AdslName: public SpecialString
{
    public:
        virtual bool Get(const String& value, String& result)
        {
            if(value.empty())
            {
                result.clear();
                return true;
            }
            if(value.size() < 4)
                return false;
            if(strncmp(value.c_str(), "ppp", 3) != 0)
                return false;
            for(String::size_type i = 3; i < value.size(); ++i)
                if(!isdigit(value[i]))
                    return false;
            result = value;
            return true;
        }
        virtual void Default(String& value)
        {
            value = "";
        }
};

typedef SpecialStringValue<AdslName> AdslNameValue;

class AdslItem: public Model
{
    public:
        typedef RangedIntValue<68, 1492, 1492> MTUValue;
        class DialState: public State
        {
            public:
                static const int Stop = 0;
                static const int Dial = 1;
                static const int Connected = 2;
                static const int Invalid = 3;
                virtual const char* Get(int state) const
                {
                    switch(state)
                    {
                        case Stop:
                        case Dial:
                        case Connected:
                        case Invalid:
                            return "";
                        default:
                            return NULL;
                    };
                }
                virtual int Default() const
                {
                    return Stop;
                }
        };
        typedef StateValue<DialState> DialStateValue;
    public:
        UnsignedValue ID;
        CommonStringValue Description;
        AdslNameValue Dev;
        NormalStringValue Ethernet;
        NormalStringValue User;
        NormalStringValue Password;
        UnsignedValue Timeout;
        HostStringValue IP;
        HostStringValue Dns;
        HostStringValue Gate;
        MTUValue MTU;
        DialStateValue Status;
        UnsignedValue RX;
        UnsignedValue TX;
        IntValue Time;
    public:
        AdslItem(Value& adsl)
            : Model(adsl)
            , ID(adsl["ID"])
            , Description(adsl["Description"])
            , Dev(adsl["Dev"])
            , Ethernet(adsl["Ethernet"])
            , User(adsl["User"])
            , Password(adsl["Password"])
            , Timeout(adsl["Timeout"])
            , IP(adsl["IP"])
            , Dns(adsl["Dns"])
            , Gate(adsl["Gate"])
            , MTU(adsl["MTU"])
            , Status(adsl["Status"])
            , RX(adsl["RX"])
            , TX(adsl["TX"])
            , Time(adsl["Time"])
        {}
};

static const char* const FuncAdslAdd            = "Adsl.Add";
static const char* const FuncAdslDel            = "Adsl.Delete";
static const char* const FuncAdslSet            = "Adsl.Set";
static const char* const FuncAdslDial           = "Adsl.Dial";
static const char* const FuncAdslStop           = "Adsl.Stop";
static const char* const FuncAdslGet            = "Adsl.Get";
static const char* const FuncAdslGetAll         = "Adsl.GetAll";
static const char* const FuncAdslStatusChanged  = "Adsl.Status.Changed";
static const char* const FuncAdslDescription    = "Adsl.Description";

#endif // __ADSL_H_MODEL__
