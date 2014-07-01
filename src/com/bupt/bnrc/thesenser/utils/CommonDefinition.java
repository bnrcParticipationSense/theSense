package com.bupt.bnrc.thesenser.utils;

public class CommonDefinition {
	// database
	public final static boolean DATABASE_SDCARD_SAVE = true;
	
	// activity request code
	public final static int REQUESTCODE_CAMERA = 0;
	public final static int REQUESTCODE_PMPREDICT = 1;
	// activity result code
	public final static int RESULTCODE_CAMERA_OK = 0;
	public final static int RESULTCODE_CAMERA_CANCEL = 1;

	// activity intent key and value
	public final static String KEY_CAMERA_MODEL_TYPE = "model_type";
	public final static int VALUE_CAMERA_MODEL_TYPE_NONE = 10;
	public final static int VALUE_CAMERA_MODEL_TYPE_NEW = 11;
	public final static int VALUE_CAMERA_MODEL_TYPE_SET = 12;
	public final static int VALUE_CAMERA_MODEL_TYPE_PREDICT = 13;
	public final static String KEY_CAMERA_MODEL_TAG = "model_tag";
	public final static int VALUE_CAMERA_MODEL_TAG_DEFAULT = 10;

	public final static String KEY_MODEL_DETAIL_TAG = "model_detail_tag";
	public final static int VALUE_MODEL_DETAIL_TAG_DEFAULT = 10;

	// setting preferences

	// other preferences
	public final static String PREFERENCE_NAME = "sense_prefs";
	public final static String PREF_FILE_INDEX = "pref_file_index"; // long,
																	// default:0
	public final static long PREF_FILE_INDEX_DEFAULT = 0;
	public final static String PREF_DATA_INDEX = "pref_data_index"; // long,
																	// default:0
	public final static long PREF_DATA_INDEX_DEFAULT = 0;
	public final static String PREF_MODEL_TAG = "pref_model_tag"; // int,
																	// default:11
	public final static int PREF_MODEL_TAG_DEFAULT = 11;
	
	// PMPredict State
	public final static int PMPREDICT_STATE_NONE = 0;
	public final static int PMPREDICT_STATE_START = 1;
	public final static int PMPREDICT_STATE_FAIL = 2;
	public final static int PMPREDICT_STATE_SUCCESS = 3;

}
