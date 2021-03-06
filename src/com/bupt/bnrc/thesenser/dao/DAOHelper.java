/**
 * 
 */
package com.bupt.bnrc.thesenser.dao;

import static android.provider.BaseColumns._ID;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author xuexiaojian
 * 
 */
public abstract class DAOHelper extends SQLiteOpenHelper implements
		DatabaseConstants {
	// file database
	protected static final String FILE_TABLE_NAME = "files";
	protected static final String FILE_FILE_NAME = "file_name";
	protected static final String FILE_CREATE_TIME = "create_time";
	protected static final String FILE_X_DIRECT = "x_direct";
	protected static final String FILE_Y_DIRECT = "y_direct";
	protected static final String FILE_Z_DIRECT = "z_direct";
	protected static final String FILE_LONGITUDE = "longitude";
	protected static final String FILE_LATITUDE = "latitude";
	protected static final String FILE_EXPOSURE_VALUE = "exposure_value";
	protected static final String FILE_FOCAL_DISTANCE = "focal_distance";
	protected static final String FILE_APERTURE = "aperture";
	protected static final String FILE_WIDTH = "width";
	protected static final String FILE_HEIGHT = "height";
	protected static final String FILE_TAG = "tag";
	protected static final String[] FILE_ALL_COLUMS = { _ID, FILE_FILE_NAME,
			FILE_CREATE_TIME, FILE_X_DIRECT, FILE_Y_DIRECT, FILE_Z_DIRECT,
			FILE_LONGITUDE, FILE_LATITUDE, FILE_EXPOSURE_VALUE,
			FILE_FOCAL_DISTANCE, FILE_APERTURE, FILE_WIDTH, FILE_HEIGHT,
			FILE_TAG };
	private static final String FILE_TABLE_CREATE = "CREATE TABLE "
			+ FILE_TABLE_NAME + "(" + _ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + FILE_FILE_NAME
			+ " NTEXT NOT NULL, " + FILE_CREATE_TIME + " INTEGER NOT NULL, "
			+ FILE_X_DIRECT + " FLOAT, " + FILE_Y_DIRECT + " FLOAT, "
			+ FILE_Z_DIRECT + " FLOAT, " + FILE_LONGITUDE + " FLOAT, "
			+ FILE_LATITUDE + " FLOAT, " + FILE_EXPOSURE_VALUE + " INTEGER, "
			+ FILE_FOCAL_DISTANCE + " FLOAT, " + FILE_APERTURE + " FLOAT, "
			+ FILE_WIDTH + " INTEGER, " + FILE_HEIGHT + " INTEGER, " + FILE_TAG
			+ " INTEGER NOT NULL" + ");";

	// data database
	protected static final String DATA_TABLE_NAME = "datas";
	protected static final String DATA_LIGHT_INTENSITY = "light_intensity";
	protected static final String DATA_SOUND_INTENSITY = "sound_intensity";
	protected static final String DATA_CREATE_TIME = "create_time";
	protected static final String DATA_LONGTITUDE = "longtitude";
	protected static final String DATA_LATITUDE = "latitude";
	protected static final String DATA_CHARGE_STATE = "charge_state";
	protected static final String DATA_BATTERY_STATE = "battery_state";
	protected static final String DATA_NET_STATE = "net_state";
	protected static final String[] DATA_ALL_COLUMS = { _ID,
			DATA_LIGHT_INTENSITY, DATA_SOUND_INTENSITY, DATA_CREATE_TIME,
			DATA_LONGTITUDE, DATA_LATITUDE, DATA_CHARGE_STATE,
			DATA_BATTERY_STATE, DATA_NET_STATE };
	private static final String DATA_TABLE_CREATE = "CREATE TABLE "
			+ DATA_TABLE_NAME + "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ DATA_LIGHT_INTENSITY	+ " FLOAT, " + DATA_SOUND_INTENSITY + " FLOAT, " + DATA_CREATE_TIME
			+ " INTEGER NOT NULL, " + DATA_LONGTITUDE + " FLOAT, "
			+ DATA_LATITUDE + " FLOAT, " + DATA_CHARGE_STATE + " INTEGER, "
			+ DATA_BATTERY_STATE + " INTEGER, " + DATA_NET_STATE + " INTEGER"
			+ ");";

	/* some private database which is not used in server */
	// pm model database
	protected static final String PM_MODEL_TABLE_NAME = "pmmodels";
	protected static final String PM_MODEL_TAG = "tag";
	protected static final String PM_MODEL_DESC = "desc";
	protected static final String[] PM_MODEL_ALL_COLUMS = { PM_MODEL_TAG,
			PM_MODEL_DESC };
	private static final String PM_MODEL_TABLE_CREATE = "CREATE TABLE "
			+ PM_MODEL_TABLE_NAME + "(" + PM_MODEL_TAG + " INTEGER NOT NULL, "
			+ PM_MODEL_DESC + " NTEXT, " + "PRIMARY KEY (" + PM_MODEL_TAG
			+ ") " + ");";

	public DAOHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(FILE_TABLE_CREATE);
		db.execSQL(DATA_TABLE_CREATE);
		db.execSQL(PM_MODEL_TABLE_CREATE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + FILE_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + DATA_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + PM_MODEL_TABLE_NAME);
		onCreate(db);
	}

	protected void closeCursor(Cursor cursor) {
		if (cursor != null) {
			cursor.close();
		}
	}
}
