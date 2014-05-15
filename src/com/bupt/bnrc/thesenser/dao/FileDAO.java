package com.bupt.bnrc.thesenser.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.bupt.bnrc.thesenser.model.FileModel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bupt.bnrc.thesenser.utils.Logger;

public class FileDAO extends DAOHelper {
	
	private static SimpleDateFormat m_sdf = null;
	public FileDAO(Context ctx) {
		super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
		if(m_sdf == null)
			m_sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	}
	
	public FileModel save(FileModel file) {
		if(file.getId() == null) {
			SQLiteDatabase db = getWritableDatabase();
			return createNewFile(db, file);
		} else {
			String msg = "Attempting to update an existing file.  file entries cannot be updated.";
			Logger.w(msg);
			throw new RuntimeException(msg);
		}
	}

	private FileModel createNewFile(SQLiteDatabase db, FileModel file) {
		// TODO Auto-generated method stub
		Logger.d("Creating new file for " + file.getFileName() + " with a file create time of '" + m_sdf.format(file.getCreateTime()) + "'");
        ContentValues values = createContentValues(file);
        long id = db.insertOrThrow(FILE_TABLE_NAME, null, values);
        return new FileModel(id, file);
	}

	private ContentValues createContentValues(FileModel file) {
		// TODO Auto-generated method stub
		ContentValues values = new ContentValues();
		values.put(FILE_FILE_NAME, file.getFileName());
		values.put(FILE_CREATE_TIME, m_sdf.format(file.getCreateTime()));
		values.put(FILE_X_DIRECT, file.getPhotoStats().getXDirect());
		values.put(FILE_Y_DIRECT, file.getPhotoStats().getYDirect());
		values.put(FILE_Z_DIRECT, file.getPhotoStats().getZDirect());
		values.put(FILE_LONGITUDE, file.getPhotoStats().getLongitude());
		values.put(FILE_LATITUDE, file.getPhotoStats().getLatitude());
		values.put(FILE_EXPOSURE_VALUE, file.getPhotoStats().getExposureValue());
		values.put(FILE_FOCAL_DISTANCE, file.getPhotoStats().getFocalDistance());
		values.put(FILE_APERTURE, file.getPhotoStats().getAperture());
		values.put(FILE_TAG, file.getTag());
		return values;
	}
	
	/*
	public List<FileModel> findNotUploadFiles(Integer num) {
		List<FileModel> files = new ArrayList<FileModel>();
		Cursor cursor = null;
		
	}
	*/
}
