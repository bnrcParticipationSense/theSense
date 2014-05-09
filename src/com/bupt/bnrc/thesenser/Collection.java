package com.bupt.bnrc.thesenser;

import java.util.Date;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
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
import android.os.BatteryManager;
//Connect
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;

//location
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.BDNotifyListener;//�����õ�λ�����ѹ��ܣ���Ҫimport����
//���ʹ�õ���Χ�����ܣ���Ҫimport������
import com.baidu.location.BDGeofence;
import com.baidu.location.BDLocationStatusCodes;
import com.baidu.location.GeofenceClient;
import com.baidu.location.GeofenceClient.OnAddBDGeofencesResultListener;
import com.baidu.location.GeofenceClient.OnGeofenceTriggerListener;
import com.baidu.location.GeofenceClient.OnRemoveBDGeofencesResultListener;

public class Collection implements SensorEventListener {
	public float light;
	
	private float [] acceleration = new float[3];
	private float [] magnetic_field = new float[3];
	
	public float [] orientation = new float[3];
	public float [] sensor_orientation = new float[3];
	
	private String picName;
	private Date date;
	
	private Activity app;
	private SensorManager sensorManager;
	private Sensor sensor;
	
	private BatteryReceiver receiver = null;
	private ConnectivityManager connectManager;
	
	private int connectionState;
	private int batteryState;
	private int percent;
	
	//is SensorListener registered
	private boolean sensorlistener_flag = true;
	//*******************************************************************************//
	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new MyLocationListener();
	
	public class MyLocationListener implements BDLocationListener {
	    @Override
	   public void onReceiveLocation(BDLocation location) {
	      if (location == null)
	          return ;
	      StringBuffer sb = new StringBuffer(256);
	      sb.append("time : ");
	      sb.append(location.getTime());
	      sb.append("\nerror code : ");
	      sb.append(location.getLocType());
	      sb.append("\nlatitude : ");
	      sb.append(location.getLatitude());
	      sb.append("\nlontitude : ");
	      sb.append(location.getLongitude());
	      sb.append("\nradius : ");
	      sb.append(location.getRadius());
	      if (location.getLocType() == BDLocation.TypeGpsLocation){
	           sb.append("\nspeed : ");
	           sb.append(location.getSpeed());
	           sb.append("\nsatellite : ");
	           sb.append(location.getSatelliteNumber());
	           } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
	           sb.append("\naddr : ");
	           sb.append(location.getAddrStr());
	        } 
	 
	      logMsg(sb.toString());
	    }
	public void onReceivePoi(BDLocation poiLocation) {
	//�����¸��汾��ȥ��poi����
	         if (poiLocation == null){
	                return ;
	          }
	         StringBuffer sb = new StringBuffer(256);
	          sb.append("Poi time : ");
	          sb.append(poiLocation.getTime());
	          sb.append("\nerror code : ");
	          sb.append(poiLocation.getLocType());
	          sb.append("\nlatitude : ");
	          sb.append(poiLocation.getLatitude());
	          sb.append("\nlontitude : ");
	          sb.append(poiLocation.getLongitude());
	          sb.append("\nradius : ");
	          sb.append(poiLocation.getRadius());
	          if (poiLocation.getLocType() == BDLocation.TypeNetWorkLocation){
	              sb.append("\naddr : ");
	              sb.append(poiLocation.getAddrStr());
	         } 
	          if(poiLocation.hasPoi()){
	               sb.append("\nPoi:");
	               sb.append(poiLocation.getPoi());
	         }else{             
	               sb.append("noPoi information");
	          }
	         logMsg(sb.toString());
	        }
	}
	
	private void logMsg(String str) {
		Log.d("logMsg", str);
	}
	//*******************************************************************************//
	
	public Collection(Activity app) {
		this.app = app;
		sensorManager = (SensorManager) this.app.getSystemService(android.content.Context.SENSOR_SERVICE);
		connectManager = (ConnectivityManager) this.app.getSystemService(Context.CONNECTIVITY_SERVICE);
		mLocationClient = new LocationClient(this.app.getApplicationContext());     //����LocationClient��
	     
		setValues();
	}
	
	@SuppressLint("InlinedApi")
	public void setValues() {
		setLight();
		setAccelerometer();
		setMagneticField();
		setOrientation();
		
		//calculateOrientation();
		
		//Battery
		receiver = new BatteryReceiver();
		IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		this.app.registerReceiver(receiver, filter);
		//int bs = this.app.getIntent().getIntExtra(BatteryManager.EXTRA_STATUS, -1);
		//Log.i("BatterManager", "BatteryManager.isCHARGING = "+bs);
		
		
		
		//Connection
		boolean flag = false;
		if (connectManager.getActiveNetworkInfo() != null) {
			flag = connectManager.getActiveNetworkInfo().isAvailable();
		}
		if (!flag) {
			//error
			connectionState = 0;
		}
		else {
			connectionState = isNetworkAvailable();
		}
		
		//Location
		mLocationClient.registerLocationListener( myListener );
		
		date = new Date();
		
		Toast toast = Toast.makeText(this.app, ""+date, Toast.LENGTH_LONG);
		toast.show();
	}
	
	private void calculateOrientation() {
		float [] R = new float[9];
		SensorManager.getRotationMatrix(R, null, acceleration, magnetic_field);
		SensorManager.getOrientation(R, orientation);
		
		Log.i("calculateOrientation", "collect.orientation = "+orientation[0]+" , "+orientation[1]+" , "+orientation[2]);
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
	private void setOrientation() {
		this.sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		sensorManager.registerListener(this, this.sensor, SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	public void setPicName(String str) {
		this.picName = str;
	}
	
	public float getLight() {
		return this.light;
	}
	
	private void returnValues(float [] e ,int Type) {
		//Log.i("SensorEvent","sensor.Type = "+Type);
		switch(Type){
		case Sensor.TYPE_LIGHT:
			this.light = e[0];
			Log.i("SensorEvent","Collection->this.light = "+this.light);
			break;
		case Sensor.TYPE_ACCELEROMETER:
			this.acceleration = e;
			Log.i("SensorEvent","Collection->this.acceleration = "+this.acceleration[0]+" , "+this.acceleration[1]+" , "+this.acceleration[2]);
			calculateOrientation();
			break;
		case Sensor.TYPE_MAGNETIC_FIELD:
			this.magnetic_field = e;
			Log.i("SensorEvent","Collection->this.magnetic_field = "+this.magnetic_field[0]+" , "+this.magnetic_field[1]+" , "+this.magnetic_field[2]);
			calculateOrientation();
			break;
		case Sensor.TYPE_ORIENTATION:
			this.sensor_orientation = e;
			Log.i("SensorEvent","Collection->this.sensor_orientation = "+this.sensor_orientation[0]+" , "+this.sensor_orientation[1]+" , "+this.sensor_orientation[2]);
			break;
		default:
			break;
		}
	}
	
	public void stopListener() {
		if(this.sensorlistener_flag) {
			sensorManager.unregisterListener(this);
			this.app.unregisterReceiver(receiver);
			this.sensorlistener_flag = false;
		}
		else {
			setLight();
			setAccelerometer();
			setMagneticField();
			setOrientation();
			this.sensorlistener_flag = true;
		}
	}
	
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onSensorChanged(SensorEvent arg0) {
		// TODO Auto-generated method stub
		//Log.i("SensorEvent","arg0.values[0] = "+arg0.values[0]);
		Log.i("SensorEvent","sensor.Type = "+arg0.sensor.getType());
		
		returnValues(arg0.values, arg0.sensor.getType());
	}
	
	
	//Battery & Connection
	private class BatteryReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			int current = arg1.getExtras().getInt("level");
			int total = arg1.getExtras().getInt("scale");
			int bs = arg1.getExtras().getInt(BatteryManager.EXTRA_STATUS);
			percent = current*100/total;
			Log.i("BatteryReceiver","percent = "+percent);
			Log.i("BatterManager", "BatteryManager.isCHARGING = "+bs);
		}
		
	}
	
	private int isNetworkAvailable() {
		State gprs = connectManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
		State wifi = connectManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
		
		if (wifi == State.CONNECTED || wifi == State.CONNECTING) {
			Log.i("isNetworkAvailable","wifi state = 3");
			return 3;
		}
		if (gprs == State.CONNECTED || gprs == State.CONNECTING) {
			Log.i("isNetworkAvailable", "wifi state = 2");
			return 2;
		}
		return 0;
	}

	
}
