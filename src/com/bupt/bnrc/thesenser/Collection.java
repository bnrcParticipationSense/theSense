package com.bupt.bnrc.thesenser;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

//noise
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

//Battery
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Looper;
//Connect
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;

//location
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
//���濡���ㄥ�颁��缃������������斤�����瑕�import璇ョ被
//import com.baidu.location.BDNotifyListener;
//************//

//濡����浣跨�ㄥ�扮����存�������斤�����瑕�import濡�涓�绫�
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
import com.bupt.bnrc.thesenser.utils.JSON;
import com.bupt.bnrc.thesenser.utils.Upload;



public class Collection implements SensorEventListener {
	
	private Activity app;
	private SensorManager sensorManager;
	private Sensor sensor;
	
	private BatteryReceiver receiver = null;
	private ConnectivityManager connectManager;
	
	//光线声音
	private float light;
	private float noise;
	private RecordThread for_noise = null;
	
	private float [] acceleration = new float[3];
	private float [] magnetic_field = new float[3];
	
	private float [] orientation = new float[3];			//getOrientation();
	private float [] sensor_orientation = new float[3];	//Sensor.TYPE_ORIENTATION
	
	//声音
	
	
	//照片
	private String picName;
	private Date date;
	
	//状态
	private int connectionState;
	private int batteryState;
	private int percent;
	//位置
	private float latitude;
	private float longitude;
	
	//is SensorListener registered
	private boolean sensorlistener_flag = true;
	
	//new things for upload or others
	private DataModel mData = null;
	private DataModel preData = null;
	
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
	//
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
	private static Collection instance = null;
	public static Collection getCollection(Activity app) {
		if(instance == null){
			instance = new Collection(app);
		}
		return instance;
	}
	public static Collection getCollention() {
		if(instance != null){
			return instance;
		}
		//
		System.exit(0);
		return null;
	}
	/*
	private Collection() {	
		Log.i("new Collection()", "new Collection @ Activity = "+this.app.getApplicationInfo().toString());
		
		//sensorManager = (SensorManager) this.app.getSystemService(android.content.Context.SENSOR_SERVICE);
		//connectManager = (ConnectivityManager) this.app.getSystemService(Context.CONNECTIVITY_SERVICE);
		//mLocationClient = new LocationClient(this.app.getApplicationContext());     //
		//mLocationClient = new LocationClient(this.app);
		sensorManager = (SensorManager) this.app.getSystemService(android.content.Context.SENSOR_SERVICE);
		connectManager = (ConnectivityManager) this.app.getSystemService(Context.CONNECTIVITY_SERVICE);
		mLocationClient = new LocationClient(this.app);
	     
		setValues();//register every sensors
	}
	*/
	
	private Collection(Activity app) {
		this.app = app;
		
		Log.i("new Collection()", "new Collection @ Activity = "+this.app.getApplicationInfo().toString());
		
		sensorManager = (SensorManager) this.app.getSystemService(android.content.Context.SENSOR_SERVICE);
		connectManager = (ConnectivityManager) this.app.getSystemService(Context.CONNECTIVITY_SERVICE);
		//mLocationClient = new LocationClient(this.app.getApplicationContext());     //
		mLocationClient = new LocationClient(this.app);
	     
		setValues();//register every sensors
	}
	
	private void setNoise(){
		Thread noise_t = new Thread() {
			public void run(){
				for_noise.start();
				for_noise.getValue();
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				for_noise.getValue();
				try {
					sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				float tmp = for_noise.getValue();
				Log.i("RecordThread", "Noise = "+tmp);
				if(tmp > 0)
					noise = tmp;
				for_noise.stop();
			}
		};
		noise_t.start();
	}
	
	private void setValues() {
		setLight();
		setAccelerometer();
		setMagneticField();
		setOrientation();
		//runForNoise();
		/*
		Thread noise_t = new Thread() {
			public void run(){
				while(true){
					if(need_noise)
					{
						runForNoise();
						need_noise = false;
					}
				}
				
			}
		};
		*/
		//noise_t.start();
		for_noise = RecordThread.getRecordThread();
		//for_noise.start();
		setNoise();
		//noise = for_noise.getValue();
		
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
	public void setDataModel() {
		date = new Date();
		//runForNoise();
		//need_noise = true;
		//noise = for_noise.getValue();
		setNoise();
		mData = new DataModel(this.light, this.noise, this.date, this.batteryState, this.percent, this.connectionState, this.longitude, this.latitude);
	}
	
	public void setPicName(String str) {
		this.picName = str;
	}
	
	public float getLight() {
		//double longitude = testLocation.getLongitude();
		Log.i("getLight() -> light = ", ""+this.light);
		Log.i("getLight() -> noise = ", ""+this.noise);
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
	public float getNoise() {
		//return this.for_noise.getValue();
		setNoise();
		return this.noise;
	}
	public Date getDate() {
		date = new Date();
		return this.date;
	}
	public String getDateSring() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		date = new Date();
		return new String(sdf.format(date));
	}
	public float getxDirect() {
		return this.sensor_orientation[0];
	}
	public float getyDirect() {
		return this.sensor_orientation[1];
	}
	public float getzDirect() {
		return this.sensor_orientation[2];
	}
	public float getLongtitude() {
		return this.longitude;
	}
	public float getLatitude() {
		return this.latitude;
	}
	public int getBatteryState() {
		return this.percent;
	}
	public int getChargeState() {
		return this.batteryState;
	}
	public int getNetState() {
		return this.connectionState;
	}
	public DataModel getDataModel() {
		setDataModel();
		return mData;
	}
	public DataModel getPreDataModel() {
		if(this.preData != null) {
			return this.preData;
		}
		else {
			setDataModel();
			this.preData = new DataModel(this.mData);
			return this.preData;
		}
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
		/*
		else {
			setLight();
			setAccelerometer();
			setMagneticField();
			setOrientation();
			this.sensorlistener_flag = true;
		}
		*/
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
		Log.i("Collection", "save()");
		if(this.sensorlistener_flag) {
			//setDataModel();
			mData.save(app);
			this.preData = new DataModel(this.mData);
		}
		else {
			Toast toast = Toast.makeText(app, "采集模块已停止工作", Toast.LENGTH_LONG);
			toast.show();
		}
	}

	private void doSomething(JSONObject obj){
		String username;
		try{
			username = (String) obj.get("username");
		}catch(JSONException e) {
			e.printStackTrace();
			username = "error";
		}
		Log.i("CameraActivity", "username = "+username);
	}
	public void upload() {
		setDataModel();
		Thread t = new Thread() {
			public void run() {
				
				Looper.prepare();
				JSONObject obj = null;
				try {
					Log.i("CameraActivity", "NEW Thread for UploadingPrecess...");
					obj = Upload.Uploading(app, "", JSON.toJSON(mData));
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				if(obj != null){
					doSomething(obj);
				}
				else{
					/*
					 * 干点啥。。。
					 */
				}
				Looper.loop();
			}
		};
		t.start();
	}
	
	public void showinfo(Activity a) {
		//need_noise = true;
		//noise = for_noise.getValue();
		String str =	"光线："+this.light+";\n"+
						"噪音："+this.noise+";\n"+
						"经度："+this.longitude+";\n"+
						"纬度："+this.latitude+"\n"+
						"x方向："+this.sensor_orientation[0]+";\n"+
						"y方向："+this.sensor_orientation[1]+";\n"+
						"z方向："+this.sensor_orientation[2]+";\n"+
						"电量："+this.percent+";\n"+
						"时间："+this.date+";";
		Toast toast = Toast.makeText(a, str, Toast.LENGTH_LONG);
		toast.show();
	}
	
	//*******************************************************************************//
	private AudioRecord ar;
    private int bs;
    private static int SAMPLE_RATE_IN_HZ = 8000;
 
    public void RecordThread() {
    	bs = AudioRecord.getMinBufferSize(SAMPLE_RATE_IN_HZ, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        ar = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE_IN_HZ, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bs);
    }
 
    public void runForNoise() {
    	RecordThread();
        ar.startRecording();
    	 
    	int v = ar.getState();
    	int j = ar.getRecordingState();
        byte[] buffer = new byte[bs];

//        while(this.isRun) {
            int r = ar.read(buffer, 0, bs);
            int w = 0;
            for (int i = 0; i < buffer.length; i++) {
                w += buffer[i] * buffer[i];
            }
            Log.d("spll", String.valueOf(w / (float) r));
            
//        }
        Log.d("spl", String.valueOf(v));
		Log.d("sp", String.valueOf(j));
		noise = w / (float) r;
    }
    public void stop() {
    	for_noise.stop();
    }
}
