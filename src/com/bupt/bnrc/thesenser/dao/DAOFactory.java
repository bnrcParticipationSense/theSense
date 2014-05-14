package com.bupt.bnrc.thesenser.dao;

import android.content.Context;

public class DAOFactory {
	private static DAOFactory instance = null;
	
	private Context globalContext = null;
    private boolean cacheDAOInstances = false;
    private TestDAO cachedTeamDAO = null;
    private FileDAO cachedFileDAO = null;

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
    
    private Context getProperDAOContext(Context context) {
        if (globalContext != null) {
            return globalContext;
        } else {
            return context;
        }
    }
}
