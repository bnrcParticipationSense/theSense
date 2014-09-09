package com.bupt.bnrc.thesenser.utils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.bupt.bnrc.thesenser.model.WebPhotoModel;

public class DataCache {
	private static DataCache instance = null;
	// Light
	List<Float> mLightList;
	// photoList
	List<WebPhotoModel> mPhotoList;

	public static DataCache getInstance() {
		if (instance == null) {
			instance = new DataCache();
		}
		return instance;
	}

	private DataCache() {
		// TODO Auto-generated constructor stub
		mLightList = new ArrayList<Float>();
	}

	// Light
	public int addLightData(Float num) {
		int size = mLightList.size();
		if (size < 5) {
			mLightList.add(num);
			return size + 1;
		} else {
			mLightList.clear();
			mLightList.add(num);
			return 1;
		}
	}

	// photoList
	public void setPhotoListFromJson(JSONObject obj) {
		mPhotoList = JSON.getPhotoListFromJson(obj);
	}

	public void setPhotoListFromJsonTemp(JSONObject obj) {
		mPhotoList = JSON.getPhotoListFromJsonTemp(obj);
	}

	public int getPhotoListLength() {
		return mPhotoList.size();
	}

	public WebPhotoModel getPhoto(int index) {
		if (index < 0 || index >= mPhotoList.size()) {
			return null;
		}
		return mPhotoList.get(index);
	}

}
