package com.bupt.bnrc.thesenser.utils;

import android.app.Application;

public class CrashApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());
	}
}
