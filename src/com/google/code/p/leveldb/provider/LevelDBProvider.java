package com.google.code.p.leveldb.provider;

import java.io.File;

import com.google.code.p.leveldb.LevelDB;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class LevelDBProvider extends ContentProvider {
	LevelDB db;
	boolean D = true;
	private static final String TAG = "LevelDB";

	protected Context mContext;
	protected String mDBdir;
	protected String mDBname;
	protected int mDBversion = 1;
	public static final String AUTHORITY = "com.google.code.p.leveldb.provider.UnstructuredData";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/data");

	@Override
	public int delete(Uri uri, String arg1, String[] arg2) {
		String result = db.dbDelete(uri.getLastPathSegment());
		if (D)
			Log.d(TAG, "The delete result: " + result);
		if (result != "NotFound") {
			// TODO it is always returning OK even when there was nothing in the
			// database
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public String getType(Uri uri) {
		return "value";
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		if (values.getAsString("key") == null
				|| values.getAsString("value") == null) {
			if (D)
				Log.d(TAG,
						"There was a problem with the values supplied, key:"
								+ values.getAsString("key") + "value: "
								+ values.getAsString("value"));
			return null;
		}
		String result = db.dbPut(values.getAsString("key"),
				values.getAsString("value"));
		// TOOD add better checking
		if (result != "NotFound") {
			Uri noteUri = Uri.withAppendedPath(CONTENT_URI, result);
			return noteUri;
		}
		return null;
	}

	private void initializeDB(Context context, String databasename,
			int databaseversion) {
		mContext = context;
		mDBname = databasename;
		mDBdir = context.getFilesDir().getAbsolutePath() + File.separator
				+ databasename;
		db = new LevelDB(context, databasename, databaseversion);
	}

	@Override
	public boolean onCreate() {
		initializeDB(this.getContext(), "db", mDBversion);
		if (mDBdir != null) {
			db.dbOpen(mDBdir);
		}
		return true;
	}

	/**
	 * Accepts a uri to look up, if it finds nothing returns null. If it finds
	 * something it returns a rather useless cursor with 1 element (the key
	 * value pair itself);
	 */
	@Override
	public Cursor query(final Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		String key = uri.getLastPathSegment();
		if (key == null || key == "") {
			return null;
		}
		String value = db.dbGet(key);
		// TOOD add better checking
		if ( value.equals("NotFound")) {
			return null;
		}

		// TODO Auto-generated method stub
		Cursor c = new Cursor() {

			public void unregisterDataSetObserver(DataSetObserver observer) {
				// TODO Auto-generated method stub

			}

			public void unregisterContentObserver(ContentObserver observer) {
				// TODO Auto-generated method stub

			}

			public void setNotificationUri(ContentResolver cr, Uri uri) {
				// TODO Auto-generated method stub

			}

			public Bundle respond(Bundle extras) {
				// TODO Auto-generated method stub
				return null;
			}

			public boolean requery() {
				// TODO Auto-generated method stub
				return false;
			}

			public void registerDataSetObserver(DataSetObserver observer) {
				// TODO Auto-generated method stub

			}

			public void registerContentObserver(ContentObserver observer) {
				// TODO Auto-generated method stub

			}

			public boolean moveToPrevious() {
				// TODO Auto-generated method stub
				return false;
			}

			public boolean moveToPosition(int position) {
				// TODO Auto-generated method stub
				return false;
			}

			public boolean moveToNext() {
				// TODO Auto-generated method stub
				return false;
			}

			public boolean moveToLast() {
				// TODO Auto-generated method stub
				return false;
			}

			public boolean moveToFirst() {
				// TODO Auto-generated method stub
				return false;
			}

			public boolean move(int offset) {
				// TODO Auto-generated method stub
				return false;
			}

			public boolean isNull(int columnIndex) {
				// TODO Auto-generated method stub
				return false;
			}

			public boolean isLast() {
				// TODO Auto-generated method stub
				return false;
			}

			public boolean isFirst() {
				// TODO Auto-generated method stub
				return false;
			}

			public boolean isClosed() {
				// TODO Auto-generated method stub
				return false;
			}

			public boolean isBeforeFirst() {
				// TODO Auto-generated method stub
				return false;
			}

			public boolean isAfterLast() {
				// TODO Auto-generated method stub
				return false;
			}

			public boolean getWantsAllOnMoveCalls() {
				// TODO Auto-generated method stub
				return false;
			}

			// TODO use the columnIndex once we are able to return more than one
			// row at a time
			public String getString(int columnIndex) {
				String key = uri.getLastPathSegment();
				if (key == null || key == "") {
					return null;
				}
				String value = db.dbGet(key);
				// TOOD add better checking
				if (! value.equals("NotFound")) {
					return value;
				}
				return null;
			}

			public short getShort(int columnIndex) {
				// TODO Auto-generated method stub
				return 0;
			}

			public int getPosition() {
				// TODO Auto-generated method stub
				return 0;
			}

			public long getLong(int columnIndex) {
				// TODO Auto-generated method stub
				return 0;
			}

			public int getInt(int columnIndex) {
				// TODO Auto-generated method stub
				return 0;
			}

			public float getFloat(int columnIndex) {
				// TODO Auto-generated method stub
				return 0;
			}

			public Bundle getExtras() {
				// TODO Auto-generated method stub
				return null;
			}

			public double getDouble(int columnIndex) {
				// TODO Auto-generated method stub
				return 0;
			}

			public int getCount() {
				// TODO Auto-generated method stub
				return 1;
			}

			public String[] getColumnNames() {
				String[] cs = "key,value".split(",");
				return cs;
			}

			public String getColumnName(int columnIndex) {
				// TODO Auto-generated method stub
				return null;
			}

			public int getColumnIndexOrThrow(String columnName)
					throws IllegalArgumentException {
				// TODO Auto-generated method stub
				return 0;
			}

			public int getColumnIndex(String columnName) {
				// TODO Auto-generated method stub
				return 0;
			}

			public int getColumnCount() {
				// TODO Auto-generated method stub
				return 0;
			}

			public byte[] getBlob(int columnIndex) {
				// TODO Auto-generated method stub
				return null;
			}

			public void deactivate() {
				// TODO Auto-generated method stub

			}

			public void copyStringToBuffer(int columnIndex,
					CharArrayBuffer buffer) {
				// TODO Auto-generated method stub

			}

			public void close() {
				// TODO Auto-generated method stub

			}
		};
		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		//TODO make it possible to update multiple values, and not use the uri for this.
		Uri temp = this.insert(uri, values);
		if(temp != null){
			return 1;
		}else{
			return 0;
		}
	}

}
