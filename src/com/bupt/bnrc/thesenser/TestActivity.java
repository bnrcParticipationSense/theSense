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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Logger.i("���寮�娴�璇�椤典富椤�");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		initViews();
	}
	
	private void initViews() {
		View cameraBtn = findViewById(R.id.cameraBtn);
		cameraBtn.setOnClickListener(this);
		View dataBtn = findViewById(R.id.dataTestBtn);
		dataBtn.setOnClickListener(this);
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
