package com.redballoonsoftware.stopwatch;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

/**
 * Created by Borham on 11/24/14.
 */
public class StopwatchAudio {

    private MediaPlayer stopwatchPlayer;
    private static final String TAG = "StopwatchAudio";

    public void stop(){
        if (stopwatchPlayer != null){
            stopwatchPlayer.release();
            stopwatchPlayer = null;
        }
    }

    public void playBeep(Context c, int wavID){
        Log.d(TAG,"playBeep");

        stop();

        stopwatchPlayer = MediaPlayer.create(c, wavID);

        stopwatchPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Log.d(TAG,"onCompletion of stopwatchPlayer");
                stop();
            }
        });

        stopwatchPlayer.start();
        Log.d(TAG, "stopwatchPlayer started");
    }

}
