package org.pm4j.task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.pm4j.data.FilterCriteria;
import org.pm4j.data.PMData;
import org.pm4j.data.PhotoData;
import org.pm4j.data.PhotoDataFilter;
import org.pm4j.data.WeatherData;
import org.pm4j.data.WeatherData.WeatherType;
import org.pm4j.process.PMProcess;
import org.pm4j.process.PMTaskStatus;
import org.pm4j.settings.PMSettings;

import com.bupt.bnrc.thesenser.utils.Upload;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class PreprocessingProcess extends PMProcess {

	public static final String LOG_TAG = "PMTasks";
	public static final String LOG_PREFIX = "PreprocessingProcess: ";

	public static int progressStep = 5;
	
	private boolean isValidData;
	private boolean isTrainingData;

	private List<PhotoData> photoData;

	public PreprocessingProcess(List<PhotoData> photoData, Handler taskHandler,
			boolean isTrainingData) {
		this.photoData = photoData;
		this.taskHandler = taskHandler;
		this.isTrainingData = isTrainingData;
		this.allStep = 3;
	}

	public void run() {
		Log.i(LOG_TAG, LOG_PREFIX + "start..");
		Message msg = null;
		// request data from server
		taskStatus = new PMTaskStatus(PMSettings.TASK_PREPROCESS, getCurrentProgress(1),
				progressStep, PMSettings.TASKRESULT_OK);
		taskStatus.setDetail("request pm data");
		sendMsgToHandler(taskStatus);

		Log.i(LOG_TAG, "requestPMData();");
		requestPMData();

		taskStatus = new PMTaskStatus(PMSettings.TASK_PREPROCESS, getCurrentProgress(1),
				progressStep, PMSettings.TASKRESULT_OK);
		taskStatus.setDetail("request weather data");
		sendMsgToHandler(taskStatus);

		Log.i(LOG_TAG, "requestWeatherData();");
		requestWeatherData();

		taskStatus = new PMTaskStatus(PMSettings.TASK_PREPROCESS, getCurrentProgress(2),
				progressStep, PMSettings.TASKRESULT_OK);
		taskStatus.setDetail("filtering data");
		sendMsgToHandler(taskStatus);

		// filter the data
		Log.i(LOG_TAG, "filter the data");
		PhotoDataFilter pdf = new PhotoDataFilter(photoData,
				new FilterCriteria(), isTrainingData);
		isValidData = pdf.ValidateData();

		Log.i(LOG_TAG, LOG_PREFIX + "isValidData: "
				+ photoData.get(0).getWeatherData().getWeather() + " "
				+ (isValidData ? "yes" : "no"));

		if (isValidData) {
			taskStatus = new PMTaskStatus(PMSettings.TASK_PREPROCESS, getCurrentProgress(3),
					progressStep, PMSettings.TASKRESULT_OK);
			taskStatus.setDetail("valid data");
		} else {
			taskStatus = new PMTaskStatus(PMSettings.TASK_PREPROCESS, getCurrentProgress(3),
					progressStep, PMSettings.TASKRESULT_INVALID);
			taskStatus.setDetail("data invalid");
		}

		sendMsgToHandler(taskStatus);

		Log.i(LOG_TAG, LOG_PREFIX + "end..");
	}

	public void requestPMData() {

		// to-do request pm data array from server, resolve the json object

		try {
			
			
			
			if(!PMConfig.debugMode)
			{
				Log.i(LOG_TAG, "requestPMData: request pm data from " + PMConfig.pmWeatherURL);
				JSONObject requestJson = getPMRequest();
				if(requestJson == null)
				{
					Log.i(LOG_TAG, "requestPMData: requestJson is null ");
					return;
				}
				JSONObject revJson = Upload.Uploading(PMConfig.pmWeatherURL,requestJson); 
				Log.i(LOG_TAG, "requestPMData: request pm data from " + PMConfig.pmWeatherURL + " over");
				if(revJson != null)
				{
					Log.i(LOG_TAG, "receive one PMData json: " + revJson.toString());
					updatePMData(revJson);
				}
				else
				{
					Log.i(LOG_TAG, "receive null valid PMData json: ");
				}
				
			}
			else
			{
				Log.i(LOG_TAG, "requestPMData: request pm data from testing data");
				Thread.sleep(3000);
			// ONLY FOR TEST
				int testPmArray[] = { 322, 13, 25, 12, 14, 102, 213, 9, 14, 22, 49,
					76, 74, 13, 18, 141 };
				for (int i = 0; i < photoData.size(); i++) 
				{
					PMData pmData = new PMData(1, new Date(), testPmArray[i], 0, 0);
					photoData.get(i).setPmData(pmData);
					
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void requestWeatherData() {

		// to-do request weather data array from server, resolve the json object
		try {
			
			
			if(!PMConfig.debugMode)
			{
				Log.i(LOG_TAG, "requestWeatherData: request weather data from " + PMConfig.pmWeatherURL);
				
				JSONObject requestJson = getWeatherRequest();
				if(requestJson == null)
				{
					Log.i(LOG_TAG, "requestWeatherData: requestJson is null ");
					return;
				}
				JSONObject revJson = Upload.Uploading(PMConfig.pmWeatherURL,requestJson); 

				Log.i(LOG_TAG, "requestWeatherData: request weather data from " + PMConfig.pmWeatherURL + " over");
				if(revJson != null)
				{
					Log.i(LOG_TAG, "receive one WeatherData json: " + revJson.toString());
					updateWeatherData(revJson);
				}
				else
				{
					Log.i(LOG_TAG, "receive null valid WeatherData json: ");
				}
			}
			else
			{
			// ONLY FOR TEST
				Log.i(LOG_TAG, "requestWeatherData: request weather data from testing data");
				Thread.sleep(3000);
				for(int i = 0; i < photoData.size(); i++)
				{
					WeatherData weatherData = new WeatherData("Beijing", new Date(), 0,0,0,0,0, WeatherType.SUNNY);
					
					photoData.get(i).setWeatherData(weatherData);
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public JSONObject getPMRequest() {

		Log.i(LOG_TAG, "start getPMRequest()");
		JSONObject requestObj = new JSONObject();
		JSONArray jsonArr = new JSONArray();
		try {
			requestObj.put("request_type", PMConfig.jsonRequestPM);
			requestObj.put("request_num", photoData.size());

			JSONObject gpsObj = new JSONObject();
			gpsObj.put("gps_lat", calGPS().get(0));
			gpsObj.put("gps_log", calGPS().get(1));
			requestObj.put("request_gps", gpsObj);

			JSONArray datetimeArray = new JSONArray();

			Iterator iterator = photoData.iterator();
			while (iterator.hasNext()) {
				PhotoData photo = (PhotoData) iterator.next();
				Log.i(LOG_TAG, "getPMRequest: parsing " + photo.getTime());
				JSONObject datetimeObj = new JSONObject();
				SimpleDateFormat timeFormat = new SimpleDateFormat(
						"yyyyMMddHHmmss");
				datetimeObj.put("datetime", timeFormat.format(photo.getTime()));
				datetimeArray.put(datetimeObj);
			}
			requestObj.put("request_datetimes", datetimeArray);
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
			return null;
		}

		return requestObj;
	}

	public JSONObject getWeatherRequest() {
		Log.i(LOG_TAG, "start getWeatherRequest()");
		JSONObject requestObj = new JSONObject();
		JSONArray jsonArr = new JSONArray();
		try {
			requestObj.put("request_type", PMConfig.jsonRequestWeather);
			requestObj.put("request_num", photoData.size());

			JSONObject gpsObj = new JSONObject();
			gpsObj.put("gps_lat", calGPS().get(0));
			gpsObj.put("gps_log", calGPS().get(1));
			requestObj.put("request_gps", gpsObj);

			JSONArray datetimeArray = new JSONArray();

			Iterator iterator = photoData.iterator();
			while (iterator.hasNext()) 
			{
				
				PhotoData photo = (PhotoData) iterator.next();
				
				Log.i(LOG_TAG, "getWeatherRequest: parsing " + photo.getTime());
				
				JSONObject datetimeObj = new JSONObject();
				SimpleDateFormat timeFormat = new SimpleDateFormat(
						"yyyyMMddHHmmss");
				datetimeObj.put("datetime", timeFormat.format(photo.getTime()));
				datetimeArray.put(datetimeObj);
			}
			requestObj.put("request_datetimes", datetimeArray);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

		return requestObj;
	}

	public JSONObject buildCRFModelRequest() {

		// to-do

		return null;
	}

	public void updatePMData(JSONObject jsonObj) {
		try {
			String responseType = (String) jsonObj.get("response_type");
			if (responseType.equals(PMConfig.jsonRequestPM)) 
			{
				if ((Integer) jsonObj.get("response_num") == photoData.size()) 
				{
					JSONArray pmArray = (JSONArray) jsonObj.get("response_pms");
					int siteId = (Integer) jsonObj.get("response_site");
					for (int i = 0; i < photoData.size(); i++) 
					{
						PhotoData photo = photoData.get(i);
						JSONObject pm = (JSONObject) pmArray.get(i);
						
						Log.i(LOG_TAG, LOG_PREFIX + "request image " + photo.getName() + " 's fpm is " + (Integer)pm.get("fpm"));
						
						photo.getPmData().setSiteId(siteId);
						photo.getPmData().setPmf((Integer) pm.get("fpm"));
						photo.getPmData().setPmc((Integer) pm.get("cpm"));
						photo.getPmData().setAqi((Integer) pm.get("aqi"));
					}
				}

			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void updateWeatherData(JSONObject jsonObj) {

		try {
			String responseType = (String) jsonObj.get("response_type");
			if (responseType.equals(PMConfig.jsonRequestWeather)) 
			{
				if ((Integer) jsonObj.get("response_num") == photoData.size()) 
				{
					JSONArray pmArray = (JSONArray) jsonObj
							.get("response_weahters");
					String city = (String) jsonObj.get("city");
					for (int i = 0; i < photoData.size(); i++) 
					{
						PhotoData photo = photoData.get(i);
						JSONObject pm = (JSONObject) pmArray.get(i);

						Log.i(LOG_TAG, LOG_PREFIX + "request image " + photo.getName() + " 's weather type is " + WeatherType.valueOf((Integer) pm.get("weather")));
						
						photo.getWeatherData().setCity(city);
						photo.getWeatherData().setTemperature(
								(Integer) pm.get("temperature"));
						photo.getWeatherData().setHumidity(
								(Integer) pm.get("humidity"));
						photo.getWeatherData().setPressure(
								(Integer) pm.get("pressure"));
						photo.getWeatherData().setWindspeed(
								(Integer) pm.get("windspeed"));
						photo.getWeatherData().setPrecipitation(
								(Integer) pm.get("precipitation"));
						photo.getWeatherData().setWeather(
								WeatherType.valueOf((Integer) pm.get("weather")));
					}
				}

			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	public void updateCRFModelData(JSONObject jsonObj) {

		// to-do

	}

	public List<Double> calGPS() 
	{
		Log.i(LOG_TAG, "start calGPS()");
		List<Double> gps = new ArrayList<Double>();
		gps.add(photoData.get(0).getGpsLat());
		gps.add(photoData.get(0).getGpsLog());

		return gps;
	}

	public List<PhotoData> getPhotoData() {
		return photoData;
	}

	public void setPhotoData(List<PhotoData> photoData) {
		this.photoData = photoData;
	}

	public boolean isValidData() {
		return isValidData;
	}

	
	public int getCurrentProgress(int step)
    {
    	return (int)(((double)step/(double)allStep)*progressStep);
    }

}
