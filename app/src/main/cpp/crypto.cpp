#include <jni.h>
#include <string>
#include "MD5_Main.h"

extern "C"
JNIEXPORT jstring JNICALL Java_cn_eflo_managewatermeter_util_CodeUtil_decode(
        JNIEnv *env,
        jobject,
        jstring inputString) {
    char *input = (char *) (env)->GetStringUTFChars(inputString, 0);
    char output[32];
    decode(input, 61, output, 32);
    char *out = (char *) malloc(sizeof(char) * (strlen(output) + 1));
    out = strcpy(out, output);
    jstring str = env->NewStringUTF(out);
    return str;
}

extern "C"
JNIEXPORT jstring JNICALL Java_cn_eflo_managewatermeter_util_CodeUtil_encode(
        JNIEnv *env,
        jobject,
        jstring inputString) {
    char *input = (char *) (env)->GetStringUTFChars(inputString, 0);
    char output[64];
    encode(input, strlen(input), output, 64);
    char *out = (char *) malloc(sizeof(char) * (strlen(output) + 1));
    out = strcpy(out, output);
    jstring str = env->NewStringUTF(out);
    return str;
}
