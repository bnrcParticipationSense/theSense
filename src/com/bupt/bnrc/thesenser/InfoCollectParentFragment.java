package com.bupt.bnrc.thesenser;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;

public class InfoCollectParentFragment extends Fragment {
	private ViewPager mPager = null;
	private InfoCollectFragmentPagerAdapter mAdapter = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_info_collect_parent,
				container, false);
		int i = getArguments().getInt(FragmentFactory.ARG_MAIN_INDEX);
		String title = getResources().getStringArray(R.array.main_list_array)[i];
		getActivity().setTitle(title);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) view
				.findViewById(R.id.info_collect_tabs);
		mPager = (ViewPager) view.findViewById(R.id.info_collect_pager);
		mAdapter = new InfoCollectFragmentPagerAdapter(
				getChildFragmentManager());
		mPager.setAdapter(mAdapter);
		tabs.setViewPager(mPager);
	}

	/*
	 * private String transDataToMsg(DataModel data) { String message = null;
	 * if(data != null) { message = "��ǰ�ܱ���ϢΪ:\n\n"; message += "����ǿ��:" +
	 * data.getLightIntensity().toString() + "\n"; message += "����ǿ��:" +
	 * data.getSoundIntensity().toString() + "\n"; message += "���״̬:" +
	 * data.getBatteryState().toString() + "\n"; message += "���״̬:" +
	 * data.getChargeState().toString() + "\n"; message += "����״̬:" +
	 * data.getNetState().toString() + "\n"; message += "����: " +
	 * data.getLongitude().toString() + "\n"; message += "γ�ȣ�" +
	 * data.getLatitude().toString() + "\n"; } return message; }
	 */

	public void refreshDataView() {
		// TODO Auto-generated method stub
		InfoCollectLightFragment lightFragment = (InfoCollectLightFragment) mAdapter
				.instantiateItem(mPager, 0);
		lightFragment.refreshDataView();
	}

}
