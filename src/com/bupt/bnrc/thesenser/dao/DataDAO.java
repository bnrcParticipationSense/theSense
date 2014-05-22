package com.bupt.bnrc.thesenser.dao;

import static android.provider.BaseColumns._ID;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.bupt.bnrc.thesenser.model.DataModel;
import com.bupt.bnrc.thesenser.model.FileModel;
import com.bupt.bnrc.thesenser.utils.Logger;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
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
	
	public List<DataModel> findNotUploadDatas(Integer num, Context context) {
		List<DataModel> datas = new ArrayList<DataModel>();
		Cursor cursor = null;
		
		SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		Long index = prefs.getLong("data_index", 0);
		
		try {
			SQLiteDatabase db = getReadableDatabase();
			cursor = db.query(DATA_TABLE_NAME, DATA_ALL_COLUMS, _ID + "> ?", new String[]{index.toString()}, 
					null, null, null, num.toString());
			index += cursor.getCount();
			editor.putLong("file_index", index);
			editor.commit();
			while(cursor.moveToNext()) {
				datas.add(createDataFromCursorData(cursor));
			}
		} finally {
			cursor.close();
		}
		
		Logger.d("Found " + datas.size() + " files to upload");
		return datas;
	}

	public DataModel findDataById(Long id) {
		Cursor cursor = null;
		DataModel data = null;
		
		try {
			SQLiteDatabase db = getReadableDatabase();
			cursor = db.query(DATA_TABLE_NAME, DATA_ALL_COLUMS, _ID + "= ?", new String[]{id.toString()}, 
					null, null, null);
			
			if(cursor.getCount() == 1 ) {
				if(cursor.moveToFirst()) {
					data = createDataFromCursorData(cursor);
				}
			}
		} finally {
			closeCursor(cursor);
		}
		
		return data;
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
		values.put(DATA_CREATE_TIME, data.getCreateTime().getTime());
		values.put(DATA_LONGTITUDE, data.getLongitude());
		values.put(DATA_LATITUDE, data.getLatitude());
		values.put(DATA_CHARGE_STATE, data.getChargeState());
		values.put(DATA_BATTERY_STATE, data.getBatteryState());
		values.put(DATA_NET_STATE, data.getNetState());
    	return values;
	}
	
	private DataModel createDataFromCursorData(Cursor cursor) {
		Long id = cursor.getLong(0);
		Float lightIntensity = cursor.getFloat(1); //光照
		Float soundIntensity = cursor.getFloat(2); // 声音
		Date createTime = new Date(cursor.getLong(3));; // 采集时间
		Float longitude = cursor.getFloat(4);
		Float latitude = cursor.getFloat(5);
		Integer chargeState = cursor.getInt(6); // 0：未充电， 1：充电
		Integer batteryState = cursor.getInt(7); // 电量的百分比
		Integer netState = cursor.getInt(8); //0:无网络    1:2G 2:3G 3:4G 4：wifi
		
		return new DataModel(id, lightIntensity, soundIntensity, createTime, chargeState, batteryState, netState, longitude, latitude);
	}
}
