#ifndef __LIBSTATIC_H_INCLUDED__
#define __LIBSTATIC_H_INCLUDED__

bool LoadISP(const char* path, uint32_t mark);

bool InitRoute();
bool UninitRoute();
bool StartRoute(int isp_num);
bool EndRoute();
bool SetIspMem(void);
bool InsertMark(uint32_t mark, int flag);
bool ChangeCheck(uint32_t ip, uint32_t mark);

#define VALID   0
#define INVALID 1

#ifdef __DEBUG__
void ShowISP();
#endif


#endif // __LIBSTATIC_H_INCLUDED__
