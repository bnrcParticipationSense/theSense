package com.bupt.bnrc.thesenser;

import com.bupt.bnrc.thesenser.BaseActivity;
import com.bupt.bnrc.thesenser.utils.Logger;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TestActivity extends BaseActivity implements OnClickListener {

	Collection collect = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Logger.i("���寮�娴�璇�椤典富椤�");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		collect = new Collection(this);
		initViews();
	}
	
	private void initViews() {
		View cameraBtn = findViewById(R.id.cameraBtn);
		View collectBtn = findViewById(R.id.collectBtn);
		View getLightBtn = findViewById(R.id.getLightBtn);
		View saveInfo = findViewById(R.id.saveInfo);
		TextView text = (TextView) findViewById(R.id.Text);
		View exitBtn = findViewById(R.id.exitBtn);
		cameraBtn.setOnClickListener(this);
		View dataBtn = findViewById(R.id.dataTestBtn);
		dataBtn.setOnClickListener(this);
		collectBtn.setOnClickListener(this);
		getLightBtn.setOnClickListener(this);
		saveInfo.setOnClickListener(this);
		exitBtn.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.cameraBtn:
			processCameraBtnClick();
			break;
		case R.id.dataTestBtn:
			processDataBtnClick();
			break;
		case R.id.collectBtn:
			Log.i("zzy", "collectBtn");
			collect.stopListener();
			//collect = new Collection(this);
			
			break;
		case R.id.getLightBtn:
			Log.i("zzy", "getLightBtn");
			if(this.collect == null) {
				Toast.makeText(this, "collect == null", Toast.LENGTH_LONG).show();
				break;
			}
			else {
				Toast.makeText(this, "Light = "+this.collect.getLight(), Toast.LENGTH_LONG).show();
				//Log.i("TestActivity", "this.collect.light = "+this.collect.light);
				//Log.i("TestActivity", "this.collect.Orientation = "+this.collect.orientation[0]);
				//Log.i("TestActivity", "this.collect.Orientation = "+this.collect.orientation[1]);
				//Log.i("TestActivity", "this.collect.Orientation = "+this.collect.orientation[2]);
				
				//Log.i("TestActivity", "this.collect.SensorOrientation = "+this.collect.sensor_orientation[0]);
				//Log.i("TestActivity", "this.collect.SensorOrientation = "+this.collect.sensor_orientation[1]);
				//Log.i("TestActivity", "this.collect.SensorOrientation = "+this.collect.sensor_orientation[2]);
			}
			break;
		case R.id.saveInfo:
			Log.i("TestActivity", "this.collect.save()");
			this.collect.save();
			break;
			
		case R.id.exitBtn:
			System.exit(0);
		}
	}

	private void processDataBtnClick() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(TestActivity.this, DataActivity.class);
		startActivity(intent);
	}

	private void processCameraBtnClick() {
		// TODO Auto-generated method stub
		Logger.d("杩���ョ�告�烘��璇�椤�");
		Intent intent = new Intent(TestActivity.this, CameraActivity.class);
		startActivity(intent);
	}

}
