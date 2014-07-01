package com.bupt.bnrc.thesenser.dao;

import com.bupt.bnrc.thesenser.utils.CommonDefinition;

import android.content.Context;
import android.provider.Settings.Global;

public class DAOFactory {
	private static DAOFactory instance = null;

	private Context globalContext = null;
	private boolean cacheDAOInstances = false;
	private FileDAO cachedFileDAO = null;
	private DataDAO cachedDataDAO = null;
	private PMModelDAO cachedPMModelDAO = null;

	public static DAOFactory getInstance() {
		if (instance == null) {
			instance = new DAOFactory();
		}
		return instance;
	}

	private DAOFactory() {
	}

	public FileDAO getFileDAO(Context context) {
		if (cacheDAOInstances) {
			if (cachedFileDAO == null) {
				cachedFileDAO = new FileDAO(getProperDAOContext(context));
			}
			return cachedFileDAO;
		} else {
			return new FileDAO(getProperDAOContext(context));
		}
	}

	public DataDAO getDataDAO(Context context) {
		if (cacheDAOInstances) {
			if (cachedDataDAO == null) {
				cachedDataDAO = new DataDAO(getProperDAOContext(context));
			}
			return cachedDataDAO;
		} else {
			return new DataDAO(getProperDAOContext(context));
		}
	}

	public PMModelDAO getPMModelDAO(Context context) {
		if (cacheDAOInstances) {
			if (cachedPMModelDAO == null) {
				cachedPMModelDAO = new PMModelDAO(getProperDAOContext(context));
			}
			return cachedPMModelDAO;
		} else {
			return new PMModelDAO(getProperDAOContext(context));
		}
	}

	private Context getProperDAOContext(Context context) {
		if(CommonDefinition.DATABASE_SDCARD_SAVE) {
			return new DatabaseContext(context);
		} else {
			if (globalContext != null) {
				return globalContext;
			} else {
				return context;
			}
		}
	}
}
