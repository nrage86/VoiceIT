package com.isep.arqam.voiceit;

import java.io.File;
import java.util.ArrayList;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


/**************************************************************************************************
 * MemoArchive
 * - Apresenta o arquivo de memos numa lista
 *************************************************************************************************/
public class MemoArchive extends Activity {
	/** Variaveis globais*/
	private static final String TAG = "MemoArchive";
	private ArrayList<String> filePath = new ArrayList<String>();
	private ArrayList<Memo> localMemoList = new ArrayList<Memo>();   
	private File sdcard = Environment.getExternalStorageDirectory();
	private File[] sdcardFiles = sdcard.listFiles();
	private ArrayAdapter<Memo> adapter = null;
	
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
		adapter = new ArrayAdapter<Memo>(this, android.R.layout.simple_list_item_1,
				localMemoList);
		MemoList.setAdapter(adapter);
		
		//registar context menu
		registerForContextMenu(MemoList);
		
		//Comportamento no clique
		MemoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				Intent myIntent = new Intent(MemoArchive.this, MemoPlay.class);
	            Memo selectedFile = (Memo) parent.getAdapter().getItem(position);
	            String sfPath = selectedFile.getFilePath();
	            Log.i("selectedFile", sfPath);
				
	            myIntent.putExtra("memoName", sfPath);
				String durationMemo = String.valueOf(selectedFile.getLengthAudio());				
	            myIntent.putExtra("length", durationMemo);
				MemoArchive.this.startActivity(myIntent);
			}
		});
	}
    
	/**********************************************************************************************
	 * getLocalMemoList
	 *********************************************************************************************/
    protected void getLocalMemoList() {
    	localMemoList.clear();
    	
		for(int n=0; n < sdcardFiles.length; n++)    
	     {			
	        File file = sdcardFiles[n];
	        filePath.add(file.getPath());
 
             String fileName=file.getName();
             //extensão do ficheiro
             String ext = fileName.substring(fileName.lastIndexOf('.')+1, fileName.length());
             
             //Nome do ficheiro sem extensão
             String[] fileSplit = fileName.split("\\.");
             
             if(ext.equals("3gp")||ext.equals("mp4"))
             {
            	 localMemoList.add(new Memo(fileSplit[0]));
             }
	     }  
    }
	
	
	/**********************************************************************************************
	 * onCreateContextMenu
	 *********************************************************************************************/
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		  super.onCreateContextMenu(menu, v, menuInfo);
		  MenuInflater inflater = getMenuInflater();
		  inflater.inflate(R.menu.activity_memos_archive, menu);
	}
	
	
	/**********************************************************************************************
	 * onContextItemSelected
	 *********************************************************************************************/
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		  AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		  
		  switch(item.getItemId()) {   
		  	case R.id.play:
		  		/*Intent myIntent = new Intent(MemoArchive.this, MemoPlay.class);
	            String selectedFile = localMemoList.get((int)info.id).getFilePath();
				myIntent.putExtra("memoName", selectedFile);
				MemoArchive.this.startActivity(myIntent);*/
				
				return true;
		  
		  	case R.id.delete:
		  		boolean del = localMemoList.get((int)info.id).delMemo();			  
			  
		      	if (del){
		      		//localMemoList.remove((int)info.id);
		      		getLocalMemoList();
		      		
		      		Intent myIntent = new Intent(this,MemoArchive.class);
			        myIntent.putExtra("DropboxTask", "download");
			        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
			        startActivityForResult(myIntent, 0);
		      		
		      		//adapter.notifyDataSetChanged();
		      		Toast.makeText(this, "Memo " + localMemoList.get((int)info.id).toString() + " deleted from SD card.",Toast.LENGTH_SHORT).show();
		      	}else{
		      		Toast.makeText(this, "An error ocourred when you tried to delete " + localMemoList.get((int)info.id).toString(),Toast.LENGTH_SHORT).show();
		      	} 
		      	return true; 		
		      
		  	default:
		        return super.onContextItemSelected(item);
		  }
	}
}
