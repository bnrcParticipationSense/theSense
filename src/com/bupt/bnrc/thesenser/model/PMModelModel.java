package com.bupt.bnrc.thesenser.model;

import com.bupt.bnrc.thesenser.dao.DAOFactory;

public class PMModelModel {
	private Integer mTag = null;
	private String mDesc = null;
	
	private static DAOFactory daoFactory = DAOFactory.getInstance();
	
	public PMModelModel(Integer tag, String desc) {
		this.mTag = tag;
		this.mDesc = desc;
	}

	public Integer getTag() {
		return mTag;
	}

	public String getDesc() {
		return mDesc;
	}
}
