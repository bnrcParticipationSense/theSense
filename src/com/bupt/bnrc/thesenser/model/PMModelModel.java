package com.bupt.bnrc.thesenser.model;

import java.util.List;

import android.content.Context;

import com.bupt.bnrc.thesenser.dao.DAOFactory;
import com.bupt.bnrc.thesenser.dao.PMModelDAO;
import com.bupt.bnrc.thesenser.utils.Logger;

public class PMModelModel {
	private Integer mTag = null;
	private String mDesc = null;

	private static DAOFactory daoFactory = DAOFactory.getInstance();

	public PMModelModel(Integer tag, String desc) {
		this.mTag = tag;
		this.mDesc = desc;
	}

	public void save(Context context) {
		PMModelDAO dao = null;
		try {
			dao = daoFactory.getPMModelDAO(context);
			dao.save(this);
		} catch (Exception e) {
			Logger.e(e.getMessage());
		} finally {
			dao.close();
		}
	}

	public static List<PMModelModel> findAllModelTags(Context context) {
		PMModelDAO dao = null;
		try {
			dao = daoFactory.getPMModelDAO(context);
			return dao.findAllModelTags(context);
		} finally {
			if (dao != null) {
				dao.close();
			}
		}
	}

	public Integer getTag() {
		return mTag;
	}

	public String getDesc() {
		return mDesc;
	}
}
