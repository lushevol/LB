#ifndef __EXCEPTION_CODE_H__
#define __EXCEPTION_CODE_H__

namespace Exception
{
#ifdef __CLIENT__
    namespace Command
    {
        static const int NotMatch                               = 200;
        static const int Connect                                = 201;
        static const int Op                                     = 202;
        static const int IntValue                               = 203;
        static const int PasswordAgain                          = 204;
        static const int TimeFormat                             = 205;
        static const int ZoneFormat                             = 206;
        static const int DelIP                                  = 207;
        static const int ISPName                                = 208;
    }
#endif
    namespace Server
    {
        static const int Password                               = 500;
        static const int Params                                 = 501;
        static const int Version                                = 502;
        static const int Licence                                = 503;
        static const int SystemCheckFault                       = 504;
        static const int ImportLicence                          = 505;
    }
    namespace Types
    {
        static const int RangedInt                              = 1000;
        static const int RangedState                            = 1001;
        static const int SpecialString                          = 1002;
        static const int RangedList                             = 1003;
        namespace GetList
        {
            static const int Start                              = 1004;
            static const int Count                              = 1005;
        }
        static const int Binary                                 = 1006;
    }
    namespace System
    {
        static const int ImportConfigure                        = 2000;
        static const int SaveConfigure                          = 2001;
        static const int Time                                   = 2002;
    }
    namespace Interface
    {
        static const int NotFoundDev                            = 3000;
        static const int ExistDev                               = 3001;
        static const int MTU                                    = 3002;
        static const int Enabled                                = 3003;
        static const int Disabled                               = 3004;
        static const int Address                                = 3005;
        namespace IP
        {
            static const int Flush                              = 3100;
            static const int Add                                = 3101;
            static const int Dhcp                               = 3102;
            static const int Duplicate                          = 3103;
        }
        namespace Ethernet
        {
            static const int DetectOn                           = 3201;
            static const int DetectOff                          = 3202;
            static const int DuplexSpeed                        = 3203;
            static const int Slave                              = 3204;
            static const int Adsl                               = 3205;
        }
        namespace Bonding
        {
            static const int Add                                = 3301;
            static const int Del                                = 3302;
            namespace Slave
            {
                static const int Add                            = 3303;
                static const int Del                            = 3304;
                static const int Empty                          = 3305;
            }
            namespace CheckIP
            {
                static const int Add                            = 3306;
                static const int Del                            = 3307;
            }
            namespace Mode
            {
                static const int Set                            = 3308;
                static const int Dot                            = 3309;
                static const int ALB                            = 3310;
                static const int TLB                            = 3311;
            }
            static const int LinkCheck                          = 3312;
        }
        namespace Adsl
        {
            static const int Used                               = 3401;
            static const int Ethernet                           = 3402;
            static const int Busy                               = 3403;
            static const int User                               = 3404;
            static const int Password                           = 3405;
            static const int Dial                               = 3406;
            static const int Connected                          = 3407;
            static const int Stopped                            = 3408;
        }
    }
    namespace Updater
    {
        static const int Upload                                 = 4000;
        static const int Reset                                  = 4001;
        static const int Failed                                 = 4002;
    }
    namespace Route
    {
        namespace Gate
        {
            static const int DetectDev                          = 5000;
            static const int EmptyDev                           = 5001;
        }
        namespace Static
        {
            static const int Empty                              = 5100;
            static const int Duplicate                          = 5101;
            static const int NotFound                           = 5102;
        }
        namespace Policy
        {
            static const int Count                              = 5200;
        }
        namespace Smart
        {
            static const int Duplicate                          = 5300;
            static const int Count                              = 5301;
        }
    }
    namespace Nat
    {
        namespace Map
        {
            static const int IP                                 = 6000;
            static const int Port                               = 6001;
        }
    }
    namespace Network
    {
        static const int Hostname                               = 7000;
        static const int DnsServer                              = 7001;
        static const int Protocol                               = 7002;
        static const int SupportPort                            = 7003;
        static const int Service                                = 7004;
    }
    namespace IPVS
    {
        namespace Service
        {
            static const int Address                            = 8000;
            namespace IP
            {
                static const int Empty                          = 8001;
                static const int Duplicate                      = 8002;
            }
            namespace Name
            {
                static const int Duplicate                      = 8003;
                static const int Invalid                        = 8004;
                static const int NotFound                       = 8005;
                static const int Empty                          = 8006;
            }
            static const int DuplicateMark                      = 8007;
            static const int Count                              = 8008;
            static const int Ports                              = 8009;
        }
        namespace Server
        {
            namespace Name
            {
                static const int Duplicate                      = 8100;
                static const int Invalid                        = 8101;
                static const int NotFound                       = 8102;
                static const int Empty                          = 8103;
            }
            namespace IP
            {
                static const int Duplicate                      = 8104;
                static const int Empty                          = 8105;
            }
            static const int DuplicateID                        = 8106;
        }
        namespace HA
        {
            static const int Running                            = 8200;
            static const int Time                               = 8201;
            namespace Dev
            {
                static const int Empty                          = 8202;
                static const int Status                         = 8203;
            }
            static const int IP                                 = 8204;
            static const int Hostname                           = 8205;
        }
        namespace Statistic
        {
            static const int Range                              = 8300;
            static const int Interval                           = 8301;
            static const int ID                                 = 8302;
            static const int IP                                 = 8303;
        }
    }
    namespace Arp
    {
        static const int Del                                    = 9000;
        static const int Set                                    = 9001;
    }
    namespace Bind
    {
        namespace Domain
        {
            static const int Empty                              = 10000;
            static const int NotFound                           = 10001;
            static const int Duplicate                          = 10002;
        }
        namespace Alias
        {
            static const int Empty                              = 10100;
            static const int Duplicate                          = 10101;
        }
        namespace Server
        {
            static const int Empty                              = 10200;
            static const int ISP                                = 10201;
        }
    }
    namespace ISP
    {
        static const int NotFoundID                             = 11000;
        static const int Count                                  = 11001;
        namespace Name
        {
            static const int Empty                              = 11002;
            static const int Duplicate                          = 11003;
        }
        static const int EmptyNet                               = 11004;
    }
}

#endif // __EXCEPTION_CODE_H__
