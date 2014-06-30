package com.bupt.bnrc.thesenser.dao;

import com.bupt.bnrc.thesenser.model.PMModelModel;
import com.bupt.bnrc.thesenser.utils.Logger;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class PMModelDAO extends DAOHelper {

	public PMModelDAO(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}
	
	public void save(PMModelModel pmModel) {
		try {
			SQLiteDatabase db = getWritableDatabase();
			ContentValues values = createContentValues(pmModel);
			db.insert(PM_MODEL_TABLE_NAME, null, values);
		} catch (SQLException e) {
			// TODO: handle exception
			Logger.e(e.getMessage());
		}
	}
	
	private ContentValues createContentValues(PMModelModel pmModel) {
		ContentValues values = new ContentValues();
		values.put(PM_MODEL_TAG, pmModel.getTag());
		values.put(PM_MODEL_DESC, pmModel.getDesc());
		return values;
	}

}
