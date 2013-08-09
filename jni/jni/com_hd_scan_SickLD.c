#include "com_hd_scan_SickLD.h"
#include <ls300/hd_laser_scan.h>

/* implement for class com_hd_scan_SickLD */

/*
 * Class:     com_hd_scan_SickLD
 * Method:    main
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_hd_scan_SickLD_main
(JNIEnv *env, jclass clazz)
{
	int ret;
	scan_job job;
	DMSG((STDOUT,"LASER scan TEST start.\r\n"));
	ret = sj_create(&job);
	e_assert(ret>0, ret);

	DMSG((STDOUT,"LASER scan TEST config.\r\n"));

	//ret = sj_config(job, 50, 180, 360, 5, 0.5, -45, 0);
	//ret = sj_config(job, 50, 0, 360, 5, 0.5, 0, 90);//???频繁出现取数据错误?sick 3030
	ret = sj_config(job, 50, 0, 360, 5, 0.25, -45, 90);
	//ret = sj_config(job, 50, 0, 90, 5, 0.5, -45, -20);
	//ret = sj_config(job, 50, 160, 200, 5, 0.5, -45, 90);
	e_assert(ret>0, ret);

	DMSG((STDOUT,"LASER scan TEST start.\r\n"));
	ret = sj_start(job);
	e_assert(ret>0, ret);

	DMSG((STDOUT,"LASER scan TEST start.\r\n"));
	ret = sj_wait(job);
	e_assert(ret>0, ret);

	DMSG((STDOUT,"LASER scan TEST PASSED.\r\n"));
	sj_destroy(job);
	return ret;
}
