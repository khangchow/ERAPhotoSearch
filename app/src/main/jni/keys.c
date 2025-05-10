#include <jni.h>

JNIEXPORT jstring JNICALL
Java_com_era_photosearch_di_NativeLib_getApiKey(JNIEnv *env, jobject instance) {

    return (*env)->NewStringUTF(env, "MeUQlK8dVvEE8hlzL3zJWQQvUe2GNhrrgEGpehTCbCWuxnMpIwSZrTNT");
}
