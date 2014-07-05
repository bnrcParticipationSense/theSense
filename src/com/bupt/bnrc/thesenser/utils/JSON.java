package com.bupt.bnrc.thesenser.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bupt.bnrc.thesenser.model.DataModel;
import com.bupt.bnrc.thesenser.model.FileModel;
import com.bupt.bnrc.thesenser.model.WebPhotoModel;

public class JSON {
	static public JSONObject toJSON(String str) {
		JSONObject obj = null;
		try {
			obj = new JSONObject(str);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}

	// webphotos request
	static public JSONObject photoListRequestToJson(int maxNum) {
		JSONObject obj = new JSONObject();
		try {
			obj.put("request_type", "photo_list");
			obj.put("request_maxnum", maxNum);
			Date date = TimeController.getDateDiffHours(-2);
			// Date date = TimeController.getToday();
			Long time = date.getTime();
			obj.put("begin_time", time.toString());
		} catch (JSONException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return obj;
	}

	// webphotos request
	static public List<WebPhotoModel> getPhotoListFromJson(JSONObject obj) {
		List<WebPhotoModel> photoList = new ArrayList<WebPhotoModel>();
		try {
			String type = obj.getString("response_type");
			if (type.equals("photo_list")) {
				int listnum = obj.getInt("response_num");
				JSONArray photoArray = obj.getJSONArray("response_photos");
				for (int i = 0; i < listnum; i++) {
					JSONObject photoObject = photoArray.getJSONObject(i);
					String packUrl = photoObject.getString("pack_url");
					String srcUrl = photoObject.getString("src_url");
					// add pre
					packUrl = "http://10.108.107.92:8080/" + packUrl;
					srcUrl = "http://10.108.107.92:8080/" + srcUrl;
					WebPhotoModel photo = new WebPhotoModel(packUrl, srcUrl);
					photoList.add(photo);
				}
			}
		} catch (JSONException e) {
			// TODO: handle exception
			Logger.e(e.getMessage());
		}

		return photoList;
	}

	static public List<WebPhotoModel> getPhotoListFromJsonTemp(JSONObject obj) {
		List<WebPhotoModel> photoList = new ArrayList<WebPhotoModel>();
		try {
			String type = obj.getString("response_type");
			if (type.equals("photo_list")) {
				int listnum = obj.getInt("response_num");
				JSONArray photoArray = obj.getJSONArray("response_photos");
				for (int i = 0; i < listnum; i++) {
					JSONObject photoObject = photoArray.getJSONObject(i);
					String packUrl = photoObject.getString("pack_url");
					String srcUrl = photoObject.getString("src_url");
					WebPhotoModel photo = new WebPhotoModel(packUrl, srcUrl);
					photoList.add(photo);
				}
			}
		} catch (JSONException e) {
			// TODO: handle exception
			Logger.e(e.getMessage());
		}

		return photoList;
	}

	static public JSONObject toJSON(List<DataModel> datas) {
		JSONObject obj = new JSONObject();
		JSONArray jsonArr = new JSONArray();
		try {
			obj.put("username", "zzy");
			for (DataModel data : datas) {
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
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}
	static public JSONObject toJSON(DataModel[] list) {
		JSONObject obj = new JSONObject();
		JSONArray jsonArr = new JSONArray();
		try {
			obj.put("username", "zzy");
			for (DataModel data : list) {
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
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}

	/*
	 * static public JSONObject toJSON(DataModel data) { JSONObject obj = new
	 * JSONObject(); try { obj.put("username", "zzy");
	 * 
	 * obj.put("Time", data.getCreateTime());
	 * 
	 * obj.put("Light", data.getLightIntensity()); obj.put("Noise",
	 * data.getSoundIntensity());
	 * 
	 * obj.put("BatteryState", data.getBatteryState()); obj.put("ChargeState",
	 * data.getChargeState()); obj.put("NetState", data.getNetState());
	 * 
	 * obj.put("Latitude", data.getLatitude()); obj.put("Longitude",
	 * data.getLongitude());
	 * 
	 * } catch(JSONException e) { e.printStackTrace(); } return obj; }
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
			} catch (JSONException e) {
				dataJSON.put("Noise", "NaN");
			}

			dataJSON.put("BatteryState", data.getBatteryState());
			dataJSON.put("ChargeState", data.getChargeState());
			dataJSON.put("NetState", data.getNetState());

			dataJSON.put("Latitude", data.getLatitude());
			dataJSON.put("Longitude", data.getLongitude());

			jsonArr.put(dataJSON);

			obj.put("data", jsonArr);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}

	static public JSONObject toJSON(FileModel file) {
		JSONObject obj = new JSONObject();
		try {
			obj.put("username", "zzy");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}

}
