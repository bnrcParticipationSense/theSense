package org.pm4j.task;

import java.io.File;

import org.opencv.android.OpenCVLoader;
import org.pm4j.engine.ModelParams;

import android.os.Environment;
import android.util.Log;

public class PMConfig {

	public static final String TAG = "PMConfig";
	
	static 
	{
    	Log.i(TAG, "load OpenCV static library!");
        if (!OpenCVLoader.initDebug()) 
        {
            // Handle initialization error here
        	Log.e(TAG, "OpenCV initDebug error!");
        } 
        else 
        {
        	Log.i(TAG, "OpenCV initDebug successfully!");
        	System.loadLibrary("org_PMEngine");
        }
    }
	
	public static final String 	version 			= "0.0.1";
	
	public static final String	jsonRequestPM		= "pm_list";
	public static final String	jsonRequestWeather	= "weather_list";
	public static final String	jsonRequestCRF		= "crf_model";
	
	public static final String  pmWeatherURL		= "";
	
	public static final int 	defaultStepDiv 		= 8;
	public static final int 	defaultBlockDiv 	= 4;
	public static final float 	defaultBlockScale 	= 1.0f;
	
	public static final String 	SDPath 				= Environment.getExternalStorageDirectory().getPath();
	public static final String 	PMHomeName 			= "aqitest";
	public static final String 	defaultModelDirName = "model";
	public static final String 	defaultTmpDirName 	= "tmp";
	public static final String 	defaultModelDirPath = SDPath + "/" + PMHomeName + "/" + defaultModelDirName + "/";
	public static final String 	defaultTmpDirPath 	= SDPath + "/" + PMHomeName + "/" + defaultTmpDirName + "/";
	
	public static final String  defaultModelBaseName= "model";
	
	public static final String 	defaultModelPath 	= defaultModelDirPath + "model1.yml";
	public static final String 	defaultCrfModelPath = defaultModelDirPath + "matmodel.xml";
	
	
	public static final int 	defaultImgWidth 	= 2048;
	public static final int 	defaultImgHeight 	= 1152;
	
	public static final int		minSelectedPhotoNum = 12;
	
	public static ModelParams getDefaultModelParams()
	{
		return new ModelParams(0, 0, defaultStepDiv, defaultBlockDiv, defaultBlockScale, defaultModelPath, defaultCrfModelPath, defaultTmpDirPath);
	}
	
	
	public static boolean init()
	{
		if(PMConfig.checkSD())
		{
			if(PMConfig.checkPMDir())
			{
				Log.i(TAG, "PM DIR OK !");
				return true;
			}
			else
			{
				Log.i(TAG, "PM DIR ERROR !");
				return false;
			}
		}
		else
		{
			Log.i(TAG, "SD CARD ERROR!");
			return false;
		}
		
	}
	
	
	public static boolean checkSD()
	{
		String status = Environment.getExternalStorageState();
		  
		if (status.equals(Environment.MEDIA_MOUNTED)) 
		{
		   return true;
		} 
		else 
		{
		   return false;
		}
	}

	public static boolean checkPMDir()
	{
		 boolean ret=true;

		 String pmDir = SDPath + "/" + PMHomeName + "/";
	     
		 File dirpath = new File(pmDir);
	     if (!dirpath.exists()) 
	     {ret &= dirpath.mkdirs();}
	     
		 File imgDirPath = new File(pmDir + defaultModelDirName);
		 if (!imgDirPath.exists()) 
		 {ret &= imgDirPath.mkdir();}
		 
		 File dataDirPath = new File(pmDir + defaultTmpDirName);
		 if (!dataDirPath.exists()) 
		 {ret &= dataDirPath.mkdir();}
	   
		 return ret;
	}
	
	public static String getPMHomePath()
	{
		return SDPath + "/" + PMHomeName + "/";
	}
	
	
}
