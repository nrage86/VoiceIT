package com.isep.arqam.voiceit;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;



import com.isep.arqam.voiceit.dropbox.DropboxMain;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;


/**************************************************************************************************
 * VoiceIT_MainActivity
 * - Inicio
 *************************************************************************************************/
public class Voiceit_main extends FragmentActivity {
	
	/** Variaveis globais*/
	private static final String TAG = "VoiceIT_MainActivity";
	private Button btn_startRecord;
	private Button btn_archive;
	
	
	private int currentFrag=0;
	private MyPagerAdapter mPagerAdapter;
	private FragmentStatePagerAdapter mPagerAdapter2;
	
	private String currentFragString=null;
	
	
	/**********************************************************************************************
	 * onCreate
	 *********************************************************************************************/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_voice_it__main);
		
		
		
		if(getIntent().hasExtra("currentFrag")){
		
			if(getIntent().getStringExtra("currentFrag").equals("1") )
				currentFrag=1;
		}
		
		// initialsie the pager
		this.initialisePaging();
		
		
		
		
		/*
		// Inicia a activity RecordMemo ao clicar no botao btn_startRecord
		btn_startRecord = (Button)findViewById(R.id.startRecord);
		btn_startRecord.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View arg0) {
				Intent myIntent = new Intent(VoiceIT_MainActivity.this, MemoRecord.class);
				VoiceIT_MainActivity.this.startActivity(myIntent);
			}
		});
		*/
	}
	
	
	
	
	
	
	/**
	 * Initialize the fragments to be paged
	 */
	private void initialisePaging() {

		
		
		
		ViewPager pager2 = (ViewPager) super.findViewById(R.id.viewpager);
		// assume this actually has stuff in it
		final ArrayList<String> titles = new ArrayList<String>();
		
		
		List<Fragment> fragments = new Vector<Fragment>();
		fragments.add(Fragment.instantiate(this, Frag_startScreen.class.getName()));
		fragments.add(Fragment.instantiate(this, Frag_memoArchive.class.getName()));
		this.mPagerAdapter = new MyPagerAdapter(super.getSupportFragmentManager(), fragments);
		ViewPager pager = (ViewPager) super.findViewById(R.id.viewpager);
		pager.setAdapter(this.mPagerAdapter);
		
		//if(extras.getString("currentFrag").equals("1"))
		pager.setCurrentItem(currentFrag);
	}
	
	
	
	
/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_voice_it__main, menu);
		return true;
	}
*/
	/**********************************************************************************************
	 * onOptionsItemSelected
	 *********************************************************************************************/
/*
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent myIntent;
		
	    switch(item.getItemId()){
	    case R.id.archive:
	        myIntent = new Intent(VoiceIT_MainActivity.this,DropboxMain.class);
	        myIntent.putExtra("DropboxTask", "download");
	        startActivityForResult(myIntent, 0);
	        return true; 
	    case R.id.menu_settings:
	    	myIntent = new Intent(VoiceIT_MainActivity.this,Settings.class);
	        startActivityForResult(myIntent, 0);
	        return true;     
	    }
	    return false;
	}
*/
}
