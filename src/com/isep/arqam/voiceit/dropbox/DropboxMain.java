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

import android.R.string;
//import android.R.string;
import android.app.Activity;
//import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
//import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
//import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
//import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.android.AuthActivity;
//import com.dropbox.client2.exception.DropboxException;
//import com.dropbox.client2.exception.DropboxUnlinkedException;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session.AccessType;
import com.dropbox.client2.session.TokenPair;
import com.isep.arqam.voiceit.R;
import com.isep.arqam.voiceit.VoiceIT_MainActivity;


/****************************************************************************************
 * MemoRecord
 * - Faz a gravação de um novo memo
 ***************************************************************************************/
public class DropboxMain extends Activity {

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
    private LinearLayout mDisplay;
    private Button mUpload;
    private Button mDownload;

    private final String FILE_DIR = "/Memos/";

    final static private int NEW_FILE = 1;
    private String mFileName;

	/************************************************************************************
	 * onCreate
	 ***********************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mFileName = savedInstanceState.getString("mFileName");
        }

        // We create a new AuthSession so that we can use the Dropbox API.
        AndroidAuthSession session = buildSession();
        mApi = new DropboxAPI<AndroidAuthSession>(session);
        
        // Basic Android widgets
        setContentView(R.layout.activity_dropbox_main);


        checkAppKeySetup();

        mSubmit = (Button)findViewById(R.id.auth_button);

        mSubmit.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // This logs you out if you're logged in, or vice versa
                if (mLoggedIn) {
                    logOut();
                } else {
                    // Start the remote authentication
                    mApi.getSession().startAuthentication(DropboxMain.this);
                }
            }
        });

        mDisplay = (LinearLayout)findViewById(R.id.logged_in_display);
        
        // This is the button to upload a file
        mUpload = (Button)findViewById(R.id.upload_button);
        
        mUpload.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	
            	Bundle extras = getIntent().getExtras();
            	String  mFileName= extras.getString("memoName");
            	
            	//File file1 = new File("/sdcard/audiorecordtest.3gp");
            	File file1 = new File(mFileName);
            	UploadFile upload = new UploadFile(DropboxMain.this, mApi, FILE_DIR, file1);
                upload.execute();           
            }
        });
        
        Bundle extras = getIntent().getExtras();
    	String  mFileName= extras.getString("memoName");
    	File file1 = new File(mFileName);
    	UploadFile upload = new UploadFile(DropboxMain.this, mApi, FILE_DIR, file1);
        upload.execute(); 
        
        /*
        // This is the button to download a file
        mDownload = (Button)findViewById(R.id.download_button);

        mDownload.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                DownloadFile download = new DownloadFile(DropBoxTest.this,
                		mApi, FILE_DIR);
                download.execute();
            }
        });
         */
        
        // Display the proper UI state if logged in or not
        setLoggedIn(mApi.getSession().isLinked());

    }
    
	/************************************************************************************
	 * onResume
	 ***********************************************************************************/
    @Override
    protected void onResume() {
        super.onResume();
        AndroidAuthSession session = mApi.getSession();

        // The next part must be inserted in the onResume() method of the
        // activity from which session.startAuthentication() was called, so
        // that Dropbox authentication completes properly.
        if (session.authenticationSuccessful()) {
            try {
                // Mandatory call to complete the auth
                session.finishAuthentication();

                // Store it locally in our app for later use
                TokenPair tokens = session.getAccessTokenPair();
                storeKeys(tokens.key, tokens.secret);
                setLoggedIn(true);
            } catch (IllegalStateException e) {
                showToast("Couldn't authenticate with Dropbox:" + e.getLocalizedMessage());
                Log.i(TAG, "Error authenticating", e);
            }
        }
    }

	/************************************************************************************
	 * onStart
	 ***********************************************************************************/
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()" + this);
    }

	/************************************************************************************
	 * onPause
	 ***********************************************************************************/
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()" + this);
    }

	/************************************************************************************
	 * onStop
	 ***********************************************************************************/
    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop()" + this);
    }

	/************************************************************************************
	 * onDestroy
	 ***********************************************************************************/
    @Override
    public void onDestroy() {
    	super.onDestroy();
        Log.d(TAG, "onDestroy()" + this);
    }
    
	/************************************************************************************
	 * onSaveInstanceState
	 ***********************************************************************************/
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("mFileName", mFileName);
        super.onSaveInstanceState(outState);
    }
    
	/************************************************************************************
	 * onActivityResult
	 * - This is what gets called on finishing a media piece to import
	 ***********************************************************************************/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NEW_FILE) {
            // return from file upload
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = null;
                if (data != null) {
                    uri = data.getData();
                }
                
                if (uri == null && mFileName != null) {
                    uri = Uri.fromFile(new File(mFileName));
                }
                File file = new File(mFileName);
                
                if (uri != null) {
                    UploadFile upload = new UploadFile(this, mApi, FILE_DIR, file);
                    upload.execute();
                    
                }
            } else {
                Log.w(TAG, "Unknown Activity Result from mediaImport: "
                        + resultCode);
            }
        }
    }

	/************************************************************************************
	 * logOut
	 ***********************************************************************************/
    private void logOut() {
        // Remove credentials from the session
        mApi.getSession().unlink();

        // Clear our stored keys
        clearKeys();
        // Change UI state to display logged out version
        setLoggedIn(false);
    }

	/************************************************************************************
	 * setLoggedIn
	 * - Convenience function to change UI state based on being logged in
	 ***********************************************************************************/
    private void setLoggedIn(boolean loggedIn) {
    	mLoggedIn = loggedIn;
    	if (loggedIn) {
    		mSubmit.setText("Unlink from Dropbox");
            mDisplay.setVisibility(View.VISIBLE);
    	} else {
    		mSubmit.setText("Link with Dropbox");
            mDisplay.setVisibility(View.GONE);
    	}
    }

	/************************************************************************************
	 * checkAppKeySetup
	 ***********************************************************************************/
    private void checkAppKeySetup() {
        // Check to make sure that we have a valid app key
        if (APP_KEY.startsWith("CHANGE") ||
                APP_SECRET.startsWith("CHANGE")) {
            showToast("You must apply for an app key and secret from developers.dropbox.com, and add them to the DBRoulette ap before trying it.");
            finish();
            return;
        }

        // Check if the app has set up its manifest properly.
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        String scheme = "db-" + APP_KEY;
        String uri = scheme + "://" + AuthActivity.AUTH_VERSION + "/test";
        testIntent.setData(Uri.parse(uri));
        PackageManager pm = getPackageManager();
        if (0 == pm.queryIntentActivities(testIntent, 0).size()) {
            showToast("URL scheme in your app's " +
                    "manifest is not set up correctly. You should have a " +
                    "com.dropbox.client2.android.AuthActivity with the " +
                    "scheme: " + scheme);
            finish();
        }
    }

	/************************************************************************************
	 * showToast
	 ***********************************************************************************/
    private void showToast(String msg) {
        Toast error = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        error.show();
    }

	/************************************************************************************
	 * getKeys
	 * - Shows keeping the access keys returned from Trusted Authenticator in a local
     *   store, rather than storing user name & password, and re-authenticating each
     *   time (which is not to be done, ever).
     *
     *   @return Array of [access_key, access_secret], or null if none stored
	 ***********************************************************************************/
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
 
	/************************************************************************************
	 * storeKeys
	 * - Shows keeping the access keys returned from Trusted Authenticator in a local
     *   store, rather than storing user name & password, and re-authenticating each
     *   time (which is not to be done, ever).
	 ***********************************************************************************/
    private void storeKeys(String key, String secret) {
        // Save the access key for later
        SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
        Editor edit = prefs.edit();
        edit.putString(ACCESS_KEY_NAME, key);
        edit.putString(ACCESS_SECRET_NAME, secret);
        edit.commit();
    }

	/************************************************************************************
	 * clearKeys
	 ***********************************************************************************/
    private void clearKeys() {
        SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
        Editor edit = prefs.edit();
        edit.clear();
        edit.commit();
    }

	/************************************************************************************
	 * buildSession
	 ***********************************************************************************/
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
}
