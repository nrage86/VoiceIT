package com.isep.arqam.voiceit;

import java.io.File;
import java.util.ArrayList;

import com.isep.arqam.voiceit.dropbox.DropBoxTest;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MemoArchive extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_memos_archive);
		
		ArrayList<String> path = new ArrayList<String>();
		ArrayList<String> memosList = new ArrayList<String>();  
	     
		File f = new File("/sdcard");
		File[] files = f.listFiles();

		// Lista os memos da pasta /sdcard do tlm numa listview no ecra
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
				//myIntent.putExtra("memoName", mFileName);
				MemoArchive.this.startActivity(myIntent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_memos_archive, menu);
		return true;
	}

}
