#include <iostream>
#include <fstream>
#include <sstream>

#include <unistd.h>
#include <dirent.h>
#include <sys/stat.h>
#include <sys/types.h>

#include "share/include.h"
#include "share/utility.h"
#include "cipher/cipher.h"
#include "licence/licence.h"

using namespace std;

#define PACKERROR(msg...) do{cout<<msg<<endl;exit(-1);}while(0)

int main(int argc, char* argv[])
{
    if(argc == 5 && strcmp(argv[1], "-p") == 0)
    {
        struct dirent **list;
        int listlen = scandir(argv[2], &list, 0, alphasort);
        if(listlen < 0)
            PACKERROR("Failed to open directory: " << argv[2]);
        int count = 0;
        String temppackage(String(argv[3]) + ".package." + RandomString());
        ostringstream cmd;
        cmd << "tar -C " << argv[2] << " -zcvf " << temppackage;
        while(listlen--)
        {
            if(*list[listlen]->d_name != '.')
            {
                cmd << " " << list[listlen]->d_name;
                ++count;
            }
            free(list[listlen]);
        }
        free(list);
        if(count == 0)
            PACKERROR("The directory is empty.");
        cout << "Package..." << endl;
        if(system(cmd.str().c_str()) != 0)
        {
            unlink(temppackage.c_str());
            PACKERROR("Failed to create temp package.");
        }
        String tempscript(String(argv[3]) + ".script." + RandomString());
        ofstream stream(tempscript.c_str());
        if(!stream)
        {
            unlink(temppackage.c_str());
            PACKERROR("Failed to create temp script.");
        }
        cout << "Genearte script..." << endl;
        stream
                << "#!/bin/bash" << endl
                << "target=$(pwd)" << endl
                << "[ ! \"$1\" = \"\" ] && target=$1" << endl
                << "[ ! -d $target ] && exit -1" << endl
                << "cd $target" << endl
                << "(read;read;read;read;read;read;read;read;read;exec cat) < \"$0\"|gunzip|tar xf -" << endl
                << "[ ! $? = 0 ] && exit -1" << endl
                << "./" << argv[4] << " &" << endl
                << "exit 0" << endl;
        stream.close();
        system((String("cat ") + temppackage + " >> " + tempscript).c_str());
        unlink(temppackage.c_str());
        Cipher::Key key;
        Cipher::GetKeyByStatic(key);
        cout << "Encrypt script..." << endl;
        ifstream fin(tempscript.c_str());
        ofstream fout(argv[3]);
        if(!fin || !fout || !Cipher::EncryptStream(key, fin, fout))
        {
            fin.close();
            fout.close();
            unlink(tempscript.c_str());
            PACKERROR("Failed to generate " << argv[3]);
        } else {
            fin.close();
            fout.close();
            unlink(tempscript.c_str());
        }
        cout << "Done." << endl;
    } else if(argc == 3 && strcmp(argv[1], "-r") == 0)
    {
        if(access(argv[2], R_OK | F_OK) != 0)
            PACKERROR("Failed to open " << argv[2]);
        Cipher::Key key;
        Cipher::GetKeyByStatic(key);
        String temp = "/tmp/" + RandomString();
        bool ret = false;
        mkdir("/var/klb", S_IRWXU);
        mkdir("/var/klb/updater", S_IRWXU);
        ifstream fin(argv[2]);
        ofstream fout(temp.c_str());
        if(fin && fout && Cipher::DecryptStream(key, fin, fout))
        {
            fin.close();
            fout.close();
            chmod(temp.c_str(), S_IRWXU);
            Exec exe(temp);
            exe << "/var/klb/updater";
            exe.Execute();
            int status;
            fdistream stream(exe.ReadOut());
            cout << stream.rdbuf();
            if(exe.Close(status))
                ret = status == 0;
        } else {
            fin.close();
            fout.close();
        }
        unlink(temp.c_str());
        if(ret)
            cout << "Done." << endl;
        else
            cout << "Update failed." << endl;
    } else if(argc == 3 && strcmp(argv[1], "-m") == 0)
    {
        LicenceManager::LicenceData data;
        LicenceManager::Clear(data);
        data.Valid = true;
        String code;
        LicenceManager::Generate(data, argv[2], code);
        cout << "Licence Code: " << code << endl;
    } else
        PACKERROR("Usage: klbpacakge -p directory package runscript" << endl <<
                  "       klbpackage -r package" << endl <<
                  "       klbpackage -m machinecode");
}
