/**
 * 
 */
package com.bupt.bnrc.thesenser.dao;

import static android.provider.BaseColumns._ID;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author xuexiaojian
 *
 */
public abstract class DAOHelper extends SQLiteOpenHelper implements DatabaseConstants {

	// test datebase
	protected static final String TEST_TABLE_NAME = "test";
    protected static final String TEST_NAME = "name";
    protected static final String TEST_INT = "int";
    protected static final String TEST_CHAR = "char";
    protected static final String TEST_FLOAT = "float";
    protected static final String TEST_DOUBLE = "double";
    protected static final String[] TEST_ALL_COLUMS = { _ID, TEST_NAME, TEST_INT, TEST_CHAR, TEST_FLOAT, TEST_DOUBLE };
    private static final String TEST_TABLE_CREATE = 
    		"CREATE TABLE" + TEST_TABLE_NAME + "(" +
    		_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + 
    		TEST_NAME + "NTEXT NOT NULL, " +
    		TEST_INT + "INTEGER, " + 
    		TEST_CHAR + "NCHAR(1), " +
    		TEST_FLOAT + "FLOAT," +
    		TEST_DOUBLE + "DOUBLE" + 
    		");";
    
    // file datebase
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
    protected static final String[] FILE_ALL_COLUMS = { _ID, FILE_FILE_NAME, FILE_CREATE_TIME, FILE_X_DIRECT, 
    	FILE_Y_DIRECT, FILE_Z_DIRECT, FILE_LONGITUDE, FILE_LATITUDE, FILE_EXPOSURE_VALUE, FILE_FOCAL_DISTANCE,
    	FILE_APERTURE };
    private static final String FILE_TABLE_CREATE = 
    		"CREATE TABLE" + FILE_TABLE_NAME + "(" +
    		_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + 
    		FILE_FILE_NAME + "NTEXT NOT NULL, " +
    		FILE_CREATE_TIME + "DATE NOT NULL, " +
    		FILE_X_DIRECT + "FLOAT, " + 
    		FILE_Y_DIRECT + "FLOAT, " + 
    		FILE_Z_DIRECT + "FLOAT, " +
    		FILE_LONGITUDE + "FLOAT, " + 
    		FILE_LATITUDE + "FLOAT, " + 
    		FILE_EXPOSURE_VALUE + "INTEGER, " + 
    		FILE_FOCAL_DISTANCE + "FLOAT, " + 
    		FILE_APERTURE + "FLOAT" +
    		");";
    		
	
	public DAOHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(TEST_TABLE_CREATE);
		db.execSQL(FILE_TABLE_CREATE);
		
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TEST_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + FILE_TABLE_NAME);
		onCreate(db);
	}
	
	protected void closeCursor(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }
    }
}
