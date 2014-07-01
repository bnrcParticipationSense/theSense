package com.bupt.bnrc.thesenser.collection;

import com.bupt.bnrc.thesenser.Collection;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class forCollection extends Service {

	static Activity app = null;
	static Collection col = null;
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void onCreate() {
		super.onCreate();
		Log.i("Collection Service", "onCreate()");
		if(col == null && app == null){
			col = Collection.getCollection();
			app = col.getActivity();
		}
		else if(col == null && app != null){
			col = Collection.getCollection(app);
		}
		//Collection col = Collection.getCollection();
		//Collection c = Collection.getCollection();
		col.collect_new();
	}
	
	public int onStartCommand(Intent intent, int flag, int startId) {
		Log.i("Collection Service", "onStartCommand()");
		return START_STICKY;
	}
	
	public void onDestroy() {
		super.onDestroy();
		Log.i("Collection Service", "onDestroy()");
	}

}
