package com.bupt.bnrc.thesenser;

import com.bupt.bnrc.thesenser.model.DataModel;
import com.bupt.bnrc.thesenser.utils.Logger;

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

public class DataActivity extends Activity implements OnClickListener {
	private View listView = null;
	private View OneView = null;
	private Dialog listDialog = null;
	private Dialog oneDialog = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Logger.i("鎵撳紑鏁版嵁娴嬭瘯椤�");
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
			builder.setTitle("瑕佹煡鎵剧殑鍓峮涓暟鎹�");
			builder.setView(getListView());
			builder.setCancelable(true);
	        builder.setPositiveButton("纭畾", lookListData());
	        builder.setNegativeButton("鍙栨秷", cancelListener());
	        listDialog = builder.create();
		}
		listDialog.show();
		
		
	}

	private DialogInterface.OnClickListener lookListData() {
		return new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                EditText collectedTextView = (EditText) getListView().findViewById(R.id.get_list);
                String dataId = collectedTextView.getText().toString();
                Integer inputNum = Integer.parseInt(dataId);
                Bundle bundle = new Bundle();
                bundle.putInt("num", inputNum);
                Intent intent = new Intent(DataActivity.this, ListDataActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        };
	}

	private void OnClickLookOneBtn() {
		if(oneDialog == null) {
			AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
			builder1.setTitle("瑕佹煡鎵剧殑id");
			builder1.setView(getOneView());
			builder1.setCancelable(true);
	        builder1.setPositiveButton("纭畾", lookOneData());
	        builder1.setNegativeButton("鍙栨秷", cancelListener());
	        oneDialog = builder1.create();
		}
		oneDialog.show();
	}
	
	public void showData(Long selectId) {
		DataModel data;
		if(selectId == null)
			selectId = Long.valueOf(1);
		data = DataModel.findDataById(selectId, this);	
		String message = transDataToMsg(data);
		
		if(message == null) {
			message = "ID 涓嶅锛� 鏌ヤ笉鍒版暟鎹紒";
		}
		// 瀵硅瘽妗�
		AlertDialog.Builder builder = new AlertDialog.Builder(this)
		.setTitle("鍙栧嚭鐨勬暟鎹�")
		.setMessage(message);
		
		builder.create().show();
	}

	protected DialogInterface.OnClickListener lookOneData() {
        return new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                EditText collectedTextView = (EditText) getOneView().findViewById(R.id.get_id);
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
	private void OnClickSaveBtn() {
		// 寰楀埌鏄庨潰涓婄殑鏁版嵁
		
		String temp = ((EditText)findViewById(R.id.data_light)).getText().toString();
		Float lightIntensity = Float.parseFloat(temp); //鍏夌収
		temp = ((EditText)findViewById(R.id.data_sound)).getText().toString();
		Float soundIntensity = Float.parseFloat(temp); // 澹伴煶
		temp = ((EditText)findViewById(R.id.data_charge)).getText().toString();
		Integer chargeState = Integer.parseInt(temp); // 0锛氭湭鍏呯數锛� 1锛氬厖鐢�
		temp = ((EditText)findViewById(R.id.data_battary)).getText().toString();
		Integer batteryState = Integer.parseInt(temp); // 鐢甸噺鐨勭櫨鍒嗘瘮
		temp = ((EditText)findViewById(R.id.data_net)).getText().toString();
		Integer netState = Integer.parseInt(temp); //0:鏃犵綉缁�    1:2G 2:3G 3:4G 4锛歸ifi
		temp = ((EditText)findViewById(R.id.data_longitude)).getText().toString();
		Float longitude = Float.parseFloat(temp);
		temp = ((EditText)findViewById(R.id.data_latitude)).getText().toString();
		Float latitude = Float.parseFloat(temp);
		

		Date createTime = new Date(); // 閲囬泦鏃堕棿
		
		DataModel data = new DataModel(lightIntensity, soundIntensity, createTime, chargeState, batteryState, netState, longitude, latitude);
		
		data.save(this);
		
		
	}
	*/
}
