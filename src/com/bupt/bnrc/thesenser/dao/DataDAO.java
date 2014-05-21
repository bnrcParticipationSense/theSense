package com.bupt.bnrc.thesenser.dao;

import java.text.SimpleDateFormat;

import com.bupt.bnrc.thesenser.model.DataModel;
import com.bupt.bnrc.thesenser.model.FileModel;
import com.bupt.bnrc.thesenser.utils.Logger;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DataDAO extends DAOHelper {
	private static SimpleDateFormat m_sdf = null;
	
	public DataDAO(Context context) {
		// TODO Auto-generated constructor stub
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		if(m_sdf == null) {
			m_sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		}
	}
	
	public DataModel save(DataModel data) {
		if(data.getId() == null) {
			SQLiteDatabase db = getWritableDatabase();
			return createNewFile(db, data);
		} else {
			String msg = "Attempting to update an existing data.  data entries cannot be updated.";
			Logger.w(msg);
			throw new RuntimeException(msg);
		}
	}

	private DataModel createNewFile(SQLiteDatabase db, DataModel data) {
		// TODO Auto-generated method stub
		// Log
		if(data.getLightIntensity() != null) {
			Logger.d("Creating new data for light " + data.getLightIntensity() + " with a file create time of '" + m_sdf.format(data.getCreateTime()) + "'");
	    }
		if(data.getSoundIntensity() != null) {
			Logger.d("Creating new data for sound " + data.getSoundIntensity() + " with a file create time of '" + m_sdf.format(data.getCreateTime()) + "'");
		}
		
		ContentValues values = createContentValues(data);
        long id = db.insertOrThrow(DATA_TABLE_NAME, null, values);
        return new DataModel(id, data);
	}

	private ContentValues createContentValues(DataModel data) {
		// TODO Auto-generated method stub
		ContentValues values = new ContentValues();
		values.put(DATA_LIGHT_INTENSITY, data.getLightIntensity());
		values.put(DATA_SOUND_INTENSITY, data.getSoundIntensity());
		values.put(DATA_CREATE_TIME, m_sdf.format(data.getCreateTime()));
		values.put(DATA_LONGTITUDE, data.getLongitude());
		values.put(DATA_LATITUDE, data.getLatitude());
		values.put(DATA_CHARGE_STATE, data.getChargeState());
		values.put(DATA_BATTERY_STATE, data.getBatteryState());
		values.put(DATA_NET_STATE, data.getNetState());
    	return values;
	}
}
