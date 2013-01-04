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
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.DropboxFileInfo;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.DropboxAPI.ThumbFormat;
import com.dropbox.client2.DropboxAPI.ThumbSize;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.exception.DropboxIOException;
import com.dropbox.client2.exception.DropboxParseException;
import com.dropbox.client2.exception.DropboxPartialFileException;
import com.dropbox.client2.exception.DropboxServerException;
import com.dropbox.client2.exception.DropboxUnlinkedException;
import com.isep.arqam.voiceit.MemoArchive;
import com.isep.arqam.voiceit.VoiceIT_MainActivity;


/****************************************************************************************
 * DownloadFile
 * - Here we show getting metadata for a directory and downloading a file in a
 *   background thread, trying to show typical exception handling and flow of
 *   control for an app that downloads a file from Dropbox.
 ***************************************************************************************/
public class DownloadFile extends AsyncTask<Void, Long, Boolean> {

	/** Variaveis globais*/
    private Context mContext;
    private final ProgressDialog mDialog;
    private DropboxAPI<?> mApi;
    private String mPath;
    private ImageView mView;
    private Drawable mDrawable;
    private FileOutputStream mFos;
    private boolean mCanceled;
    private Long mFileLen;
    private String mErrorMsg;

    // Note that, since we use a single file name here for simplicity, you
    // won't be able to use this code for two simultaneous downloads.
    private final static String FILE_NAME = "testeDownload.mp4";

	/************************************************************************************
	 * DownloadFile
	 ***********************************************************************************/
    public DownloadFile(Context context, DropboxAPI<?> api,
            String dropboxPath) {
        // We set the context this way so we don't accidentally leak activities
        mContext = context.getApplicationContext();

        mApi = api;
        mPath = dropboxPath;
        //mView = view;

        
        
        mDialog = new ProgressDialog(context);
        mDialog.setMessage("A sincronizar com o Dropbox");
        mDialog.setButton("Cancel", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                mCanceled = true;
                mErrorMsg = "Canceled";

                // This will cancel the getThumbnail operation by closing
                // its stream
                if (mFos != null) {
                    try {
                        mFos.close();
                    } catch (IOException e) {
                    }
                }
            }
        });

        mDialog.show();
    }

	/************************************************************************************
	 * doInBackground
	 ***********************************************************************************/
    @Override
    protected Boolean doInBackground(Void... params) {
        
    	try {
            if (mCanceled) {
                return false;
            }

            
            
            
            
            
            
            
            
            
            
            // Get the metadata for a directory
            Entry dirent = mApi.metadata(mPath, 1000, null, true, null);

            if (!dirent.isDir || dirent.contents == null) {
                // It's not a directory, or there's nothing in it
                mErrorMsg = "File or empty directory";
                return false;
            }
         
            
            int i=0;
            
            ArrayList<Entry> files = new ArrayList<Entry>();
            ArrayList<String> dropboxFileList=new ArrayList<String>();

            for (Entry ent: dirent.contents) 
            {
                files.add(ent);// Add it to the list of thumbs we can choose from                       
                //dir = new ArrayList<String>();
                //dir.add(new String(files.get(i++).path));
                dropboxFileList.add(new String(ent.fileName()));
                mFileLen = ent.bytes;
            }
            
            i=0;
           
            
            
            
            /*
            // Make a list of everything in it that we can get a thumbnail for
            ArrayList<Entry> thumbs = new ArrayList<Entry>();
            for (Entry ent: dirent.contents) {
                // Add it to the list of thumbs we can choose from
                thumbs.add(ent);
            }

            if (mCanceled) {
                return false;
            }

            if (thumbs.size() == 0) {
                // No thumbs in that directory
                mErrorMsg = "No pictures in that directory";
                return false;
            }

            // Now pick a random one
            int index = (int)(Math.random() * thumbs.size());
            Entry ent = thumbs.get(index);
            String path = ent.path;
            mFileLen = ent.bytes;
            */
            
            
    		ArrayList<String> path = new ArrayList<String>();
    		ArrayList<String> memosList = new ArrayList<String>();  
    	     
    		File sdcard = Environment.getExternalStorageDirectory();
    		File[] sdcardFiles = sdcard.listFiles();
         // Lista os memos da pasta /sdcard do tlm numa listview no ecra
    		for(int n=0; n < sdcardFiles.length; n++)    
    	     {
    	          File file = sdcardFiles[n];
    	          path.add(file.getPath());
      
                  String filename=file.getName();
                  String ext = filename.substring(filename.lastIndexOf('.')+1, filename.length());
                  if(ext.equals("3gp")||ext.equals("mp4"))
                  {
                	  memosList.add(file.getName());
                  }
    	     }   
            
            
            
            
            for (String dropboxMemoName : dropboxFileList) {
            	
            	Boolean flag=false;
            	String[] splitDropboxMemoName = dropboxMemoName.split("\\.");
            	
            	for (String localMemoName : memosList) {
            		
            		String[] splitLocalMemoName = localMemoName.split("\\.");
            		
    				if(splitDropboxMemoName[0].equals(splitLocalMemoName[0]))
    					flag=true;
					//if(true)
					
				
				}
            	
            	if(!flag){
            		// Get file.
		            FileOutputStream outputStream = null;
		            try {
		                File file = new File("/sdcard/"+splitDropboxMemoName[0]+".3gp");
		                
		                outputStream = new FileOutputStream(file);
		                DropboxFileInfo info = mApi.getFile("/Memos/" + splitDropboxMemoName[0] + ".3gp",
		                		null, outputStream, null);

		                //Log.i("DbExampleLog", "The file's rev is: " + info.getMetadata().rev);
		                // /path/to/new/file.txt now has stuff in it.
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
		            
		            if (mCanceled) {
		                return false;
		            }
		            
		            return true;    		
            	}
			}
            


            //mDrawable = Drawable.createFromPath(cachePath);
            // We must have a legitimate picture
              
              
             
            

        } catch (DropboxUnlinkedException e) {
            // The AuthSession wasn't properly authenticated or user unlinked.
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

	/************************************************************************************
	 * onProgressUpdate
	 ***********************************************************************************/
    @Override
    protected void onProgressUpdate(Long... progress) {
        int percent = (int)(100.0*(double)progress[0]/mFileLen + 0.5);
        mDialog.setProgress(percent);
    }

	/************************************************************************************
	 * onPostExecute
	 ***********************************************************************************/
    @Override
    protected void onPostExecute(Boolean result) {
        mDialog.dismiss();
        if (result) {
            // Success msg
        	showToast("File successfully downloaded");
        	
        	
        	Intent myIntent = new Intent(mContext, MemoArchive.class);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
        	mContext.startActivity(myIntent);

            
        } else {
            // Couldn't download it, so show an error
            showToast(mErrorMsg);
        }
    }

	/************************************************************************************
	 * showToast
	 ***********************************************************************************/
    private void showToast(String msg) {
        Toast error = Toast.makeText(mContext, msg, Toast.LENGTH_LONG);
        error.show();
    }
}
