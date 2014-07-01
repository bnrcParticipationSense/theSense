package com.bupt.bnrc.thesenser.model;

public class WebPhotoModel {
	private String mPackUrl = null;
	private String mSrcUrl = null;

	public WebPhotoModel(String packUrl, String srcUrl) {
		mPackUrl = packUrl;
		mSrcUrl = srcUrl;
	}

	public String getPackUrl() {
		return mPackUrl;
	}

	public String getSrcUrl() {
		return mSrcUrl;
	}
}
