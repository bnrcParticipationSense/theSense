package com.bupt.bnrc.thesenser;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import com.bupt.bnrc.thesenser.model.FileModel;
import com.bupt.bnrc.thesenser.utils.Logger;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class ListFileActivity extends Activity implements OnClickListener {
	
	private Integer num = null;
	private List<FileModel> files = null;
	private Integer index = null;
	
	private TextView titleView = null;
	private TextView detailsView = null;
	private ImageView picView = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_file);
		
		initDatas();
		initViews();
		refreshState();
	}

	private void refreshState() {
		String titleStr = "娌℃湁鎵惧埌鏂囦欢";
		String detailsStr = "鏃犳枃浠�";
		
		View preBtn = findViewById(R.id.listFile_pre_btn);
		View nextBtn = findViewById(R.id.listFile_next_btn);
		
		if(index != null) {
			titleStr = String.format("瑕佹煡鎵�%d涓枃浠讹紝鍏辨壘鍒� %d 涓枃浠�, 褰撳墠涓虹 %d 涓�", num, files.size(), index+1);
			if(files.get(index).getTag() == 0) {
				picView.setVisibility(ImageView.VISIBLE);
				FileInputStream fileStream = null;
				try {
					fileStream = new FileInputStream(files.get(index).getFileName());
				} catch (Exception e) {
					Logger.i(e.getMessage());
				}
				
				BitmapDrawable bitmapDrawable = (BitmapDrawable)picView.getDrawable();
				// 濡傛灉鍥剧墖杩樻病鏈夋敹鍥烇紝鍏堟敹鍥�
				if(bitmapDrawable != null && !bitmapDrawable.getBitmap().isRecycled()) {
					bitmapDrawable.getBitmap().recycle();
				}
				// 鏀瑰彉鏄剧ず鐨勫浘鐗�
				picView.setImageBitmap(BitmapFactory.decodeStream(fileStream));
				
			} else {
				picView.setVisibility(ImageView.INVISIBLE);
			}
			detailsStr = transFileToMsg(files.get(index));
			preBtn.setEnabled(index > 0);
			nextBtn.setEnabled(index < files.size() - 1);
		} else {
			preBtn.setEnabled(false);
			nextBtn.setEnabled(false);
			picView.setVisibility(ImageView.GONE);
		}
		
		titleView.setText(titleStr);
		detailsView.setText(detailsStr);
	}

	private String transFileToMsg(FileModel file) {
		String message = null;
		if(file != null) {
			message = "鍙栧埌鐨勬暟鎹槸:\n";
			message += "ID:" + file.getId().toString() + "\n";
			message += "鍒涘缓鏃堕棿 :" + file.getCreateTimeString() + "\n";
			message += "鏂囦欢璺緞锛�" + file.getFileName() + "\n";
			if(file.getPhotoStats() != null) {
				message += "x鏂瑰悜:" + file.getPhotoStats().getXDirect().toString() + "\n";
				message += "y鏂瑰悜:" + file.getPhotoStats().getYDirect().toString() + "\n";
				message += "z鏂瑰悜:" + file.getPhotoStats().getZDirect().toString() + "\n";
				message += "缁忓害:" + file.getPhotoStats().getLongitude().toString() + "\n";
				message += "绾害:" + file.getPhotoStats().getLatitude().toString() + "\n";
				message += "鏇濆厜搴�:" + file.getPhotoStats().getExposureValue().toString() + "\n";
				message += "鐒﹁窛:" + file.getPhotoStats().getFocalDistance().toString() + "\n";
				message += "鍏夊湀:" + file.getPhotoStats().getAperture().toString() + "\n";
			}
		}
		return message;
	}

	private void initViews() {
		// TODO Auto-generated method stub
		titleView = (TextView)findViewById(R.id.listFile_title);
		detailsView = (TextView)findViewById(R.id.listFile_details);
		picView = (ImageView)findViewById(R.id.listFile_pic);
		
		View preBtn = findViewById(R.id.listFile_pre_btn);
		View nextBtn = findViewById(R.id.listFile_next_btn);
		
		preBtn.setOnClickListener(this);
		nextBtn.setOnClickListener(this);
	}

	private void initDatas() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		num = intent.getIntExtra("num", 0);
		files = FileModel.findNotUploadFiles(num, this);
		
		int datasSize = files.size();
		if(datasSize > 0) {
			index = 0;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.listFile_pre_btn:
			OnClickPreBtn();
			break;
		case R.id.listFile_next_btn:
			OnClickNextBtn();
			break;
		default:
			break;
		}
	}
	
	private void OnClickNextBtn() {
		// TODO Auto-generated method stub
		index++;
		refreshState();
	}

	private void OnClickPreBtn() {
		// TODO Auto-generated method stub
		index--;
		refreshState();
	}
}
