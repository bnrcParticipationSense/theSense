package com.bupt.bnrc.thesenser.dao;

import android.content.Context;

public class DAOFactory {
	private static DAOFactory instance = null;
	
	private Context globalContext = null;
    private boolean cacheDAOInstances = false;
    private TestDAO cachedTeamDAO = null;

    public static DAOFactory getInstance() {
        if (instance == null) {
            instance = new DAOFactory();
        }
        return instance;
    }

    private DAOFactory() {
    }
}
