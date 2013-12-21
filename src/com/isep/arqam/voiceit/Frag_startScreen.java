package com.isep.arqam.voiceit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;


/**************************************************************************************************
 * Frag_startScreen
 *************************************************************************************************/
public class Frag_startScreen extends Fragment {
	private RelativeLayout rl;
	private FragmentActivity fa;

	/**********************************************************************************************
	 * onCreateView
	 *********************************************************************************************/
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		fa = super.getActivity();
		rl = (RelativeLayout) inflater.inflate(R.layout.frag_start_screen, container, false);
		Button btn_startRecord;btn_startRecord = (Button)rl.findViewById(R.id.startRecord);
		btn_startRecord.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View arg0) {
				
				Intent myIntent = new Intent(fa, Act_memoRecord.class);
				startActivity(myIntent);
			}
		});
		return rl;
	}
}
