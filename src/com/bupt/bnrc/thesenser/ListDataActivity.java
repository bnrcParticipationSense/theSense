package com.bupt.bnrc.thesenser;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.bupt.bnrc.thesenser.model.DataModel;
import com.bupt.bnrc.thesenser.utils.Logger;

public class ListDataActivity extends Activity implements OnClickListener {
	private Integer num = null;
	private List<DataModel> datas = null;
	private Integer index = null;

	private TextView titleView = null;
	private TextView detailsView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Logger.i("打开数据列表页");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_data);

		initDatas();
		initViews();
		refreshState();
	}

	private void refreshState() {
		// TODO Auto-generated method stub
		String titleStr = "没有找到数据";
		String detailsStr = "无数据";

		View preBtn = findViewById(R.id.listData_pre_btn);
		View nextBtn = findViewById(R.id.listData_next_btn);

		if (index != null) {
			titleStr = String.format("要查找%d个数据，共找到 %d 个数据, 当前为第 %d 个", num,
					datas.size(), index + 1);
			detailsStr = transDataToMsg(datas.get(index));
			preBtn.setEnabled(index > 0);
			nextBtn.setEnabled(index < datas.size() - 1);
		} else {
			preBtn.setEnabled(false);
			nextBtn.setEnabled(false);
		}

		titleView.setText(titleStr);
		detailsView.setText(detailsStr);
	}

	private void initViews() {
		// TODO Auto-generated method stub
		titleView = (TextView) findViewById(R.id.listData_title);

		detailsView = (TextView) findViewById(R.id.listData_details);

		View preBtn = findViewById(R.id.listData_pre_btn);
		View nextBtn = findViewById(R.id.listData_next_btn);

		preBtn.setOnClickListener(this);
		nextBtn.setOnClickListener(this);

	}

	private void initDatas() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		num = intent.getIntExtra("num", 0);
		datas = DataModel.findNotUploadDatas(num, this);

		int datasSize = datas.size();
		if (datasSize > 0) {
			index = 0;
		}
	}

	private String transDataToMsg(DataModel data) {
		String message = null;
		if (data != null) {
			message = "取到的数据是:\n";
			message += "ID:" + data.getId().toString() + "\n";
			message += "CreateTime:" + data.getCreateTimeString() + "\n";
			message += "光照强度:" + data.getLightIntensity().toString() + "\n";
			message += "噪声强度:" + data.getSoundIntensity().toString() + "\n";
			message += "电池状态:" + data.getBatteryState().toString() + "\n";
			message += "充电状态:" + data.getChargeState().toString() + "\n";
			message += "网络状态:" + data.getNetState().toString() + "\n";
			message += "经度: " + data.getLongitude().toString() + "\n";
			message += "纬度：" + data.getLatitude().toString() + "\n";
		}
		return message;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.listData_pre_btn:
			OnClickPreBtn();
			break;
		case R.id.listData_next_btn:
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
