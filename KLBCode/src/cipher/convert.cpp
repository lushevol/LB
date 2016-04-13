#include <string.h>

#include <openssl/des.h>

#include "share/include.h"

#include "cipher.h"

using namespace std;
using namespace Cipher;

#ifdef __DEBUG__
static const int MAX_SIZE = 16;
#else
static const int MAX_SIZE = 1024 * 256 * 8;
#endif

bool Cipher::ClearStream(std::istream& in, std::ostream& out)
{
    try
    {
        char buf[MAX_SIZE];
        int length = 0, size;
        do
        {
            size = in.readsome(buf, MAX_SIZE);
            length += size;
        } while(size != 0);
        for(int i = 0; i < MAX_SIZE; ++i)
            buf[i] = rand();
        while(length > 0)
        {
            out.write(buf, (MAX_SIZE > length) ? length : MAX_SIZE);
            length -= MAX_SIZE;
        }
    } catch(ios_base::failure&)
    {
        return false;
    }
    return true;
}

void InitDES(const Key& key, DES_cblock& block, des_key_schedule& sched)
{
    ASSERT(sizeof(DES_cblock) * 2 == sizeof(Key));
    DES_cblock temp;
    memcpy(&temp, &key, sizeof(DES_cblock));
    DES_set_key(&temp, &sched);
    DES_ncbc_encrypt(((const unsigned char*)&key) + sizeof(DES_cblock), (unsigned char*)&block, sizeof(DES_cblock), &sched, &temp, DES_DECRYPT);
    DES_set_key(&block, &sched);
}

bool Cipher::EncryptStream(const Key& key, std::istream& in, std::ostream& out)
{
    DES_cblock block;
    DES_key_schedule sched;
    InitDES(key, block, sched);
    try
    {
        char bufin[MAX_SIZE];
        char bufout[MAX_SIZE];
        int size;
        do
        {
            size = in.readsome(bufin, MAX_SIZE);
            DES_ncbc_encrypt((unsigned char*)bufin, (unsigned char*)bufout, size, &sched, &block, DES_ENCRYPT);
            out.write((char*)&size, sizeof(int));
            out.write(bufout, CIPHER_EXPAND_SIZE(size));
        } while(size != 0);
    } catch(ios_base::failure&)
    {
        return false;
    }
    return true;
}

bool Cipher::DecryptStream(const Key& key, std::istream& in, std::ostream& out)
{
    DES_cblock block;
    DES_key_schedule sched;
    InitDES(key, block, sched);
    try
    {
        char bufin[MAX_SIZE];
        char bufout[MAX_SIZE];
        do
        {
            int realsize, size;
            size = in.readsome((char*)&realsize, sizeof(int));
            if(size == 0)
                return true;
            else if(size != sizeof(int))
                return false;
            if(realsize > MAX_SIZE)
                return false;
            int len = CIPHER_EXPAND_SIZE(realsize);
            size = in.readsome(bufin, len);
            if(len == size)
            {
                DES_ncbc_encrypt((unsigned char*)bufin, (unsigned char*)bufout, len, &sched, &block, DES_DECRYPT);
                out.write(bufout, realsize);
            } else
                return false;
        } while(true);
    } catch(ios_base::failure&)
    {
        return false;
    }
    return true;
}

void Cipher::EncryptData(const Key& key, const void* in, void* out, int realsize)
{
    DES_cblock block;
    DES_key_schedule sched;
    InitDES(key, block, sched);
    DES_ncbc_encrypt((const unsigned char*)in, (unsigned char*)out, realsize, &sched, &block, DES_ENCRYPT);
}

void Cipher::DecryptData(const Key& key, const void* in, void* out, int realsize)
{
    DES_cblock block;
    DES_key_schedule sched;
    InitDES(key, block, sched);
    unsigned char buf[CIPHER_EXPAND_SIZE(realsize)];
    DES_ncbc_encrypt((const unsigned char*)in, buf, CIPHER_EXPAND_SIZE(realsize), &sched, &block, DES_DECRYPT);
    memcpy(out, buf, realsize);
}
