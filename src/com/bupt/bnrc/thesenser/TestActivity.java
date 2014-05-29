package com.bupt.bnrc.thesenser;

import com.bupt.bnrc.thesenser.BaseActivity;
import com.bupt.bnrc.thesenser.utils.Logger;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class TestActivity extends BaseActivity implements OnClickListener {

	Collection collect = null;
	Thread t = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Logger.i("���寮�娴�璇�椤典富椤�");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		collect = new Collection(this);
		/*
		t = new Thread() {
			public void run() {
				while(true) {
					try {
						sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					collect.save();
				}
			}
		};
		t.start();
		*/
		initViews();
	}
	
	private void initViews() {
		View cameraBtn = findViewById(R.id.cameraBtn);
		View collectBtn = findViewById(R.id.collectBtn);
		View saveInfo = findViewById(R.id.saveInfo);
		View exitBtn = findViewById(R.id.exitBtn);
		View uploadBtn = findViewById(R.id.upload);
		cameraBtn.setOnClickListener(this);
		View dataBtn = findViewById(R.id.dataTestBtn);
		View fileBtn = findViewById(R.id.fileTestBtn);
		fileBtn.setOnClickListener(this);
		dataBtn.setOnClickListener(this);
		collectBtn.setOnClickListener(this);
		saveInfo.setOnClickListener(this);
		uploadBtn.setOnClickListener(this);
		exitBtn.setOnClickListener(this);
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
			collect.stopListener();
			//collect = new Collection(this);
			/*
			 * something WRONG with this part need to lock THREAD before wait() or stop()
			try {
				t.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			*/
			break;
			
		case R.id.fileTestBtn:
			processFileBtnClick();
			break;
			
		case R.id.saveInfo:
			Log.i("TestActivity", "this.collect.save()");
			this.collect.save();
			break;
			
		case R.id.upload:
			Log.i("TestActivity", "this.collect.upload()");
			this.collect.upload();
			break;
			
		case R.id.exitBtn:
			System.exit(0);
		}
	}

	private void processFileBtnClick() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(TestActivity.this, FileActivity.class);
		startActivity(intent);
	}

	private void processDataBtnClick() {
		Intent intent = new Intent(TestActivity.this, DataActivity.class);
		startActivity(intent);
	}

	private void processCameraBtnClick() {
		Logger.d("杩���ョ�告�烘��璇�椤�");
		Intent intent = new Intent(TestActivity.this, CameraActivity.class);
		startActivity(intent);
	}

}
