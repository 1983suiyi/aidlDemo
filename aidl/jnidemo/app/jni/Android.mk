LOCAL_PATH:=$(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE :=static

LOCAL_SRC_FILES := com_suiyi_jnidemo_NativeLib.c

LOCAL_LDLIBS    := -llog

include $(BUILD_STATIC_LIBRARY)


include $(CLEAR_VARS)

LOCAL_MODULE :=native-demo-lib

LOCAL_SRC_FILES := com_suiyi_jnidemo_NativeLib.c \
                   native-lib.cpp\
                   cfile.c\

LOCAL_STATIC_LIBRARIES :=static

LOCAL_LDLIBS    := -llog

include $(BUILD_SHARED_LIBRARY)