package com.redballoonsoftware.stopwatch;

import android.app.Activity;
import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class StopwatchActivity extends ListActivity {
	private static String TAG = "StopwatchActivity";
		
	// View elements in stopwatch.xml
	private TextView m_elapsedTime;
	private Button m_start;
	private Button m_pause;
	private Button m_reset;
	private Button m_lap;
	private ArrayAdapter<String> m_lapList;
	
	// Timer to update the elapsedTime display
    private final long mFrequency = 100;    // milliseconds
    private final int TICK_WHAT = 2; 
	private Handler mHandler = new Handler() {
        public void handleMessage(Message m) {
        	updateElapsedTime();
        	sendMessageDelayed(Message.obtain(this, TICK_WHAT), mFrequency);
        }
    };

    // Connection to the backgorund StopwatchService
	private StopwatchService m_stopwatchService;
	private ServiceConnection m_stopwatchServiceConn = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			m_stopwatchService = ((StopwatchService.LocalBinder)service).getService();
			showCorrectButtons();
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			m_stopwatchService = null;
		}
	};
	
	private void bindStopwatchService() {
        bindService(new Intent(this, StopwatchService.class), 
        			m_stopwatchServiceConn, Context.BIND_AUTO_CREATE);
	}
	
	private void unbindStopwatchService() {
		if ( m_stopwatchService != null ) {
			unbindService(m_stopwatchServiceConn);
		}
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.stopwatch);
        setListAdapter(new ArrayAdapter<String>(this, R.layout.laps_row));

        startService(new Intent(this, StopwatchService.class));
        bindStopwatchService();
        
        m_elapsedTime = (TextView)findViewById(R.id.ElapsedTime);
        
        m_start = (Button)findViewById(R.id.StartButton);
        m_pause = (Button)findViewById(R.id.PauseButton);
        m_reset = (Button)findViewById(R.id.ResetButton);
        m_lap = (Button)findViewById(R.id.LapButton);
        
        m_lapList = (ArrayAdapter<String>)getListAdapter();

        mHandler.sendMessageDelayed(Message.obtain(mHandler, TICK_WHAT), mFrequency);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindStopwatchService();
    }
    
    private void showCorrectButtons() {
    	Log.d(TAG, "showCorrectButtons");
    	
    	if ( m_stopwatchService != null ) {
    		if ( m_stopwatchService.isStopwatchRunning() ) {
    			showPauseLapButtons();
    		} else {
    			showStartResetButtons();
    		}
    	}
    }
    
    private void showPauseLapButtons() {
    	Log.d(TAG, "showPauseLapButtons");
    	
    	m_start.setVisibility(View.GONE);
    	m_reset.setVisibility(View.GONE);
    	m_pause.setVisibility(View.VISIBLE);
    	m_lap.setVisibility(View.VISIBLE);
    }
    
    private void showStartResetButtons() {
    	Log.d(TAG, "showStartResetButtons");

    	m_start.setVisibility(View.VISIBLE);
    	m_reset.setVisibility(View.VISIBLE);
    	m_pause.setVisibility(View.GONE);
    	m_lap.setVisibility(View.GONE);
    }
    
    public void onStartClicked(View v) {
    	Log.d(TAG, "start button clicked");
    	m_stopwatchService.start();
    	
    	showPauseLapButtons();
    }
    
    public void onPauseClicked(View v) {
    	Log.d(TAG, "pause button clicked");
    	m_stopwatchService.pause();
    	
    	showStartResetButtons();
    }
    
    public void onResetClicked(View v) {
    	Log.d(TAG, "reset button clicked");
    	m_stopwatchService.reset();
    	
    	m_lapList.clear();
    }
    
    public void onLapClicked(View v) {
    	Log.d(TAG, "lap button clicked");
    	m_stopwatchService.lap();
    	
    	m_lapList.insert(m_stopwatchService.getFormattedElapsedTime(), 0);
    }
    
    public void updateElapsedTime() {
    	if ( m_stopwatchService != null )
    		m_elapsedTime.setText(m_stopwatchService.getFormattedElapsedTime());
    }
    
}