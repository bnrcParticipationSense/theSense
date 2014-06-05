package com.bupt.bnrc.thesenser;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CollectSceneFragment extends Fragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO If there is some argv needed, read them here
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_collect_scene, container, false);
	}
}
