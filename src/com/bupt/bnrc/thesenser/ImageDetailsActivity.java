package com.bupt.bnrc.thesenser;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.Window;

import com.bupt.bnrc.thesenser.model.WebPhotoModel;
import com.bupt.bnrc.thesenser.utils.DataCache;
import com.bupt.bnrc.thesenser.utils.ImageLoader;
import com.bupt.bnrc.thesenser.view.ZoomImageView;

public class ImageDetailsActivity extends Activity {
	private ZoomImageView mZoomImageView;
	private WebPhotoModel mPhoto;
	private ImageLoader imageLoader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_image_details);
		mZoomImageView = (ZoomImageView) findViewById(R.id.zoom_image_view);
		Integer imageIndex = getIntent().getIntExtra("image_index", 0);
		mPhoto = DataCache.getInstance().getPhoto(imageIndex);
		imageLoader = ImageLoader.getInstance();
		// TODO
		Bitmap bitmap = BitmapFactory.decodeFile(getImagePath(mPhoto
				.getPackUrl()));
		// Bitmap bitmap =
		// BitmapFactory.decodeFile(getImagePath(mPhoto.getSrcUrl()));
		mZoomImageView.setImageBitmap(bitmap);

		LoadSrcImageTask task = new LoadSrcImageTask();
		task.execute(mPhoto.getSrcUrl());
	}

	private String getImagePath(String imageUrl) {
		int lastSlashIndex = imageUrl.lastIndexOf("/");
		String imageName = imageUrl.substring(lastSlashIndex + 1);
		String imageDir = Environment.getExternalStorageDirectory().getPath()
				+ "/PhotoWallFalls/";
		File file = new File(imageDir);
		if (!file.exists()) {
			file.mkdirs();
		}
		String imagePath = imageDir + imageName;
		return imagePath;
	}

	class LoadSrcImageTask extends AsyncTask<String, Integer, Bitmap> {
		private String mImageUrl;

		public LoadSrcImageTask() {
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			mImageUrl = params[0];
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
			}
			Bitmap imageBitmap = imageLoader
					.getBitmapFromMemoryCache(mImageUrl);
			if (imageBitmap == null) {
				imageBitmap = loadImage(mImageUrl);
			}
			return imageBitmap;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (bitmap != null) {
				mZoomImageView.setImageBitmap(bitmap);
			}
		}

		private Bitmap loadImage(String imageUrl) {
			File imageFile = new File(getImagePath(imageUrl));
			if (!imageFile.exists()) {
				downloadImage(imageUrl);
			}
			if (imageUrl != null) {
				Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getPath());
				if (bitmap != null) {
					imageLoader.addBitmapToMemoryCache(imageUrl, bitmap);
					return bitmap;
				}
			}
			return null;
		}

		private void downloadImage(String imageUrl) {
			HttpURLConnection con = null;
			FileOutputStream fos = null;
			BufferedOutputStream bos = null;
			BufferedInputStream bis = null;
			File imageFile = null;
			try {
				URL url = new URL(imageUrl);
				con = (HttpURLConnection) url.openConnection();
				con.setConnectTimeout(5 * 1000);
				con.setReadTimeout(15 * 1000);
				con.setDoInput(true);
				con.setDoOutput(true);
				bis = new BufferedInputStream(con.getInputStream());
				imageFile = new File(getImagePath(imageUrl));
				fos = new FileOutputStream(imageFile);
				bos = new BufferedOutputStream(fos);
				byte[] b = new byte[1024];
				int length;
				while ((length = bis.read(b)) != -1) {
					bos.write(b, 0, length);
					bos.flush();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (bis != null) {
						bis.close();
					}
					if (bos != null) {
						bos.close();
					}
					if (con != null) {
						con.disconnect();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (imageFile != null) {
				// Bitmap bitmap = ImageLoader.decodeSampledBitmapFromResource(
				// imageFile.getPath(), columnWidth);
				Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getPath());
				if (bitmap != null) {
					imageLoader.addBitmapToMemoryCache(imageUrl, bitmap);
				}
			}
		}
	}
}
