package com.isep.arqam.voiceit;

import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;


/****************************************************************************************
 * MemoPlay
 * - Faz a reprodução do memo selecionado previamente
 ***************************************************************************************/
public class MemoPlay extends Activity {
	/** Variaveis globais*/
	private static final String TAG = "MemoPlay";
	private static String mFileName = null;
	private MediaPlayer   mPlayer = null;
		
	/************************************************************************************
	 * onCreate
	 ***********************************************************************************/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_memo_play);
		
		mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
		String memoName = getIntent().getStringExtra("memoName");
        //mFileName += "/audiorecordtest.3gp";
        mFileName += "/"+memoName;
        
    	final Button btn_play = (Button)findViewById(R.id.btn_Play);	
		btn_play.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(mPlayer == null) {
    				mPlayer = new MediaPlayer();
    				try {
    					mPlayer.setDataSource(mFileName);
    					mPlayer.prepare();
	    				mPlayer.start();
	    				btn_play.setText("Stop");
    				} catch (Exception e) {
    					e.printStackTrace();
    				}
				} else {
    				mPlayer.stop();
    				mPlayer.release();
    				mPlayer = null;
    				btn_play.setText("Play");
				}
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
    	if(mPlayer != null) {
    		mPlayer.release();
    	}
        Log.d(TAG, "onDestroy()" + this);
    }

	/************************************************************************************
	 * onCreateOptionsMenu
	 ***********************************************************************************/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_memo_play, menu);
		return true;
	}
}
