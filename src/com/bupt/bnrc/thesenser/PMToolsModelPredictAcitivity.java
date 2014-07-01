package com.bupt.bnrc.thesenser;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bupt.bnrc.thesenser.model.FileModel;
import com.bupt.bnrc.thesenser.model.PMModelModel;
import com.bupt.bnrc.thesenser.utils.CommonDefinition;
import com.bupt.bnrc.thesenser.utils.PMPredictTools;

public class PMToolsModelPredictAcitivity extends Activity {
	private List<PMModelModel> mModelList;
	private List<PMModelModel> mCanPredictModelList = new ArrayList<PMModelModel>();
	private PMToolsModelPredictAdapter mAdapter;

	private ListView mListView = null;
	private RelativeLayout mRelativeLayout = null;
	private Button mButton = null;
	
	private PMPredictTools mPredictTools = PMPredictTools.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.actitvity_pmtools_model_predict);

		mListView = (ListView) findViewById(R.id.pmtools_model_predict_listView);
		mRelativeLayout = (RelativeLayout) findViewById(R.id.pmtools_model_predict_none_layout);
		mButton = (Button) findViewById(R.id.pmtools_model_predict_none_button);
		mButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				goToModelPick();
			}
		});

		mModelList = PMModelModel.findAllModelTags(getApplicationContext());
		mCanPredictModelList.clear();

		for (int i = 0; i < mModelList.size(); i++) {
			Integer modelTag = mModelList.get(i).getTag();
			List<FileModel> tempList = FileModel.findFilesByTag(modelTag,
					getApplicationContext());
			if (tempList.size() >= 10) {
				mCanPredictModelList.add(mModelList.get(i));
			}
		}

		if (mCanPredictModelList == null || mCanPredictModelList.size() == 0) {
			mListView.setVisibility(View.GONE);
			mRelativeLayout.setVisibility(View.VISIBLE);
		} else {
			mListView.setVisibility(View.VISIBLE);
			mRelativeLayout.setVisibility(View.GONE);
		}

		mAdapter = new PMToolsModelPredictAdapter(this);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Integer modelTag = mCanPredictModelList.get(position).getTag();
				takePhotoForPredictPM(modelTag);
			}
		});

	}

	protected void takePhotoForPredictPM(Integer modelTag) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, CameraActivity.class);
		intent.putExtra(CommonDefinition.KEY_CAMERA_MODEL_TYPE,
				CommonDefinition.VALUE_CAMERA_MODEL_TYPE_PREDICT);
		startActivityForResult(intent, CommonDefinition.REQUESTCODE_CAMERA);
	}

	protected void goToModelPick() {
		Intent intent = new Intent(this, PMToolsModelPickActivity.class);
		startActivity(intent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == CommonDefinition.REQUESTCODE_CAMERA) {
			switch (resultCode) {
			case CommonDefinition.RESULTCODE_CAMERA_OK:
				modelPredict();
				break;
			case CommonDefinition.RESULTCODE_CAMERA_CANCEL:
				System.out.println("返回cancel");
				break;
			default:
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void modelPredict() {
		// TODO Auto-generated method stub
		finish();
	}

	public class PMToolsModelPredictAdapter extends BaseAdapter {
		private Context mContext;

		public PMToolsModelPredictAdapter(Context c) {
			mContext = c;
		}

		@Override
		public int getCount() {
			return mCanPredictModelList.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			if (convertView == null) {
				view = LayoutInflater.from(mContext).inflate(
						R.layout.item_pmtools_models_pick, null);
			} else {
				view = convertView;
			}

			ImageView imageView = (ImageView) view
					.findViewById(R.id.model_image);
			TextView textView = (TextView) view.findViewById(R.id.model_text);
			Integer modelTag = mCanPredictModelList.get(position).getTag();
			String modelDesc = mCanPredictModelList.get(position).getDesc();
			imageView.setTag(modelTag);
			FileModel file = FileModel.findOneFileFromTag(modelTag, mContext);
			Bitmap bitmap = BitmapFactory.decodeFile(file.getFileName());
			imageView.setImageBitmap(bitmap);
			textView.setText(modelDesc);

			return view;
		}

		@Override
		public Object getItem(int posiiton) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}
	}
}
