package com.bupt.bnrc.thesenser.utils;

public class CommonDefinition {
	
	// Collection
	public static boolean AUTO_COLLECTION = true;
	public final static int COLLECTION_SPACE_TIME = 1000*60*60;  //1800000
	
	//Upload URL
	public final static String SERVER_URL_ROOT = "http://10.108.109.124:8080/";
	//public final static String SERVER_URL_JSON= "http://10.108.108.11/uploadjson.php";
	public final static String SERVER_URL_JSON= "http://10.108.109.124:8080/uploadFile/fileServlet";
	//public final static String TEST_SERVER_URL_FILE = "http://10.108.108.11/upload11.php";
	public final static String SERVER_URL_FILE= "http://10.108.109.124:8080/uploadFile/fileServlet";
	public final static String SERVER_URL= "http://10.108.109.124:8080/uploadFile/fileServlet";
	
	// Download URL
	public final static String SERVER_PHOTO_WALL_URL = "http://10.108.109.124:8080/queryDB/getPhotoServlet";
	
	// database
	public final static boolean DATABASE_SDCARD_SAVE = true;
	
	// pic
	public final static int PIC_WIDTH = 2048;
	public final static int PIC_HEIGHT = 1152;
	
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
	public final static String KEY_CAMERA_MODEL_ID = "model_id";
	public final static long VALUE_CAMERA_MODEL_ID_DEFAULT = -1; 
	public final static String KEY_CAMERA_MODEL_DESC = "model_desc";
	public final static String VALUE_CAMERA_MODEL_DESC_DEFAULT = "未定义模型";

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
	// PMModeling State
	public final static int PMMODELING_STATE_NONE = 0;
	
	
	// public path
	

}
