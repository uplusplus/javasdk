#include <android/log.h>

#define STDOUT __stdout__
#define __stdout__ ANDROID_LOG_INFO,__func__
#define EMS_FPRIINT __android_log_print
#define DMSG(__args__) EMS_FPRIINT __args__
#define NULL (0x0)
