package com.isep.arqam.voiceit;

import com.isep.arqam.voiceit.dropbox.DropBoxTest;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class Save_TEMP extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_save__temp);
		
		//teste ao github
		
		
		Button btn_yes = (Button)findViewById(R.id.btn_yes);
		
		btn_yes.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent myIntent = new Intent(Save_TEMP.this, DropBoxTest.class);
				Save_TEMP.this.startActivity(myIntent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_save__temp, menu);
		return true;
	}

}
