#include <jni.h>
#include <string>
#include<android/log.h>
#include<pthread.h>

static char* Jstring2Char(JNIEnv *env,jstring jstr);

static pthread_t* pthread;

extern "C"
JNIEXPORT jstring JNICALL
Java_com_suiyi_jnidemo_MainActivity_stringFromJNIa(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT jintArray JNICALL
Java_com_suiyi_jnidemo_MainActivity_arrayFromJNI(JNIEnv *env, jobject instance, jint size) {

    __android_log_print(ANDROID_LOG_ERROR,"SUIYI","arrayFromJNI");

    jint temp[size];
    for (int i = 0; i < size; ++i) {
        temp[i] = i;
    }
    jintArray iarr = env->NewIntArray(size);
    env->SetIntArrayRegion(iarr,0,size,temp);
    //free(temp);
    env->ReleaseIntArrayElements(iarr,temp,1);
    return iarr;

}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_suiyi_jnidemo_MainActivity_stringFromJNI(JNIEnv *env, jobject instance, jstring msg_) {
    //const char *msg = env->GetStringUTFChars(msg_, 0);

    char * msg = Jstring2Char(env,msg_);
    char* hello = ",Hello from C++";
    strcat(msg,hello);
    // TODO
    printf("printABC MID IS NULL");

    __android_log_print(ANDROID_LOG_ERROR,"SUIYI","JNI TEST");
    //env->ReleaseStringUTFChars(msg_, msg);

    jclass cls = env->GetObjectClass(instance);
    jmethodID mid = env->GetMethodID(cls,"printABC","()V");

    if (mid !=NULL){
        printf("printABC MID IS not NULL");
        env->CallVoidMethod(instance,mid);
    }
    else
        printf("printABC MID IS NULL");

    return env->NewStringUTF(msg);
}

static char* Jstring2Char(JNIEnv *env,jstring jstr){

    char * rtn = NULL;
    jclass classtring = env->FindClass("java/lang/String");
    jstring strencode = env->NewStringUTF("GB2312");
    jmethodID mid = env->GetMethodID(classtring,"getBytes","(Ljava/lang/String;)[B");
    jbyteArray barr = (jbyteArray)env->CallObjectMethod(jstr,mid,strencode);
    jsize alen = env->GetArrayLength(barr);
    jbyte * ba = env->GetByteArrayElements(barr,JNI_FALSE);
    if (alen>0){
        rtn = new char(alen+1);
        //rtn = (char*)malloc(alen+1);
        memcpy(rtn,ba,alen);
        rtn[alen]=0;
    }
    env->ReleaseByteArrayElements(barr,ba,0);
    env->DeleteLocalRef(classtring);


    return rtn;
}


extern "C"
JNIEXPORT  void JNICALL
Java_com_suiyi_jnidemo_MainActivity_initIDs(JNIEnv *env, jclass type) {

    // TODO
    //jmethodID  staticMid = NULL;
    //staticMid = env->GetMethodID(type,"initIDs","()V");
    JavaVMInitArgs jvmAags;
    JavaVMOption option[2];
    jvmAags.version = 0x00010002;
    jvmAags.options = option;
    __android_log_print(ANDROID_LOG_ERROR,"SUIYI","OnlyNativeFun_initIDs");
}

extern void* isPrime(void*);

static bool loadCToJavaMethod(JNIEnv* env);

void* isPrime(void* args) {
    int num =25;
    int i = 0;
    for (i = 2; i * i <= num; i++)     //这里必须是 i*i<= num, 比如25,如果 i*i<25,
    {                               //那么这个数就会被判为奇数.以为,i=5的时候. i*i = 25,
        //但是它循环不会继续.
        if (num % i == 0) {
            __android_log_print(ANDROID_LOG_ERROR,"SUIYI","OnlyNativeFun 0");

        }
    }
    __android_log_print(ANDROID_LOG_ERROR,"SUIYI","OnlyNativeFun 1");
}

extern "C"
JNIEXPORT void JNICALL exec(JNIEnv *env, jclass type){

    __android_log_print(ANDROID_LOG_ERROR,"SUIYI","OnlyNativeFun_exec");

    //JavaVM * javaVM;
    //env->GetJavaVM(&javaVM);
    //pthread_create(pthread,NULL,isPrime,NULL);
    //pthread_join(*pthread,NULL);

}

static const  char *ClASS_NAME_EXEC = "com/suiyi/jnidemo/OnlyNativeFun";

static JNINativeMethod exeMethod = {
        "exec",
        "()V",
        (void*)exec
};

static const  char *ClASS_NAME_UNLOAD = "com/suiyi/jnidemo/OnlyNativeFun";

static JNINativeMethod unLoadMethod = {
        "jniOnUnload",
        "()V",
        (void*)JNI_OnUnload
};

static bool bindMethodExec(JNIEnv *env){

    jclass clazz = env->FindClass(ClASS_NAME_EXEC);

    if (clazz==NULL)
        return JNI_FALSE;

    return env->RegisterNatives(clazz,&exeMethod,1)==0;

}


static bool bindMethodUnload(JNIEnv *env){

    jclass clazz = env->FindClass(ClASS_NAME_UNLOAD);

    if (clazz==NULL)
        return JNI_FALSE;

    return env->RegisterNatives(clazz,&unLoadMethod,1)==0;

}

static bool unBindMethod(JNIEnv* env){

    jclass clazz = env->FindClass(ClASS_NAME_EXEC);

    if (clazz==NULL)
        return JNI_FALSE;
    return env->UnregisterNatives(clazz)==0;
}

 jint JNI_OnLoad(JavaVM* vm,void* reserved){

     __android_log_print(ANDROID_LOG_ERROR,"SUIYI","jni_onload");
     JNIEnv *env ;
     if(vm->GetEnv((void**)&env,JNI_VERSION_1_4)!=JNI_OK){
         __android_log_print(ANDROID_LOG_ERROR,"SUIYI","jni_onload not jni_verson_1_4");
         return -1;
     }
     __android_log_print(ANDROID_LOG_ERROR,"SUIYI","jni_onload IS jni_verson_1_4");

     bool res = bindMethodExec(env);
     __android_log_print(ANDROID_LOG_ERROR,"SUIYI","bind result is %s",res?"ok":"error");

     //bool resUnload = bindMethodUnload(env);
     //__android_log_print(ANDROID_LOG_ERROR,"SUIYI","bind unload result is %s",resUnload?"ok":"error");

     bool resCToJava = loadCToJavaMethod(env);
     __android_log_print(ANDROID_LOG_ERROR,"SUIYI","bind ctojava result is %s",resCToJava?"ok":"error");

     return JNI_VERSION_1_4;
}

void JNI_OnUnload(JavaVM* vm,void* reserved){

    JNIEnv *env =NULL;
    if(vm->GetEnv((void**)&env,JNI_VERSION_1_4)!=JNI_OK){
        __android_log_print(ANDROID_LOG_ERROR,"SUIYI","jni_onunload not jni_verson_1_4");
        return ;
    }
    bool res = unBindMethod(env);
    __android_log_print(ANDROID_LOG_ERROR,"SUIYI","bind result is %s",res?"ok":"error");


}


extern "C"
JNIEXPORT jint JNICALL cGetJavaInt(JNIEnv * env , jobject classObject){

    jclass jclazz = env->GetObjectClass(classObject);

    if (jclazz==NULL)
        return 0;

    jfieldID fid = env->GetFieldID(jclazz,"i","I");


    jint result = env->GetIntField(classObject,fid);

    return result;

}
static const  char *ClASS_NAME_CTOJAVA = "com/suiyi/jnidemo/MainActivity";

static JNINativeMethod cToJavaMethod = {
        "cGetJavaInt",
        "()I",
        (void*)cGetJavaInt
};

static bool loadCToJavaMethod(JNIEnv* env){

    jclass clazz = env->FindClass(ClASS_NAME_CTOJAVA);

    if (clazz==NULL)
        return JNI_FALSE;
    return env->RegisterNatives(clazz,&cToJavaMethod,1)==0;
}



