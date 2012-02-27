/*
 *
 */
#include <string.h>
#include <jni.h>

#include <stdio.h>
#include <stdlib.h>
#include <android/log.h>

#include "leveldb/db.h"

#define  LOG_TAG    "IntheCPP"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

//leveldb::Benchmark benchTest = new leveldb::Benchmark();


extern "C" {
JNIEXPORT jstring JNICALL Java_com_example_hellojni_HelloJni_dbGet(JNIEnv * env, jobject thiz);
};

JNIEXPORT jstring JNICALL Java_com_example_hellojni_HelloJni_dbGet(JNIEnv * env, jobject thiz)
{
	return env->NewStringUTF("calling get");
}

extern "C" {
JNIEXPORT jstring JNICALL Java_com_example_hellojni_HelloJni_dbPut(JNIEnv * env, jobject thiz);
};

JNIEXPORT jstring JNICALL Java_com_example_hellojni_HelloJni_dbPut(JNIEnv * env, jobject thiz)
{
	return env->NewStringUTF("calling put");
}
extern "C" {
JNIEXPORT jstring JNICALL Java_com_example_hellojni_HelloJni_dbOpen(JNIEnv* env, jobject thiz, jstring dbpath);


};

JNIEXPORT jstring JNICALL Java_com_example_hellojni_HelloJni_dbOpen(JNIEnv* env, jobject thiz, jstring dbpath)
{
	const char* path = env->GetStringUTFChars(dbpath,0);
	LOGI(path);
	leveldb::DB* db;
	leveldb::Options options;
	options.create_if_missing = true;
	leveldb::Status status = leveldb::DB::Open(options, path, &db);
	const char* re =  status.ToString().c_str();
	return env->NewStringUTF(re);
}
extern "C" {
	JNIEXPORT jstring JNICALL Java_com_example_hellojni_HelloJni_dbDelete(JNIEnv * env, jobject thiz);
};

JNIEXPORT jstring JNICALL Java_com_example_hellojni_HelloJni_dbDelete(JNIEnv * env, jobject thiz)
{
	return env->NewStringUTF("calling delete");
}
extern "C" {
JNIEXPORT jstring JNICALL Java_com_example_hellojni_HelloJni_GetProperty(JNIEnv * env, jobject thiz);
};

JNIEXPORT jstring JNICALL Java_com_example_hellojni_HelloJni_GetProperty(JNIEnv * env, jobject thiz)
{
	return env->NewStringUTF("calling get property to find out db size etc");
}
