package com.bupt.bnrc.thesenser;

import com.bupt.bnrc.thesenser.BaseActivity;
import com.bupt.bnrc.thesenser.utils.Logger;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TestActivity extends BaseActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Logger.i("打开测试页主页");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		
		initViews();
	}
	
	private void initViews() {
		View cameraBtn = findViewById(R.id.cameraBtn);
		cameraBtn.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.cameraBtn:
			processCameraBtnClick();
			break;
		}
	}

	private void processCameraBtnClick() {
		// TODO Auto-generated method stub
		Logger.d("进入相机测试页");
		Intent intent = new Intent(TestActivity.this, CameraActivity.class);
		startActivity(intent);
	}

}
