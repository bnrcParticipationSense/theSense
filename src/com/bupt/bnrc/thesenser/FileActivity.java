package com.bupt.bnrc.thesenser;

import com.bupt.bnrc.thesenser.utils.Logger;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class FileActivity extends BaseActivity implements OnClickListener {
	private View listView = null;
	private Dialog listDialog = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Logger.i("打开文件测试页");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_file);
		
		initViews();
	}

	private void initViews() {
		View lookListBtn = findViewById(R.id.file_lookListBtn);
		
		lookListBtn.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.file_lookListBtn:
			OnClickLookListBtn();
			break;
		default:
			break;
		}
	}

	private void OnClickLookListBtn() {
		if (listDialog == null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("要查找的前n个数据");
			builder.setView(getListView());
			builder.setCancelable(true);
	        builder.setPositiveButton("确定", lookListFile());
	        builder.setNegativeButton("取消", cancelListener());
	        listDialog = builder.create();
		}
		listDialog.show();
		
		
	}

	private DialogInterface.OnClickListener lookListFile() {
		return new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                EditText collectedTextView = (EditText) getListView().findViewById(R.id.get_list_file);
                String dataId = collectedTextView.getText().toString();
                Integer inputNum = Integer.parseInt(dataId);
                Bundle bundle = new Bundle();
                bundle.putInt("num", inputNum);
                Intent intent = new Intent(FileActivity.this, ListFileActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        };
	}

	
	
	protected DialogInterface.OnClickListener cancelListener() {
        return new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        };
    }
	
	synchronized protected View getListView() {
        if (listView == null) {
            LayoutInflater factory = LayoutInflater.from(this);
            listView = factory.inflate(R.layout.get_file_list, null);
        }
        return listView;
    }
}
