package com.bupt.bnrc.thesenser;

import java.io.FileInputStream;

import com.bupt.bnrc.thesenser.model.FileModel;
import com.bupt.bnrc.thesenser.utils.Logger;

import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class PMToolsLocalFragment extends Fragment {
	private ImageView backPicView = null;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_pmtools_local, container, false);
		InitData();
		InitView(view);
		refreshState();
		return view;
	}

	
	private void InitData() {
		// TODO Auto-generated method stub
		
	}

	private void InitView(View view) {
		// TODO Auto-generated method stub
		backPicView = (ImageView)view.findViewById(R.id.pmtools_id_pic);
	}
	
	private void refreshState() {
		refreshPic();
	}

	private void refreshPic() {
		// TODO Auto-generated method stub
		FileModel nowFile = FileModel.findLastFile(getActivity());
		if(nowFile != null) {
			FileInputStream fileStream = null;
			try {
				fileStream = new FileInputStream(nowFile.getFileName());
			} catch (Exception e) {
				Logger.i(e.getMessage());
			}
			
			BitmapDrawable bitmapDrawable = (BitmapDrawable)backPicView.getDrawable();
			// 如果图片还没有收回，先收回
			if(bitmapDrawable != null && !bitmapDrawable.getBitmap().isRecycled()) {
				bitmapDrawable.getBitmap().recycle();
			}
			// 改变显示的图片
			backPicView.setImageBitmap(BitmapFactory.decodeStream(fileStream));
		}
		
	}


	public void updateOnTakePhoto() {
		// TODO Auto-generated method stub
		refreshState();
	}
	
	
}
