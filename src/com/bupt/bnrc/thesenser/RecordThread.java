package com.bupt.bnrc.thesenser;

import java.io.IOException;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import android.util.Log;

public class RecordThread {
 
    /*
     * ********************************************************************************************************
     */
    private MediaRecorder mRecorder = null;
    private String fileName = "/storage/sdcard0/SensorTest1/a.mp3";
    
    static private RecordThread instance = null;
    static public RecordThread getRecordThread() {
    	if(instance == null)
    		instance = new RecordThread();
    	return instance;
    }
    private RecordThread() {
    	start();
    }
    
    public void start() {
        if (mRecorder == null) {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile(fileName);
            try {
				mRecorder.prepare();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            mRecorder.start();
        }
    }
    
    public void stop() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
    }
    
    private int BASE = 1;
    private double getAmplitude() {
        if (mRecorder != null)
        {
        	int amp = mRecorder.getMaxAmplitude();
        	Log.i("RecordThread", "getAmplitude = "+amp);
            return  (amp/BASE);
        }
        else
        {
        	Log.i("RecordThread", "getAmplitude = null");
            return 0;
        }
        
    }
    public int getValue() {
    	int value = 0;
    	//start();
        double amp = getAmplitude();
        //mEMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * mEMA;
        if (amp > 1)
			value = (int) (10 * Math.log10(amp));
        //stop();
        Log.i("RecordThread", "Value = "+value);
        return value;
    }

}
