package com.bupt.bnrc.thesenser;

import com.bupt.bnrc.thesenser.utils.Logger;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

public class TestFragment extends Fragment implements OnClickListener {

	Collection collect = null;
	Thread collect_t = null;
	boolean thread_flag = true;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View testView = inflater.inflate(R.layout.activity_test, container, false);
		int i = getArguments().getInt(FragmentFactory.ARG_MAIN_INDEX);
		String title = getResources().getStringArray(R.array.main_list_array)[i];
		initViews(testView);
		getActivity().setTitle(title);
		return testView;
	}
	
	private void initViews(View parentView) {
		View cameraBtn = parentView.findViewById(R.id.cameraBtn);
		View collectBtn = parentView.findViewById(R.id.collectBtn);
		View saveInfo = parentView.findViewById(R.id.saveInfo);
		View exitBtn = parentView.findViewById(R.id.exitBtn);
		View dataBtn = parentView.findViewById(R.id.dataTestBtn);
		View fileBtn = parentView.findViewById(R.id.fileTestBtn);
		collect = new Collection(getActivity());
		collect_t = new Thread() {
			public void run() {
				thread_flag = true;
				while(thread_flag) {
					try {
						sleep(2000);
					} catch(InterruptedException e) {
						e.printStackTrace();
					}
					collect.save();
				}
			}
		};
		collect_t.start();
		
		
		cameraBtn.setOnClickListener(this);
		fileBtn.setOnClickListener(this);
		dataBtn.setOnClickListener(this);
		collectBtn.setOnClickListener(this);
		saveInfo.setOnClickListener(this);
		exitBtn.setOnClickListener(this);
	}
	
	private synchronized void threadc() {
		if(thread_flag) {
				//collect_t.wait();
			thread_flag = false;
		}
		else {
			//thread_flag = true;
			//collect_t.run();
				//collect_t.notify();
		}
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
			//collect.stopListener();
			//collect = new Collection(this);
			threadc();
			break;
			
		case R.id.fileTestBtn:
			processFileBtnClick();
			break;
			
		case R.id.saveInfo:
			Log.i("TestActivity", "this.collect.save()");
			this.collect.save();
			break;
			
		case R.id.exitBtn:
			System.exit(0);
		}
	}

	private void processFileBtnClick() {
		Intent intent = new Intent(getActivity(), FileActivity.class);
		startActivity(intent);
	}

	private void processDataBtnClick() {
		Intent intent = new Intent(getActivity(), DataActivity.class);
		startActivity(intent);
	}

	private void processCameraBtnClick() {
		Logger.d("杩���ョ�告�烘��璇�椤�");
		Intent intent = new Intent(getActivity(), CameraActivity.class);
		startActivity(intent);
	}

}
