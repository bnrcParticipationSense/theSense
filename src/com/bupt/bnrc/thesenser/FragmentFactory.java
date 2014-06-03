package com.bupt.bnrc.thesenser;

import android.app.Fragment;

public class FragmentFactory {
	public static final String ARG_MAIN_INDEX = "main_list_index";
	private static FragmentFactory instance = null;
	
	public static FragmentFactory getInstance() {
		if(instance == null) {
			instance = new FragmentFactory();
		}
		return instance;
	}
	
	private FragmentFactory() {
	}
	
	public Fragment createFragment(String type) {
		Fragment fragment = null;
		if(type.equals("collect")) {
			fragment = new CollectFragment();
		} else if(type.equals("test")) {
			fragment = new TestFragment();
		}
		return fragment;
	}
}
