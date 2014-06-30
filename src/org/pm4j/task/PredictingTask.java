package org.pm4j.task;

import java.util.ArrayList;
import java.util.List;

import org.pm4j.data.PhotoData;
import org.pm4j.engine.ModelParams;
import org.pm4j.process.PMProcess;
import org.pm4j.process.PredictProcess;

import android.os.Handler;

import com.bupt.bnrc.thesenser.model.FileModel;

public class PredictingTask extends PMTask {

	static {
		TAG = "PredictingTask";
	}

	private PhotoData photoData;

	public PredictingTask(FileModel fileModel, Handler taskHandler,
			boolean enablePreprocess) {
		this.taskHandler = taskHandler;

		this.photoData = new PhotoData(fileModel.getFileName(),
				fileModel.getCreateTime(), fileModel.getPhotoStats()
						.getLatitude(), fileModel.getPhotoStats()
						.getLongitude(), fileModel.getTag());
		// to be modified here!
		// photoData.setSize(PMConfig.defaultImgWidth,
		// PMConfig.defaultImgHeight);
		photoData.setSize(fileModel.getPhotoStats().getWidth(), fileModel
				.getPhotoStats().getHeight());

		this.modelParams = PMConfig.getDefaultModelParams();
		this.modelParams.setImgWidth(photoData.getWidth());
		this.modelParams.setImgHeight(photoData.getHeight());

		// to-do set modelname by tag HERE

		createHandler();

		List<PhotoData> photoList = new ArrayList<PhotoData>();
		photoList.add(photoData);

		taskStep = PredictProcess.progressStep;

		if (enablePreprocess) {
			taskStep += PreprocessingProcess.progressStep;
			this.preprocessingProcess = new PreprocessingProcess(photoList,
					handler, false);
		} else {
			this.preprocessingProcess = null;
		}
	}

	public PredictingTask(PhotoData photoDataArg, ModelParams modelParamsArg,
			Handler taskHandlerArg, boolean enablePreprocess) {
		this.taskHandler = taskHandlerArg;
		this.modelParams = modelParamsArg;
		this.photoData = photoDataArg;

		createHandler();

		List<PhotoData> photoList = new ArrayList<PhotoData>();
		photoList.add(photoData);

		taskStep = PredictProcess.progressStep;

		if (enablePreprocess) {
			taskStep += PreprocessingProcess.progressStep;
			this.preprocessingProcess = new PreprocessingProcess(photoList,
					handler, false);
		} else {
			this.preprocessingProcess = null;
		}

	}

	@Override
	protected PMProcess createPMProcess(Handler handler) {
		if (preprocessingProcess != null)
			return new PredictProcess(handler, modelParams,
					preprocessingProcess.getPhotoData().get(0).getName());
		else
			return new PredictProcess(handler, modelParams, photoData.getName());
	}

}
