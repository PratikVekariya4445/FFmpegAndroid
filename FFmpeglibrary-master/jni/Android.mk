LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := ARM_ARCH

LOCAL_SRC_FILES := armArch.c

LOCAL_CFLAGS := -I$(NDK)/sources/ffmpeg
LOCAL_STATIC_LIBRARIES := cpufeatures

LOCAL_LDLIBS := -llog
ANDROID_LIB := -landroid

include $(BUILD_SHARED_LIBRARY)
$(call import-add-path,/home/test/android-ndk-r17c/sources/ffmpeg/android)
$(call import-module,android/cpufeatures)
