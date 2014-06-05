package com.bupt.bnrc.thesenser;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class CollectFragmentPagerAdapter extends FragmentPagerAdapter {
	
	private final String[] mTabTitles = {"信息界面", "实时景色"};

	public CollectFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		switch (position) {
		case 0:
			CollectInfoFragment infoFragment = new CollectInfoFragment();
			return infoFragment;
		case 1:
			CollectSceneFragment sceneFragment = new CollectSceneFragment();
			return sceneFragment;
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
