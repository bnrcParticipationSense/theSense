package com.bupt.bnrc.thesenser;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bupt.bnrc.thesenser.model.FileModel;
import com.bupt.bnrc.thesenser.utils.CommonDefinition;
import com.bupt.bnrc.thesenser.utils.ImageLoader;

public class PMToolsModelDetailActivity extends Activity {
	private List<FileModel> mFiles;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pmtools_models_detail);

		Intent intent = getIntent();
		Integer modelTag = intent.getIntExtra(
				CommonDefinition.KEY_MODEL_DETAIL_TAG,
				CommonDefinition.VALUE_MODEL_DETAIL_TAG_DEFAULT);
		mFiles = FileModel.findFilesByTag(modelTag, getApplicationContext());

		GridView gridView = (GridView) findViewById(R.id.pmtools_model_detail_gridview);
		gridView.setAdapter(new PMToolsModelDetailAdapter(this));
	}

	public class PMToolsModelDetailAdapter extends BaseAdapter {
		private Context mContext;

		public PMToolsModelDetailAdapter(Context c) {
			mContext = c;
		}

		@Override
		public int getCount() {
			return mFiles.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			if (convertView == null) {
				view = LayoutInflater.from(mContext).inflate(
						R.layout.item_pmtools_model_detail, null);
			} else {
				view = convertView;
			}
			ImageView imageView = (ImageView) view
					.findViewById(R.id.pmtools_model_detail_image_view);
			// Bitmap bitmap = BitmapFactory.decodeFile(mFiles.get(position).getFileName());
			Bitmap bitmap = ImageLoader.decodeSampledBitmapFromResource(mFiles.get(position).getFileName(), 90);
			imageView.setImageBitmap(bitmap);
			return view;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}
	}
}
