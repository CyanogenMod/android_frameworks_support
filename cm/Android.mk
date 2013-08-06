LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_SRC_FILES := $(call all-java-files-under, src)

LOCAL_MODULE := org.cyanogenmod.support

include $(BUILD_STATIC_JAVA_LIBRARY)
