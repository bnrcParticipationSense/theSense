package com.bupt.bnrc.thesenser;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class InfoCollectFragmentPagerAdapter extends FragmentPagerAdapter {
	private final String[] mTabTitles = {"π‚’’", "…˘“Ù"};
	
	public InfoCollectFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		switch (position) {
		case 0:
			InfoCollectLightFragment lightFragment = new InfoCollectLightFragment();
			return lightFragment;
		case 1:
			InfoCollectSoundFragment soundFragment = new InfoCollectSoundFragment();
			return soundFragment;
		default:
			return null;
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mTabTitles.length;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		// TODO Auto-generated method stub
		return mTabTitles[position];
	}

}
