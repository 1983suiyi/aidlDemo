/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
#include "com_suiyi_jnidemo_NativeLib.h"
#include<android/log.h>
#include <time.h>
#include "cfile.h"

/*
 * Class:     com_suiyi_jnidemo_NativeLib
 * Method:    printHello
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_suiyi_jnidemo_NativeLib_printHello
        (JNIEnv * env, jclass clazz){
    __android_log_print(ANDROID_LOG_ERROR,"suiyi","nativelib printhello");


    jstring cfile =(*env)->NewStringUTF(env,printCfile());

    if (cfile ==NULL){
        __android_log_print(ANDROID_LOG_ERROR,"suiyi","MALLOC FAIL");
        return;
    }

    const char * constcfile = (*env)->GetStringUTFChars(env,cfile,JNI_FALSE);
    __android_log_print(ANDROID_LOG_ERROR,"suiyi","%s",constcfile);
}