package com.bupt.bnrc.thesenser;

import android.support.v4.app.Fragment;


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
			fragment = new InfoCollectParentFragment();
		} else if(type.equals("pmtools")){ 
			fragment = new PMToolsParentFragment();
		} else if(type.equals("settings")) {
			fragment = new SettingsFragment();			 
		} else if(type.equals("test")) {
			fragment = new TestFragment();
		} else if(type.equals("user")) {
			fragment = new UserFragment();
		} else if(type.equals("tasks")) {
			fragment = new TaskFragment();
		}
		return fragment;
	}
	
}
