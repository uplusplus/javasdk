/* DO NOT EDIT THIS FILE - it is machine generated */
#include "com_hd_internal_Control.h"
#include <ls300/hd_laser_control.h>
/* Header for class com_hd_internal_Control */

/*
 * Class:     com_hd_internal_Control
 * Method:    hl_Constructor
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_com_hd_internal_Control_hl_1Constructor
(JNIEnv *env, jclass clazz)
{
	laser_control_t* control = (laser_control_t*) malloc(sizeof(laser_control_t));
	e_assert(control, 0);
	return (jlong) control;
}

/*
 * Class:     com_hd_internal_Control
 * Method:    hl_Destructor
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_hd_internal_Control_hl_1Destructor
(JNIEnv *env, jclass clazz, jlong priv)
{
	laser_control_t* control = (laser_control_t*) priv;
	e_assert(control!=NULL);
	free(control);
}

/*
 * Class:     com_hd_internal_Control
 * Method:    hl_open
 * Signature: (JLjava/lang/String;I)I
 */
JNIEXPORT jint JNICALL Java_com_hd_internal_Control_hl_1open
(JNIEnv *env, jclass clazz, jlong priv, jstring str, jint bandrate)
{
	char *com;
	int ret;
	laser_control_t* control = (laser_control_t*) priv;
	e_assert(control!=NULL, 0);
	com = (*env)->GetStringUTFChars(env, str, 0);
	ret = hl_open(control, com, bandrate);
	(*env)->ReleaseStringUTFChars(env, str, com);
	e_assert(ret>0, 0);
	return 1;
}

/*
 * Class:     com_hd_internal_Control
 * Method:    hl_open_socket
 * Signature: (JLjava/lang/String;I)I
 */
JNIEXPORT jint JNICALL Java_com_hd_internal_Control_hl_1open_1socket
(JNIEnv *env, jclass clazz, jlong priv, jstring str, jint port)
{
	char *ip;
	int ret;
	laser_control_t* control = (laser_control_t*) priv;
	e_assert(control!=NULL, 0);
	ip = (*env)->GetStringUTFChars(env, str, 0);
	ret = hl_open_socket(control, ip, port);
	(*env)->ReleaseStringUTFChars(env, str, ip);
	e_assert(ret>0, 0);
	return 1;
}

/*
 * Class:     com_hd_internal_Control
 * Method:    hl_close
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_hd_internal_Control_hl_1close
(JNIEnv *env, jclass clazz, jlong priv)
{
	char *ip;
	int ret;
	laser_control_t* control = (laser_control_t*) priv;
	e_assert(control!=NULL, 0);
	ret = hl_close(control);
	e_assert(ret>0, 0);
	return 1;
}

/*
 * Class:     com_hd_internal_Control
 * Method:    hl_turntable_prepare
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_com_hd_internal_Control_hl_1turntable_1prepare
(JNIEnv *env, jclass clazz, jlong priv, jint backslash)
{
	int ret;
	laser_control_t* control = (laser_control_t*) priv;
	e_assert(control!=NULL, 0);
	ret = hl_turntable_prepare(control, backslash);
	e_assert(ret>0, 0);
	return 1;
}

/*
 * Class:     com_hd_internal_Control
 * Method:    hl_turntable_start
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_hd_internal_Control_hl_1turntable_1start
(JNIEnv *env, jclass clazz, jlong priv)
{
	int ret;
	laser_control_t* control = (laser_control_t*) priv;
	e_assert(control!=NULL, 0);
	ret = hl_turntable_start(control);
	e_assert(ret>0, 0);
	return 1;
}

/*
 * Class:     com_hd_internal_Control
 * Method:    hl_turntable_stop
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_hd_internal_Control_hl_1turntable_1stop
(JNIEnv *env, jclass clazz, jlong priv)
{
	int ret;
	laser_control_t* control = (laser_control_t*) priv;
	e_assert(control!=NULL, 0);
	ret = hl_turntable_stop(control);
	e_assert(ret>0, 0);
	return 1;
}

/*
 * Class:     com_hd_internal_Control
 * Method:    hl_turntable_is_stopped
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_hd_internal_Control_hl_1turntable_1is_1stopped
(JNIEnv *env, jclass clazz, jlong priv)
{
	int ret;
	laser_control_t* control = (laser_control_t*) priv;
	e_assert(control!=NULL, 0);
	ret = hl_turntable_is_stopped(control);
	return ret;
}

/*
 * Class:     com_hd_internal_Control
 * Method:    hl_turntable_get_angle
 * Signature: (J)D
 */
JNIEXPORT jdouble JNICALL Java_com_hd_internal_Control_hl_1turntable_1get_1angle
(JNIEnv *env, jclass clazz, jlong priv)
{
	jdouble ret;
	laser_control_t* control = (laser_control_t*) priv;
	e_assert(control!=NULL, 0);
	ret = hl_turntable_get_angle(control);
	return ret;
}

/*
 * Class:     com_hd_internal_Control
 * Method:    hl_turntable_config
 * Signature: (JIDD)I
 */
JNIEXPORT jint JNICALL Java_com_hd_internal_Control_hl_1turntable_1config
(JNIEnv *env, jclass clazz, jlong priv, jint p1, jdouble p2, jdouble p3)
{
	int ret;
	laser_control_t* control = (laser_control_t*) priv;
	e_assert(control!=NULL, 0);
	ret = hl_turntable_config(control, p1, p2, p3);
	e_assert(ret>0, 0);
	return 1;
}

/*
 * Class:     com_hd_internal_Control
 * Method:    hl_turntable_turn
 * Signature: (JD)I
 */
JNIEXPORT jint JNICALL Java_com_hd_internal_Control_hl_1turntable_1turn
(JNIEnv *env, jclass clazz, jlong priv, jdouble p1)
{
	int ret;
	laser_control_t* control = (laser_control_t*) priv;
	e_assert(control!=NULL, 0);
	ret = hl_turntable_turn(control, p1);
	e_assert(ret>0, 0);
	return 1;
}

JNIEXPORT jint JNICALL Java_com_hd_internal_Control_hl_1turntable_1check
(JNIEnv *env, jclass clazz, jlong priv)
{
	int ret;
	laser_control_t* control = (laser_control_t*) priv;
	e_assert(control!=NULL, 0);
	ret = hl_turntable_check(control);
	e_assert(ret>0, 0);
	return 1;
}

/*
 * Class:     com_hd_internal_Control
 * Method:    hl_camera_take_photo
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_hd_internal_Control_hl_1camera_1take_1photo
(JNIEnv *env, jclass clazz, jlong priv)
{
	int ret;
	laser_control_t* control = (laser_control_t*) priv;
	e_assert(control!=NULL, 0);
	ret = hl_camera_take_photo(control);
	e_assert(ret>0, 0);
	return 1;
}

/*
 * Class:     com_hd_internal_Control
 * Method:    hl_camera_scan
 * Signature: (JDD)I
 */
JNIEXPORT jint JNICALL Java_com_hd_internal_Control_hl_1camera_1scan
(JNIEnv *env, jclass clazz, jlong priv, jdouble p1, jdouble p2)
{
	int ret;
	laser_control_t* control = (laser_control_t*) priv;
	e_assert(control!=NULL, 0);
	ret = hl_camera_scan(control, p1, p2);
	e_assert(ret>0, 0);
	return 1;
}

/*
 * Class:     com_hd_internal_Control
 * Method:    hl_get_temperature
 * Signature: (J)D
 */
JNIEXPORT jdouble JNICALL Java_com_hd_internal_Control_hl_1get_1temperature
(JNIEnv *env, jclass clazz, jlong priv)
{
	jdouble ret;
	laser_control_t* control = (laser_control_t*) priv;
	e_assert(control!=NULL, 0);
	ret = hl_get_temperature(control);
	return ret;
}

/*
 * Class:     com_hd_internal_Control
 * Method:    hl_get_tilt
 * Signature: (J)F
 */
JNIEXPORT jint JNICALL Java_com_hd_internal_Control_hl_1get_1tilt
(JNIEnv *env, jclass clazz, jlong priv,jdoubleArray jda)
{
	jdouble dangle[2];
	angle_t angle;
	int ret;
	laser_control_t* control = (laser_control_t*) priv;
	e_assert(control!=NULL, 0);
	ret = hl_get_tilt(control,&angle);
	e_assert(ret>0,0);
	dangle[0] = angle.dX;dangle[1] = angle.dY;
	(*env)->SetDoubleArrayRegion(env,jda,0,2,dangle);
	return 1;
}

/*
 * Class:     com_hd_internal_Control
 * Method:    hl_get_battery
 * Signature: (J)D
 */
JNIEXPORT jdouble JNICALL Java_com_hd_internal_Control_hl_1get_1battery
(JNIEnv *env, jclass clazz, jlong priv)
{
	jdouble ret;
	laser_control_t* control = (laser_control_t*) priv;
	e_assert(control!=NULL, 0);
	ret = hl_get_battery(control);
	return ret;
}

/*
 * Class:     com_hd_internal_Control
 * Method:    hl_led_Red
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_hd_internal_Control_hl_1led_1red
(JNIEnv *env, jclass clazz, jlong priv)
{
	jint ret;
	laser_control_t* control = (laser_control_t*) priv;
	e_assert(control!=NULL, 0);
	ret = hl_led_red(control);
	e_assert(ret>0, 0);
	return 1;
}

/*
 * Class:     com_hd_internal_Control
 * Method:    hl_led_greed
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_hd_internal_Control_hl_1led_1green
(JNIEnv *env, jclass clazz, jlong priv)
{
	jint ret;
	laser_control_t* control = (laser_control_t*) priv;
	e_assert(control!=NULL, 0);
	ret = hl_led_green(control);
	e_assert(ret>0, 0);
	return 1;
}

/*
 * Class:     com_hd_internal_Control
 * Method:    hl_led_off
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_hd_internal_Control_hl_1led_1off
(JNIEnv *env, jclass clazz, jlong priv)
{
	jint ret;
	laser_control_t* control = (laser_control_t*) priv;
	e_assert(control!=NULL, 0);
	ret = hl_led_off(control);
	e_assert(ret>0, 0);
	return 1;
}

/*
 * Class:     com_hd_internal_Control
 * Method:    hl_search_zero
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_hd_internal_Control_hl_1search_1zero
(JNIEnv *evn, jclass c, jlong priv)
{
	jint ret;
	laser_control_t* control = (laser_control_t*) priv;
	e_assert(control!=NULL, 0);
	ret = hl_search_zero(control);
	e_assert(ret>0, 0);
	return 1;
}

/*
 * Class:     com_hd_internal_Control
 * Method:    hl_get_info
 * Signature: (JILjava/lang/String;I)I
 */
JNIEXPORT jstring JNICALL Java_com_hd_internal_Control_hl_1get_1info
(JNIEnv *env, jclass c, jlong priv, jint idx,jint len)
{
	jint ret;
	jstring info;
	char *buf = (char*)malloc(len);
	laser_control_t* control = (laser_control_t*) priv;
	e_assert(control!=NULL, 0);
	ret = hl_get_info(control,idx,buf,len);
	e_assert(ret>0, 0);
	//TODO:GBK2UTF8
	info = (*env)->NewStringUTF(env, buf);
	free(buf);
	return info;
}

