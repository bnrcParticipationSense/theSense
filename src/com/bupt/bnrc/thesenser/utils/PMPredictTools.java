package com.bupt.bnrc.thesenser.utils;

import java.util.ArrayList;

import org.pm4j.process.PMTaskStatus;
import org.pm4j.settings.PMSettings;
import org.pm4j.task.PredictingTask;

import android.os.Handler;
import android.os.Message;
import com.bupt.bnrc.thesenser.interfaces.PMPredictObserver;
import com.bupt.bnrc.thesenser.interfaces.PMPredictSubject;
import com.bupt.bnrc.thesenser.model.FileModel;

public class PMPredictTools implements PMPredictSubject {
	private ArrayList<PMPredictObserver> observers;
	private int mState = CommonDefinition.PMPREDICT_STATE_NONE;
	private int mPMNum = 0;
	
	private static PMPredictTools instance = null;
	public static PMPredictTools getInstance() {
		if(instance == null)
			instance = new PMPredictTools();
		return instance;
	}
	
	private PMPredictTools() {
		observers = new ArrayList<PMPredictObserver>();
	}
	
	public int getState() {
		return mState;
	}
	
	public int getPMNum() {
		return mPMNum;
	}
	
	public void startPredict(FileModel fileModel) {
		/*
		 * // Synchronized way: ModelingTask modelingTask = new
		 * ModelingTask(photoList, AppConfig.MODELPARAMS, false);
		 * modelingTask.runSync();
		 */

		// Asynchronized way:
		Handler predictHandler = new Handler() {
			public void handleMessage(Message msg) { // 接收message对象
				PMTaskStatus revTaskStatus = (PMTaskStatus) msg.obj;

				switch (revTaskStatus.getStatusCode()) {
				case PMSettings.TASKRESULT_OK:

					// DO SOMETHING HERE TO UPDATE THE PROGRESS
					// USING int PMTaskStatus.getProgress()
					break;
				case PMSettings.TASKRESULT_FAILURE:
					mState = CommonDefinition.PMPREDICT_STATE_FAIL;
					notifyObservers();
					break;
				case PMSettings.TASKRESULT_INVALID:
					// DO SOMETHING HERE TO HANDLE THE INVALID DATA
					break;

				}

				if (revTaskStatus.isCompleted()) {
					mState = CommonDefinition.PMPREDICT_STATE_SUCCESS;
					mPMNum = revTaskStatus.getPmValue();
					notifyObservers();
				}

				super.handleMessage(msg);
			}
		};
		PredictingTask predictingTask = new PredictingTask(fileModel, predictHandler, false);
		predictingTask.runAsync();
		mState = CommonDefinition.PMPREDICT_STATE_START;
		notifyObservers();
	}

	@Override
	public void registerObserver(PMPredictObserver o) {
		// TODO Auto-generated method stub
		observers.add(o);
	}

	@Override
	public void removeObserver(PMPredictObserver o) {
		// TODO Auto-generated method stub
		int i = observers.indexOf(o);
		if(i>=0)
			observers.remove(i);
	}

	@Override
	public void notifyObservers() {
		// TODO Auto-generated method stub
		for(int i=0; i < observers.size();i++) {
			PMPredictObserver observer = (PMPredictObserver)observers.get(i);
			observer.update();
		}
	}

}
