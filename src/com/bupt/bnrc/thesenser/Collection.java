package com.bupt.bnrc.thesenser;

import java.util.Date;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

//Battery
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
//Connect
import android.net.ConnectivityManager;

public class Collection implements SensorEventListener {
	public float light;
	
	private float [] acceleration = new float[3];
	private float [] magnetic_field = new float[3];
	
	public float [] orientation = new float[3];
	
	private String picName;
	private Date date;
	
	private Activity app;
	private SensorManager sensorManager;
	private Sensor sensor;
	
	private BatteryReceiver receiver = null;
	private ConnectivityManager connectManager;
	
	public Collection(Activity app) {
		this.app = app;
		sensorManager = (SensorManager) this.app.getSystemService(android.content.Context.SENSOR_SERVICE);
		connectManager = (ConnectivityManager) this.app.getSystemService(Context.CONNECTIVITY_SERVICE);
		setValues();
	}
	
	@SuppressLint("InlinedApi")
	public void setValues() {
		setLight();
		setAccelerometer();
		setMagneticField();
		
		calculateOrientation();
		
		//Battery
		receiver = new BatteryReceiver();
		IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		this.app.registerReceiver(receiver, filter);
		
		
		//Connection
		boolean flag = false;
		if (connectManager.getActiveNetworkInfo() != null) {
			flag = connectManager.getActiveNetworkInfo().isAvailable();
		}
		if (!flag) {
			//error
		}
		else {
			int state = isNetworkAvailable();
		}
		
		date = new Date();
		
		Toast toast = Toast.makeText(this.app, ""+date, Toast.LENGTH_LONG);
		toast.show();
	}
	
	private void calculateOrientation() {
		float [] R = new float[9];
		SensorManager.getRotationMatrix(R, null, acceleration, magnetic_field);
		SensorManager.getOrientation(R, orientation);
	}
	private void setLight() {
		this.sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
		sensorManager.registerListener(this, this.sensor, SensorManager.SENSOR_DELAY_NORMAL);
	}
	private void setAccelerometer() {
		this.sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sensorManager.registerListener(this, this.sensor, SensorManager.SENSOR_DELAY_NORMAL);
	}
	private void setMagneticField() {
		this.sensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		sensorManager.registerListener(this, this.sensor, SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	public void setPicName(String str) {
		this.picName = str;
	}
	
	public float getLight() {
		return this.light;
	}
	
	private void returnValues(float [] e ,int Type) {
		Log.i("SensorEvent","sensor.Type = "+Type);
		switch(Type){
		case Sensor.TYPE_LIGHT:
			//sensorManager.unregisterListener(this);
			this.light = e[0];
			Log.i("SensorEvent","Collection->this.light = "+this.light);
			break;
		case Sensor.TYPE_ACCELEROMETER:
			//sensorManager.unregisterListener(this);
			this.acceleration = e;
			Log.i("SensorEvent","Collection->this.acceleration = "+this.acceleration[0]+" , "+this.acceleration[1]+" , "+this.acceleration[2]);
			break;
		case Sensor.TYPE_MAGNETIC_FIELD:
			this.magnetic_field = e;
			Log.i("SensorEvent","Collection->this.magnetic_field = "+this.magnetic_field[0]+" , "+this.magnetic_field[1]+" , "+this.magnetic_field[2]);
			break;
		default:
			//sensorManager.unregisterListener(this);
			break;
		}
	}
	
	@SuppressWarnings("unused")
	private void stopListener() {
		sensorManager.unregisterListener(this);
		this.app.unregisterReceiver(receiver);
	}
	
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onSensorChanged(SensorEvent arg0) {
		// TODO Auto-generated method stub
		Log.i("SensorEvent","arg0.values[0] = "+arg0.values[0]);
		Log.i("SensorEvent","sensor.Type = "+arg0.sensor.getType());
		
		returnValues(arg0.values, arg0.sensor.getType());
	}
	
	private class BatteryReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			int current = arg1.getExtras().getInt("level");
			int total = arg1.getExtras().getInt("scale");
			int percent = current*100/total;
			Log.i("BatteryReceiver","percent = "+percent);
		}
		
	}
	
	private int isNetworkAvailable() {
		return 0;
	}

	
}
