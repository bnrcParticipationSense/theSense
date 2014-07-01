package com.bupt.bnrc.thesenser.dao;

import java.io.File;

import com.bupt.bnrc.thesenser.utils.Logger;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.os.Environment;

public class DatabaseContext extends ContextWrapper {

	public DatabaseContext(Context base) {
		super(base);
	}

	@Override
	public File getDatabasePath(String name) {
		// TODO Auto-generated method stub
		boolean sdExist = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
		if(!sdExist) {
			Logger.e("SD卡不存在，请加载SD卡");
			return null;
		} else {
			String dbDir = Environment.getExternalStorageDirectory().getAbsolutePath();
			dbDir += "/theSenser/database";
			String dbPath = dbDir + "/" + name;
			File dirFile = new File(dbDir);
			if(!dirFile.exists()) {
				dirFile.mkdirs();
			}
			boolean isFileCreateSuccess = false;
			File dbFile = new File(dbPath);
			if(!dbFile.exists()) {
				try {
					isFileCreateSuccess = dbFile.createNewFile();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			} else {
				isFileCreateSuccess = true;
			}
			if (isFileCreateSuccess) {
				return dbFile;
			} else {
				return null;
			}
		}
		
	}
	
	@Override
	public SQLiteDatabase openOrCreateDatabase(String name, int mode,
			CursorFactory factory) {
		SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
		return result;
	}
	
	@Override
	public SQLiteDatabase openOrCreateDatabase(String name, int mode,
			CursorFactory factory, DatabaseErrorHandler errorHandler) {
		SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
		return result;
	}
}
