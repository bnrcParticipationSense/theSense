package com.bupt.bnrc.thesenser;

import java.io.FileInputStream;

import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bupt.bnrc.thesenser.interfaces.PMPredictObserver;
import com.bupt.bnrc.thesenser.model.FileModel;
import com.bupt.bnrc.thesenser.utils.CommonDefinition;
import com.bupt.bnrc.thesenser.utils.Logger;
import com.bupt.bnrc.thesenser.utils.PMPredictTools;

public class PMToolsLocalFragment extends Fragment implements PMPredictObserver {
	private TextView mTextView = null;
	private ImageView backPicView = null;
	private PMPredictTools mPredictTools = PMPredictTools.getInstance();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_pmtools_local,
				container, false);
		InitData();
		InitView(view);
		refreshState();
		mPredictTools.registerObserver(this);
		return view;
	}

	private void InitData() {
		// TODO Auto-generated method stub

	}

	private void InitView(View view) {
		// TODO Auto-generated method stub
		backPicView = (ImageView) view.findViewById(R.id.pmtools_id_pic);
		mTextView = (TextView) view.findViewById(R.id.pmtools_id_pm_text);
	}

	private void refreshState() {
		refreshPic();
	}

	private void refreshPic() {
		// TODO Auto-generated method stub
		FileModel nowFile = FileModel.findLastFile(getActivity());
		if (nowFile != null) {
			FileInputStream fileStream = null;
			try {
				fileStream = new FileInputStream(nowFile.getFileName());
			} catch (Exception e) {
				Logger.i(e.getMessage());
			}

			BitmapDrawable bitmapDrawable = (BitmapDrawable) backPicView
					.getDrawable();
			// ���ͼƬ��û���ջأ����ջ�
			if (bitmapDrawable != null
					&& !bitmapDrawable.getBitmap().isRecycled()) {
				bitmapDrawable.getBitmap().recycle();
			}
			// �ı���ʾ��ͼƬ
			backPicView.setImageBitmap(BitmapFactory.decodeStream(fileStream));
		}

	}

	public void updateOnTakePhoto() {
		// TODO Auto-generated method stub
		refreshState();
	}

	@Override
	public void updatePredictState() {
		// TODO Auto-generated method stub
		switch (mPredictTools.getPredictState()) {
		case CommonDefinition.PMPREDICT_STATE_START:
			mTextView.setText("PM2.5：\n正在预测..");
			break;
		case CommonDefinition.PMPREDICT_STATE_FAIL:
			mTextView.setText("PM2.5：\n预测失败");
			break;
		case CommonDefinition.PMPREDICT_STATE_SUCCESS:
			String text = "PM2.5:\n" + mPredictTools.getPMNum();
			mTextView.setText(text);
		default:
			break;
		}
	}

}
