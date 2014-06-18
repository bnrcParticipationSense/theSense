package com.bupt.bnrc.thesenser.utils;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import com.bupt.bnrc.thesenser.model.*;

public class JSON {
	static public JSONObject toJSON(String str) {
		JSONObject obj = null;
		try {
			obj = new JSONObject(str);
		} catch(JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	static public JSONObject toJSON(DataModel [] list) {
		JSONObject obj = new JSONObject();
		JSONArray jsonArr = new JSONArray();
		try {
			obj.put("username", "zzy");
			for(DataModel data : list) {
				JSONObject dataJSON = new JSONObject();
				
				dataJSON.put("Time", data.getCreateTimeString());
				
				dataJSON.put("Light", data.getLightIntensity());
				dataJSON.put("Noise", data.getSoundIntensity());
				
				dataJSON.put("BatteryState", data.getBatteryState());
				dataJSON.put("ChargeState", data.getChargeState());
				dataJSON.put("NetState", data.getNetState());
				
				dataJSON.put("Latitude", data.getLatitude());
				dataJSON.put("Longitude", data.getLongitude());
				
				jsonArr.put(dataJSON);
			}
			obj.put("data", jsonArr);
		} catch(JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}
	/*
	static public JSONObject toJSON(DataModel data) {
		JSONObject obj = new JSONObject();
		try {
			obj.put("username", "zzy");
				
			obj.put("Time", data.getCreateTime());
				
			obj.put("Light", data.getLightIntensity());
			obj.put("Noise", data.getSoundIntensity());
				
			obj.put("BatteryState", data.getBatteryState());
			obj.put("ChargeState", data.getChargeState());
			obj.put("NetState", data.getNetState());
				
			obj.put("Latitude", data.getLatitude());
			obj.put("Longitude", data.getLongitude());
				
		} catch(JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}
	*/
	static public JSONObject toJSON(DataModel data) {
		JSONObject obj = new JSONObject();
		JSONArray jsonArr = new JSONArray();
		try {
			obj.put("username", "zzy");
			
				JSONObject dataJSON = new JSONObject();
				
				dataJSON.put("Time", data.getCreateTimeString());
				
				dataJSON.put("Light", data.getLightIntensity());
				try {
					dataJSON.put("Noise", data.getSoundIntensity());
				} catch(JSONException e) {
					dataJSON.put("Noise", "NaN");
				}
				
				dataJSON.put("BatteryState", data.getBatteryState());
				dataJSON.put("ChargeState", data.getChargeState());
				dataJSON.put("NetState", data.getNetState());
				
				dataJSON.put("Latitude", data.getLatitude());
				dataJSON.put("Longitude", data.getLongitude());
				
				jsonArr.put(dataJSON);
			
			obj.put("data", jsonArr);
		} catch(JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	static public JSONObject toJSON(FileModel file) {
		JSONObject obj = new JSONObject();
		try {
			obj.put("username", "zzy");
		} catch(JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}
	

}
