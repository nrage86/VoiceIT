package com.isep.arqam.voiceit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Smooth Horizontal View Slider
 * 
 * First Fragment (or former View)
 * 
 * @see http://www.e-nature.ch/tech
 * @author Dominik Erbsland
 */
public class Frag_startScreen extends Fragment {
	/**
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 *      android.view.ViewGroup, android.os.Bundle)
	 */
	private RelativeLayout rl;
	private FragmentActivity fa;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		fa = super.getActivity();
		rl = (RelativeLayout) inflater.inflate(R.layout.fragment0_layout, container, false);
			
		Button btn_startRecord;btn_startRecord = (Button)rl.findViewById(R.id.startRecord);
		btn_startRecord.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View arg0) {
				
				Intent myIntent = new Intent(fa, MemoRecord.class);
				startActivity(myIntent);
			}
		});
	
		// We have different layouts, and in one of them this
		// fragment's containing frame doesn't exist. The fragment
		// may still be created from its saved state, but there is
		// no reason to try to create its view hierarchy because it
		// won't be displayed. Note this is not needed -- we could
		// just run the code below, where we would create and return
		// the view hierarchy; it would just never be used.
		
		return rl;
	}
}
