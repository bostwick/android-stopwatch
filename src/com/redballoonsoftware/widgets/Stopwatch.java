package com.redballoonsoftware.widgets;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import java.util.Timer;
import java.util.TimerTask;

public class Stopwatch extends TextView {
	private static final String TAG = "Stopwatch";
	private static final int TICK_WHAT = 2;
	
	// in milliseconds
	private long mStartTime;
	private long mNow;
	private long mNowOffset;
	private long mDelay;
	
	private boolean mPaused;
	private boolean mRunning;
	
	private Handler mHandler = new Handler();
	
	
	private Runnable mUpdateTimeTask = new Runnable() {
		@Override
		public void run() {
			mNow = (SystemClock.elapsedRealtime() - mStartTime) + mNowOffset;
			updateText(mNow);
			mHandler.postDelayed(this, mDelay);
		}
	};
	
	public void start() {
		if ( !mRunning ) {
			mStartTime = SystemClock.elapsedRealtime();
			mRunning = true;
			mHandler.removeCallbacks(mUpdateTimeTask);
			mHandler.postDelayed(mUpdateTimeTask, mDelay);
		}
	}
	
	
	
	public void stop() {
		mRunning = false;
		mHandler.removeCallbacks(mUpdateTimeTask);
	}
	
	public void reset() {
		mNow = 0;
		mRunning = false;
	}

	
	public Stopwatch(Context context) {
		this(context, null, 0);
	}
	
	public Stopwatch(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public Stopwatch(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		mDelay = 10; 	// Update every hundredth of a second
		reset();
		updateText(mNow);
	}
	
	/* 
	public void reset() {
		// mTimer = new Timer();
		mNow = 0;
		mNowOffset = 0;
		mIsPaused = true;
		mIsStarted = false;
	}
	
	public void start() {
		Log.d(TAG, "start");
		mIsPaused = false;
		mIsStarted = true;
		mStart = SystemClock.elapsedRealtime();
		mHandler.removeCallbacks(mUpdateTimeTask);
		mHandler.postDelayed(mUpdateTimeTask, mDelay);
	}
	
	public void stop() {
		Log.d(TAG, "stop");
		mPaused = true;
		mNowOffset = mNow;
	}
	*/
	
	public boolean isStarted() {
		return mRunning;
	}
	
	public boolean isPaused() {
		return mPaused;
	}
	
	private synchronized void updateText(long now) {
		Log.d(TAG, "updateText");
		setText(formatElapsedTime(now));
	}
	
	/***
	 * Given the time elapsed in tenths of seconds, returns the string
	 * representation of that time. 
	 * 
	 * @param now, the current time in tenths of seconds
	 * @return 	String with the current time in the format MM:SS.T or 
	 * 			HH:MM:SS.T, depending on elapsed time.
	 */
	private String formatElapsedTime(long now) {
		long hours=0, minutes=0, seconds=0, tenths=0;
		StringBuilder sb = new StringBuilder();
		
		if (now < 1000) {
			tenths = now / 100;
		} else if (now < 60000) {
			seconds = now / 1000;
			now -= seconds * 1000;
			tenths = (now / 100);
		} else if (now < 3600000) {
			hours = now / 3600000;
			now -= hours * 3600000;
			minutes = now / 60000;
			now -= minutes * 60000;
			seconds = now / 1000;
			now -= seconds * 1000;
			tenths = (now / 100);
		}
		
		if (hours > 0) {
			sb.append(hours).append(":")
				.append(formatDigits(minutes)).append(":")
				.append(formatDigits(seconds)).append(".")
				.append(tenths);
		} else {
			sb.append(formatDigits(minutes)).append(":")
			.append(formatDigits(seconds)).append(".")
			.append(tenths);
		}
		
		return sb.toString();
	}
	
	private String formatDigits(long num) {
		return (num < 10) ? "0" + num : new Long(num).toString();
	}

}