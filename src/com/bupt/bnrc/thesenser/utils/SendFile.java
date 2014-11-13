package com.bupt.bnrc.thesenser.utils;

import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.bupt.bnrc.thesenser.model.FileModel;

public class SendFile {
    	
	static public void send(final Context context) {
		final List<FileModel> files = FileModel.findNotUploadFiles(500, context);
		
	
		Thread t = new Thread() {
			public void run() {
				int number = 0;
				int failed = 0;
				Looper.prepare();

				if(Upload.isConnection()) {
				    for(FileModel file:files){
	                    try {
	                        JSONObject jsonObject = null;
	                        //Upload.Uploading(context,CommonDefinition.SERVER_URL_FILE, file.getFileName());
	                        Log.i("SendFile", "NEW Thread for UploadingPrecess...");

	                        if(Upload.Uploading(CommonDefinition.SERVER_URL_FILE, file.getFileName())) {
	                            number++;
	                            Toast.makeText(context, "上传中："+number+"/"+files.size()+"；上传失败："+failed+"张；", Toast.LENGTH_SHORT).show();
	                        }
	                        else {
	                            failed++;
	                            Toast.makeText(context, "上传中："+number+"/"+files.size()+"；上传失败："+failed+"张；", Toast.LENGTH_SHORT).show();
	                        }
	                        //Upload.Uploading(CommonDefinition.TEST_SERVER_URL_FILE, file.getFileName());
	                    } catch (Exception e) {
	                        Toast.makeText(context, "上传中："+number+"/"+files.size()+"；上传失败："+failed+"张；", Toast.LENGTH_SHORT).show();
	                        e.printStackTrace();
	                    }   
	                }
	                
	                if(number < files.size()) {
	                    Toast.makeText(context, "上传失败,请等网络较好时再传", Toast.LENGTH_SHORT).show();
	                } else {
	                    Toast.makeText(context, "上传成功,上传数量为" + number, Toast.LENGTH_SHORT).show();
    	                FileModel.setUploadFilesNumber(number, context);
	                }
				} else {
				    Toast.makeText(context, "无法连接服务器，请稍后再试", Toast.LENGTH_LONG).show();
				}
					
				Looper.loop();
			}
		};
		
		if(files.size()>0){
			Toast.makeText(context, "上传开始，请耐心等待", Toast.LENGTH_LONG).show();
			t.start();
			try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
		} else {
			Toast.makeText(context, "没有需要上传的文件", Toast.LENGTH_SHORT).show();
		}
		
	}

}
