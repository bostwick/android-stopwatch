package com.redballoonsoftware.stopwatch;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class Stopwatch extends ListActivity {
	private static String TAG = "StopwatchActivity";
	private com.redballoonsoftware.widgets.Stopwatch mStopwatch;
	
	private Button mStartStop;
	private Button mResetLap;
	private ListView mLapList; 
	
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
        mLapList = getListView();

        setListAdapter(new ArrayAdapter<String>(this, R.layout.laps_row));
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
    	    ((ArrayAdapter<String>)getListAdapter()).clear();
    	} else {
            Log.d(TAG, mStopwatch.currentFormattedTime());
            ((ArrayAdapter<String>)mLapList.getAdapter()).insert(mStopwatch.currentFormattedTime(), 0);
    	}
    }
    
}