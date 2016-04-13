#ifndef __SERIALIZE_H__
#define __SERIALIZE_H__

#include "model/model.h"

class SerializeModel
{
    public:
        typedef void (*ImportFunc)(Value& data, bool reload);
        typedef void (*ExportFunc)(Value& data);
    private:
        int FPriority;
        ImportFunc FBeforeImport;
        ImportFunc FImport;
        ImportFunc FAfterImport;
        ExportFunc FExport;
    public:
        static void Export(Value& data);
        static void Import(Value& data, bool reload);
        SerializeModel(ImportFunc before, ImportFunc in, ImportFunc after, ExportFunc ex, int priority);
        ~SerializeModel();
};

#endif // __SERIALIZE_H__
