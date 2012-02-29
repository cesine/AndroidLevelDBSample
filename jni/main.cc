/*
 * This file contains sample wrappers showing Android Java Developers
 * who are not familiar with the NDK or JNI how to get data in and out of the
 * CPP and how to incorporate the sample code in
 * http://leveldb.googlecode.com/svn/trunk/doc/index.html into their JNI
 */
#include <string.h>
#include <jni.h>

#include <stdio.h>
#include <stdlib.h>
#include <android/log.h>

#include "leveldb/db.h"

#define  LOG_TAG    "AndroidLevelDB"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)


leveldb::DB* db;

extern "C" {
JNIEXPORT jstring JNICALL Java_com_example_hellojni_HelloJni_dbGet(JNIEnv * env, jobject thiz, jstring key1);
};

JNIEXPORT jstring JNICALL Java_com_example_hellojni_HelloJni_dbGet(JNIEnv * env, jobject thiz, jstring key1)
{

	LOGI("In the get ");

	const char* key = env->GetStringUTFChars(key1,0);
	LOGI("Key");
	LOGI(key);

	std::string value;
	leveldb::Status status = db->Get(leveldb::ReadOptions(), key, &value);


	if (status.ok()) {
		const char* re =  value.c_str();
		return env->NewStringUTF(re);
	}else{
		const char* re =  status.ToString().c_str();
		return env->NewStringUTF(re);
	}

}

extern "C" {
JNIEXPORT jstring JNICALL Java_com_example_hellojni_HelloJni_dbPut(JNIEnv * env, jobject thiz, jstring key1, jstring value1);
};

JNIEXPORT jstring JNICALL Java_com_example_hellojni_HelloJni_dbPut(JNIEnv * env, jobject thiz, jstring key1, jstring value1)
{
	LOGI("In the put ");

	const char* key = env->GetStringUTFChars(key1,0);
	const char* value = env->GetStringUTFChars(value1,0);
	LOGI("Key");
	LOGI(key);
	LOGI("Value");
	LOGI(value);

	leveldb::Status status = db->Put(leveldb::WriteOptions(), key, value);
	if (status.ok()) {
		const char* re =  status.ToString().c_str();
		return env->NewStringUTF(value);
	}else{
		const char* re =  status.ToString().c_str();
		return env->NewStringUTF(re);
	}

}
extern "C" {
JNIEXPORT jstring JNICALL Java_com_example_hellojni_HelloJni_dbOpen(JNIEnv* env, jobject thiz, jstring dbpath);


};

JNIEXPORT jstring JNICALL Java_com_example_hellojni_HelloJni_dbOpen(JNIEnv* env, jobject thiz, jstring dbpath)
{
	const char* path = env->GetStringUTFChars(dbpath,0);
	LOGI("Opening database");
	LOGI(path);

	leveldb::Options options;
	options.create_if_missing = true;
	leveldb::Status status = leveldb::DB::Open(options, path, &db);
	const char* re =  status.ToString().c_str();
	return env->NewStringUTF(re);
}

extern "C" {
JNIEXPORT jstring JNICALL Java_com_example_hellojni_HelloJni_dbClose(JNIEnv* env, jobject thiz, jstring dbpath);


};

JNIEXPORT jstring JNICALL Java_com_example_hellojni_HelloJni_dbClose(JNIEnv* env, jobject thiz, jstring dbpath)
{
	delete db;
	LOGI("Closed database");
	return env->NewStringUTF("Closed database");
}
extern "C" {
JNIEXPORT jstring JNICALL Java_com_example_hellojni_HelloJni_dbDelete(JNIEnv * env, jobject thiz, jstring key1);
};

JNIEXPORT jstring JNICALL Java_com_example_hellojni_HelloJni_dbDelete(JNIEnv * env, jobject thiz, jstring key1)
{

	LOGI("In the delete ");

	const char* key = env->GetStringUTFChars(key1,0);
	LOGI("Key");
	LOGI(key);

	leveldb::Status status = db->Delete(leveldb::WriteOptions(), key);


	if (status.ok()) {
		const char* re =  status.ToString().c_str();
		return env->NewStringUTF(re);
	}else{
		const char* re =  status.ToString().c_str();
		return env->NewStringUTF(re);
	}

}
extern "C" {
JNIEXPORT jstring JNICALL Java_com_example_hellojni_HelloJni_GetProperty(JNIEnv * env, jobject thiz);
};

JNIEXPORT jstring JNICALL Java_com_example_hellojni_HelloJni_GetProperty(JNIEnv * env, jobject thiz)
{
	return env->NewStringUTF("calling get property to find out db size etc");
}
