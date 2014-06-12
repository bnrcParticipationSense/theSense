package com.bupt.bnrc.thesenser;

import com.bupt.bnrc.thesenser.view.ZoomImageView;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Window;

public class ImageDetailsActivity extends Activity {
	private ZoomImageView zoomImageView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_image_details);
		zoomImageView = (ZoomImageView)findViewById(R.id.zoom_image_view);
		String imagePath = getIntent().getStringExtra("image_path");  
		Bitmap bitmap = BitmapFactory.decodeFile(imagePath);  
		zoomImageView.setImageBitmap(bitmap);  
	}
}
