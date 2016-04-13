#ifndef __CIPHER_H_INCLUDED__
#define __CIPHER_H_INCLUDED__

#include <iostream>

namespace Cipher
{
    static const int Size = 16;

    struct Key
    {
        unsigned char Data[Size];
    };

    void GetKeyBySerailNo(Key& key);
    void GetKeyByRandom(Key& key);
    void GetKeyByStatic(Key& key);

#define CIPHER_EXPAND_SIZE(size) ((size) + (8 - (size) % 8) % 8)

    bool ClearStream(std::istream& in, std::ostream& out);
    bool EncryptStream(const Key& key, std::istream& in, std::ostream& out);
    bool DecryptStream(const Key& key, std::istream& in, std::ostream& out);

    void EncryptData(const Key& key, const void* in, void* out, int realsize);
    void DecryptData(const Key& key, const void* in, void* out, int realsize);
}

#endif // __CIPHER_H_INCLUDED__
