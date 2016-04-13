#include <stdlib.h>
#include <string.h>
#include <sstream>
#include <fstream>

#include <openssl/md5.h>
#include <fcntl.h>
#include <unistd.h>
#include <sys/ioctl.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <linux/hdreg.h>
#include <scsi/sg.h>

#include "share/include.h"

#include "cipher.h"

using namespace std;
using namespace Cipher;

bool GetIDE(const char* dev, String& id)
{
    struct hd_driveid info;
    int fd = open(dev, O_RDONLY | O_NONBLOCK);
    if(fd < 0)
        return false;
    if(ioctl(fd, HDIO_GET_IDENTITY, &info) < 0)
        return false;
    id = (char*)info.serial_no;
    close(fd);
    return true;
}

bool GetSCSI(const char* dev, String& id)
{
    bool ret = false;
    int fd, vers;
    fd = open(dev, O_RDWR);
    if(fd < 0)
        goto returnret;
    if((ioctl(fd, SG_GET_VERSION_NUM, &vers) < 0) || (vers < 30000))
        goto closefd;
    unsigned char cdb[6];
    static const unsigned int data_size = 0x00ff;
    static const unsigned int sense_len = 32;
    unsigned char data[data_size];
    unsigned char sense[sense_len];
    cdb[0] = 0x12;
    cdb[1] = 0x01;
    cdb[2] = 0x80;
    cdb[3] = (data_size >> 8) & 0xff;
    cdb[4] = data_size & 0xff;
    cdb[5] = 0;
    sg_io_hdr_t io_hdr;
    memset(&io_hdr, 0, sizeof(sg_io_hdr_t));
    io_hdr.interface_id = 'S';
    io_hdr.cmdp = cdb;
    io_hdr.cmd_len = sizeof(cdb);
    io_hdr.sbp = sense;
    io_hdr.mx_sb_len = sense_len;
    io_hdr.dxfer_direction = SG_DXFER_FROM_DEV;
    io_hdr.dxferp = data;
    io_hdr.dxfer_len = data_size;
    static const int SCSI_TIMEOUT = 5000;
    io_hdr.timeout = SCSI_TIMEOUT;
    if(ioctl(fd, SG_IO, &io_hdr) < 0)
        goto closefd;
    if((io_hdr.info & SG_INFO_OK_MASK) != SG_INFO_OK)
        if(io_hdr.sb_len_wr > 0)
            if(io_hdr.sb_len_wr)
                goto closefd;
    if(io_hdr.masked_status || io_hdr.host_status || io_hdr.driver_status)
        goto closefd;
    id.append((char*)(data) + 4, data[3]);
    ret = true;
closefd:
    close(fd);
returnret:
    return ret;
}

bool GetDiskSerailNo(const char* dev, String& id)
{
    if(GetIDE(dev, id))
        return true;
    if(GetSCSI(dev, id))
        return true;
    return false;
}

void GetAllDiskSerailNo(ostream& stream)
{
    String id;
    String hd("/dev/hda");
    String sg("/dev/sda");
    for(char index = 'a'; index <= 'z'; ++index)
    {
        hd[7] = index;
        sg[7] = index;
        if(GetDiskSerailNo(hd.c_str(), id))
            stream << index << id;
        if(GetDiskSerailNo(sg.c_str(), id))
            stream << index << id;
    }
}

void Cipher::GetKeyBySerailNo(Key& key)
{
    ostringstream stream;
    stream << "kylin";
    GetAllDiskSerailNo(stream);
    String data;
    data = stream.str();
    ASSERT(Size == MD5_DIGEST_LENGTH);
    MD5((const unsigned char*)data.c_str(), data.size(), (unsigned char*)&key);
}

void Cipher::GetKeyByRandom(Key& key)
{
    ifstream stream("/dev/urandom");
    if(stream)
    {
        int size = 0, len = 0;
        do
        {
            size = stream.readsome((char*)&key, Size - len);
            len += size;
        } while(size != 0);
        stream.close();
    } else {
        for(int i = 0; i < Size; i++)
            key.Data[i] = rand();
    }
}

void Cipher::GetKeyByStatic(Key& key)
{
    key.Data[0]  = 0x04;
    key.Data[1]  = 0x7E;
    key.Data[2]  = 0x92;
    key.Data[3]  = 0xBA;
    key.Data[4]  = 0xC1;
    key.Data[5]  = 0x3F;
    key.Data[6]  = 0x83;
    key.Data[7]  = 0xD6;
    key.Data[8]  = 0x1C;
    key.Data[9]  = 0x5B;
    key.Data[10] = 0xA5;
    key.Data[11] = 0x68;
    key.Data[12] = 0x2D;
    key.Data[13] = 0xF7;
    key.Data[14] = 0x49;
    key.Data[15] = 0xE0;
}

