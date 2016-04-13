#ifndef __LICENCE_H__
#define __LICENCE_H__

#include "share/include.h"

#ifdef __DEBUG__
void Translate(const String& key, void* result, size_t size);
void Translate(const void* data, size_t size, String& result);
#endif

namespace LicenceManager
{
    typedef struct
    {
        bool Valid;
        char Reserved[1];
    } LicenceData;

    void Clear(LicenceData& licence);
    void Clear();
    bool IsExist();
    bool Import(const String& licencecode);
    bool Import();
    void GetMachine(String& machine);
#if (defined __DEBUG__ || defined __GENERATOR__)
    void Generate(const LicenceData& data, const String& machine, String& licencecode);
#endif
    extern const LicenceData& Licence;

};

#endif // __LICENCE_H__
