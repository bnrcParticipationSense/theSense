package com.bupt.bnrc.thesenser;

import com.astuetz.PagerSlidingTabStrip;
import com.bupt.bnrc.thesenser.utils.CommonDefinition;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PMToolsParentFragment extends Fragment {
	private ViewPager mPager = null;
	private PMToolsFragmentPagerAdapter mAdapter = null;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_pmtools_parent, container, false);
		int i = getArguments().getInt(FragmentFactory.ARG_MAIN_INDEX);
		String title = getResources().getStringArray(R.array.main_list_array)[i];
		getActivity().setTitle(title);
	
		return view;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		PagerSlidingTabStrip tabs = (PagerSlidingTabStrip)view.findViewById(R.id.pmtools_tabs);
		mPager = (ViewPager)view.findViewById(R.id.pmtools_pager);
		mAdapter = new PMToolsFragmentPagerAdapter(getChildFragmentManager());
		mPager.setAdapter(mAdapter);
		tabs.setViewPager(mPager);
	}
	
	public void updateOnTakePhoto() {
		PMToolsLocalFragment localFragment = (PMToolsLocalFragment)mAdapter.instantiateItem(mPager, 0);
		localFragment.updateOnTakePhoto();
	}

	public void takePhoto() {
		// get to know which pager is sellected
		int nowPosition = mPager.getCurrentItem();
		switch (nowPosition) {
		case 0: // local pm2.5
	    	Intent intent = new Intent(getActivity(), CameraActivity.class);
	    	startActivityForResult(intent, CommonDefinition.REQUESTCODE_CAMERA);
			break;
		case 1: // network photos
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case CommonDefinition.REQUESTCODE_CAMERA:
	    	updateOnTakePhoto();
			break;

		default:
			super.onActivityResult(requestCode, resultCode, data);
			break;
		}
	}
}
