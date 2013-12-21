package com.isep.arqam.voiceit;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


/**************************************************************************************************
 * MyPagerAdapter
 *************************************************************************************************/
public class MyPagerAdapter extends FragmentStatePagerAdapter {
	private final List<Fragment> fragments;

	/**********************************************************************************************
	 * MyPagerAdapter
	 *********************************************************************************************/
	public MyPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
		super(fm);
		this.fragments = fragments;
	}

	/**********************************************************************************************
	 * getItem
	 *********************************************************************************************/
	@Override
	public Fragment getItem(int position) {
		return this.fragments.get(position);
	}

	/**********************************************************************************************
	 * getCount
	 *********************************************************************************************/
	@Override
	public int getCount() {
		return this.fragments.size();
	}
}
