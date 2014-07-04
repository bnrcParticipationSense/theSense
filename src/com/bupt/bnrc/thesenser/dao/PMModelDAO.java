package com.bupt.bnrc.thesenser.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.bupt.bnrc.thesenser.model.PMModelModel;
import com.bupt.bnrc.thesenser.utils.CommonDefinition;
import com.bupt.bnrc.thesenser.utils.Logger;

public class PMModelDAO extends DAOHelper {

	public PMModelDAO(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	public void save(PMModelModel pmModel) {
		try {
			SQLiteDatabase db = getReadableDatabase();
			ContentValues values = createContentValues(pmModel);
			db.insert(PM_MODEL_TABLE_NAME, null, values);
		} catch (SQLException e) {
			// TODO: handle exception
			Logger.e(e.getMessage());
		}
	}

	public List<PMModelModel> findAllModelTags(Context context) {
		List<PMModelModel> modelList = new ArrayList<PMModelModel>();
		Cursor cursor = null;
		try {
			SQLiteDatabase db = getReadableDatabase();
			cursor = db.query(PM_MODEL_TABLE_NAME, PM_MODEL_ALL_COLUMS, null,
					null, null, null, null, null);
			while (cursor.moveToNext()) {
				modelList.add(createPMModelFromCursorData(cursor));
			}
		} catch (Exception e) {
			// TODO: handle exception
			Logger.e(e.getMessage());
		} finally {
			cursor.close();
		}

		return modelList;
	}

	private ContentValues createContentValues(PMModelModel pmModel) {
		ContentValues values = new ContentValues();
		values.put(PM_MODEL_TAG, pmModel.getTag());
		values.put(PM_MODEL_DESC, pmModel.getDesc());
		return values;
	}

	private PMModelModel createPMModelFromCursorData(Cursor cursor) {
		Integer tag = cursor.getInt(0);
		String desc = cursor.getString(1);
		return new PMModelModel(tag, desc);
	}

}
