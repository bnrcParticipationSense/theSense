package org.pm4j.task;

import java.util.List;

import org.pm4j.data.PhotoData;
import org.pm4j.engine.ModelParams;
import org.pm4j.process.ModelProcess;
import org.pm4j.process.PMProcess;
import org.pm4j.process.PMTaskStatus;
import org.pm4j.process.PredictProcess;
import org.pm4j.settings.PMSettings;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public abstract class PMTask {

	public static String TAG = "PMTask";
	protected boolean isAsync;
	protected PreprocessingProcess preprocessingProcess;

	public int taskStep;

	protected PMProcess pmProcess;
	protected ModelParams modelParams;
	protected Handler taskHandler;
	protected Handler handler;
	
	public PMTask()
	{
		
	}
	
	protected void createHandler()
	{
		if(taskHandler == null)
		{
			handler = null;
			return;
		}
		
		this.handler = new Handler()
    	{
    		public void handleMessage(Message msg) 
    		{	//接收message对象  
    			Message taskMsg = null;
    			PMTaskStatus revTaskStatus = (PMTaskStatus)msg.obj;
    			int step = 0;
    			
    			switch (revTaskStatus.getTaskId()) 
    			{  
    				case PMSettings.TASK_PREPROCESS:   
    					Log.i(TAG, TAG + " Handler: " + revTaskStatus.getInfo());
    					
    					boolean isCompleted = revTaskStatus.isCompleted();
    					revTaskStatus.setAllSteps(taskStep);
    					taskMsg = taskHandler.obtainMessage(0, 0, 0,revTaskStatus);
    					taskHandler.sendMessage(taskMsg);

    					if(isAsync && isCompleted)
    					{
    						Log.i(TAG, TAG +" Handler: preprocessing ok! start " + TAG + "...");
    						pmProcess = createPMProcess(this);
    						pmProcess.runAsync();
    					}
    					
            		break;
		
    				case PMSettings.TASK_MODELING:   
                		Log.i(TAG, TAG +" Handler: " + revTaskStatus.getInfo());

                		if(preprocessingProcess == null)
                			step = revTaskStatus.getStep();
                		else
                			step = revTaskStatus.getStep() + PreprocessingProcess.progressStep;
                		
                		revTaskStatus.setAllSteps(taskStep);	
                		revTaskStatus.setStep(step);
                		taskMsg = taskHandler.obtainMessage(0, 0, 0,revTaskStatus);
        				taskHandler.sendMessage(taskMsg); 
					break;
					
                	case PMSettings.TASK_PREDICTING:   
                		Log.i(TAG, TAG +" Handler: " + revTaskStatus.getInfo());
                		
                		if(preprocessingProcess == null)
                			step = revTaskStatus.getStep();
                		else
                			step = revTaskStatus.getStep() + PreprocessingProcess.progressStep;
                		
                		revTaskStatus.setAllSteps(taskStep);	
                		revTaskStatus.setStep(step);
                		taskMsg = taskHandler.obtainMessage(0, 0, 0,revTaskStatus);
        				taskHandler.sendMessage(taskMsg); 
					break;

            	}
    			
    			super.handleMessage(msg);  
        	} 
    	};

		

	}
	
	
	public PMTaskStatus runSync()
	{
		isAsync = false;
		if(preprocessingProcess != null)
		{
			PMTaskStatus taskStatus = preprocessingProcess.runSync();

			if(taskStatus.isCompleted() && preprocessingProcess.isValidData())
			{
				Log.i(TAG, TAG +": runSync():  preprocessingProcess completed and result is valid! start to sync run " + TAG);
				pmProcess = createPMProcess(handler);
				return pmProcess.runSync();
			}
			else
			{
				return taskStatus;
			}
		}
		else
		{
			pmProcess = createPMProcess(handler);
			return pmProcess.runSync();
		}

	}
	
	public void start()
	{
		isAsync = true;
		if(preprocessingProcess != null)
		{
			preprocessingProcess.start();
		}
		else
		{
			pmProcess = createPMProcess(handler);
			pmProcess.runAsync();
		}
			
	}
	
	public void runAsync()
	{
		start();
	}
	
	
	protected abstract PMProcess createPMProcess(Handler handler);

	public int getTaskStep() {
		return taskStep;
	}

	public void setTaskStep(int taskStep) {
		this.taskStep = taskStep;
	}
}
