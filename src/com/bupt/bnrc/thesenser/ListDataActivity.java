package com.bupt.bnrc.thesenser;

import java.util.List;

import com.bupt.bnrc.thesenser.model.DataModel;
import com.bupt.bnrc.thesenser.utils.Logger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ListDataActivity extends Activity implements OnClickListener {
	private Integer num = null;
	private List<DataModel> datas = null;
	private Integer index = null;
	
	private TextView titleView = null;
	private TextView detailsView = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Logger.i("鎵撳紑鏁版嵁鍒楄〃椤�");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_data);
		
		initDatas();
		initViews();
		refreshState();
	}

	private void refreshState() {
		// TODO Auto-generated method stub
		String titleStr = "娌℃湁鎵惧埌鏁版嵁";
		String detailsStr = "鏃犳暟鎹�";
		
		View preBtn = findViewById(R.id.listData_pre_btn);
		View nextBtn = findViewById(R.id.listData_next_btn);
		
		if(index != null) {
			titleStr = String.format("瑕佹煡鎵�%d涓暟鎹紝鍏辨壘鍒� %d 涓暟鎹�, 褰撳墠涓虹 %d 涓�", num, datas.size(), index+1);
			detailsStr = transDataToMsg(datas.get(index));
			preBtn.setEnabled(index > 0);
			nextBtn.setEnabled(index < datas.size() - 1);
		} else {
			preBtn.setEnabled(false);
			nextBtn.setEnabled(false);
		}
		
		titleView.setText(titleStr);
		detailsView.setText(detailsStr);
	}

	private void initViews() {
		// TODO Auto-generated method stub
		titleView = (TextView)findViewById(R.id.listData_title);
		
		
		detailsView = (TextView)findViewById(R.id.listData_details);
		
		
		View preBtn = findViewById(R.id.listData_pre_btn);
		View nextBtn = findViewById(R.id.listData_next_btn);
		
		preBtn.setOnClickListener(this);
		nextBtn.setOnClickListener(this);
		
		
		
	}

	private void initDatas() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		num = intent.getIntExtra("num", 0);
		datas = DataModel.findNotUploadDatas(num, this);
		
		int datasSize = datas.size();
		if(datasSize > 0) {
			index = 0;
		}
	}
	
	private String transDataToMsg(DataModel data) {
		String message = null;
		if(data != null) {
			message = "鍙栧埌鐨勬暟鎹槸:\n";
			message += "ID:" + data.getId().toString() + "\n";
			message += "CreateTime:" + data.getCreateTimeString() + "\n";
			message += "鍏夌収寮哄害:" + data.getLightIntensity().toString() + "\n";
			message += "鍣０寮哄害:" + data.getSoundIntensity().toString() + "\n";
			message += "鐢垫睜鐘舵��:" + data.getBatteryState().toString() + "\n";
			message += "鍏呯數鐘舵��:" + data.getChargeState().toString() + "\n";
			message += "缃戠粶鐘舵��:" + data.getNetState().toString() + "\n";
			message += "缁忓害: " + data.getLongitude().toString() + "\n";
			message += "绾害锛�" + data.getLatitude().toString() + "\n";
		}
		return message;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.listData_pre_btn:
			OnClickPreBtn();
			break;
		case R.id.listData_next_btn:
			OnClickNextBtn();
			break;
		default:
			break;
		}
	}

	private void OnClickNextBtn() {
		// TODO Auto-generated method stub
		index++;
		refreshState();
	}

	private void OnClickPreBtn() {
		// TODO Auto-generated method stub
		index--;
		refreshState();
	}
}
