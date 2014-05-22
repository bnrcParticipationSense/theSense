package com.bupt.bnrc.thesenser;

import java.util.Date;
import java.util.List;

import com.bupt.bnrc.thesenser.model.DataModel;
import com.bupt.bnrc.thesenser.utils.Logger;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class DataActivity extends BaseActivity implements OnClickListener {
	private List<DataModel> datas;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Logger.i("打开数据测试页");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.antivity_data);
		
		initViews();
	}
	
	private void initViews() {
		View saveBtn = findViewById(R.id.data_saveBtn);
		saveBtn.setOnClickListener(this);
		View lookBtn = findViewById(R.id.data_lookBtn);
		lookBtn.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.data_saveBtn:
			OnClickSaveBtn();
			break;
		case R.id.data_lookBtn:
			OnClickLookBtn();
		default:
			break;
		}
	}

	private void OnClickLookBtn() {
		// TODO Auto-generated method stub
		DataModel data;
		data = DataModel.findDataById(Long.valueOf(1), this);	
		String message = transDataToMsg(data);
		
		if(message == null) {
			message = "ID 不对！ 查不到数据！";
		}
		// 对话框
		AlertDialog.Builder builder = new AlertDialog.Builder(this)
		.setTitle("取出的数据")
		.setMessage(message);
		
		builder.create().show();
	}


	private String transDataToMsg(DataModel data) {
		// TODO Auto-generated method stub
		String message = null;
		if(data != null) {
			message = "取到的数据是:\n";
			message += "ID:" + data.getId().toString() + "\n";
			message += "CreateTime:" + data.getCreateTimeString() + "\n";
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

	private void OnClickSaveBtn() {
		// 得到明面上的数据
		String temp = ((EditText)findViewById(R.id.data_light)).getText().toString();
		Float lightIntensity = Float.parseFloat(temp); //光照
		temp = ((EditText)findViewById(R.id.data_sound)).getText().toString();
		Float soundIntensity = Float.parseFloat(temp); // 声音
		temp = ((EditText)findViewById(R.id.data_charge)).getText().toString();
		Integer chargeState = Integer.parseInt(temp); // 0：未充电， 1：充电
		temp = ((EditText)findViewById(R.id.data_battary)).getText().toString();
		Integer batteryState = Integer.parseInt(temp); // 电量的百分比
		temp = ((EditText)findViewById(R.id.data_net)).getText().toString();
		Integer netState = Integer.parseInt(temp); //0:无网络    1:2G 2:3G 3:4G 4：wifi
		temp = ((EditText)findViewById(R.id.data_longitude)).getText().toString();
		Float longitude = Float.parseFloat(temp);
		temp = ((EditText)findViewById(R.id.data_latitude)).getText().toString();
		Float latitude = Float.parseFloat(temp);
		

		Date createTime = new Date(); // 采集时间
		
		DataModel data = new DataModel(lightIntensity, soundIntensity, createTime, chargeState, batteryState, netState, longitude, latitude);
		
		data.save(this);
		
		
	}
}
