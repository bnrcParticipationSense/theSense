package com.bupt.bnrc.thesenser;

import com.bupt.bnrc.thesenser.utils.Logger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

public class TestFragment extends Fragment implements OnClickListener {

	Collection collect = null;
	static Thread collect_t = null;
	boolean thread_flag = true;
	static boolean thread_uniqueness = false;
	int sleeptime = 2000;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setHasOptionsMenu(true);
		super.onCreate(savedInstanceState);
	}
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
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
		menu.findItem(R.id.action_take_photo).setVisible(false);
	}
	
	
	private void initViews(View parentView) {
		View cameraBtn = parentView.findViewById(R.id.cameraBtn);
		View collectBtn = parentView.findViewById(R.id.collectBtn);
		View saveInfo = parentView.findViewById(R.id.saveInfo);
		View exitBtn = parentView.findViewById(R.id.exitBtn);
		View dataBtn = parentView.findViewById(R.id.dataTestBtn);
		View fileBtn = parentView.findViewById(R.id.fileTestBtn);
		View infoBtn = parentView.findViewById(R.id.infoBtn);
		View uploadBtn = parentView.findViewById(R.id.uploadBtn);
		
		collect = new Collection(getActivity());
		
		if(!TestFragment.thread_uniqueness) {
			collect_t = new Thread() {
				private boolean flag = false;
				public void run() {
					flag = true;
					while(true) {
						if(!thread_flag) {
							thread_flag = true;
							forWait();
						}
						try {
							sleep(sleeptime);
						} catch(InterruptedException e) {
							e.printStackTrace();
						}
						collect.save();
					}
				}
				public synchronized void forWait() {
					if(flag) {
						try{
							wait();
							flag = false;
						}catch(InterruptedException e) {
							e.printStackTrace();
						}
					}
					else {
						try{
							flag = true;
							notify();
						}catch(Exception e) {
							e.printStackTrace();
						}
					}
				}
			};
		
			collect_t.start();
			TestFragment.thread_uniqueness = true;
		}
			
		
		
		cameraBtn.setOnClickListener(this);
		fileBtn.setOnClickListener(this);
		dataBtn.setOnClickListener(this);
		collectBtn.setOnClickListener(this);
		saveInfo.setOnClickListener(this);
		exitBtn.setOnClickListener(this);
		infoBtn.setOnClickListener(this);
		uploadBtn.setOnClickListener(this);
	}
	
	
	private void showinfo() {
		collect.showinfo(getActivity());
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
			if(thread_flag) {
				thread_flag = false;
			}
			break;
			
		case R.id.fileTestBtn:
			processFileBtnClick();
			break;
			
		case R.id.saveInfo:
			Log.i("TestActivity", "this.collect.save()");
			this.collect.save();
			break;
			
		case R.id.infoBtn:
			Log.i("TestActivity", "infoBtn");
			showinfo();
			break;
		case R.id.uploadBtn:
			Log.i("TestActivity", "this.collect.upload()");
			this.collect.upload();
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
