package com.bupt.bnrc.thesenser;

import com.bupt.bnrc.thesenser.model.DataModel;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class InfoCollectFragment extends Fragment {
	
	private TextView mShowTextView;
	private DataModel mData;
	private Collection mCollection = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO If there is some argv needed, read them here
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View infoCollectView = inflater.inflate(R.layout.fragment_info_collect, container, false);
		InitData();
		InitView(infoCollectView);
		return infoCollectView;
	}

	private void InitData() {
		// TODO Auto-generated method stub
		mCollection = new Collection(getActivity());
		RefreshData();
	}

	private void RefreshData() {
		mData = mCollection.getDataModel();
		
	}

	private void InitView(View view) {
		// TODO Auto-generated method stub
		mShowTextView = (TextView)view.findViewById(R.id.info_collect_id_show);
		RefreshView();
	}
	
	private void RefreshView() {
		// TODO Auto-generated method stub
		String tempStr = transDataToMsg(mData);
		mShowTextView.setText(tempStr);
	}

	private String transDataToMsg(DataModel data) {
		String message = null;
		if(data != null) {
			message = "当前周边信息为:\n\n";
			message += "光照强度:" + data.getLightIntensity().toString() + "\n";
			message += "噪声强度:" + data.getSoundIntensity().toString() + "\n";
			message += "电池状态:" + data.getBatteryState().toString() + "\n";
			message += "充电状态:" + data.getChargeState().toString() + "\n";
			message += "网络状态:" + data.getNetState().toString() + "\n";
			message += "经度: " + data.getLongitude().toString() + "\n";
			message += "纬度：" + data.getLatitude().toString() + "\n";
		}
		return message;
	}

}
