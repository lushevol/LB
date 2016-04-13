#include <string.h>
#include <sstream>
#include <fstream>

#include <sys/types.h>
#include <sys/stat.h>

#include "cipher/cipher.h"

#include "licence.h"

using namespace std;

static const int DataSize = sizeof(LicenceManager::LicenceData);
static const int ExpandDataSize = CIPHER_EXPAND_SIZE(DataSize);

typedef struct
{
    LicenceManager::LicenceData Data;
    char Checkout[ExpandDataSize];
} LicenceDataPack;

static const int PackSize = sizeof(LicenceDataPack);
static const int CodeSize = CIPHER_EXPAND_SIZE(PackSize);

static const char* const LicenceFile = "/var/klb/licence";
static const char* const LicencePath = "/var/klb/";

LicenceManager::LicenceData FLicence;
const LicenceManager::LicenceData& LicenceManager::Licence = FLicence;

void Translate(const void* data, size_t size, String& result)
{
    ostringstream stream;
    const unsigned int* head = (const unsigned int*)data;
    while(size > 0)
    {
        unsigned int value;
        if(size >= sizeof(unsigned int))
        {
            value = *head;
            ++head;
            size -= sizeof(unsigned int);
        } else {
            value = 0;
            memcpy(&value, head, size);
            size = 0;
        }
        static const int MapSize = 32;
        static const char Map[MapSize] = {'2', '6', '3', '7', '4', '8', '0', '5',
                                          '1', '9', 'A', 'M', 'W', 'R', 'S', 'T',
                                          'Y', 'F', 'B', 'P', 'U', 'V', 'C', 'K',
                                          'L', 'X', 'N', 'D', 'G', 'H', 'Q', 'Z'
                                         };
        do
        {
            stream << Map[value % MapSize];
            value /= MapSize;
        } while(value > 0);
        if(size > 0)
            stream << '-';
    }
    result = stream.str();
}

void Translate(const String& key, void* result, size_t size)
{
    String::const_iterator pos = key.begin();
    unsigned int* head = (unsigned int*)result;
    memset(result, 0, size);
    while(size > 0)
    {
        unsigned int value = 0;
        unsigned int bit = 1;
        while(pos != key.end())
        {
            if(*pos == '-')
            {
                ++pos;
                break;
            }
            int delta;
            switch(*pos)
            {
                case '2':
                    delta = 0;
                    break;
                case '6':
                    delta = 1;
                    break;
                case '3':
                    delta = 2;
                    break;
                case '7':
                    delta = 3;
                    break;
                case '4':
                    delta = 4;
                    break;
                case '8':
                    delta = 5;
                    break;
                case '0':
                    delta = 6;
                    break;
                case '5':
                    delta = 7;
                    break;
                case '1':
                    delta = 8;
                    break;
                case '9':
                    delta = 9;
                    break;
                case 'A':
                    delta = 10;
                    break;
                case 'M':
                    delta = 11;
                    break;
                case 'W':
                    delta = 12;
                    break;
                case 'R':
                    delta = 13;
                    break;
                case 'S':
                    delta = 14;
                    break;
                case 'T':
                    delta = 15;
                    break;
                case 'Y':
                    delta = 16;
                    break;
                case 'F':
                    delta = 17;
                    break;
                case 'B':
                    delta = 18;
                    break;
                case 'P':
                    delta = 19;
                    break;
                case 'U':
                    delta = 20;
                    break;
                case 'V':
                    delta = 21;
                    break;
                case 'C':
                    delta = 22;
                    break;
                case 'K':
                    delta = 23;
                    break;
                case 'L':
                    delta = 24;
                    break;
                case 'X':
                    delta = 25;
                    break;
                case 'N':
                    delta = 26;
                    break;
                case 'D':
                    delta = 27;
                    break;
                case 'G':
                    delta = 28;
                    break;
                case 'H':
                    delta = 29;
                    break;
                case 'Q':
                    delta = 30;
                    break;
                case 'Z':
                    delta = 31;
                    break;
                default:
                    return;
            };
            value += delta * bit;
            static const int MapSize = 32;
            ++pos;
            bit *= MapSize;
        }
        if(size >= sizeof(unsigned int))
        {
            *head = value;
            ++head;
            size -= sizeof(unsigned int);
        } else {
            memcpy(head, &value, size);
            break;
        }
    }
}

void LicenceManager::Clear(LicenceData& licence)
{
    memset(&licence, 0, DataSize);
}

void LicenceManager::Clear()
{
    Clear(FLicence);
}

bool LicenceManager::IsExist()
{
    return FLicence.Valid;
}

bool Decode(const String& licencecode, LicenceManager::LicenceData& licence)
{
    char code[CodeSize];
    Translate(licencecode, code, CodeSize);
    LicenceDataPack pack;
    Cipher::Key key;
    Cipher::GetKeyBySerailNo(key);
    Cipher::DecryptData(key, &code, &pack, PackSize);
    char check[ExpandDataSize];
    Cipher::GetKeyByStatic(key);
    Cipher::EncryptData(key, &pack.Data, check, DataSize);
    if(memcmp(check, pack.Checkout, ExpandDataSize) != 0 || !pack.Data.Valid)
        return false;
    licence = pack.Data;
    return true;
}

bool LicenceManager::Import(const String& licencecode)
{
    bool ret = false;
    LicenceData licence;
    if(Decode(licencecode, licence))
    {
        mkdir(LicencePath, S_IRWXU);
        ofstream stream(LicenceFile);
        try
        {
            if(stream && stream << licencecode)
            {
                FLicence = licence;
                ret = true;
            }
        } catch(ios_base::failure)
        {
        }
    }
    return ret;
}

bool LicenceManager::Import()
{
    bool ret = false;
    ifstream stream(LicenceFile);
    try
    {
        String code;
        if(stream && stream >> code)
            ret = Decode(code, FLicence);
    } catch(ios_base::failure)
    {
    }
    return ret;
}

#if (defined __DEBUG__ || defined __GENERATOR__)

void LicenceManager::Generate(const LicenceData& data, const String& machine, String& licencecode)
{
    LicenceDataPack pack;
    pack.Data = data;
    Cipher::Key key;
    Cipher::GetKeyByStatic(key);
    Cipher::EncryptData(key, &pack.Data, pack.Checkout, DataSize);
    char code[CodeSize];
    Translate(machine, &key, sizeof(key));
    Cipher::EncryptData(key, &pack, code, PackSize);
    Translate(code, CodeSize, licencecode);
}

#endif

void LicenceManager::GetMachine(String& machine)
{
    Cipher::Key key;
    Cipher::GetKeyBySerailNo(key);
    Translate(&key, sizeof(key), machine);
}
