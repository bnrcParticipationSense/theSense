package org.pm4j.task;

import java.util.ArrayList;
import java.util.List;

import org.pm4j.data.PhotoData;
import org.pm4j.engine.ModelParams;
import org.pm4j.process.ModelProcess;
import org.pm4j.process.PMProcess;
import org.pm4j.process.PMTaskStatus;
import org.pm4j.process.PredictProcess;
import org.pm4j.settings.PMSettings;

import com.bupt.bnrc.thesenser.model.FileModel;


import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ModelingTask extends PMTask{

	static
	{
		TAG = "ModelingTask";
	}
	

	private List<PhotoData> photoData;
	
	
	public ModelingTask(List<FileModel> fileModelList, Handler taskHandler, boolean enablePreprocess)
	{
		this.taskHandler = taskHandler;
		
		this.photoData = new ArrayList<PhotoData>();
		for(int i = 0; i < fileModelList.size(); i++)
		{
			FileModel file = fileModelList.get(i);
			PhotoData data = new PhotoData(file.getFileName(), file.getCreateTime(), file.getPhotoStats().getLatitude(), file.getPhotoStats().getLongitude(), file.getTag());
			// to be modified here!
			//data.setSize(PMConfig.defaultImgWidth, PMConfig.defaultImgHeight);\
			data.setSize(file.getPhotoStats().getWidth(),file.getPhotoStats().getHeight());
			
			this.photoData.add(data);
		}
		
		this.modelParams = PMConfig.getDefaultModelParams();
		this.modelParams.setImgWidth(photoData.get(0).getWidth());
		this.modelParams.setImgHeight(photoData.get(0).getHeight());
		
		createHandler();
		
		taskStep = ModelProcess.progressStep;
		
		if(enablePreprocess)
		{
			taskStep += PreprocessingProcess.progressStep;
			this.preprocessingProcess = new PreprocessingProcess(photoData, handler, true);
		}
		else
		{
			this.preprocessingProcess = null;
		}
	}
	
	
	public ModelingTask(List<PhotoData> photoListArg, ModelParams modelParamsArg, Handler taskHandlerArg, boolean enablePreprocess)
	{
		this.taskHandler = taskHandlerArg;
		this.modelParams = modelParamsArg;
		this.photoData = photoListArg;
		
		taskStep = ModelProcess.progressStep;
		
		createHandler();
		
		if(enablePreprocess)
		{
			taskStep += PreprocessingProcess.progressStep;
			this.preprocessingProcess = new PreprocessingProcess(photoData, handler, true);
		}
		else
		{
			this.preprocessingProcess = null;
		}
	}
	
	
	
	@Override
	protected PMProcess createPMProcess(Handler handler) 
	{
		if(preprocessingProcess != null)
			return new ModelProcess(handler, modelParams, preprocessingProcess.getPhotoData());
		else
			return new ModelProcess(handler, modelParams, photoData);
	}
	

	
}
