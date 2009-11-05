package com.redballoonsoftware.stopwatch;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Stopwatch extends Activity {
	private static String TAG = "StopwatchActivity";
	private com.redballoonsoftware.widgets.Stopwatch mStopwatch;
	
	private Button mStartStop;
	private Button mResetLap; 
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.d(TAG, "Setting View");
        
        setContentView(R.layout.stopwatch);
        
        Log.d(TAG, "Setting members");
                
        mStopwatch = (com.redballoonsoftware.widgets.Stopwatch)findViewById(R.id.Stopwatch01);
        mStartStop = (Button)findViewById(R.id.StartStopButton);
        mResetLap = (Button)findViewById(R.id.ResetLapButton);
    }
    
    
    public void onStartStopButtonClick(View v) {
    	Log.d(TAG, "onStartStopButtonClick called");
    	if (mStopwatch.isPaused()) {
    	    mStartStop.setText("Stop");
    	    mResetLap.setText("Lap");
    	    mStopwatch.start();
	    } else {
	        mStartStop.setText("Start");
	        mResetLap.setText("Reset");
	        mStopwatch.stop();
	    }
    }
    
    public void onResetLapButtonClick(View v) {
    	Log.d(TAG, "onResetLapButtonClick called");
    	if (mStopwatch.isPaused()) {    // currently says "Reset"
    	    mStopwatch.reset();
    	} else {
            Log.d(TAG, mStopwatch.currentFormattedTime());
    	}
    }
    
}