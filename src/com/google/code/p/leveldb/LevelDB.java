/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.code.p.leveldb;

public class LevelDB {
	/*
	 * Methods which wrap LevelDB calls, see jni/main.cc for details
	 */
	public static native String dbOpen(String dbpath);

	public static native String dbClose(String dbpath);

	public static native String dbPut(String key1, String value1);

	public static native String dbGet(String key1);

	public static native String dbDelete(String key1);
	
	public static native String dbDestroy(String dbpath);

	/*
	 * A native method that is implemented by the 'hello-jni' native library,
	 * which is packaged with this application.
	 */
	public static native String stringFromJNI();

	/*
	 * This is another native method declaration that is *not* implemented by
	 * 'leveldb'. This is simply to show that you can declare as many native
	 * methods in your Java code as you want, their implementation is searched
	 * in the currently loaded native libraries only the first time you call
	 * them.
	 * 
	 * Trying to call this function will result in a
	 * java.lang.UnsatisfiedLinkError exception !
	 */
	public static native String unimplementedStringFromJNI();

	/*
	 * this is used to load the 'leveldb' library on application startup. The
	 * library has already been unpacked into
	 * /data/data/com.example.HelloJni/lib/libleveldb.so at installation time by
	 * the package manager.
	 */
	static {
		System.loadLibrary("leveldb");
	}
}