package com.bupt.bnrc.thesenser;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.hardware.Camera.ShutterCallback;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.SurfaceHolder.Callback;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import android.media.ExifInterface;
import com.bupt.bnrc.thesenser.model.FileModel;
import com.bupt.bnrc.thesenser.model.PhotoStats;

public class CameraActivity extends BaseActivity {

	
	private static final String ACTIVITY_TAG="CameraActivity";
	private static final String LOG_PREFIX="Camera: ";
	
	ImageButton photoBut;
	SurfaceView sView;
	SurfaceHolder surfaceHolder;
	int screenWidth, screenHeight;
	
	Handler handler;
	Bitmap nbm;
	
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
	private Activity app;
	
    @Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
		collect = new Collection(this);
		app = this;
		
		
		
		//设置横屏！！
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		// 设置全屏
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//			WindowManager.LayoutParams.FLAG_FULLSCREEN);
		

		sView = (SurfaceView) findViewById(R.id.sView);
		photoBut = (ImageButton) findViewById(R.id.photoBut);
		infoText = (TextView) findViewById(R.id.info);
		
		//infoText.setText("hello");
		
		photoBut.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		    	
		    	// auto focus before taking photo
		    	 mcamera.autoFocus(mAutoFocusCallback);
		    	 mcamera.autoFocus(mAutoFocusCallback);
		    	 mcamera.autoFocus(mAutoFocusCallback);
		    	 
		    	 
		    	 
		    	// 拍照
				mcamera.takePicture(new ShutterCallback() {
					public void onShutter() {
						collect.getLight();
					}
				}, null , myjpegCallback);
		    }
		});
		
		
		
		
		
		
//		WindowManager wm = (WindowManager) getSystemService(
//			Context.WINDOW_SERVICE);
//		Display display = wm.getDefaultDisplay();
		// 获取屏幕的宽和高
	//	screenWidth = display.getWidth();
		//screenHeight = display.getHeight();
		// 获取界面中SurfaceView组件
		
		
		// 获得SurfaceView的SurfaceHolder
		surfaceHolder = sView.getHolder();
		
		//surfaceHolder.setFixedSize(960, 720); 
		
		// 为surfaceHolder添加一个回调监听器
		surfaceHolder.addCallback(new Callback()
		{
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height)
			{
				mcamera.stopPreview();
				isPreview = false;
				
				Log.i(ACTIVITY_TAG, LOG_PREFIX+"surfaceChanged");
					
				  Camera.Parameters parameters = mcamera.getParameters();// 获得相机参数
				  Size s = parameters.getPictureSize();
				  double h = s.width;
				  double w = s.height;
				  
				  Log.i(ACTIVITY_TAG, LOG_PREFIX+"getPictureSize() width = "+h+" ; height = "+w);

			        
				  if( width>height )
				  {
					  sView.setLayoutParams(new LinearLayout.LayoutParams( (int)(height*(w/h)), height) );
				  }
				  else
				  {
					  sView.setLayoutParams(new LinearLayout.LayoutParams( width, (int)(width*(h/w)) ) );
				  }
				  
				  // API 10 ERROR
				  //parameters.setPreviewSize( height,width); // 设置预览图像大小
				  Log.i(ACTIVITY_TAG, LOG_PREFIX+"Modified width: " + width + " ,height: " + height);
				  Camera.Size optSize=getOptimalSize(supPreSize,height,width); //added
	              Log.i(ACTIVITY_TAG, LOG_PREFIX+"Set modified preview size to " + Integer.valueOf((int)optSize.width) + "x" + Integer.valueOf((int)optSize.height));
	              parameters.setPreviewSize((int)optSize.width,(int)optSize.height);
				  
	              Camera.Size previewSize = mcamera.getParameters().getPreviewSize();
	  		    Log.i(ACTIVITY_TAG, LOG_PREFIX+previewSize.width+" x "+previewSize.height);
	              
				  parameters.setPictureFormat(PixelFormat.JPEG); // 设置照片格式
				  
				  mcamera.setParameters(parameters);// 设置相机参数
				  
				  
				  mcamera.startPreview();// 开始预览
				  isPreview = true;
				
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
					if (isPreview)
						mcamera.stopPreview();
					mcamera.release();
					mcamera = null;
				}
			}		
		});
		// 设置该SurfaceView自己不维护缓冲    
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

    @Override 
    protected void onStart() {  
        Log.i(ACTIVITY_TAG, LOG_PREFIX+"　onStart called!");  
        super.onStart();  
    }  
 
    @Override 
    protected void onRestart() {  
        Log.i(ACTIVITY_TAG, LOG_PREFIX+"　onRestart called!");  
        super.onRestart();  
    }  
 
    @Override 
    protected void onResume() {  
        Log.i(ACTIVITY_TAG, LOG_PREFIX+"　onResume called!");
        //sensors.SensorsInit();
        super.onResume();  
    }  
 
    @Override 
    protected void onPause() {  
        Log.i(ACTIVITY_TAG, LOG_PREFIX+"　onPause called!");  
     
        //sensors.SensorsRelease();
        super.onPause();  
    }  
 
    @Override 
    protected void onStop() {  
        Log.i(ACTIVITY_TAG, LOG_PREFIX+"　onStop called!");  

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
    
	@SuppressLint("NewApi")
	private void initCamera()
	{
		if (!isPreview)
		{
			mcamera = Camera.open();
		}
		if (mcamera != null && !isPreview)
		{
			try
			{
				Camera.Parameters parameters = mcamera.getParameters();
				// 设置预览照片的大小
				
				// show parameters:
			    supPreSize = parameters.getSupportedPreviewSizes();
                for(int i=0;i<supPreSize.size();i++)
                	Log.i(ACTIVITY_TAG, LOG_PREFIX+"supported preview size "+(i+1)+": "+supPreSize.get(i).width+"x"+supPreSize.get(i).height);
				
                supPicSize = parameters.getSupportedPictureSizes();
                for(int i=0;i<supPicSize.size();i++)
                	Log.i(ACTIVITY_TAG, LOG_PREFIX+"supported picture size "+(i+1)+": "+supPicSize.get(i).width+"x"+supPicSize.get(i).height);
				
                supFps = parameters.getSupportedPreviewFrameRates();
                for(int i=0;i<supFps.size();i++)
                {
                	Log.i(ACTIVITY_TAG, LOG_PREFIX+"supported frame rate: "+(i+1)+": "+supFps.get(i).intValue());
                	
                }
				
                Camera.Size optSize=getOptimalSize(supPreSize,1280,720); //added
                Log.i(ACTIVITY_TAG, LOG_PREFIX+"Set preview size to " + Integer.valueOf((int)optSize.width) + "x" + Integer.valueOf((int)optSize.height));
                parameters.setPreviewSize((int)optSize.width,(int)optSize.height);
                optSize=getOptimalSize(supPicSize,2048,1152);
                parameters.setPictureSize((int)optSize.width,(int)optSize.height);
                Log.i(ACTIVITY_TAG, LOG_PREFIX+"setPic†ureSize() wid†h = "+(int)optSize.width+" ; hegih† = "+(int)optSize.height);
                //parameters.setPictureSize(1200, 1600);
				// end show parameters
                
				
                
                parameters.set("iso", 100);
                
				Camera.Size previewSize = mcamera.getParameters().getPreviewSize();
				// Log.i(ACTIVITY_TAG, LOG_PREFIX+previewSize.width+" x "+previewSize.height);
				
				// API 10 ERROR
				//parameters.setPreviewSize(AppConfig.IMGHEIGHT,AppConfig.IMGWIDTH);//(screenWidth, screenHeight);
				// 每秒显示15帧
				parameters.setPreviewFrameRate(Collections.max(supFps));
				Log.i(ACTIVITY_TAG, LOG_PREFIX+"final frame rate: "+Collections.max(supFps));
				
				// 设置图片格式
				parameters.setPictureFormat(PixelFormat.JPEG);
				// 设置JPG照片的质量
				parameters.set("jpeg-quality", 90);
				//设置照片的大小
				
				//parameters.set("orientation", "landscape");
				// API 10 ERROR BLACK SCREEN
				//parameters.setPictureSize(AppConfig.IMGHEIGHT,AppConfig.IMGWIDTH);//(previewSize.width,previewSize.height);//(screenWidth, screenHeight);
				//parameters.set("rotation", 90); // !!
				
				
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) 
				{
                    
					//set Metering area if supported
					if (parameters.getMaxNumMeteringAreas() > 0){ // check that metering areas are supported
						Log.i(ACTIVITY_TAG, LOG_PREFIX+"metering area supported");
						
					   List<Camera.Area> meteringAreas = new ArrayList<Camera.Area>();
	
					   Rect areaRect = new Rect(-200, -200, 200, 200);  // specify an area in upper right of image
	
					   meteringAreas.add(new Camera.Area(areaRect, 1000)); // set weight to 40%
	
					   parameters.setMeteringAreas(meteringAreas);
	
					}
					else
						Log.i(ACTIVITY_TAG, LOG_PREFIX+"metering area not supported");
					
					//set focus area if supported
					if (parameters.getMaxNumFocusAreas() > 0){ // check that metering areas are supported
						Log.i(ACTIVITY_TAG, LOG_PREFIX+"focus area supported");
						
						   List<Camera.Area> focusAreas = new ArrayList<Camera.Area>();
	
						   Rect areaRect = new Rect(-200, -200, 200, 200);  // specify an area in upper right of image
						   
						   focusAreas.add(new Camera.Area(areaRect, 1000)); // set weight to 40%
	
						   parameters.setFocusAreas(focusAreas);
						   
						  
	
						}
					else
						Log.i(ACTIVITY_TAG, LOG_PREFIX+"focus area not supported");
					
					
                } 
                else
                {
                	
                } 
				

				
				// now work for some device!
				//parameters.setRotation(90);
				List<String> FocusModes = parameters.getSupportedFocusModes();
                if (FocusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO))
                {
                	parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
                	Log.i(ACTIVITY_TAG, LOG_PREFIX+"FOCUS_MODE_CONTINUOUS_VIDEO");
                }
                else
                {
                	parameters.setFocusMode(parameters.FOCUS_MODE_AUTO);
                	Log.i(ACTIVITY_TAG, LOG_PREFIX+"FOCUS_MODE_AUTO");
                }
				
				parameters.setSceneMode(parameters.SCENE_MODE_AUTO);
				parameters.setWhiteBalance(parameters.WHITE_BALANCE_AUTO);
				
				
				// set parameters
				mcamera.setParameters(parameters);
				
				mcamera.setDisplayOrientation(90);
				
				//通过SurfaceView显示取景画面
				mcamera.setPreviewDisplay(surfaceHolder);
				// 开始预览
				mcamera.startPreview();
				
				mAutoFocusCallback = new Camera.AutoFocusCallback() {
					   
					   public void onAutoFocus(boolean success, Camera camera) {
					    // TODO Auto-generated method stub
//						   Toast.makeText(AQIClientActivity.this,
//							          "onAutoFocus" , Toast.LENGTH_LONG).show();
						   
					    if(success){
					    	camera.setOneShotPreviewCallback(null);
					    	
//					        Toast.makeText(AQIClientActivity.this,
//					          "自动聚焦成功" , Toast.LENGTH_LONG).show();
					    }
					     
					   }
					  }; 
				
				
				// 自动对焦
				mcamera.autoFocus(mAutoFocusCallback);
				
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
			//test:
			mcamera.stopPreview();
			mcamera.startPreview();
			
			isPreview = true;
		}
	}
	
	
	
	
	private Camera.Size getOptimalSize(List<Camera.Size> sizes, int w, int h) 
	{
		final double ASPECT_TOLERANCE = 0.05;
  	  	double targetRatio = (double) w / h;
  	  	if (sizes == null)
  	  	return null;

  	  	Camera.Size optimalSize = null;
  	  	double minDiff = Double.MAX_VALUE;
  	  
  	  	int targetHeight = h;

  	  	// Try to find an size match aspect ratio and size
  	  	for (Camera.Size size : sizes) 
  	  	{
  	  		double ratio = (double) size.width / size.height;
  	  		if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
  	  			continue;
  	  		if (Math.abs(size.height - targetHeight) < minDiff) 
  	  		{
  	  				optimalSize = size;
  	  				minDiff = Math.abs(size.height - targetHeight);
  	  		}
  	  	}

  	  // Cannot find the one match the aspect ratio, ignore the requirement
  	  	if (optimalSize == null) 
  	  	{
  	  		minDiff = Double.MAX_VALUE;
  	  		for (Camera.Size size : sizes) 
  	  		{
  	  			if (Math.abs(size.height - targetHeight) < minDiff) 
  	  			{
  	  				optimalSize = size;
  	  				minDiff = Math.abs(size.height - targetHeight);
  	  			}
  	  		}
  	  	}
  	 return optimalSize;
  	}
	
	public void getExifInfo(String picName) {
		
	}
	
	
	
	

	
	PictureCallback myjpegCallback = new PictureCallback()
	{
		@Override
		public void onPictureTaken(byte[] data, Camera camera)
		{
			
//			Camera.Size previewSize = camera.getParameters().getPreviewSize();
//		    Log.i(ACTIVITY_TAG, LOG_PREFIX+previewSize.width+" x "+previewSize.height);
			
			//sensors.updateOrientation();
			//sensors.SensorsRelease();
			
			//if(GPSState)
			//sensors.updateGPS();
			
			
			
			// 加载/layout/save.xml文件对应的布局资源
			saveView = getLayoutInflater().inflate(R.layout.save, null);
			
			photoInfoText = (TextView)saveView.findViewById(R.id.photo_name);

			
			
			photoInfoText.setText("......");
			
			// 根据拍照所得的数据创建位图
			final Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);
			
			//将照片旋转90度
			
			Matrix matrix = new Matrix();  
			matrix.preRotate(90);  
			nbm = Bitmap.createBitmap(bm ,0,0, bm .getWidth(), bm.getHeight(),matrix,true);
			
			
			
			
			
			//Toast.makeText(this, "拍照成功，照片保存在"+fileName+"文件中", Toast.LENGTH_SHORT).show();
			
			// 获取saveDialog对话框上的ImageView组件
			ImageView show = (ImageView) saveView.findViewById(R.id.show);
			// 显示刚刚拍得的照片
			show.setImageBitmap(nbm);
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
				    	
				        //Button saveButton = saveAlertDialog.getButton(android.content.DialogInterface.BUTTON_POSITIVE);
						//saveButton.setText("上传中...");
						//saveButton.setTextColor(Color.BLUE);
						//saveButton.invalidate(); 
					    
					    //handler=new Handler(); 
					    //handler.post(UploadingProcess); 
						
						mcamera.startPreview(); //do this function later

						Log.i(ACTIVITY_TAG, LOG_PREFIX+"saving..");
					}
					
				});
				saveAlertBuilder.setNegativeButton("保存", new OnClickListener()
				{
					public void onClick(DialogInterface dialog,
							int which)
					{
						// name the img in term of date.
						
						
						String fileName = Environment.getExternalStorageDirectory().toString()
								+File.separator
								+"SensorTest1"
								+File.separator
								+"ST_"
								+System.currentTimeMillis()
								+".jpg";
						File file = new File(fileName);
						if(!file.getParentFile().exists()){
							file.getParentFile().mkdirs();
						}
						Log.i(ACTIVITY_TAG,LOG_PREFIX+"new bos!");
						BufferedOutputStream bos;
						try {
							bos = new BufferedOutputStream(new FileOutputStream(file));
							nbm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
							bos.flush();
							Log.i(ACTIVITY_TAG,LOG_PREFIX+"bos.flush()");
							bos.close();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						//**************Exif设置*************
						
						//**************Exif设置***END*******
						
						FileModel fileModel = new FileModel(fileName, collect.getDate());
						fileModel.save(app);
						
						mcamera.startPreview();
					
					}
				});
				
				saveAlertDialog = saveAlertBuilder.create();
				saveAlertDialog.show();
			
			//重新浏览
			
			camera.stopPreview();
			
			isPreview = false;
		}
	};
	
}
