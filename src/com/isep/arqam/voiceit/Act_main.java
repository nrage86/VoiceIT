package com.isep.arqam.voiceit;

import java.util.List;
import java.util.Vector;



import com.isep.arqam.voiceit.dropbox.Act_dropbox;

import android.os.Bundle;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


/**************************************************************************************************
 * Act_main
 * - Inicio, contem um viewpager com dois fragments para janela deslizante
 *************************************************************************************************/
public class Act_main extends FragmentActivity {
	private static final String TAG = "Act_main";
	private int currentFrag=0;
	private MyPagerAdapter mPagerAdapter;
	
	/**********************************************************************************************
	 * onCreate
	 *********************************************************************************************/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_act_main);

		if(getIntent().hasExtra("currentFrag")){
			currentFrag=Integer.parseInt(getIntent().getStringExtra("currentFrag"));
		}

		// initialsie the pager
		this.initialisePaging();
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
	 * initialisePaging
	 * - Initialize the fragments to be paged
	 *********************************************************************************************/
	private void initialisePaging() {
		List<Fragment> fragments = new Vector<Fragment>();
		fragments.add(Fragment.instantiate(this, Frag_startScreen.class.getName()));
		fragments.add(Fragment.instantiate(this, Frag_memoArchive.class.getName()));
		this.mPagerAdapter = new MyPagerAdapter(super.getSupportFragmentManager(), fragments);
		ViewPager pager = (ViewPager) super.findViewById(R.id.viewpager);
		pager.setAdapter(this.mPagerAdapter);
		pager.setCurrentItem(currentFrag);
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

	/**********************************************************************************************
	 * onOptionsItemSelected
	 *********************************************************************************************/
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent myIntent;
		
	    switch(item.getItemId()){
	    
	    case R.id.about:
	        /*myIntent = new Intent(Act_main.this,Act_dropbox.class);
	        myIntent.putExtra("DropboxTask", "download");
	        startActivityForResult(myIntent, 0);*/
	        return true; 
	        
	    case R.id.settings:
	    	myIntent = new Intent(Act_main.this,Act_settings.class);
	        startActivityForResult(myIntent, 0);
	        return true;     
	    }
	    return false;
	}
}
