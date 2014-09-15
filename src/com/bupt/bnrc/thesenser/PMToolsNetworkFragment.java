package com.bupt.bnrc.thesenser;

import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bupt.bnrc.thesenser.utils.DataCache;
import com.bupt.bnrc.thesenser.utils.JSON;
import com.bupt.bnrc.thesenser.utils.Logger;
import com.bupt.bnrc.thesenser.utils.Upload;
import com.bupt.bnrc.thesenser.view.WaterFallsView;

public class PMToolsNetworkFragment extends Fragment {
	private static final int TIMEOUT = 0;
	private static final int SUCCESS = 1;
	private static final int UNACCESS = 2;

	private boolean isLoadedFirst = true;
	private WaterFallsView mWaterFallsView = null;
	private TextView mLoadingTextView = null;

	private Handler mHandler = null;

	private DataCache mDataCache = DataCache.getInstance();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO If there is some argv needed, read them here
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_pmtools_network,
				container, false);
		initView(view);
		initControl();
		return view;
	}

	private void initControl() {
		// TODO Auto-generated method stub
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				case TIMEOUT:

					break;
				case UNACCESS:
					onNotAccessToServer();
					break;
				case SUCCESS:
					onGetPhotoListSuccess();
					break;
				default:
					break;
				}
				super.handleMessage(msg);
			}

		};
	}

	private void onNotAccessToServer() {
		if(getActivity() == null || mWaterFallsView == null) {
			return;
		}
		Toast.makeText(getActivity(), "wrong. can't access to server",
				Toast.LENGTH_SHORT).show();
		mLoadingTextView.setText("加载失败！");
		// showWaterFallView(false);
		showWaterFallView(true);
		mWaterFallsView.loadMoreImages();
	}

	private void onGetPhotoListSuccess() {
		if(getActivity() == null || mWaterFallsView == null) {
			return;
		}
		Toast.makeText(getActivity(), "request photo list success",
				Toast.LENGTH_SHORT).show();
		showWaterFallView(true);
		mWaterFallsView.loadMoreImages();
	}

	private void initView(View view) {
		// TODO Auto-generated method stub
		mWaterFallsView = (WaterFallsView) view
				.findViewById(R.id.pmtools_network_waterfalls_view);
		mLoadingTextView = (TextView) view
				.findViewById(R.id.pmtools_network_load_text);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (isLoadedFirst) {
			// �������󣬵õ�list
			// mWaterFallsView.loadMoreImages();
			showWaterFallView(false);
			new GetPhotoListThread().start();
			isLoadedFirst = false;
		}
	}

	private void showWaterFallView(boolean isShown) {
		if (isShown) {
			mLoadingTextView.setVisibility(View.GONE);
			mWaterFallsView.setVisibility(View.VISIBLE);
		} else {
			mLoadingTextView.setVisibility(View.VISIBLE);
			mWaterFallsView.setVisibility(View.GONE);
		}
	}

	private class GetPhotoListThread extends Thread {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Looper.prepare();
			JSONObject responseObject = null;
			Message msg = new Message();
			try {
				// String tempSendStr =
				// "{\"request_type\":\"photo_list\",\"request_maxnum\":\"50\",\"begin_time\":\"2010-12-26 03:36:25\"}";
				responseObject = Upload.Uploading(
						"http://10.108.107.92:8080/queryDB/getPhotoServlet",
						JSON.photoListRequestToJson(60));
				// responseObject =
				// Upload.Uploading("http://10.108.107.92:8080/queryDB/getPhotoServlet",
				// JSON.toJSON(tempSendStr));
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			if (responseObject == null) {
				// TODO
				String tempJSONStr = "{\"response_type\":\"photo_list\",\"response_num\":\"2\",\"response_photos\":["
						+ "{\"pack_url\":\"http://img.my.csdn.net/uploads/201309/01/1378037235_3453.jpg\","
						+ "\"src_url\":\"http://img.my.csdn.net/uploads/201309/01/1378037235_7476.jpg\","
						+ "\"photo_gps\":{\"gps_lat\":\"102.12\",\"gps_log\":\"23.45\"},\"photo_time\":\"174940285028\"},"
						+ "{\"pack_url\":\"http://img.my.csdn.net/uploads/201309/01/1378037235_9280.jpg\","
						+ "\"src_url\":\"http://img.my.csdn.net/uploads/201309/01/1378037234_3539.jpg\","
						+ "\"photo_gps\":{\"gps_lat\":\"102.12\",\"gps_log\":\"23.45\"},\"photo_time\":\"174940285028\"}]}";
				mDataCache.setPhotoListFromJson(JSON.toJSON(tempJSONStr));
				msg.what = UNACCESS;
				Logger.i("wrong. can't access to server");
			} else {
				mDataCache.setPhotoListFromJson(responseObject);
				msg.what = SUCCESS;
				Logger.i("success");
			}
			mHandler.sendMessage(msg);
			Looper.loop();
		}
	}
}
