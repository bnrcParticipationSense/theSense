package com.bupt.bnrc.thesenser.utils;

import java.util.List;


import android.content.Context;
import android.os.Looper;
import android.util.Log;

import com.bupt.bnrc.thesenser.model.FileModel;

public class SendFile {
    	
	static public void send(final Context context) {
		final List<FileModel> files = FileModel.findNotUploadFiles(500, context);
		
	
		Thread t = new Thread() {
			public void run() {

				Looper.prepare();

				for(FileModel file:files){
					try {
						//Upload.Uploading(context,CommonDefinition.SERVER_URL_FILE, file.getFileName());
						Log.i("SendFile", "NEW Thread for UploadingPrecess...");
                        Upload.Uploading(context,CommonDefinition.SERVER_URL_FILE, file.getFileName());
                        Upload.Uploading(CommonDefinition.TEST_SERVER_URL_FILE, file.getFileName());
					} catch (Exception e) {
						e.printStackTrace();
					}	
				}
					
				Looper.loop();
			}
		};
		
		if(files.size()>0){
			t.start();
		}
		
	}

}
