package com.isep.arqam.voiceit;

import java.io.File;
import java.util.ArrayList;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


/****************************************************************************************
 * MemoArchive
 * - Apresenta o arquivo de memos numa lista
 ***************************************************************************************/
public class MemoArchive extends Activity {
	/** Variaveis globais*/
	private static final String TAG = "MemoArchive";
	
	/************************************************************************************
	 * onCreate
	 ***********************************************************************************/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_memos_archive);
		
		ArrayList<String> path = new ArrayList<String>();
		ArrayList<String> memosList = new ArrayList<String>();  
	     
		File sdcard = Environment.getExternalStorageDirectory();
		File[] files = sdcard.listFiles();
		
		

/*		
		 Intent myIntent = new Intent(getApplicationContext(), DropboxService.class);
		 myIntent.putExtra("memoName", "/audiorecordtest0.3gp");
		 startService(myIntent);
*/		
/*
		//AndroidAuthSession session = buildSession();
		mApi = new DropboxAPI<AndroidAuthSession>(session);
		
		final String FILE_DIR = "/Memos/";
		
        // Get the metadata for a directory
        Entry dirent = mApi.metadata(FILE_DIR, 1000, null, true, null);
        
        ArrayList<Entry> filesDropbox = new ArrayList<Entry>();
        ArrayList<String> dir=new ArrayList<String>();

        for (Entry ent: dirent.contents) 
        {
            filesDropbox.add(ent);// Add it to the list of thumbs we can choose from                       
            //dir = new ArrayList<String>();
            //dir.add(new String(files.get(i++).path));
            dir.add(new String(ent.fileName()));
        }
*/
		
		
		
		
		
		
		
		

		// Lista os memos no directorio /sdcard do tlm numa listview no ecra
		for(int i=0; i < files.length; i++)    
	     {
	          File file = files[i];
	          path.add(file.getPath());
  
              String filename=file.getName();
              String ext = filename.substring(filename.lastIndexOf('.')+1, filename.length());
              if(ext.equals("3gp")||ext.equals("mp4"))
              {
            	  memosList.add(file.getName());
              }
	     }    
		
		ListView MemoList = (ListView) findViewById(R.id.MemoList);
		MemoList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
				memosList));
		
		MemoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent myIntent = new Intent(MemoArchive.this, MemoPlay.class);
				//MemoList.getSelectedItemId()
				// selected item
	            String selectedFile = ((TextView) arg1).getText().toString();
				myIntent.putExtra("memoName", selectedFile);
				MemoArchive.this.startActivity(myIntent);
			}
		});
	}
	
	/************************************************************************************
	 * onResume
	 ***********************************************************************************/
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()" + this);
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
	 * onCreateOptionsMenu
	 ***********************************************************************************/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_memos_archive, menu);
		return true;
	}
}
