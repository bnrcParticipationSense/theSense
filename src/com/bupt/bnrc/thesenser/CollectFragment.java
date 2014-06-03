package com.bupt.bnrc.thesenser;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CollectFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_collect, container, false);
		int i = getArguments().getInt(FragmentFactory.ARG_MAIN_INDEX);
		String title = getResources().getStringArray(R.array.main_list_array)[i];
		initViews(view);
		getActivity().setTitle(title);
		return view;
	}

	private void initViews(View parentView) {
		// TODO Auto-generated method stub
		
	}
}
