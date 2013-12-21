package com.isep.arqam.voiceit;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;


/**************************************************************************************************
 * Act_memoPlay
 * - Faz a reproducao do memo selecionado previamente
 *************************************************************************************************/
public class Act_memoPlay extends Activity implements OnCompletionListener {
	private static final String TAG = "MemoPlay";
	private static String mFileName = null;
	private static String mLength = null;
	private MediaPlayer mPlayer = null;
	private SeekBar seekbar = null;
	private Button btnPlay = null;
	private TextView songCurrentDurationLabel = null;
    private TextView songTotalDurationLabel = null;
    private int minutesT;
	private int secondsT;
	private int minutesC;
	private int secondsC;
	private final Handler handler = new Handler();
		
	/**********************************************************************************************
	 * onCreate
	 *********************************************************************************************/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_act_memo_play);
		
		mFileName = getIntent().getStringExtra("memoName");
		mLength = getIntent().getStringExtra("length");	
		mPlayer = new MediaPlayer();
		
		try {
			mPlayer.setDataSource(mFileName);
			mPlayer.prepare();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mPlayer.setOnCompletionListener(this);
		songCurrentDurationLabel = (TextView) findViewById(R.id.songCurrentDurationLabel);
		songTotalDurationLabel = (TextView) findViewById(R.id.songTotalDurationLabel);	
		minutesT = (int) TimeUnit.MILLISECONDS.toMinutes(Integer.parseInt(mLength));
		secondsT = (int) TimeUnit.MILLISECONDS.toSeconds(Integer.parseInt(mLength));
		
		if (minutesT<10){
			songTotalDurationLabel.setText("0"+minutesT+":"+secondsT);
			if(secondsT<10){
				songTotalDurationLabel.setText("0"+minutesT+":"+"0"+secondsT);
			}			
		}if(secondsT<10){
			songTotalDurationLabel.setText(minutesT+":"+"0"+secondsT);
			if (minutesT<10){
				songTotalDurationLabel.setText("0"+minutesT+":"+"0"+secondsT);
			}
		}else{
			songTotalDurationLabel.setText(+minutesT+":"+secondsT);
		}
		
		songCurrentDurationLabel.setText("00:00");
		seekbar = (SeekBar) findViewById(R.id.seekbar);
		seekbar.setMax(Integer.parseInt(mLength));		
		seekbar.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				seekChange(arg0);		
				return false;
			}
		});		

		btnPlay = (Button)findViewById(R.id.btnPlay);	
		btnPlay.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View arg0) {
				buttonClick();
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
    	if(mPlayer != null) {
    		mPlayer.release();
    	}
        Log.d(TAG, "onDestroy()" + this);
    }

	/**********************************************************************************************
	 * onCreateOptionsMenu
	 *********************************************************************************************/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_memo_play, menu);
		return true;
	}
	
	/**********************************************************************************************
	 * startPlayProgressUpdater
	 * - Método recursivo que faz com que a seekbar acompanhe a reprodução do memo
	 *********************************************************************************************/
	public void startPlayProgressUpdater() {
    	seekbar.setProgress(mPlayer.getCurrentPosition());
		if (mPlayer.isPlaying()) {
			minutesC = (int) TimeUnit.MILLISECONDS.toMinutes(mPlayer.getCurrentPosition());
			secondsC = (int) TimeUnit.MILLISECONDS.toSeconds(mPlayer.getCurrentPosition());

			if (minutesC<10){
				songCurrentDurationLabel.setText("0"+minutesC+":"+secondsC);
				if(secondsT<10){
					songCurrentDurationLabel.setText("0"+minutesC+":"+"0"+secondsC);
				}			
			}if(secondsC<10){
				songCurrentDurationLabel.setText(minutesC+":"+"0"+secondsC);
				if (minutesC<10){
					songCurrentDurationLabel.setText("0"+minutesC+":"+"0"+secondsC);
				}
			}else{
				songCurrentDurationLabel.setText(+minutesT+":"+secondsT);
			}
				
			Runnable notification = new Runnable() {
		        public void run() {
		        	startPlayProgressUpdater();
				}
		    };
		    handler.postDelayed(notification,1000);
    	}else{
    		mPlayer.pause();
    		seekbar.setProgress(0);
    		btnPlay.setText("Play");
    		minutesC = (int) TimeUnit.MILLISECONDS.toMinutes(mPlayer.getCurrentPosition());
			secondsC = (int) TimeUnit.MILLISECONDS.toSeconds(mPlayer.getCurrentPosition());

			if (minutesC<10){
				songCurrentDurationLabel.setText("0"+minutesC+":"+secondsC);
				if(secondsT<10){
					songCurrentDurationLabel.setText("0"+minutesC+":"+"0"+secondsC);
				}			
			}if(secondsC<10){
				songCurrentDurationLabel.setText(minutesC+":"+"0"+secondsC);
				if (minutesC<10){
					songCurrentDurationLabel.setText("0"+minutesC+":"+"0"+secondsC);
				}
			}else{
				songCurrentDurationLabel.setText(+minutesT+":"+secondsT);
			}
    	}
    }
	
	/**********************************************************************************************
	 * seekChange
	 * - This is event handler thumb moving event
	 *********************************************************************************************/
    private void seekChange(View v){
    	if(mPlayer.isPlaying()){
	    	SeekBar sb = (SeekBar)v;
			mPlayer.seekTo(sb.getProgress());
		}
    }
    
	/**********************************************************************************************
	 * buttonClick
	 *********************************************************************************************/
    private void buttonClick(){
        if (btnPlay.getText() == getString(R.string.play_str)) {
            btnPlay.setText(getString(R.string.pause_str));
            try{
            	mPlayer.start();
                startPlayProgressUpdater();
            }catch (IllegalStateException e) {
            	mPlayer.pause();
            }
        }else {
            btnPlay.setText(getString(R.string.play_str));
            mPlayer.pause();
        }
    }

	/**********************************************************************************************
	 * onCompletion
	 *********************************************************************************************/
	@Override
	public void onCompletion(MediaPlayer mp) {
		btnPlay.setText(getString(R.string.pause_str));
		songCurrentDurationLabel.setText("00:00");
	}
}
