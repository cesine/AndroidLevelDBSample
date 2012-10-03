package com.google.code.p.leveldb;

import java.io.File;

import android.content.Context;

public class LevelDB {
	public String mDBdir;
	public Context mContext;
	public String mDatabaseName;
	public int mDatabaseVersion;
	public int mContentCount = 0;
	public LevelDB(Context context, String databasename, int databaseversion) {
		mContext = context;
		mDatabaseName = databasename;
		mDatabaseVersion = databaseversion;
		mDBdir = mContext.getFilesDir().getAbsolutePath() + File.separator
				+ mDatabaseName;
		new File(mDBdir).mkdirs();
	}
	/*
	 * Methods which wrap LevelDB calls, see jni/main.cc for details
	 */
	public native String dbOpen(String dbpath);

	public native String dbClose(String dbpath);

	public native String dbPut(String key1, String value1);

	public native String dbGet(String key1);

	public native String dbDelete(String key1);

	public native String dbDestroy(String dbpath);

	/*
	 * A native method that is implemented by the 'hello-jni' native library,
	 * which is packaged with this application.
	 */
	public native String stringFromJNI();

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
	public native String unimplementedStringFromJNI();

	/*
	 * This is used to load the 'leveldb' library on application startup. The
	 * library has already been unpacked into
	 * /data/data/com.example.HelloJni/lib/libleveldb.so at installation time by
	 * the package manager.
	 */
	{
		System.loadLibrary("leveldb");
	}
}