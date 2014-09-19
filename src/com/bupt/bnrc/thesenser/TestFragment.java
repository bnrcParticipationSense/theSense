package com.bupt.bnrc.thesenser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.json.JSONObject;

import android.R.string;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bupt.bnrc.thesenser.model.DataModel;
import com.bupt.bnrc.thesenser.utils.CommonDefinition;
import com.bupt.bnrc.thesenser.utils.JSON;
import com.bupt.bnrc.thesenser.utils.Logger;
import com.bupt.bnrc.thesenser.utils.NetworkUtil;
import com.bupt.bnrc.thesenser.utils.SendData;
import com.bupt.bnrc.thesenser.utils.SendFile;
import com.bupt.bnrc.thesenser.utils.Upload;

public class TestFragment extends Fragment implements OnClickListener {

	Collection collect = null;
	List<DataModel> datas = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setHasOptionsMenu(true);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View testView = inflater.inflate(R.layout.activity_test, container,
				false);
		int i = getArguments().getInt(FragmentFactory.ARG_MAIN_INDEX);
		String title = getResources().getStringArray(R.array.main_list_array)[i];
		initViews(testView);
		getActivity().setTitle(title);
		return testView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
		menu.findItem(R.id.action_take_photo).setVisible(false);
	}

	private void initViews(View parentView) {
		View cameraBtn = parentView.findViewById(R.id.cameraBtn);
		View collectBtn = parentView.findViewById(R.id.collectBtn);
		View saveInfo = parentView.findViewById(R.id.saveInfo);
		View exitBtn = parentView.findViewById(R.id.exitBtn);
		View dataBtn = parentView.findViewById(R.id.dataTestBtn);
		View fileBtn = parentView.findViewById(R.id.fileTestBtn);
		View infoBtn = parentView.findViewById(R.id.infoBtn);
		View uploadBtn = parentView.findViewById(R.id.uploadBtn);
		View sendBtn = parentView.findViewById(R.id.sendmsg);
		View sendPic = parentView.findViewById(R.id.sendpic);
//		View wifiBtn = parentView.findViewById(R.id.wifiBtn);

		//collect = Collection.getCollection(getActivity().getBaseContext());
		collect = Collection.getCollection();
		collect.setLocation();

		
		

		cameraBtn.setOnClickListener(this);
		fileBtn.setOnClickListener(this);
		dataBtn.setOnClickListener(this);
		collectBtn.setOnClickListener(this);
		saveInfo.setOnClickListener(this);
		exitBtn.setOnClickListener(this);
		infoBtn.setOnClickListener(this);
		uploadBtn.setOnClickListener(this);
		sendBtn.setOnClickListener(this);
		sendPic.setOnClickListener(this);
//		wifiBtn.setOnClickListener(this);
	}

	private void showinfo() {
		// collect.showinfo(getActivity());
		String str = "光线：" + collect.getLight() + ";\n" + "噪音："
				+ collect.getNoise() + ";\n" + "经度：" + collect.longitude
				+ ";\n" + "纬度：" + collect.latitude + "\n" + "x方向："
				+ collect.getxDirect() + ";\n" + "y方向：" + collect.getyDirect()
				+ ";\n" + "z方向：" + collect.getzDirect() + ";\n" + "电量："
				+ collect.getBatteryState() + ";\n" + "时间："
				+ collect.getDateSring() + ";";
		Toast toast = Toast.makeText(getActivity(), str, Toast.LENGTH_LONG);
		toast.show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cameraBtn:
			processCameraBtnClick();
			break;
		case R.id.dataTestBtn:
			processDataBtnClick();
			break;
		case R.id.collectBtn:
			Log.i("zzy", "collectBtn");
			//collect.stopListener();
			//collect = new Collection(this);
			//if(thread_flag) {
			//	thread_flag = false;
			//}
			//collect.stop();
			getActivity().stopService(MainActivity.intent);
			System.exit(0);
			break;

		case R.id.fileTestBtn:
			processFileBtnClick();
			break;

		case R.id.saveInfo:
			Log.i("TestActivity", "this.collect.save()");
			this.collect.save();
			break;

		case R.id.infoBtn:
			Log.i("TestActivity", "infoBtn");
			showinfo();
			break;
		case R.id.uploadBtn:
			Log.i("TestActivity", "this.collect.upload()");
			this.collect.upload();
			break;

		case R.id.sendmsg:
			SendData.send(getActivity());
//			datas = DataModel.findNotUploadDatas(500, getActivity());
//			Thread t = new Thread() {
//				public void run() {
//
//					Looper.prepare();
////					JSONObject obj = null;
////					String str = "{\"request_type\":\"photo_list\",\"request_maxnum\": \"50\",\"begin_time\": \"2010-12-26 03:36:25\"}";
////					try {
////						obj = Upload.Uploading(getActivity(), "",
////								JSON.toJSON(str));
////					} catch (Exception e) {
////						e.printStackTrace();
////					}
////
////					if (obj != null) {
////						/*
////						 * 解析obj，干点啥。。。
////						 */
////						Toast.makeText(getActivity(), obj.toString(),
////								Toast.LENGTH_LONG).show();
////					} else {
////						/*
////						 * 发送失败，干点啥。。。
////						 */
////						Toast.makeText(getActivity(), "obj == null",
////								Toast.LENGTH_LONG).show();
////					}
//					JSONObject obj = null;
//					obj = JSON.toJSON(datas);
//					try {
//						obj = Upload.Uploading(getActivity(), "",
//								obj);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//						
//					Looper.loop();
//				}
//			};
//			t.start();
			break;
		case R.id.sendpic:
		    SendFile.send(getActivity());
		    break;
		/*case R.id.wifiBtn:
            func();
            break;*/

		case R.id.exitBtn:
			System.exit(0);
		}
	}
	
	private void func() {
	    //
	    Thread t = new Thread() {
	        public void run() {
	            Looper.prepare();
	            try{
	                if(Upload.isConnection())
	                    Log.i("Upload", "isConnection = ture");
	                else
	                    Log.i("Upload", "isConnection = false");
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	            Looper.loop();
	        }
	    };
	    
	    t.start();
	}

	private void processFileBtnClick() {
		Intent intent = new Intent(getActivity(), FileActivity.class);
		startActivity(intent);
	}

	private void processDataBtnClick() {
		Intent intent = new Intent(getActivity(), DataActivity.class);
		startActivity(intent);
	}

	private void processCameraBtnClick() {
		Logger.d("processCameraBtnClick()");
		Intent intent = new Intent(getActivity(), CameraUploadAcitivity.class);
		//getActivity().startService(intent);
		startActivity(intent);
		
	}

}
