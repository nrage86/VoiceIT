package com.isep.arqam.voiceit;

import com.isep.arqam.voiceit.dropbox.DropBoxTest;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MemoPlay extends Activity {

	private static String mFileName = null;
	private MediaPlayer   mPlayer = null;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_memo_play);
		
		
		mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/audiorecordtest.3gp";
		
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
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	if(mPlayer != null) {
    		mPlayer.release();
    	}
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_memo_play, menu);
		return true;
	}

}
