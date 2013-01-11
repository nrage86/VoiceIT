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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.exception.DropboxIOException;
import com.dropbox.client2.exception.DropboxParseException;
import com.dropbox.client2.exception.DropboxPartialFileException;
import com.dropbox.client2.exception.DropboxServerException;
import com.dropbox.client2.exception.DropboxUnlinkedException;
import com.isep.arqam.voiceit.MemoArchive;
import com.isep.arqam.voiceit.Voiceit_main;


/**************************************************************************************************
 * DownloadFile
 * - Here we show getting metadata for a directory and downloading a file in a
 *   background thread, trying to show typical exception handling and flow of
 *   control for an app that downloads a file from Dropbox.
 *************************************************************************************************/
public class DownloadFile extends AsyncTask<Void, Long, Boolean> {

	/** Variaveis globais*/
    private Context mContext;
    private final ProgressDialog mDialog;
    private DropboxAPI<?> mApi;
    private String mPath;
    private Long mFileLen;
    private String mErrorMsg;
    
	private ArrayList<String> filePath = new ArrayList<String>();
	private ArrayList<String> localMemoList = new ArrayList<String>(); 
    private ArrayList<String> memosToUpload=new ArrayList<String>();
	private File sdcard = Environment.getExternalStorageDirectory();
	private File[] sdcardFiles = sdcard.listFiles();

	/**********************************************************************************************
	 * DownloadFile
	 *********************************************************************************************/
    public DownloadFile(Context context, DropboxAPI<?> api, String dropboxPath) {
        // We set the context this way so we don't accidentally leak activities
        mContext = context.getApplicationContext();
        
        mApi = api;
        mPath = dropboxPath;

        // Dialog box
        mDialog = new ProgressDialog(context);
        mDialog.setMessage("A sincronizar com o Dropbox");        
        mDialog.show();
    }

	/**********************************************************************************************
	 * doInBackground
	 *********************************************************************************************/
    @Override
    protected Boolean doInBackground(Void... params) {
        
        ArrayList<String> dropboxMemoList=new ArrayList<String>();
        String[] splitDropboxMemoName;
        Boolean existsInLocal;
        Boolean existsInDropbox;
        
    	try {
            // Get the metadata for a directory
            Entry dirent = mApi.metadata(mPath, 1000, null, true, null);

            if (!dirent.isDir || dirent.contents == null) {
                // It's not a directory, or there's nothing in it
                mErrorMsg = "File or empty directory";
                return false;
            }

            /** Vai buscar todos os ficheiros na pasta do dropbox e guarda-os num array*/
            for (Entry ent: dirent.contents) 
            {                   
                dropboxMemoList.add(new String(ent.fileName()));
                mFileLen = ent.bytes;
            }
           
            getLocalMemoList();
            
            /** Verifica se existe algum memo no Dropbox que nao exista localmente no tlm.
             * 	Se for o caso ent�o faz o download desse memo*/
            for (String dropboxMemoName : dropboxMemoList) {	
            	existsInLocal=false;
            	splitDropboxMemoName = dropboxMemoName.split("\\.");
            	
            	for (String localMemoName : localMemoList) {
            		String[] splitLocalMemoName = localMemoName.split("\\.");
            		
    				if(splitDropboxMemoName[0].equals(splitLocalMemoName[0]))
    					existsInLocal=true;
				}
            	
            	if(!existsInLocal){
	                File file = new File(sdcard + "/" + splitDropboxMemoName[0]+".3gp");
	                FileOutputStream outputStream = null;
	                
	                // Get file.
		            try {
		                outputStream = new FileOutputStream(file);
		                mApi.getFile("/Memos/" + splitDropboxMemoName[0] + ".3gp", null,
		                		outputStream, null);

		            } catch (DropboxException e) {
		                Log.e("DbExampleLog", "Something went wrong while downloading.");
		            } catch (FileNotFoundException e) {
		                Log.e("DbExampleLog", "File not found.");
		            } finally {
		                if (outputStream != null) {
		                    try {
		                        outputStream.close();
		                    } catch (IOException e) {}
		                }
		            }
		            
		            return true;    		
            	}
			}
            
            /** Verifica se existe algum memo no tlm que nao exista no Dropbox.
             * 	Se for o caso ent�o guarda esse memo num array para fazer upload*/	
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
            
        } catch (DropboxUnlinkedException e) {
            // The AuthSession wasn't properly authenticated or user unlinked.
        	mErrorMsg = "AuthSession failed.";
        } catch (DropboxPartialFileException e) {
            // We canceled the operation
            mErrorMsg = "Download canceled";
        } catch (DropboxServerException e) {
            // Server-side exception.  These are examples of what could happen,
            // but we don't do anything special with them here.
            if (e.error == DropboxServerException._304_NOT_MODIFIED) {
                // won't happen since we don't pass in revision with metadata
            } else if (e.error == DropboxServerException._401_UNAUTHORIZED) {
                // Unauthorized, so we should unlink them.  You may want to
                // automatically log the user out in this case.
            } else if (e.error == DropboxServerException._403_FORBIDDEN) {
                // Not allowed to access this
            } else if (e.error == DropboxServerException._404_NOT_FOUND) {
                // path not found (or if it was the thumbnail, can't be
                // thumbnailed)
            } else if (e.error == DropboxServerException._406_NOT_ACCEPTABLE) {
                // too many entries to return
            } else if (e.error == DropboxServerException._415_UNSUPPORTED_MEDIA) {
                // can't be thumbnailed
            } else if (e.error == DropboxServerException._507_INSUFFICIENT_STORAGE) {
                // user is over quota
            } else {
                // Something else
            }
            // This gets the Dropbox error, translated into the user's language
            mErrorMsg = e.body.userError;
            if (mErrorMsg == null) {
                mErrorMsg = e.body.error;
            }
        } catch (DropboxIOException e) {
            // Happens all the time, probably want to retry automatically.
            mErrorMsg = "Network error.  Try again.";
        } catch (DropboxParseException e) {
            // Probably due to Dropbox server restarting, should retry
            mErrorMsg = "Dropbox error.  Try again.";
        } catch (DropboxException e) {
            // Unknown error
            mErrorMsg = "Unknown error.  Try again.";
        }
    	
        return true;
    }

	/**********************************************************************************************
	 * getLocalMemoList
	 *********************************************************************************************/
    protected void getLocalMemoList() {
		for(int n=0; n < sdcardFiles.length; n++)    
	     {
	          File file = sdcardFiles[n];
	          filePath.add(file.getPath());
 
             String fileName=file.getName();
             String ext = fileName.substring(fileName.lastIndexOf('.')+1, fileName.length());
             if(ext.equals("3gp")||ext.equals("mp4"))
             {
            	 localMemoList.add(file.getName());
             }
	     }  
    }
    
	/**********************************************************************************************
	 * onProgressUpdate
	 *********************************************************************************************/
    @Override
    protected void onProgressUpdate(Long... progress) {
        int percent = (int)(100.0*(double)progress[0]/mFileLen + 0.5);
        mDialog.setProgress(percent);
    }

	/**********************************************************************************************
	 * onPostExecute
	 *********************************************************************************************/
    @Override
    protected void onPostExecute(Boolean result) {
        mDialog.dismiss();
        if (result) {
        	// Success msg
        	showToast("Sincroniza��o concluida");
        	Intent myIntent;
        	
        	
        	
        	
        	
        	/*
        	Intent myIntent = new Intent(mContext, MemoArchive.class);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
        	mContext.startActivity(myIntent);
			*/
        	
        	if(memosToUpload.size()>0){
	        	myIntent = new Intent(mContext, DropboxMain.class);
	            myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
	        	myIntent.putExtra("memosToUpload", memosToUpload); //envia o caminho do ficheiro para a activity DropboxTest
				myIntent.putExtra("DropboxTask", "multiUpload");
				mContext.startActivity(myIntent);
        	}
        	else{
            	myIntent = new Intent(mContext, MemoArchive.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
            	mContext.startActivity(myIntent);
        	}	
        	
        	
        	
        	
        	
        	
        } else {
            // Couldn't download it, so show an error
            showToast(mErrorMsg);
        }
    }

	/**********************************************************************************************
	 * showToast
	 *********************************************************************************************/
    private void showToast(String msg) {
        Toast error = Toast.makeText(mContext, msg, Toast.LENGTH_LONG);
        error.show();
    }
}
