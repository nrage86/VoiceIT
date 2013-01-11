package com.isep.arqam.voiceit;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * The <code>PagerAdapter</code> serves the fragments when paging.
 * 
 * Smooth Horizontal View Slider
 * 
 * Original tutorial from:
 * http://thepseudocoder.wordpress.com/2011/10/05/android
 * -page-swiping-using-viewpager
 * 
 * @see http://www.e-nature.ch/tech
 * @author Dominik Erbsland
 */

public class MyPagerAdapter extends FragmentStatePagerAdapter {

	private final List<Fragment> fragments;

	/**
	 * @param fm
	 * @param fragments
	 */
	public MyPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
		super(fm);
		this.fragments = fragments;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentPagerAdapter#getItem(int)
	 */
	/*
	@Override
	public int getItemPosition(Object object) {
	    return POSITION_NONE;
	}
	*/
	@Override
	public Fragment getItem(int position) {
		return this.fragments.get(position);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.view.PagerAdapter#getCount()
	 */
	@Override
	public int getCount() {
		return this.fragments.size();
	}

}
