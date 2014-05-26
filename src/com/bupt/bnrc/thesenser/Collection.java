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
//假如用到位置提醒功能，需要import该类
//import com.baidu.location.BDNotifyListener;
//************//

//如果使用地理围栏功能，需要import如下类
//import com.baidu.location.BDGeofence;
//import com.baidu.location.BDLocationStatusCodes;
//import com.baidu.location.GeofenceClient;
//import com.baidu.location.GeofenceClient.OnAddBDGeofencesResultListener;
//import com.baidu.location.GeofenceClient.OnGeofenceTriggerListener;
//import com.baidu.location.GeofenceClient.OnRemoveBDGeofencesResultListener;
//*****************************************************************//

import com.bupt.bnrc.thesenser.model.FileModel;
import com.bupt.bnrc.thesenser.model.PhotoStats;
import com.bupt.bnrc.thesenser.model.DataModel;

public class Collection implements SensorEventListener {
	
	//TEST*****************************
	private float light_test = 1000;
	private float noise_test = 36;
	private float [] orientation_test = new float[] {1,2,3};
	private String picName_test = "test.jpg";
	private Date date_test = new Date();
	private int connectionState_test = 1;
	private int batteryState_test = 1;
	private int percent_test = 85;
	private float latitude_test = 123;
	private float longitude_test = 321;
	
	private int exposureValue = 100;
	private float focalDistance = 35;
	private float aperture = 1/200;
	
	public int flog_test = 15;
	
	
//	public void save() {
//		FileModel fileModel = new FileModel(picName_test, date_test, orientation_test[0], orientation_test[1], orientation_test[2], 
//				longitude_test, latitude_test, exposureValue, focalDistance, aperture);
//		fileModel.save(this.app);
//	}
//	public FileModel getFileModel() {
//		FileModel fileModel = new FileModel(picName_test, date_test, orientation_test[0], orientation_test[1], orientation_test[2], 
//				longitude_test, latitude_test, exposureValue, focalDistance, aperture);
//		return fileModel;
//	}
//	public float getLight() {
//		return this.light_test;
//	}
	public float[] getOrientation() {
		return this.orientation_test;
	}
	//TEST*****************************

	
	private Activity app;
	private SensorManager sensorManager;
	private Sensor sensor;
	
	private BatteryReceiver receiver = null;
	private ConnectivityManager connectManager;
	
	//传感器
	private float light;
	
	private float [] acceleration = new float[3];
	private float [] magnetic_field = new float[3];
	
	private float [] orientation = new float[3];			//getOrientation();
	private float [] sensor_orientation = new float[3];	//Sensor.TYPE_ORIENTATION
	
	//图片
	private String picName;
	private Date date;
	
	//电池&网络
	private int connectionState;
	private int batteryState;
	private int percent;
	//位置
	private float latitude;
	private float longitude;
	
	//is SensorListener registered
	private boolean sensorlistener_flag = true;
	//*******************************************************************************//
	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new MyLocationListener();
	
	private BDLocation testLocation = new BDLocation();
	
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
	 
	      //testLocation = location;
	      //logMsg(sb.toString());
	      latitude =(float) location.getLatitude();
	      longitude =(float) location.getLongitude();
	      Log.i("BD_LBS_SDK", sb.toString());
	    }
	public void onReceivePoi(BDLocation poiLocation) {
	//将在下个版本中去除poi功能
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
		//Log.d("logMsg", str);
		Toast toast = Toast.makeText(this.app, str, Toast.LENGTH_LONG);
		toast.show();
	}
	//*******************************************************************************//
	
	public Collection(Activity app) {
		this.app = app;
		
		Log.i("new Collection()", "new Collection @ Activity = "+this.app.getApplicationInfo().toString());
		
		sensorManager = (SensorManager) this.app.getSystemService(android.content.Context.SENSOR_SERVICE);
		connectManager = (ConnectivityManager) this.app.getSystemService(Context.CONNECTIVITY_SERVICE);
		mLocationClient = new LocationClient(this.app.getApplicationContext());     //声明LocationClient类
	     
		setValues();//register every sensors
	}
	
	public void setValues() {
		setLight();
		setAccelerometer();
		setMagneticField();
		setOrientation();
		
		calculateOrientation();
		
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
		mLocationClient.start();
		
		date = new Date();
		
		Toast toast = Toast.makeText(this.app, ""+date, Toast.LENGTH_LONG);
		toast.show();
	}
	
	private void calculateOrientation() {
		float [] R = new float[9];
		SensorManager.getRotationMatrix(R, null, acceleration, magnetic_field);
		SensorManager.getOrientation(R, orientation);
		
		//Log.i("calculateOrientation", "collect.orientation = "+orientation[0]+" , "+orientation[1]+" , "+orientation[2]);
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
		//double longitude = testLocation.getLongitude();
		Log.i("getLight() -> light = ", ""+this.light);
		Log.i("getLight() -> longitude = ", ""+longitude);
		Log.i("getLight() -> latitude = ", ""+latitude);
		Log.i("getLight() -> connect = ",""+this.connectionState);
		Log.i("getLight() -> battery = ",""+this.batteryState+" , "+this.percent+"%");
		Log.i("getLight() -> ///////////", "/////////////////////////");
		Log.i("getLight() -> orientation", "x = "+this.orientation[0]);
		Log.i("getLight() -> orientation", "y = "+this.orientation[1]);
		Log.i("getLight() -> orientation", "z = "+this.orientation[2]);
		Log.i("getLight() -> ///////////", "/////////////////////////");
		Log.i("getLight() -> orientation", "x = "+this.sensor_orientation[0]);
		Log.i("getLight() -> orientation", "y = "+this.sensor_orientation[1]);
		Log.i("getLight() -> orientation", "z = "+this.sensor_orientation[2]);
		return this.light;
	}
	public Date getDate() {
		return this.date;
	}
	
	private void returnValues(float [] e ,int Type) {
		//Log.i("SensorEvent","sensor.Type = "+Type);
		switch(Type){
		case Sensor.TYPE_LIGHT:
			this.light = e[0];
			//Log.i("SensorEvent","Collection->this.light = "+this.light);
			break;
		case Sensor.TYPE_ACCELEROMETER:
			this.acceleration = e;
			//Log.i("SensorEvent","Collection->this.acceleration = "+this.acceleration[0]+" , "+this.acceleration[1]+" , "+this.acceleration[2]);
			calculateOrientation();
			break;
		case Sensor.TYPE_MAGNETIC_FIELD:
			this.magnetic_field = e;
			//Log.i("SensorEvent","Collection->this.magnetic_field = "+this.magnetic_field[0]+" , "+this.magnetic_field[1]+" , "+this.magnetic_field[2]);
			calculateOrientation();
			break;
		case Sensor.TYPE_ORIENTATION:
			this.sensor_orientation = e;
			//Log.i("SensorEvent","Collection->this.sensor_orientation = "+this.sensor_orientation[0]+" , "+this.sensor_orientation[1]+" , "+this.sensor_orientation[2]);
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
		//Log.i("SensorEvent","sensor.Type = "+arg0.sensor.getType());
		
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
			batteryState = bs;
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
	
	public void save() {
		Date tempDate = new Date();
		DataModel model = new DataModel(this.light, this.noise_test, tempDate, this.batteryState, this.percent, this.connectionState, this.longitude, this.latitude);
		model.save(app);
	}

	
}
