package com.bupt.bnrc.thesenser;

import java.io.FileInputStream;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.bupt.bnrc.thesenser.model.FileModel;
import com.bupt.bnrc.thesenser.utils.Logger;

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
		String titleStr = "没有找到文件";
		String detailsStr = "无文件";

		View preBtn = findViewById(R.id.listFile_pre_btn);
		View nextBtn = findViewById(R.id.listFile_next_btn);

		if (index != null) {
			titleStr = String.format("要查找%d个文件，共找到 %d 个文件, 当前为第 %d 个", num,
					files.size(), index + 1);
			if (files.get(index).getTag() == 0) {
				picView.setVisibility(ImageView.VISIBLE);
				FileInputStream fileStream = null;
				try {
					fileStream = new FileInputStream(files.get(index)
							.getFileName());
				} catch (Exception e) {
					Logger.i(e.getMessage());
				}

				BitmapDrawable bitmapDrawable = (BitmapDrawable) picView
						.getDrawable();
				// 如果图片还没有收回，先收回
				if (bitmapDrawable != null
						&& !bitmapDrawable.getBitmap().isRecycled()) {
					bitmapDrawable.getBitmap().recycle();
				}
				// 改变显示的图片
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
		if (file != null) {
			message = "取到的数据是:\n";
			message += "ID:" + file.getId().toString() + "\n";
			message += "创建时间 :" + file.getCreateTimeString() + "\n";
			message += "文件路径：" + file.getFileName() + "\n";
			if (file.getPhotoStats() != null) {
				message += "x方向:"
						+ file.getPhotoStats().getXDirect().toString() + "\n";
				message += "y方向:"
						+ file.getPhotoStats().getYDirect().toString() + "\n";
				message += "z方向:"
						+ file.getPhotoStats().getZDirect().toString() + "\n";
				message += "经度:"
						+ file.getPhotoStats().getLongitude().toString() + "\n";
				message += "纬度:"
						+ file.getPhotoStats().getLatitude().toString() + "\n";
				message += "曝光度:"
						+ file.getPhotoStats().getExposureValue().toString()
						+ "\n";
				message += "焦距:"
						+ file.getPhotoStats().getFocalDistance().toString()
						+ "\n";
				message += "光圈:"
						+ file.getPhotoStats().getAperture().toString() + "\n";
			}
		}
		return message;
	}

	private void initViews() {
		// TODO Auto-generated method stub
		titleView = (TextView) findViewById(R.id.listFile_title);
		detailsView = (TextView) findViewById(R.id.listFile_details);
		picView = (ImageView) findViewById(R.id.listFile_pic);

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
		if (datasSize > 0) {
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
