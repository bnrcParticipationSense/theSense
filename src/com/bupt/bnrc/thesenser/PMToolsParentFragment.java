package com.bupt.bnrc.thesenser;

import com.astuetz.PagerSlidingTabStrip;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PMToolsParentFragment extends Fragment {
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
		ViewPager pager = (ViewPager)view.findViewById(R.id.pmtools_pager);
		PMToolsFragmentPagerAdapter adapter = new PMToolsFragmentPagerAdapter(getChildFragmentManager());
		pager.setAdapter(adapter);
		tabs.setViewPager(pager);
	}
}
