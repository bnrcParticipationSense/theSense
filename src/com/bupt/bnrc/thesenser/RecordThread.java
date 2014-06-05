package com.bupt.bnrc.thesenser;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import android.util.Log;

public class RecordThread {
	private AudioRecord ar;
    private int bs;
    private static int SAMPLE_RATE_IN_HZ = 8000;
    private boolean isRun = false;
 
    public RecordThread() {
//        super();
    	bs = AudioRecord.getMinBufferSize(SAMPLE_RATE_IN_HZ,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT);
        ar = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE_IN_HZ,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT, bs);
    }
 
    public float run() {
//        super.run();
        ar.startRecording();
    	 
    	int v = ar.getState();
    	int j = ar.getRecordingState();
                // ”√”⁄∂¡»°µƒ buffer
        byte[] buffer = new byte[bs];
        isRun = true;

//        while(this.isRun) {
            int r = ar.read(buffer, 0, bs);
            int w = 0;
                        // Ω´ buffer ƒ⁄»›»°≥ˆ£¨Ω¯––∆Ω∑Ω∫Õ‘ÀÀ„
            for (int i = 0; i < buffer.length; i++) {
                // ’‚¿Ô√ª”–◊ˆ‘ÀÀ„µƒ”≈ªØ£¨Œ™¡À∏¸º”«ÂŒ˙µƒ’π æ¥˙¬Î
                w += buffer[i] * buffer[i];
            }
            
            Log.d("spll", String.valueOf(w / (float) r));
//        }
        Log.d("spl", String.valueOf(v));
		Log.d("sp", String.valueOf(j));
		return w / (float) r;
    }
    public void stop() {
    	ar.stop();
    	int i = ar.getRecordingState();
    	Log.d("sp", String.valueOf(i));
    }
 
    public void pause() {
                // ‘⁄µ˜”√±æœﬂ≥Ãµƒ Activity µƒ onPause ¿Ôµ˜”√£¨“‘±„ Activity ‘›Õ£ ± Õ∑≈¬ÛøÀ∑Á
        isRun = false;
    }
 
    public void start() {
                // ‘⁄µ˜”√±æœﬂ≥Ãµƒ Activity µƒ onResume ¿Ôµ˜”√£¨“‘±„ Activity ª÷∏¥∫ÛºÃ–¯ªÒ»°¬ÛøÀ∑Á ‰»Î“Ù¡ø
        if (!isRun) {
//            super.start();
        	isRun = true;
        }
    }

}
