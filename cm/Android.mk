LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional

LOCAL_SRC_FILES := $(call all-java-files-under, src) \
    ../../base/core/java/android/net/ProxyProperties.java

LOCAL_MODULE := org.cyanogenmod.support

include $(BUILD_STATIC_JAVA_LIBRARY)
