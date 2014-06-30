package com.bupt.bnrc.thesenser;

import java.util.ArrayList;
import java.util.List;

import com.bupt.bnrc.thesenser.model.FileModel;
import com.bupt.bnrc.thesenser.utils.CommonDefinition;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

public class PMToolsModelPickActivity extends Activity {
	private List<Integer> mModelTagList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pmtools_models_pick);
		
		mModelTagList = new ArrayList<Integer>();
		ListView listView = (ListView)findViewById(R.id.pm_model_sel_listView);
		listView.setAdapter(new PMToolsModelPickAdapter(this));
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO pick model
				if(position == mModelTagList.size()) {
					// TODO 增加新的model
					addNewModel();
				} else {
					// TODO 已经有的model
				}
			}
		});
	}
	
	private void addNewModel() {
		Intent intent = new Intent(this, CameraActivity.class);
		intent.putExtra(CommonDefinition.KEY_CAMERA_NEW_MODEL, CommonDefinition.VALUE_CAMERA_NEW_MODEL);
		startActivity(intent);
	}
	
	/*adapter 用来搞定gridview的*/
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
				view = LayoutInflater.from(mContext).inflate(R.layout.item_pmtools_models_pick, null);
			} else {
				view = convertView;
			}
			
			ImageView imageView = (ImageView)view.findViewById(R.id.model_image);
			
			if(position < mModelTagList.size()) {
				Integer modelTag = mModelTagList.get(position);
				imageView.setTag(modelTag);
				FileModel file = FileModel.findOneFileFromTag(modelTag, mContext);
				Bitmap bitmap = BitmapFactory.decodeFile(file.getFileName());
				imageView.setImageBitmap(bitmap);
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
