package com.bupt.bnrc.thesenser.utils;

import java.util.List;
import org.json.JSONObject;

import android.content.Context;
import android.os.Looper;
import com.bupt.bnrc.thesenser.model.DataModel;

public class SendData {
	
	static public void send(final Context context) {
		final List<DataModel> datas = DataModel.findNotUploadDatas(500, context);
	
		Thread t = new Thread() {
			public void run() {

				Looper.prepare();

				JSONObject obj = null;
				obj = JSON.toJSON(datas);
				try {
					obj = Upload.Uploading(context,CommonDefinition.SERVER_URL_JSON, obj);
				} catch (Exception e) {
					e.printStackTrace();
				}
					
				Looper.loop();
			}
		};
		if(datas.size()>0){
			t.start();
		}
	}
}
