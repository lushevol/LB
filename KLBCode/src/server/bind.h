#ifndef __BIND_H__
#define __BIND_H__

#include "model/bind.h"

class BindControl
{
    public:
        typedef BindItem::ARecord ARecord;
        typedef ARecord::ServerItem ServerItem;
    private:
        void WriteZoneConfigure(int ispid);
        void WriteReverseZoneConfigure();
        void WriteConfigure();
    private:
        void EnsureARecordID(ARecord& item);
        static void CheckDomain(StringCollection& existdomain, ARecord& item);
        void RefreshConfigure();
        static void GetISP(IntCollection& collection);
        void GetDomainCollection(StringCollection& domains, const IntCollection& except);
        void GetDomainCollection(StringCollection& domains, int except);
        void GetDomainCollection(StringCollection& domains);
        static void CopyARecord(ARecord& target, ARecord& recent);
    public:
        BindItem Bind;
        BindControl();
        //Bind
        void SetBind(BindItem& item);
        void SetEnabled(BindItem& item);
        //ARecord
        void AddARecord(ARecord& item);
        void SetARecord(ARecord& item);
        void DelARecord(ARecord& item);
        //Get
        void GetBind(BindItem& item);
        void GetAll(BindItem& item);
        void GetARecord(ARecord& item);
        void GetIDByName(List<ARecord>& list);
        //Static
        static void ISPRefresh();
        static void InterfaceRefresh(const StringCollection& dev);
};

#endif // __BIND_H__
