package com.bupt.bnrc.thesenser;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.hardware.Camera.ShutterCallback;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.SurfaceHolder.Callback;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import android.media.ExifInterface;

import com.bupt.bnrc.thesenser.model.DataModel;
import com.bupt.bnrc.thesenser.model.FileModel;
import com.bupt.bnrc.thesenser.model.PhotoStats;
import com.bupt.bnrc.thesenser.utils.Upload;

public class CameraActivity extends Activity {


	private static final String ACTIVITY_TAG="CameraActivity";
	private static final String LOG_PREFIX="Camera: ";

	ImageButton photoBut;
	SurfaceView sView;
	SurfaceHolder surfaceHolder;
	int screenWidth, screenHeight;

	Handler handler;

	// 定义系统所用的照相机
	Camera mcamera;
	String imgName;
	TextView infoText;
	TextView photoInfoText;
	View saveView;
	AlertDialog.Builder saveAlertBuilder;
	AlertDialog saveAlertDialog;
	//是否在浏览中
	boolean isPreview = false;

	private Camera.AutoFocusCallback mAutoFocusCallback;
	List<android.hardware.Camera.Size> supPreSize;
	List<android.hardware.Camera.Size> supPicSize;
	List<Integer> supFps;




	java.util.Date date;

	boolean GPSState;

	Collection collect = null;
	PhotoStats photoStats = null;
	DataModel dataModel = null;
	String fileName;
	private Activity app;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		collect = new Collection(this);
		app = this;
		// 设置全屏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
			WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_camera);
		// 获取窗口管理器
		WindowManager wm = getWindowManager();
		Display display = wm.getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		// 获取屏幕的宽和高
		display.getMetrics(metrics);
		screenWidth = metrics.widthPixels;
		screenHeight = metrics.heightPixels;
		// 获取界面中SurfaceView组件
		sView = (SurfaceView) findViewById(R.id.sView);
		// 设置该Surface不需要自己维护缓冲区
		sView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		// 获得SurfaceView的SurfaceHolder
		surfaceHolder = sView.getHolder();
		// 为surfaceHolder添加一个回调监听器
		surfaceHolder.addCallback(new Callback()
		{
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format,
				int width, int height)
			{
			}
			@Override
			public void surfaceCreated(SurfaceHolder holder)
			{
				// 打开摄像头
				initCamera();
			}
			@Override
			public void surfaceDestroyed(SurfaceHolder holder)
			{
				// 如果camera不为null ,释放摄像头
				if (mcamera != null)
				{
					if (isPreview) mcamera.stopPreview();
					mcamera.release();
					mcamera = null;
				}
			}
		});
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	private void initCamera()
	{
		if (!isPreview)
		{
			// 此处默认打开后置摄像头。
			// 通过传入参数可以打开前置摄像头
			mcamera = Camera.open(0);  //①
			mcamera.setDisplayOrientation(90);
		}
		if (mcamera != null && !isPreview)
		{
			try
			{
				Camera.Parameters parameters = mcamera.getParameters();
				// 设置预览照片的大小
				//parameters.setPreviewSize(1280, 720);
				// 设置预览照片时每秒显示多少帧的最小值和最大值
				//parameters.setPreviewFpsRange(4, 10);
				// 设置图片格式
				parameters.setPictureFormat(ImageFormat.JPEG);
				// 设置JPG照片的质量
				parameters.set("jpeg-quality", 100);
				// 设置照片的大小
				parameters.setPictureSize(2048, 1152);
				//
				mcamera.setParameters(parameters);
				// 通过SurfaceView显示取景画面
				mcamera.setPreviewDisplay(surfaceHolder);  //②
				// 开始预览
				mcamera.startPreview();  //③
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			isPreview = true;
		}
	}

	public void capture(View source)
	{
		if (mcamera != null)
		{
			// 控制摄像头自动对焦后才拍照
			mcamera.autoFocus(autoFocusCallback);  //④
		}
	}

	AutoFocusCallback autoFocusCallback = new AutoFocusCallback()
	{
		// 当自动对焦时激发该方法
		@Override
		public void onAutoFocus(boolean success, Camera camera)
		{
			if (success)
			{
				// takePicture()方法需要传入3个监听器参数
				// 第1个监听器：当用户按下快门时激发该监听器
				// 第2个监听器：当相机获取原始照片时激发该监听器
				// 第3个监听器：当相机获取JPG照片时激发该监听器
				camera.takePicture(new ShutterCallback()
				{
					public void onShutter()
					{
						// 按下快门瞬间会执行此处代码
						collect.getLight();
						dataModel = collect.getDataModel();
						Camera.Parameters parameters = mcamera.getParameters();
						photoStats = new PhotoStats(collect.getxDirect(), collect.getyDirect(), collect.getzDirect(), collect.getLongtitude(), 
								collect.getLatitude(), 0, parameters.getFocalLength(), (float)0);
						Log.i("onShutter", "Hello ShutterCallback");
					}
				}, new PictureCallback()
				{
					public void onPictureTaken(byte[] data, Camera c)
					{
						// 此处代码可以决定是否需要保存原始照片信息
						if(data != null)
						{
							Log.i("RAW-PictureCallback", "data != null");
							int l = data.length;
							Log.i("RAW-PictureCallback", "data.length = "+l);
						}
						else{
							Log.i("RAW-PictureCallback", "data == null");
						}
					}
				}, myJpegCallback);  //⑤
			}
		}
	};

	PictureCallback myJpegCallback = new PictureCallback()
	{
		@Override
		public void onPictureTaken(byte[] data, Camera camera)
		{
			
			fileName = Environment.getExternalStorageDirectory().toString()
					+File.separator
					+"SensorTest1"
					+File.separator
					+"ST_"
					+System.currentTimeMillis()
					+".jpg";
			
			// 根据拍照所得的数据创建位图
			final Bitmap bm = BitmapFactory.decodeByteArray(data, 0,
				data.length);
			// 加载/layout/save.xml文件对应的布局资源
			saveView = getLayoutInflater().inflate(R.layout.save, null);

			photoInfoText = (TextView)saveView.findViewById(R.id.photo_name);
			photoInfoText.setText("相片名称");
			
			TextView textView = (TextView)saveView.findViewById(R.id.phone_name2);
			textView.setText(fileName);
			// 加载/layout/save.xml文件对应的布局资源
			//View saveDialog = getLayoutInflater().inflate(R.layout.save,
			//	null);
			//final EditText photoName = (EditText) saveDialog
			//	.findViewById(R.id.phone_name);
			// 获取saveDialog对话框上的ImageView组件
			ImageView show = (ImageView) saveView.findViewById(R.id.show);
			// 显示刚刚拍得的照片
			show.setImageBitmap(bm);
			// 使用对话框显示saveDialog组件
			/*
			new AlertDialog.Builder(CaptureImage.this).setView(saveDialog)
				.setPositiveButton("保存", new OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						// 创建一个位于SD卡上的文件
						File file = new File(Environment
							.getExternalStorageDirectory(), photoName
							.getText().toString() + ".jpg");
						String path = file.getParent().toString();
						Toast toast = Toast.makeText(CaptureImage.this, path, Toast.LENGTH_LONG);
						toast.show();
						FileOutputStream outStream = null;
						try
						{
							// 打开指定文件对应的输出流
							outStream = new FileOutputStream(file);
							// 把位图输出到指定文件中
							bm.compress(CompressFormat.JPEG, 100,
								outStream);
							outStream.close();
						}
						catch (IOException e)
						{
							e.printStackTrace();
						}
					}
				}).setNegativeButton("取消", null).show();
				*/
			
			
			
				//使用对话框显示saveDialog组件
				saveAlertBuilder = new AlertDialog.Builder(CameraActivity.this);
				saveAlertBuilder.setView(saveView);
				saveAlertBuilder.setPositiveButton("上传", new OnClickListener()
				{

					@Override
					public void onClick(DialogInterface dialog,
						int which)
					{
			            //更新界面  

				        Button saveButton = saveAlertDialog.getButton(android.content.DialogInterface.BUTTON_POSITIVE);
						saveButton.setText("上传中...");
						saveButton.setTextColor(Color.BLUE);
						saveButton.invalidate(); 

					    //handler=new Handler(); 
					    //handler.post(UploadingProcess); 
						File file = new File(fileName);
						if(!file.getParentFile().exists()){
							file.getParentFile().mkdirs();
						}
						Log.i("CaptureImage","new bos!");
						BufferedOutputStream bos;
						try {
							bos = new BufferedOutputStream(new FileOutputStream(file));
							bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
							bos.flush();
							Log.i("CaptureImage","bos.flush()");
							bos.close();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Log.i("CaptureImage", "uploading...");
						//**************Exif设置*************
						try {
							ExifInterface exif = new ExifInterface(fileName);
							exif.setAttribute("Light", ""+collect.getLight());
							exif.setAttribute(ExifInterface.TAG_DATETIME, collect.getDate().toString());
							exif.setAttribute(ExifInterface.TAG_MODEL, "model");
							exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, ""+collect.getLatitude());
							exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, ""+collect.getLongtitude());
							exif.setAttribute(ExifInterface.TAG_MAKE, ""+dataModel.toString());
							//exif.setAttribute("Light", ""+collect.getLight());
							exif.saveAttributes();
							Log.i("CameraActivity", "Exif.Light = "+exif.getAttribute("Light"));
						} catch (IOException e) {
							e.printStackTrace();
						}

						//**************Exif设置***END*******
						
						Thread t = new Thread() {
							public void run() {
								Looper.prepare();
								try {
									Log.i("CameraActivity", "NEW Thread for UploadingPrecess...");
									Upload.Uploading(app, "", fileName);
								} catch (Exception e) {
									e.printStackTrace();
								}
								Looper.loop();
							}
						};
						t.start();

						mcamera.startPreview(); //do this function later

						
					}

				});
				saveAlertBuilder.setNegativeButton("保存", new OnClickListener()
				{
					public void onClick(DialogInterface dialog,
							int which)
					{
						// name the img in term of date.


						/*
						String fileName = Environment.getExternalStorageDirectory().toString()
								+File.separator
								+"SensorTest1"
								+File.separator
								+"ST_"
								+System.currentTimeMillis()
								+".jpg";
								*/
						File file = new File(fileName);
						if(!file.getParentFile().exists()){
							file.getParentFile().mkdirs();
						}
						Log.i("CaptureImage","new bos!");
						BufferedOutputStream bos;
						try {
							bos = new BufferedOutputStream(new FileOutputStream(file));
							bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
							bos.flush();
							Log.i("CaptureImage","bos.flush()");
							bos.close();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						//**************Exif设置*************
						try {
							ExifInterface exif = new ExifInterface(fileName);
							exif.setAttribute("Light", ""+collect.getLight());
							exif.setAttribute(ExifInterface.TAG_DATETIME, collect.getDate().toString());
							exif.setAttribute(ExifInterface.TAG_MODEL, "model");
							exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, ""+collect.getLatitude());
							exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, ""+collect.getLongtitude());
							exif.setAttribute(ExifInterface.TAG_MAKE, "");
							//exif.setAttribute("Light", ""+collect.getLight());
							exif.saveAttributes();
							Log.i("CameraActivity", "Exif.Light = "+exif.getAttribute("Light"));
						} catch (IOException e) {
							e.printStackTrace();
						}

						//**************Exif设置***END*******

						FileModel fileModel = new FileModel(fileName, collect.getDate(), photoStats);
						fileModel.save(app);

						mcamera.startPreview();

					}
				});

				saveAlertDialog = saveAlertBuilder.create();
				saveAlertDialog.show();
				// 重新浏览
				camera.stopPreview();
				camera.startPreview();
				isPreview = true;
		}
	};
    

    @Override 
    protected void onStart() {  
        // Log.i(ACTIVITY_TAG, LOG_PREFIX+"　onStart called!");  
        super.onStart();  
    }  
 
    @Override 
    protected void onRestart() {  
        // Log.i(ACTIVITY_TAG, LOG_PREFIX+"　onRestart called!");  
        super.onRestart();  
    }  
 
    @Override 
    protected void onResume() {  
       //  Log.i(ACTIVITY_TAG, LOG_PREFIX+"　onResume called!");
        //sensors.SensorsInit();
        super.onResume();  
    }  
 
    @Override 
    protected void onPause() {  
        // Log.i(ACTIVITY_TAG, LOG_PREFIX+"　onPause called!");  
     
        //sensors.SensorsRelease();
        super.onPause();  
    }  
 
    @Override 
    protected void onStop() {  
        // Log.i(ACTIVITY_TAG, LOG_PREFIX+"　onStop called!");  

        super.finish();
        //退出后台线程,以及销毁静态变量
        //System.exit(0);
        super.onStop();  
    }  
 
    @Override 
    protected void onDestroy() {  
        Log.i(ACTIVITY_TAG, LOG_PREFIX+"onDestroy called!");  

        super.onDestroy();  
    }
}
