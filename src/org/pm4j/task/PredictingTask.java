package org.pm4j.task;

import java.util.ArrayList;
import java.util.List;

import org.pm4j.data.PhotoData;
import org.pm4j.engine.ModelParams;
import org.pm4j.process.PMProcess;
import org.pm4j.process.PredictProcess;

import com.bupt.bnrc.thesenser.model.FileModel;

import android.os.Handler;
import android.util.Log;


public class PredictingTask extends PMTask {

	static {
		TAG = "PredictingTask";
	}

	private PhotoData photoData; 
	
	
	
	public PredictingTask(FileModel fileModel, Handler taskHandler, boolean enablePreprocess)
	{
		
		int tagId = fileModel.getTag();
		this.taskHandler = taskHandler;
		
		this.photoData	 = new PhotoData(fileModel.getFileName(), fileModel.getCreateTime(), fileModel.getPhotoStats().getLatitude(), fileModel.getPhotoStats().getLongitude(), fileModel.getTag());
		// to be modified here!
		//photoData.setSize(PMConfig.defaultImgWidth, PMConfig.defaultImgHeight);
		photoData.setSize(fileModel.getPhotoStats().getWidth(),fileModel.getPhotoStats().getHeight());

		this.modelParams = PMConfig.getDefaultModelParams();
		this.modelParams.setImgWidth(photoData.getWidth());
		this.modelParams.setImgHeight(photoData.getHeight());
		this.modelParams.setModelPath(PMConfig.defaultModelDirPath + PMConfig.defaultModelBaseName + tagId + ".yml");
		Log.i(TAG, "setModelPath: " + PMConfig.defaultModelDirPath + PMConfig.defaultModelBaseName + tagId + ".yml");

		
		List<PhotoData> photoList = new ArrayList<PhotoData>();
    	photoList.add(photoData);
    	
    	
    	if (enablePreprocess) 
		{
			allProgress = PredictProcess.progressStep + PreprocessingProcess.progressStep;
			createHandler();
			this.preprocessingProcess = new PreprocessingProcess(photoList,
					handler, false);
		}
		else 
		{
			allProgress = PredictProcess.progressStep;
			createHandler();
			this.preprocessingProcess = null;
		}
		
	}
	

	/*
	public PredictingTask(PhotoData photoDataArg, ModelParams modelParamsArg, Handler taskHandlerArg, boolean enablePreprocess)
	{

		this.taskHandler = taskHandlerArg;
		this.modelParams = modelParamsArg;
		this.photoData = photoDataArg;

		allProgress = PredictProcess.progressStep + PreprocessingProcess.progressStep;
		
		createHandler();

		List<PhotoData> photoList = new ArrayList<PhotoData>();
		photoList.add(photoData);


		if (enablePreprocess) {
			//taskStep += PreprocessingProcess.progressStep;
			this.preprocessingProcess = new PreprocessingProcess(photoList,
					handler, false);
		} else {
			this.preprocessingProcess = null;
		}
		
	}
	*/
	
	@Override
	protected PMProcess createPMProcess(Handler handler) {
		if (preprocessingProcess != null)
		{
			return new PredictProcess(handler, modelParams,preprocessingProcess.getPhotoData().get(0).getName());
		}
		else
		{
			return new PredictProcess(handler, modelParams, photoData.getName());
		}
	}

}
