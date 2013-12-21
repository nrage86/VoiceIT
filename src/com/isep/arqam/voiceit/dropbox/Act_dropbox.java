/*
 * Copyright (c) 2010-11 Dropbox, Inc.
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package com.isep.arqam.voiceit.dropbox;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session.AccessType;
import com.dropbox.client2.session.TokenPair;
import com.isep.arqam.voiceit.Act_memoRecord;
import com.isep.arqam.voiceit.R;
import com.isep.arqam.voiceit.Act_main;


/**************************************************************************************************
 * Act_dropbox
 *************************************************************************************************/
public class Act_dropbox extends Activity {

	/** Variaveis globais*/
    private static final String TAG = "DropboxMain";


    ///////////////////////////////////////////////////////////////////////////
    //                      Your app-specific settings.                      //
    /////////////////////////////////////////////////////////////////////////// 
    
    // Replace this with your app key and secret assigned by Dropbox.
    // Note that this is a really insecure way to do this, and you shouldn't
    // ship code which contains your key & secret in such an obvious way.
    // Obfuscation is good.
    final static private String APP_KEY = "uqsveq6pi6xodrr";
    final static private String APP_SECRET = "vstidydf4th7bhn";
    
    // If you'd like to change the access type to the full Dropbox instead of
    // an app folder, change this value.
    final static private AccessType ACCESS_TYPE = AccessType.APP_FOLDER;

    ///////////////////////////////////////////////////////////////////////////
    //                      End app-specific settings.                       //
    ///////////////////////////////////////////////////////////////////////////

    // You don't need to change these, leave them alone.
    final static private String ACCOUNT_PREFS_NAME = "prefs";
    final static private String ACCESS_KEY_NAME = "ACCESS_KEY";
    final static private String ACCESS_SECRET_NAME = "ACCESS_SECRET";

    DropboxAPI<AndroidAuthSession> mApi;

    private boolean mLoggedIn;
    // Android widgets
    private Button mSubmit;

    private final String FILE_DIR = "/Memos/";

	/**********************************************************************************************
	 * onCreate
	 *********************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        /** Salta esta activity caso não haja ligação ha internet */
        boolean isConnected = checkInternetConnection();
        if (!isConnected){
        	Intent disconnected = new Intent(Act_dropbox.this, Act_main.class);
        	disconnected.putExtra("currentFrag", "1");
			Act_dropbox.this.startActivity(disconnected);
        }
        
        setContentView(R.layout.activity_act_dropbox);

        /** We create a new AuthSession so that we can use the Dropbox API.*/
        AndroidAuthSession session = buildSession();
        mApi = new DropboxAPI<AndroidAuthSession>(session);

        /** Botao para fazer login ou logout */
        mSubmit = (Button)findViewById(R.id.auth_button);
        mSubmit.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // This logs you out if you're logged in, or vice versa
                if (mLoggedIn) {
                    logOut();
                } else {
                    // Start the remote authentication
                    mApi.getSession().startAuthentication(Act_dropbox.this);
                }
            }
        });

        /** Display the proper UI state if logged in or not*/
        setLoggedIn(mApi.getSession().isLinked());
    }
    
	/**********************************************************************************************
	 * onResume
	 *********************************************************************************************/
    @Override
    protected void onResume() {
        super.onResume();
        AndroidAuthSession session = mApi.getSession();

        /** The next part must be inserted in the onResume() method of the
            activity from which session.startAuthentication() was called, so
            that Dropbox authentication completes properly.*/
        if (session.authenticationSuccessful()) {
            try {
                // Mandatory call to complete the auth
                session.finishAuthentication();

                // Store it locally in our app for later use
                TokenPair tokens = session.getAccessTokenPair();
                storeKeys(tokens.key, tokens.secret);
                setLoggedIn(true);
                mLoggedIn=true;
                
            } catch (IllegalStateException e) {
                showToast("Couldn't authenticate with Dropbox:" + e.getLocalizedMessage());
                Log.i(TAG, "Error authenticating", e);
            }
        }
        
        /** Obtem os parametros extra passados para esta activity*/
        Bundle extras = getIntent().getExtras();
        
        if(mApi.getSession().isLinked()){
        	
        	
        	
        	
        	
        	
        	
            /** Verifica se existe algum memo no tlm que nao exista no Dropbox.
             * 	Se for o caso ent�o guarda esse memo num array para fazer upload*/	
        	/*
        	for (String localMemoName : localMemoList) {
        		existsInDropbox=false;
        		String[] splitLocalMemoName = localMemoName.split("\\.");
        		
                for (String dropboxMemoName : dropboxMemoList) {	
                	splitDropboxMemoName = dropboxMemoName.split("\\.");
	        		
					if(splitDropboxMemoName[0].equals(splitLocalMemoName[0]))
						existsInDropbox=true;
                }
                if(!existsInDropbox){
                	
					String localMemoPath = Environment.getExternalStorageDirectory().getAbsolutePath();
					localMemoPath += "/"+ localMemoName;
                	memosToUpload.add(localMemoPath);
                }
			}
        	*/
        	
        	
        	
        	
        	
        	
        	
	        /** Faz o download de ficheiros do dropbox*/
	    	if(extras.getString("DropboxTask").equals("download")){
	    		AsyncTask_down download = new AsyncTask_down(Act_dropbox.this, mApi, FILE_DIR);
	    		download.execute();
	    	}
	    	/** Faz o upload de ficheiros para o dropbox*/
	    	if(extras.getString("DropboxTask").equals("upload")){
	    		String  memoName= extras.getString("memoName");
    			File memoFile = new File(memoName);
            	AsyncTask_up upload = new AsyncTask_up(Act_dropbox.this, mApi, FILE_DIR,
            			memoFile,null,0);
                upload.execute();
	    	}
	    	
	    	/** Faz o upload de fixeiros para o dropbox*/
	    	if(extras.getString("DropboxTask").equals("multiUpload")){
	            ArrayList<String> memosToUpload = extras.getStringArrayList("memosToUpload");          
	
	        	//File memoFile = new File(memoName);
            	AsyncTask_up upload = new AsyncTask_up(Act_dropbox.this, mApi, FILE_DIR,
            			null,memosToUpload,0);	
                upload.execute();
	    	} 

        }
        
        

            
    }

	/**********************************************************************************************
	 * onStart
	 *********************************************************************************************/
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()" + this);
    }

	/**********************************************************************************************
	 * onPause
	 *********************************************************************************************/
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()" + this);
    }

	/**********************************************************************************************
	 * onStop
	 *********************************************************************************************/
    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop()" + this);
    }

	/**********************************************************************************************
	 * onDestroy
	 *********************************************************************************************/
    @Override
    public void onDestroy() {
    	super.onDestroy();
        Log.d(TAG, "onDestroy()" + this);
    }

	/**********************************************************************************************
	 * logOut
	 *********************************************************************************************/
    private void logOut() {
        // Remove credentials from the session
        mApi.getSession().unlink();
        
        // Clear our stored keys
        clearKeys();
        
        // Change UI state to display logged out version
        setLoggedIn(false);
    }

	/**********************************************************************************************
	 * setLoggedIn
	 * - Convenience function to change UI state based on being logged in
	 *********************************************************************************************/
    private void setLoggedIn(boolean loggedIn) {
    	mLoggedIn = loggedIn;
    	if (loggedIn) {
    		mSubmit.setText("Unlink from Dropbox");
    	} else {
    		mSubmit.setText("Link with Dropbox");
    	}
    }


	/**********************************************************************************************
	 * showToast
	 * - Mostra uma toast message no ecr�
	 *********************************************************************************************/
    private void showToast(String msg) {
        Toast error = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        error.show();
    }

	/**********************************************************************************************
	 * getKeys
	 * - Shows keeping the access keys returned from Trusted Authenticator in a local
     *   store, rather than storing user name & password, and re-authenticating each
     *   time (which is not to be done, ever).
     *
     *   @return Array of [access_key, access_secret], or null if none stored
	 *********************************************************************************************/
    private String[] getKeys() {
        SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
        String key = prefs.getString(ACCESS_KEY_NAME, null);
        String secret = prefs.getString(ACCESS_SECRET_NAME, null);
        if (key != null && secret != null) {
        	String[] ret = new String[2];
        	ret[0] = key;
        	ret[1] = secret;
        	return ret;
        } else {
        	return null;
        }
    }
 
	/**********************************************************************************************
	 * storeKeys
	 * - Shows keeping the access keys returned from Trusted Authenticator in a local
     *   store, rather than storing user name & password, and re-authenticating each
     *   time (which is not to be done, ever).
	 *********************************************************************************************/
    private void storeKeys(String key, String secret) {
        // Save the access key for later
        SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
        Editor edit = prefs.edit();
        edit.putString(ACCESS_KEY_NAME, key);
        edit.putString(ACCESS_SECRET_NAME, secret);
        edit.commit();
    }

	/**********************************************************************************************
	 * clearKeys
	 *********************************************************************************************/
    private void clearKeys() {
        SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
        Editor edit = prefs.edit();
        edit.clear();
        edit.commit();
    }

	/**********************************************************************************************
	 * buildSession
	 *********************************************************************************************/
    private AndroidAuthSession buildSession() {
        AppKeyPair appKeyPair = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session;

        String[] stored = getKeys();
        if (stored != null) {
            AccessTokenPair accessToken = new AccessTokenPair(stored[0], stored[1]);
            session = new AndroidAuthSession(appKeyPair, ACCESS_TYPE, accessToken);
        } else {
            session = new AndroidAuthSession(appKeyPair, ACCESS_TYPE);
        }
        
        return session;
    }
    
  //FUNÇÃO QUE TESTA A EXISTÊNCIA DE LIGAÇÃO HÁ INTERNET
    private boolean checkInternetConnection() {
    	ConnectivityManager conMgr = (ConnectivityManager) getSystemService (Context.CONNECTIVITY_SERVICE);
    	
    	if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected()) {
    		return true;
    	} else {
    		return false;
    	}

    } 
}
