package com.bupt.bnrc.thesenser;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bupt.bnrc.thesenser.model.DataModel;

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
		View infoCollectView = inflater.inflate(R.layout.fragment_info_collect,
				container, false);
		InitData();
		InitView(infoCollectView);
		return infoCollectView;
	}

	private void InitData() {
		// TODO Auto-generated method stub
		// mCollection = new Collection(getActivity());
		// mCollection = Collection.getCollection(getActivity());
		RefreshData();
	}

	private void RefreshData() {
		mData = mCollection.getDataModel();

	}

	private void InitView(View view) {
		// TODO Auto-generated method stub
		mShowTextView = (TextView) view.findViewById(R.id.info_collect_id_show);
		RefreshView();
	}

	private void RefreshView() {
		// TODO Auto-generated method stub
		String tempStr = transDataToMsg(mData);
		mShowTextView.setText(tempStr);
	}

	private String transDataToMsg(DataModel data) {
		String message = null;
		if (data != null) {
			message = "��ǰ�ܱ���ϢΪ:\n\n";
			message += "����ǿ��:" + data.getLightIntensity().toString() + "\n";
			message += "����ǿ��:" + data.getSoundIntensity().toString() + "\n";
			message += "���״̬:" + data.getBatteryState().toString() + "\n";
			message += "���״̬:" + data.getChargeState().toString() + "\n";
			message += "����״̬:" + data.getNetState().toString() + "\n";
			message += "����: " + data.getLongitude().toString() + "\n";
			message += "γ�ȣ�" + data.getLatitude().toString() + "\n";
		}
		return message;
	}

}
