package org.pm4j.task;

import java.util.ArrayList;
import java.util.List;

import org.pm4j.data.PhotoData;
import org.pm4j.engine.ModelParams;
import org.pm4j.process.ModelProcess;
import org.pm4j.process.PMProcess;

import com.bupt.bnrc.thesenser.model.FileModel;

import android.os.Handler;
import android.util.Log;

public class ModelingTask extends PMTask {

	static {
		TAG = "ModelingTask";
		
	}

	private List<PhotoData> photoData;

	
	
	
	
	
	
	public ModelingTask(List<FileModel> fileModelList, Handler taskHandler,
			boolean enablePreprocess) {
		this.taskHandler = taskHandler;

		this.photoData = new ArrayList<PhotoData>();
		for (int i = 0; i < fileModelList.size(); i++) {
			FileModel file = fileModelList.get(i);
			PhotoData data = new PhotoData(file.getFileName(),
					file.getCreateTime(), file.getPhotoStats().getLatitude(),
					file.getPhotoStats().getLongitude(), file.getTag());
			// to be modified here!
			// data.setSize(PMConfig.defaultImgWidth,
			// PMConfig.defaultImgHeight);\
			data.setSize(file.getPhotoStats().getWidth(), file.getPhotoStats()
					.getHeight());

			this.photoData.add(data);
		}
	
		int tagId = photoData.get(0).getTagId();
		
		this.modelParams = PMConfig.getDefaultModelParams();
		this.modelParams.setImgWidth(photoData.get(0).getWidth());
		this.modelParams.setImgHeight(photoData.get(0).getHeight());
		
		this.modelParams.setModelPath(PMConfig.defaultModelDirPath + PMConfig.defaultModelBaseName + tagId + ".yml");
		Log.i(TAG, "setModelPath: " + PMConfig.defaultModelDirPath + PMConfig.defaultModelBaseName + tagId + ".yml");
		
		
		if (enablePreprocess) 
		{
			allProgress = ModelProcess.progressStep + PreprocessingProcess.progressStep;
			createHandler();
			this.preprocessingProcess = new PreprocessingProcess(photoData,
					handler, true);
			
		} 
		else 
		{
			allProgress = ModelProcess.progressStep;
			createHandler();
			this.preprocessingProcess = null;
			
		}
		
		
	}

	/*
	public ModelingTask(List<PhotoData> photoListArg, ModelParams modelParamsArg, Handler taskHandlerArg, boolean enablePreprocess) 
	{
		this.taskHandler = taskHandlerArg;
		this.modelParams = modelParamsArg;
		this.photoData = photoListArg;

		
		Log.i(TAG, TAG + "ModelingTask allProgress: " + allProgress);

		if (enablePreprocess) 
		{
			allProgress = ModelProcess.progressStep + PreprocessingProcess.progressStep;
			createHandler();
			this.preprocessingProcess = new PreprocessingProcess(photoData,
					handler, true);
			
		} 
		else 
		{
			allProgress = ModelProcess.progressStep;
			createHandler();
			this.preprocessingProcess = null;
			
		}
		
		
	}
*/
	@Override
	protected PMProcess createPMProcess(Handler handler) {
		if (preprocessingProcess != null)
			return new ModelProcess(handler, modelParams, preprocessingProcess.getPhotoData());
		else
			return new ModelProcess(handler, modelParams, photoData);
	}

}
