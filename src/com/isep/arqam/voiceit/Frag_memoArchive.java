package com.isep.arqam.voiceit;

import java.io.File;
import java.util.ArrayList;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class Frag_memoArchive extends ListFragment {
	private static final String TAG = "Fragment1";
	private ArrayList<String> filePath = new ArrayList<String>();
	private ArrayList<Memo> localMemoList = new ArrayList<Memo>();   
	private File sdcard = Environment.getExternalStorageDirectory();
	private File[] sdcardFiles = sdcard.listFiles();
	private ArrayAdapter<Memo> adapter = null;
	private FragmentActivity fa;
	
	/**********************************************************************************************
	 * onActivityCreated
	 *********************************************************************************************/
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		fa = super.getActivity();		
		getLocalMemoList();	
		adapter = new ArrayAdapter<Memo>(fa,
				android.R.layout.simple_list_item_1, localMemoList);
		setListAdapter(adapter);
		registerForContextMenu(getListView());
	}

	/**********************************************************************************************
	 * onCreateContextMenu
	 *********************************************************************************************/
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		  super.onCreateContextMenu(menu, v, menuInfo);
		  MenuInflater inflater = fa.getMenuInflater();
		  inflater.inflate(R.menu.activity_memos_archive, menu);
	}
	
	/**********************************************************************************************
	 * onContextItemSelected
	 *********************************************************************************************/
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		  AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		  Toast.makeText(fa, "Contextoooo ",Toast.LENGTH_SHORT).show();
		  Log.d("item", String.valueOf(item.getItemId()));
		  Log.d("item", String.valueOf(R.id.play));
		  
		  switch(item.getItemId()) {  
		  	case R.id.play:
		  		//Intent myIntent = new Intent(fa, MemoPlay.class);
	            //String selectedFile = localMemoList.get((int)info.id).getFilePath();
				//myIntent.putExtra("memoName", selectedFile);
				//fa.startActivity(myIntent);
				return true;
				
		  	case R.id.delete:
		  		boolean del = localMemoList.get((int)info.id).delMemo();
		  		
		      	if (del){
		      		getLocalMemoList();
		      		Intent myIntent2 = new Intent(fa,Voiceit_main.class);
			        myIntent2.putExtra("currentFrag", "1");
			        myIntent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		            myIntent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
			        startActivityForResult(myIntent2, 0);
		      		Toast.makeText(fa, "Memo " + localMemoList.get((int)info.id).toString() +
		      				" deleted from SD card.",Toast.LENGTH_SHORT).show();
		      	}else{
		      		Toast.makeText(fa, "An error ocourred when you tried to delete " +
		      				localMemoList.get((int)info.id).toString(),Toast.LENGTH_SHORT).show();
		      	} 
		      	return true; 	
		      	
		  	default:
		        return super.onContextItemSelected(item);
		  }
	}

	/**********************************************************************************************
	 * onListItemClick
	 *********************************************************************************************/
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
	    // Do something with the data	
		Toast.makeText(fa, "Clicked ",Toast.LENGTH_SHORT).show();
		Intent myIntent = new Intent(fa, MemoPlay.class);
        Memo selectedFile = (Memo) this.getListAdapter().getItem(position);
        String sfPath = selectedFile.getFilePath();
        Log.i("selectedFile", sfPath);
        myIntent.putExtra("memoName", sfPath);
		String durationMemo = String.valueOf(selectedFile.getLengthAudio());				
        myIntent.putExtra("length", durationMemo);
		fa.startActivity(myIntent);
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
}