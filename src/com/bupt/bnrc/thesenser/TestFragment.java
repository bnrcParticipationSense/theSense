package com.bupt.bnrc.thesenser;

import com.bupt.bnrc.thesenser.utils.Logger;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

public class TestFragment extends Fragment implements OnClickListener {

	Collection collect = null;
	
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
		
		
		cameraBtn.setOnClickListener(this);
		fileBtn.setOnClickListener(this);
		dataBtn.setOnClickListener(this);
		collectBtn.setOnClickListener(this);
		saveInfo.setOnClickListener(this);
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
