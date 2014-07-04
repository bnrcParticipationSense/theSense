package com.bupt.bnrc.thesenser;

import java.util.List;

import org.opencv.core.Core.MinMaxLocResult;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bupt.bnrc.thesenser.model.FileModel;
import com.bupt.bnrc.thesenser.model.PMModelModel;
import com.bupt.bnrc.thesenser.utils.CommonDefinition;
import com.bupt.bnrc.thesenser.utils.ImageLoader;
import com.bupt.bnrc.thesenser.utils.PMPredictTools;

public class PMToolsModelPickActivity extends Activity {
	private List<PMModelModel> mModelTagList;
	private PMToolsModelPickAdapter mAdapter;
	private PMPredictTools mPredictTools = PMPredictTools.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pmtools_models_pick);

		mModelTagList = PMModelModel.findAllModelTags(getApplicationContext());
		ListView listView = (ListView) findViewById(R.id.pm_model_sel_listView);
		mAdapter = new PMToolsModelPickAdapter(this);
		listView.setAdapter(mAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO pick model
				if (position == mModelTagList.size()) {
					// TODO 增加新的model
					addNewModel();
				} else {
					Integer modelTag = mModelTagList.get(position).getTag();
					// TODO 已经有的model
					predictModel(modelTag);
				}
			}
		});

		listView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

			@Override
			public void onCreateContextMenu(ContextMenu menu, View view,
					ContextMenuInfo menuInfo) {
				// TODO Auto-generated method stub
				AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
				int position = info.position;
				if (position < mModelTagList.size()) {
					menu.setHeaderTitle(mModelTagList.get(position).getDesc());
					menu.add(0, 0, 0, "查看详情");
				}
			}
		});

		/*
		 * listView.setOnItemLongClickListener(new OnItemLongClickListener() {
		 * 
		 * @Override public boolean onItemLongClick(AdapterView<?> parent, View
		 * view, int position, long id) { // TODO Auto-generated method stub
		 * return false; } });
		 */
	}

	protected void predictModel(Integer modelTag) {
		if(mPredictTools.isModeling(modelTag)) {
			Toast.makeText(getApplicationContext(), "该系列正在总结以前照片（大约1-2分钟），请稍后再试", Toast.LENGTH_SHORT).show();
		} else {
			Intent intent = new Intent(this, CameraActivity.class);
			// intent.putExtra(CommonDefinition.KEY_CAMERA_MODEL_TYPE, CommonDefinition.VALUE_CAMERA_MODEL_TYPE_SET);
			intent.putExtra(CommonDefinition.KEY_CAMERA_MODEL_TYPE, CommonDefinition.VALUE_CAMERA_MODEL_TYPE_PREDICT);
			intent.putExtra(CommonDefinition.KEY_CAMERA_MODEL_TAG, modelTag);
			startActivityForResult(intent, CommonDefinition.REQUESTCODE_CAMERA);
		}
	}

	private void addNewModel() {
		Intent intent = new Intent(this, CameraActivity.class);
		intent.putExtra(CommonDefinition.KEY_CAMERA_MODEL_TYPE,
				CommonDefinition.VALUE_CAMERA_MODEL_TYPE_NEW);
		// startActivity(intent);
		startActivityForResult(intent, CommonDefinition.REQUESTCODE_CAMERA);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item
				.getMenuInfo();
		int position = menuInfo.position;
		Integer modelTag = mModelTagList.get(position).getTag();
		String desc = mModelTagList.get(position).getDesc();
		getModelDetails(modelTag, desc);
		return super.onContextItemSelected(item);
	}

	private void getModelDetails(Integer tag, String desc) {
		Intent intent = new Intent(this, PMToolsModelDetailActivity.class);
		intent.putExtra(CommonDefinition.KEY_MODEL_DETAIL_TAG, tag);
		startActivity(intent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode == CommonDefinition.REQUESTCODE_CAMERA && resultCode == CommonDefinition.RESULTCODE_CAMERA_OK) {
			int type = data.getIntExtra(CommonDefinition.KEY_CAMERA_MODEL_TYPE, CommonDefinition.VALUE_CAMERA_MODEL_TYPE_NONE);
			int tag = data.getIntExtra(CommonDefinition.KEY_CAMERA_MODEL_TAG, CommonDefinition.VALUE_CAMERA_MODEL_TAG_DEFAULT);
			long id = data.getLongExtra(CommonDefinition.KEY_CAMERA_MODEL_ID, CommonDefinition.VALUE_CAMERA_MODEL_ID_DEFAULT);
			if(type == CommonDefinition.VALUE_CAMERA_MODEL_TYPE_PREDICT) {
				startPredict(id);
			} else if (type == CommonDefinition.VALUE_CAMERA_MODEL_TYPE_NEW) {
				startModeling(tag);
			}
		}

		refreshList();
		
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void startPredict(long id) {
		// TODO Auto-generated method stub
		FileModel file = FileModel.findFileById(id, getApplicationContext());
		mPredictTools.startPredict(file);
	}

	private void startModeling(int tag) {
		// TODO Auto-generated method stub
		mPredictTools.startModeling(tag);
	}

	private void refreshList() {
		mModelTagList = PMModelModel.findAllModelTags(getApplicationContext());
		mAdapter.notifyDataSetChanged();
	}

	/* adapter 用来搞定gridview的 */
	public class PMToolsModelPickAdapter extends BaseAdapter {
		private Context mContext;

		public PMToolsModelPickAdapter(Context c) {
			mContext = c;
		}

		@Override
		public int getCount() {
			return mModelTagList.size() + 1;
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

			if (position < mModelTagList.size()) {
				Integer modelTag = mModelTagList.get(position).getTag();
				String modelDesc = mModelTagList.get(position).getDesc();
				imageView.setTag(modelTag);
				FileModel file = FileModel.findOneFileFromTag(modelTag,
						mContext);
				// Bitmap bitmap = BitmapFactory.decodeFile(file.getFileName());
				Bitmap bitmap = ImageLoader.decodeSampledBitmapFromResource(file.getFileName(), 90);
				imageView.setImageBitmap(bitmap);
				textView.setText(modelDesc);
			} else {
				imageView.setImageResource(R.drawable.add_model);
				textView.setText(R.string.pm_model_add_model);
			}
			return view;
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

	}
}
