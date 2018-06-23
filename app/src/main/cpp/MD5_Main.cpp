#include "string.h"
#include "WjcDes.h"
#include "stdio.h"

char *g_key="z]o04/.diy,df*s-";

void decode(char *pv_InBuffer,int pi_InBufferLength,char *pv_OutBuffer,int pi_OutBufferLength)
{
        char lv_TmpBuf[3];
        char lv_TmpCode[1000];
        memset(lv_TmpCode, 0, 1000);
        memset(pv_OutBuffer, 0, pi_OutBufferLength);

        for(int i=0;i<pi_InBufferLength;i++)
        {
            memset(lv_TmpBuf, 0, 3);
            memcpy( lv_TmpBuf,pv_InBuffer+i*2,2);
            sscanf( lv_TmpBuf,"%X" ,lv_TmpCode+i);
        }

		Des_Go(pv_OutBuffer, lv_TmpCode, pi_OutBufferLength, g_key, 16, true);
}

void encode(char *pv_InBuffer,int pi_InBufferLength,char *pv_OutBuffer,int pi_OutBufferLength)
{

   char lv_Decode[500];
   unsigned char lc_Tmp;

   memset(lv_Decode, 0, 500);
   memset(pv_OutBuffer, 0, pi_OutBufferLength);

   Des_Go(lv_Decode, pv_InBuffer,pi_InBufferLength, g_key, 16, false);

   for(int i=0;i<pi_InBufferLength;i++)
   {
        lc_Tmp= lv_Decode[i];
        sprintf(pv_OutBuffer+i*2,"%02X",lc_Tmp);
   }
}
