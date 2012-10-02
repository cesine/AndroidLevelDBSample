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
bool isDBopen;
char* databasePath;

extern "C" {
JNIEXPORT jstring JNICALL Java_com_google_code_p_leveldb_LevelDB_dbGet(JNIEnv * env, jobject thiz, jstring key1);
};

JNIEXPORT jstring JNICALL Java_com_google_code_p_leveldb_LevelDB_dbGet(JNIEnv * env, jobject thiz, jstring key1)
{
	//LOGI("In the get ");
	if(! isDBopen){
		LOGE("Trying to get when DB wasn't open");
//		LOGI("Opening database");

		leveldb::Options options;
		options.create_if_missing = true;
		leveldb::Status status = leveldb::DB::Open(options, databasePath, &db);
		if (status.ok()) {
			LOGI("Opened database");
			isDBopen = true;
		}else{
			LOGE("Failed to open database");
			isDBopen = false;
			databasePath = const_cast<char*>("");
			return env->NewStringUTF("NotFound");
		}
	}
	const char* key = env->GetStringUTFChars(key1,0);
	//LOGI("Key");
//	LOGI(key);

	std::string value;
	leveldb::Status status = db->Get(leveldb::ReadOptions(), key, &value);


	if (status.ok()) {
		const char* re =  value.c_str();
//		LOGI(re);
		return env->NewStringUTF(re);
	}else{
		const char* re =  status.ToString().c_str();
//		LOGI(re);
		return env->NewStringUTF("NotFound");
	}

}

extern "C" {
JNIEXPORT jstring JNICALL Java_com_google_code_p_leveldb_LevelDB_dbPut(JNIEnv * env, jobject thiz, jstring key1, jstring value1);
};

JNIEXPORT jstring JNICALL Java_com_google_code_p_leveldb_LevelDB_dbPut(JNIEnv * env, jobject thiz, jstring key1, jstring value1)
{
	if(! isDBopen){
		LOGE("Trying to put when DB wasn't open");
//		LOGI("Opening database");
		leveldb::Options options;
		options.create_if_missing = true;
		leveldb::Status status = leveldb::DB::Open(options, databasePath, &db);
		if (status.ok()) {
			LOGI("Opened database");
			isDBopen = true;
		}else{
			LOGE("Failed to open database");
			isDBopen = false;
			databasePath = const_cast<char*>("");
			return env->NewStringUTF("NotFound");
		}
	}
//	LOGI("In the put ");

	const char* key = env->GetStringUTFChars(key1,0);
	const char* value = env->GetStringUTFChars(value1,0);
	//LOGI("Key");
//	LOGI(key);
	//LOGI("Value");
//	LOGI(value);

	leveldb::Status status = db->Put(leveldb::WriteOptions(), key, value);
	if (status.ok()) {
		const char* re =  status.ToString().c_str();
//		LOGI(re);
		return env->NewStringUTF(key);
	}else{
		const char* re =  status.ToString().c_str();
		return env->NewStringUTF("NotFound");
	}

}
extern "C" {
JNIEXPORT jstring JNICALL Java_com_google_code_p_leveldb_LevelDB_dbOpen(JNIEnv* env, jobject thiz, jstring dbpath);
};

JNIEXPORT jstring JNICALL Java_com_google_code_p_leveldb_LevelDB_dbOpen(JNIEnv* env, jobject thiz, jstring dbpath)
{
	if(isDBopen){
		LOGI("DB was already open");
		return env->NewStringUTF("DB was already open");
	}
	const char* path = env->GetStringUTFChars(dbpath,0);
//	LOGI("Opening database");

	leveldb::Options options;
	options.create_if_missing = true;
	leveldb::Status status = leveldb::DB::Open(options, path, &db);
	if (status.ok()) {
		LOGI(path);
		LOGI("Opened database");
		isDBopen = true;
		databasePath = const_cast<char*>(path);
	}else{
		LOGE("Failed to open database");
		isDBopen = false;
		databasePath = const_cast<char*>("");
	}
	const char* re =  status.ToString().c_str();
	return env->NewStringUTF(re);
}

extern "C" {
JNIEXPORT jstring JNICALL Java_com_google_code_p_leveldb_LevelDB_dbClose(JNIEnv* env, jobject thiz, jstring dbpath);

};

JNIEXPORT jstring JNICALL Java_com_google_code_p_leveldb_LevelDB_dbClose(JNIEnv* env, jobject thiz, jstring dbpath)
{
	if(isDBopen){
		delete db;
		isDBopen = false;
		LOGI("Closed database");
	}else{
		LOGI("DB was already closed.");
	}
	return env->NewStringUTF("Closed database");
}
extern "C" {
JNIEXPORT jstring JNICALL Java_com_google_code_p_leveldb_LevelDB_dbDestroy(JNIEnv* env, jobject thiz, jstring dbpath);

};

JNIEXPORT jstring JNICALL Java_com_google_code_p_leveldb_LevelDB_dbDestroy(JNIEnv* env, jobject thiz, jstring dbpath)
{
	if(isDBopen){
		delete db;
	}
	const char* path = env->GetStringUTFChars(dbpath,0);
	leveldb::Options options;
	options.create_if_missing = true;
	leveldb::Status status = DestroyDB(path, options);
	LOGI("Destroyed (ie, cleared) database");

	/*re-open database */
	status = leveldb::DB::Open(options, path, &db);
	if (status.ok()) {
		LOGI(path);
		LOGI("Opened database");
		isDBopen = true;
		databasePath = const_cast<char*>(path);
	}else{
		LOGE("Failed to open database");
		isDBopen = false;
		databasePath = const_cast<char*>("");
	}
	return env->NewStringUTF("Destroyed (ie, cleared) database");
}
extern "C" {
JNIEXPORT jstring JNICALL Java_com_google_code_p_leveldb_LevelDB_dbDelete(JNIEnv * env, jobject thiz, jstring key1);
};

JNIEXPORT jstring JNICALL Java_com_google_code_p_leveldb_LevelDB_dbDelete(JNIEnv * env, jobject thiz, jstring key1)
{
	if(! isDBopen){
		LOGE("Trying to delete when DB wasn't open");
//		LOGI("Opening database");
		leveldb::Options options;
		options.create_if_missing = true;
		leveldb::Status status = leveldb::DB::Open(options, databasePath, &db);
		if (status.ok()) {
			LOGI("Opened database");
			isDBopen = true;
		}else{
			LOGE("Failed to open database");
			isDBopen = false;
			databasePath = const_cast<char*>("");
			return env->NewStringUTF("NotFound");
		}
	}
	LOGI("In the delete ");

	const char* key = env->GetStringUTFChars(key1,0);
	//LOGI("Key");
//	LOGI(key);

	leveldb::Status status = db->Delete(leveldb::WriteOptions(), key);
	if (status.ok()) {
		const char* re =  status.ToString().c_str();
		return env->NewStringUTF(re);
	}else{
		const char* re =  status.ToString().c_str();
		return env->NewStringUTF("NotFound");
	}

}
extern "C" {
JNIEXPORT jstring JNICALL Java_com_google_code_p_leveldb_LevelDB_GetProperty(JNIEnv * env, jobject thiz);
};

JNIEXPORT jstring JNICALL Java_com_google_code_p_leveldb_LevelDB_GetProperty(JNIEnv * env, jobject thiz)
{
	return env->NewStringUTF("calling get property to find out db size etc");
}
