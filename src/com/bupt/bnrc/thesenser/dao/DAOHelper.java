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
	
	public DAOHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(TEST_TABLE_CREATE);
		
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TEST_TABLE_NAME);
		onCreate(db);
	}
	
	protected void closeCursor(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }
    }
}
