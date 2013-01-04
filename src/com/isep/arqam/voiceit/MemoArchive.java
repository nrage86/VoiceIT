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


/**************************************************************************************************
 * MemoArchive
 * - Apresenta o arquivo de memos numa lista
 *************************************************************************************************/
public class MemoArchive extends Activity {
	/** Variaveis globais*/
	private static final String TAG = "MemoArchive";
	private ArrayList<String> filePath = new ArrayList<String>();
	private ArrayList<String> localMemoList = new ArrayList<String>();   
	private File sdcard = Environment.getExternalStorageDirectory();
	private File[] sdcardFiles = sdcard.listFiles();
	
	/**********************************************************************************************
	 * onCreate
	 *********************************************************************************************/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_memos_archive);

		getLocalMemoList();   

		/** Apresenta uma lista com os memos locais*/
		ListView MemoList = (ListView) findViewById(R.id.MemoList);
		MemoList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
				localMemoList));
		MemoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent myIntent = new Intent(MemoArchive.this, MemoPlay.class);
	            String selectedFile = ((TextView) arg1).getText().toString();
				myIntent.putExtra("memoName", selectedFile);
				MemoArchive.this.startActivity(myIntent);
			}
		});
	}
	
	/**********************************************************************************************
	 * onResume
	 *********************************************************************************************/
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()" + this);
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
	 * onCreateOptionsMenu
	 *********************************************************************************************/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_memos_archive, menu);
		return true;
	}
}
