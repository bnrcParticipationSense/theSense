package com.bupt.bnrc.thesenser.dao;

import static android.provider.BaseColumns._ID;
import com.bupt.bnrc.thesenser.utils.Logger;

import com.bupt.bnrc.thesenser.model.testModel;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class TestDAO extends DAOHelper {

	public TestDAO(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}
/*
	public testModel save(testModel test) {
        SQLiteDatabase db = getWritableDatabase();
        if (test.getId() != null) {
            return updateExistingTest(db, test);
        } else {
            return createNewTest(db, test);
        }
    }

	private testModel createNewTest(SQLiteDatabase db, testModel test) {
		// TODO Auto-generated method stub
		return null;
	}

	private testModel updateExistingTest(SQLiteDatabase db, testModel test) {
		// TODO Auto-generated method stub
		Logger.d("Updating team with the name of '" + test.getTestName() + "'");
		ContentValues values = new ContentValues();
        values.put(TEST_NAME, test.getTestName());
        long id = db.update(TEST_TABLE_NAME, values, _ID + " = ?", new String[]{test.getId().toString()});
        return new testModel(id, test.getTestName());
	}
	*/
}
