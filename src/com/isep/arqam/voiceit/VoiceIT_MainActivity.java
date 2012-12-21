package com.isep.arqam.voiceit;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class VoiceIT_MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_voice_it__main);
		
		Button btn_startRecord = (Button)findViewById(R.id.startRecord);
		Button btn_archive = (Button)findViewById(R.id.archive);
		
		
		//Inicia RecordMemo activity
		btn_startRecord.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent myIntent = new Intent(VoiceIT_MainActivity.this, MemoRecord.class);
				VoiceIT_MainActivity.this.startActivity(myIntent);
			}
		});
		
		//Inicia MemosArchive activity
		btn_archive.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent myIntent = new Intent(VoiceIT_MainActivity.this, MemoArchive.class);
				VoiceIT_MainActivity.this.startActivity(myIntent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_voice_it__main, menu);
		return true;
	}

}
