package com.bupt.bnrc.thesenser.utils;

import java.util.ArrayList;
import java.util.List;

public class DataCache {
	// Light
	List<Float> mLightList;
	public DataCache() {
		// TODO Auto-generated constructor stub
		mLightList = new ArrayList<Float>();
	}
	public int addLightData(Float num) {
		int size = mLightList.size();
		if(size < 19) {
			mLightList.add(num);
			return size + 1;
		} else {
			mLightList.clear();
			mLightList.add(num);
			return 1;
		}
	}
}
