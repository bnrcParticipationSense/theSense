package com.bupt.bnrc.thesenser;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import android.annotation.SuppressLint;
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

public class CameraActivity extends BaseActivity {

	
	private static final String ACTIVITY_TAG="CameraActivity";
	private static final String LOG_PREFIX="Camera: ";
	
	ImageButton photoBut;
	SurfaceView sView;
	SurfaceHolder surfaceHolder;
	int screenWidth, screenHeight;
	
	Handler handler;
	Bitmap nbm;
	
	// ����ϵͳ���õ������
	Camera mcamera;
	String imgName;
	TextView infoText;
	TextView photoInfoText;
	View saveView;
	AlertDialog.Builder saveAlertBuilder;
	AlertDialog saveAlertDialog;
	//�Ƿ��������
	boolean isPreview = false;

	private Camera.AutoFocusCallback mAutoFocusCallback;
	List<android.hardware.Camera.Size> supPreSize;
	List<android.hardware.Camera.Size> supPicSize;
	List<Integer> supFps;

	
	
	
	java.util.Date date;
	
	boolean GPSState;
	
	
    @Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
		
		
		//�趨camera���� (�̶�)
		//sensors.camera_rx  = AppConfig.IMGWIDTH;
		//sensors.camera_ry  = AppConfig.IMGHEIGHT;
		
		

//		Toast.makeText(getApplicationContext(),
//		          "IMEI: "+sensors.user_imei + "\nConnection: " + connStr, Toast.LENGTH_LONG).show();
				
		//���ú�������
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		// ����ȫ��
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
		    	 
		    	 
		    	 
		    	// ����
				mcamera.takePicture(null, null , myjpegCallback);
		    }
		});
		
		
		
		
		
		
//		WindowManager wm = (WindowManager) getSystemService(
//			Context.WINDOW_SERVICE);
//		Display display = wm.getDefaultDisplay();
		// ��ȡ��Ļ�Ŀ�͸�
	//	screenWidth = display.getWidth();
		//screenHeight = display.getHeight();
		// ��ȡ������SurfaceView���
		
		
		// ���SurfaceView��SurfaceHolder
		surfaceHolder = sView.getHolder();
		
		//surfaceHolder.setFixedSize(960, 720); 
		
		// ΪsurfaceHolder���һ���ص�������
		surfaceHolder.addCallback(new Callback()
		{
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height)
			{
				mcamera.stopPreview();
				isPreview = false;
				
				Log.i(ACTIVITY_TAG, LOG_PREFIX+"surfaceChanged");
					
				  Camera.Parameters parameters = mcamera.getParameters();// ����������
				  Size s = parameters.getPictureSize();
				  double h = s.width;
				  double w = s.height;

			        
				  if( width>height )
				  {
					  sView.setLayoutParams(new LinearLayout.LayoutParams( (int)(height*(w/h)), height) );
				  }
				  else
				  {
					  sView.setLayoutParams(new LinearLayout.LayoutParams( width, (int)(width*(h/w)) ) );
				  }
				  
				  // API 10 ERROR
				  //parameters.setPreviewSize( height,width); // ����Ԥ��ͼ���С
				  Log.i(ACTIVITY_TAG, LOG_PREFIX+"Modified width: " + width + " ,height: " + height);
				  Camera.Size optSize=getOptimalSize(supPreSize,height,width); //added
	              Log.i(ACTIVITY_TAG, LOG_PREFIX+"Set modified preview size to " + Integer.valueOf((int)optSize.width) + "x" + Integer.valueOf((int)optSize.height));
	              parameters.setPreviewSize((int)optSize.width,(int)optSize.height);
				  
	              Camera.Size previewSize = mcamera.getParameters().getPreviewSize();
	  		    Log.i(ACTIVITY_TAG, LOG_PREFIX+previewSize.width+" x "+previewSize.height);
	              
				  parameters.setPictureFormat(PixelFormat.JPEG); // ������Ƭ��ʽ
				  
				  mcamera.setParameters(parameters);// �����������
				  
				  
				  mcamera.startPreview();// ��ʼԤ��
				  isPreview = true;
				
			}
			@Override
			public void surfaceCreated(SurfaceHolder holder)
			{
				// ������ͷ
				initCamera();
			}
			@Override
			public void surfaceDestroyed(SurfaceHolder holder)
			{
				// ���camera��Ϊnull ,�ͷ�����ͷ
				if (mcamera != null)
				{
					if (isPreview)
						mcamera.stopPreview();
					mcamera.release();
					mcamera = null;
				}
			}		
		});
		// ���ø�SurfaceView�Լ���ά������    
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

    @Override 
    protected void onStart() {  
        Log.i(ACTIVITY_TAG, LOG_PREFIX+"��onStart called!");  
        super.onStart();  
    }  
 
    @Override 
    protected void onRestart() {  
        Log.i(ACTIVITY_TAG, LOG_PREFIX+"��onRestart called!");  
        super.onRestart();  
    }  
 
    @Override 
    protected void onResume() {  
        Log.i(ACTIVITY_TAG, LOG_PREFIX+"��onResume called!");
        //sensors.SensorsInit();
        super.onResume();  
    }  
 
    @Override 
    protected void onPause() {  
        Log.i(ACTIVITY_TAG, LOG_PREFIX+"��onPause called!");  
     
        //sensors.SensorsRelease();
        super.onPause();  
    }  
 
    @Override 
    protected void onStop() {  
        Log.i(ACTIVITY_TAG, LOG_PREFIX+"��onStop called!");  

        super.finish();
        //�˳���̨�߳�,�Լ����پ�̬����
        System.exit(0);
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
				// ����Ԥ����Ƭ�Ĵ�С
				
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
                //parameters.setPreviewSize((int)optSize.width,(int)optSize.height);
                parameters.setPictureSize((int)optSize.width,(int)optSize.height);
				// end show parameters
                
				
                
                parameters.set("iso", 100);
                
				Camera.Size previewSize = mcamera.getParameters().getPreviewSize();
				// Log.i(ACTIVITY_TAG, LOG_PREFIX+previewSize.width+" x "+previewSize.height);
				
				// API 10 ERROR
				//parameters.setPreviewSize(AppConfig.IMGHEIGHT,AppConfig.IMGWIDTH);//(screenWidth, screenHeight);
				// ÿ����ʾ15֡
				parameters.setPreviewFrameRate(Collections.max(supFps));
				Log.i(ACTIVITY_TAG, LOG_PREFIX+"final frame rate: "+Collections.max(supFps));
				
				// ����ͼƬ��ʽ
				parameters.setPictureFormat(PixelFormat.JPEG);
				// ����JPG��Ƭ������
				parameters.set("jpeg-quality", 90);
				//������Ƭ�Ĵ�С
				
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
				
				//ͨ��SurfaceView��ʾȡ������
				mcamera.setPreviewDisplay(surfaceHolder);
				// ��ʼԤ��
				mcamera.startPreview();
				
				mAutoFocusCallback = new Camera.AutoFocusCallback() {
					   
					   public void onAutoFocus(boolean success, Camera camera) {
					    // TODO Auto-generated method stub
//						   Toast.makeText(AQIClientActivity.this,
//							          "onAutoFocus" , Toast.LENGTH_LONG).show();
						   
					    if(success){
					    	camera.setOneShotPreviewCallback(null);
					    	
//					        Toast.makeText(AQIClientActivity.this,
//					          "�Զ��۽��ɹ�" , Toast.LENGTH_LONG).show();
					    }
					     
					   }
					  }; 
				
				
				// �Զ��Խ�
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
			
			
			
			// ����/layout/save.xml�ļ���Ӧ�Ĳ�����Դ
			saveView = getLayoutInflater().inflate(R.layout.save, null);
			
			photoInfoText = (TextView)saveView.findViewById(R.id.photo_name);

			
			
			photoInfoText.setText("......");
			
			// �����������õ����ݴ���λͼ
			final Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);
			
			//����Ƭ��ת90��
			
			Matrix matrix = new Matrix();  
			matrix.preRotate(90);  
			nbm = Bitmap.createBitmap(bm ,0,0, bm .getWidth(), bm.getHeight(),matrix,true);
			
			
			
			
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
			
			//Toast.makeText(this, "���ճɹ�����Ƭ������"+fileName+"�ļ���", Toast.LENGTH_SHORT).show();
			
			// ��ȡsaveDialog�Ի����ϵ�ImageView���
			ImageView show = (ImageView) saveView.findViewById(R.id.show);
			// ��ʾ�ո��ĵõ���Ƭ
			show.setImageBitmap(nbm);
			//ʹ�öԻ�����ʾsaveDialog���
			saveAlertBuilder = new AlertDialog.Builder(CameraActivity.this);
			saveAlertBuilder.setView(saveView);
			saveAlertBuilder.setPositiveButton("����", new OnClickListener()
				{

					@Override
					public void onClick(DialogInterface dialog,
						int which)
					{
			            //���½���  
				    	
				        //Button saveButton = saveAlertDialog.getButton(android.content.DialogInterface.BUTTON_POSITIVE);
						//saveButton.setText("�ϴ���...");
						//saveButton.setTextColor(Color.BLUE);
						//saveButton.invalidate(); 
					    
					    //handler=new Handler(); 
					    //handler.post(UploadingProcess); 

						Log.i(ACTIVITY_TAG, LOG_PREFIX+"saving..");
					}
					
				});
				saveAlertBuilder.setNegativeButton("ȡ��", new OnClickListener()
				{
					public void onClick(DialogInterface dialog,
							int which)
					{
						mcamera.startPreview();
					
					}
				});
				
				saveAlertDialog = saveAlertBuilder.create();
				saveAlertDialog.show();
			
			//�������
			
			camera.stopPreview();
			
			isPreview = false;
		}
	};
	
	
	
	
	/*Runnable   UploadingProcess=new  Runnable()
	{  
      public void run() 
      {  
    	  Log.i(ACTIVITY_TAG, LOG_PREFIX+"UploadingProcess..");
			
    	  if(IOUtil.checkSD() && IOUtil.checkAppDir())
			{
				
				if(IOUtil.writeImg(nbm, imgName))
				{
					
					DataManager dm = new DataManager(sensors,IOUtil.getImgFile(imgName),date);
					
					if(conn.checkConn() != ConnUtil.connState.NO_CONN)
					{
						
					JSONObject receiveJson = conn.uploadJson(dm.json);
						if(receiveJson == null)
						{
							Toast.makeText(getApplicationContext(),
							          "uploading error!" , Toast.LENGTH_LONG).show();
						}	
						else  // upload success
						{
							String ret = null;
							try {
								ret = receiveJson.getString("msg");
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							Toast.makeText(getApplicationContext(),
									ret, Toast.LENGTH_LONG).show();
							// TO-DO 
						}
					
					}
					else
					{
						Toast.makeText(getApplicationContext(),
						          "upload error, check connection!" , Toast.LENGTH_LONG).show();
					}
	
				}
				else
				{
					Toast.makeText(getApplicationContext(),
					          "save error!" , Toast.LENGTH_LONG).show();
				}
			}

			mcamera.startPreview();
			sensors.OSensorStart();
      }
			
        
          
    };
    */
}
