/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.redballoonsoftware.widgets;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import java.util.Formatter;
import java.util.IllegalFormatException;
import java.util.Locale;

public class Stopwatch extends TextView {
    private static final String TAG = "Stopwatch";
    
    private static final int TICK_WHAT = 2; 
    
    private long mNow;			// The recorded elapsed time
    private long mStartTime;	// When did we start?
    private long mNowOffset;	// How much time is carried over
    private final long mFrequency = 100;    // milliseconds
    
    private boolean mPaused;	// true if paused, o/w false
	private Handler mHandler = new Handler() {
        public void handleMessage(Message m) {
            if (!mPaused) {
                updateText(currentTime());
                sendMessageDelayed(Message.obtain(this, TICK_WHAT), mFrequency);
            }
        }
    };
    
    
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
        Log.d(TAG, "init()");
    	reset();
        updateText(currentTime());
    }


    /***
     * Begins or resumes the stopwatch counting.
     */
    public void start() {
        Log.d(TAG, "start()");
    	mPaused = false;
    	mStartTime = SystemClock.elapsedRealtime();
        mHandler.sendMessageDelayed(Message.obtain(mHandler, TICK_WHAT), mFrequency);
    }

    /***
     * Stops the progression of time in the stopwatch, but does 
     * not reset the display.
     * 
     * Stopwatch can be restarted with start()
     */
    public void stop() {
        Log.d(TAG, "stop()");
    	mNowOffset = currentTime();
    	mPaused = true;
    }
    
    /***
     * Resets the stopwatch's current time to 0.
     */
    public void reset() {
    	mPaused = true;
        mStartTime = SystemClock.elapsedRealtime();
    	mNow = mNowOffset = 0;
    	updateText(mNow);
    }
    
    
    /***
     * @return the current time recorded, in milliseconds.
     */
    public synchronized long currentTime() {
    	if (!mPaused)
    		mNow = (SystemClock.elapsedRealtime() - mStartTime) + mNowOffset;

    	return mNow;
    }
    
    /***
     * @return  the current time, as formatted by formatElapsedTime()
     */
    public String currentFormattedTime() {
        return formatElapsedTime(currentTime());
    }
    
    /***
     * @returns true if the stopwatch is paused and not incrementing time, 
     *          otherwise false
     */
    public boolean isPaused() {
        return mPaused;
    }
    
    /***
     * Sets the widget to display the time specified by now
     * @param now	The time, in milliseconds, to display in this widget
     */
    private synchronized void updateText(long now) {
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
