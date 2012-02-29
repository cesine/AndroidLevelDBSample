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
package com.example.hellojni;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.content.res.AssetManager;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;


public class HelloJni extends Activity
{
	
	String mDBdir;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        /*
         * Use the files dir to store the database /data/data/package..../db
         */
        mDBdir =  this.getFilesDir().getAbsolutePath() + File.separator+"db" ;
		new File(mDBdir).mkdirs();
		
    }
    
    @Override
	protected void onResume() {
    	/*
    	 * Sample Database code:
    	 * Open the database using the path to its directory
    	 * Insert some keys,
    	 * Delete a key, 
    	 * Create a TextView and show the value of a key retrieved from the DB.
    	 */
		dbOpen(mDBdir);
		dbPut("firstkey","this is the value of the first key");
		dbPut("secondkey","this is the value of the first key");
		dbPut("keyToDelete","this is the value of the key that i want to delete");
		dbPut("fourthkey","this is the value of the fourth key");
		dbDelete("keyToDelete");
		
		TextView  tv = new TextView(this);
		tv.setText( dbGet("fourthkey")  );
        setContentView(tv);
        
        ArrayList<String> keystoquery= new ArrayList<String>();
        try {
        	AssetManager assetManager = getAssets();
    	  	InputStream in = assetManager.open("sample.json");
    	  	BufferedReader sourcefile = new BufferedReader(new InputStreamReader(in, "UTF-8"));
    	  	String contents = "";
    	  	String line = "";
    	  	while ((line = sourcefile.readLine()) != null) {
    	  		contents = contents+"\n"+line;
    	  	}
    	  	sourcefile.close();
    	  	JSONObject json = new JSONObject(contents);
			Iterator<String> keys = json.keys();
			while (keys.hasNext()) {
				String key = (String) keys.next();
				keystoquery.add(key);
				dbPut(key, json.get(key).toString());
			}
	    	   
			int j = json.length()-1;
			tv.setText( "element"+j );
	        setContentView(tv);
	        
		} catch (IOException e) {
			Toast.makeText(this, "File read problem"+e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
			tv.setText( "File read problem"+e.getLocalizedMessage() );
	        setContentView(tv);
		} 
        catch (JSONException e) {
			Toast.makeText(this, "Json problem"+e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
			tv.setText( "JSON problem"+e.getLocalizedMessage() );
        	setContentView(tv);
        }

		/*
		 * Query entries randomly
		 */
        long startime = System.currentTimeMillis();
		int maxkey = keystoquery.size() -1;
		int querycount = 1000;
		Random randomGenerator = new Random();
	    for (int k = 0; k < querycount; k++){
			int randomKey = randomGenerator.nextInt(maxkey);
		    String it = dbGet(keystoquery.get(randomKey) );
		}
        long endtime = System.currentTimeMillis();
        long querytime = (endtime-startime);
        tv.setText( "Random quering of "+querycount+" entries took this many miliseconds: "+querytime );
    	setContentView(tv);
        
		super.onResume();
	}
    private String readInFile(String filePath) throws IOException {
		File currentFile = new File(filePath);
		BufferedReader sourcefile = new BufferedReader(new InputStreamReader(
				new FileInputStream(currentFile), "UTF-8"));
		String contents = "";
		String line = "";
		
		while ((line = sourcefile.readLine()) != null) {

			contents = contents+"\n"+line;
			
		}
		sourcefile.close();
		return contents ;
	}
    /*
     * Methods which wrap LevelDB calls, see jni/main.cc for details
     */
    public native String  dbOpen(String dbpath);
    public native String  dbClose(String dbpath);
    public native String  dbPut(String key1, String value1);
    public native String  dbGet(String key1);
    public native String  dbDelete(String key1);
    
    /* A native method that is implemented by the
     * 'hello-jni' native library, which is packaged
     * with this application.
     */
    public native String  stringFromJNI();
    
    /* This is another native method declaration that is *not*
     * implemented by 'leveldb'. This is simply to show that
     * you can declare as many native methods in your Java code
     * as you want, their implementation is searched in the
     * currently loaded native libraries only the first time
     * you call them.
     *
     * Trying to call this function will result in a
     * java.lang.UnsatisfiedLinkError exception !
     */
    public native String  unimplementedStringFromJNI();

    /* this is used to load the 'leveldb' library on application
     * startup. The library has already been unpacked into
     * /data/data/com.example.HelloJni/lib/libleveldb.so at
     * installation time by the package manager.
     */
    static {
        System.loadLibrary("leveldb");
    }

	
	@Override
	protected void onPause() {
		super.onPause();
		/*
		 * Close the db in the onPause to not waste memory
		 */
		dbClose(mDBdir);
	}
	
    
}

