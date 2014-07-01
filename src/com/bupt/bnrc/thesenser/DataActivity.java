package com.bupt.bnrc.thesenser;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.bupt.bnrc.thesenser.model.DataModel;
import com.bupt.bnrc.thesenser.utils.Logger;

public class DataActivity extends Activity implements OnClickListener {
	private View listView = null;
	private View OneView = null;
	private Dialog listDialog = null;
	private Dialog oneDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Logger.i("打开数据测试页");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.antivity_data);

		initViews();
	}

	private void initViews() {
		View lookOneBtn = findViewById(R.id.data_lookOneBtn);
		View lookListBtn = findViewById(R.id.data_lookListBtn);

		lookOneBtn.setOnClickListener(this);
		lookListBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.data_lookOneBtn:
			OnClickLookOneBtn();
			break;
		case R.id.data_lookListBtn:
			OnClickLookListBtn();
		default:
			break;
		}
	}

	private void OnClickLookListBtn() {
		// TODO Auto-generated method stub
		if (listDialog == null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("要查找的前n个数据");
			builder.setView(getListView());
			builder.setCancelable(true);
			builder.setPositiveButton("确定", lookListData());
			builder.setNegativeButton("取消", cancelListener());
			listDialog = builder.create();
		}
		listDialog.show();

	}

	private DialogInterface.OnClickListener lookListData() {
		return new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				EditText collectedTextView = (EditText) getListView()
						.findViewById(R.id.get_list);
				String dataId = collectedTextView.getText().toString();
				Integer inputNum = Integer.parseInt(dataId);
				Bundle bundle = new Bundle();
				bundle.putInt("num", inputNum);
				Intent intent = new Intent(DataActivity.this,
						ListDataActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		};
	}

	private void OnClickLookOneBtn() {
		if (oneDialog == null) {
			AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
			builder1.setTitle("要查找的id");
			builder1.setView(getOneView());
			builder1.setCancelable(true);
			builder1.setPositiveButton("确定", lookOneData());
			builder1.setNegativeButton("取消", cancelListener());
			oneDialog = builder1.create();
		}
		oneDialog.show();
	}

	public void showData(Long selectId) {
		DataModel data;
		if (selectId == null)
			selectId = Long.valueOf(1);
		data = DataModel.findDataById(selectId, this);
		String message = transDataToMsg(data);

		if (message == null) {
			message = "ID 不对！ 查不到数据！";
		}
		// 对话框
		AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle(
				"取出的数据").setMessage(message);

		builder.create().show();
	}

	protected DialogInterface.OnClickListener lookOneData() {
		return new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				EditText collectedTextView = (EditText) getOneView()
						.findViewById(R.id.get_id);
				String dataId = collectedTextView.getText().toString();
				Long inputNum = Long.parseLong(dataId);
				showData(inputNum);
			}
		};
	}

	protected DialogInterface.OnClickListener cancelListener() {
		return new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		};
	}

	private String transDataToMsg(DataModel data) {
		String message = null;
		if (data != null) {
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

	synchronized protected View getListView() {
		if (listView == null) {
			LayoutInflater factory = LayoutInflater.from(this);
			listView = factory.inflate(R.layout.get_data_list, null);
		}
		return listView;
	}

	synchronized protected View getOneView() {
		if (OneView == null) {
			LayoutInflater factory = LayoutInflater.from(this);
			OneView = factory.inflate(R.layout.get_data_id, null);
		}
		return OneView;
	}

	/*
	 * private void OnClickSaveBtn() { // 得到明面上的数据
	 * 
	 * String temp =
	 * ((EditText)findViewById(R.id.data_light)).getText().toString(); Float
	 * lightIntensity = Float.parseFloat(temp); //光照 temp =
	 * ((EditText)findViewById(R.id.data_sound)).getText().toString(); Float
	 * soundIntensity = Float.parseFloat(temp); // 声音 temp =
	 * ((EditText)findViewById(R.id.data_charge)).getText().toString(); Integer
	 * chargeState = Integer.parseInt(temp); // 0：未充电， 1：充电 temp =
	 * ((EditText)findViewById(R.id.data_battary)).getText().toString(); Integer
	 * batteryState = Integer.parseInt(temp); // 电量的百分比 temp =
	 * ((EditText)findViewById(R.id.data_net)).getText().toString(); Integer
	 * netState = Integer.parseInt(temp); //0:无网络 1:2G 2:3G 3:4G 4：wifi temp =
	 * ((EditText)findViewById(R.id.data_longitude)).getText().toString(); Float
	 * longitude = Float.parseFloat(temp); temp =
	 * ((EditText)findViewById(R.id.data_latitude)).getText().toString(); Float
	 * latitude = Float.parseFloat(temp);
	 * 
	 * 
	 * Date createTime = new Date(); // 采集时间
	 * 
	 * DataModel data = new DataModel(lightIntensity, soundIntensity,
	 * createTime, chargeState, batteryState, netState, longitude, latitude);
	 * 
	 * data.save(this);
	 * 
	 * 
	 * }
	 */
}
