package com.isep.arqam.voiceit;

import com.isep.arqam.voiceit.dropbox.DropboxMain;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;


/**************************************************************************************************
 * VoiceIT_MainActivity
 * - Inicio
 *************************************************************************************************/
public class VoiceIT_MainActivity extends Activity {
	
	/** Variaveis globais*/
	private static final String TAG = "VoiceIT_MainActivity";
	private Button btn_startRecord;
	private Button btn_archive;
	
	/**********************************************************************************************
	 * onCreate
	 *********************************************************************************************/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_voice_it__main);

		/** Inicia a activity RecordMemo ao clicar no botao btn_startRecord*/
		btn_startRecord = (Button)findViewById(R.id.startRecord);
		btn_startRecord.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View arg0) {
				Intent myIntent = new Intent(VoiceIT_MainActivity.this, MemoRecord.class);
				VoiceIT_MainActivity.this.startActivity(myIntent);
			}
		});
		
		/** Inicia a activity MemosArchive ao clicar no botao btn_archive (depois de sincronizar
			com o dropbox)*/
		btn_archive = (Button)findViewById(R.id.archive);
		btn_archive.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View arg0) {						
				Intent myIntent = new Intent(VoiceIT_MainActivity.this, DropboxMain.class);
				myIntent.putExtra("DropboxTask", "download");
				VoiceIT_MainActivity.this.startActivity(myIntent);
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
	 * onCreateOptionsMenu
	 *********************************************************************************************/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_voice_it__main, menu);
		return true;
	}
}
