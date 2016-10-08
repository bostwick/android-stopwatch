package com.redballoonsoftware.stopwatch;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class StopwatchService extends Service {
	private static final String TAG = "StopwatchService";
	private static final int NOTIFICATION_ID = 1;

	public class LocalBinder extends Binder {
		StopwatchService getService() {
			return StopwatchService.this;
		}
	}

	private Stopwatch m_stopwatch;
	private LocalBinder m_binder = new LocalBinder();
	private NotificationManager m_notificationMgr;
	private NotificationCompat.Builder mBuilder;

	// Timer to update the ongoing notification
	private final long mFrequency = 100;    // milliseconds
	private final int TICK_WHAT = 2;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message m) {
			updateNotification();
			sendMessageDelayed(Message.obtain(this, TICK_WHAT), mFrequency);
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		Log.d(TAG, "bound");

		return m_binder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "created");

		m_stopwatch = new Stopwatch();
		m_notificationMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		createNotification();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i("LocalService", "Received start id " + startId + ": " + intent);
		// We want this service to continue running until it is explicitly
		// stopped, so return sticky.
		return START_REDELIVER_INTENT;
	}

	public void createNotification() {
		mBuilder = new NotificationCompat.Builder(this)
				.setSmallIcon(R.drawable.icon)
				.setContentTitle("Stopwatch")
				.setContentText("Stopwatch");
	}

	public void updateNotification() {

		CharSequence contentText = getFormattedElapsedTime();
		NotificationManager mNotificationManager =
				(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		mBuilder = new NotificationCompat.Builder(this)
				.setSmallIcon(R.drawable.icon)
				.setContentTitle("Stopwatch")
				.setContentText(contentText);

		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
	}

	public void showNotification() {
		Log.d(TAG, "showing notification");

		updateNotification();
		mHandler.sendMessageDelayed(Message.obtain(mHandler, TICK_WHAT), mFrequency);
	}

	public void hideNotification() {
		Log.d(TAG, "removing notification");

		m_notificationMgr.cancel(NOTIFICATION_ID);
		mHandler.removeMessages(TICK_WHAT);
	}

	public void start() {
		Log.d(TAG, "start");

		m_stopwatch.start();
		showNotification();
	}

	public void pause() {
		Log.d(TAG, "pause");

		m_stopwatch.pause();
		hideNotification();
	}

	public void lap() {
		Log.d(TAG, "lap");

		m_stopwatch.lap();
	}

	public void reset() {
		Log.d(TAG, "reset");

		m_stopwatch.reset();
	}

	public long getElapsedTime() {
		return m_stopwatch.getElapsedTime();
	}

	public String getFormattedElapsedTime() {
		return formatElapsedTime(getElapsedTime());
	}

	public boolean isStopwatchRunning() {
		return m_stopwatch.isRunning();
	}

	/***
	 * Given the time elapsed in tenths of seconds, returns the string
	 * representation of that time.
	 *
	 * @param now, the current time in tenths of seconds
	 * @return String with the current time in the format MM:SS.T or
	 * HH:MM:SS.T, depending on elapsed time.
	 */
	private String formatElapsedTime(long now) {
		long hours = 0, minutes = 0, seconds = 0, tenths = 0;
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
		return (num < 10) ? "0" + num : Long.valueOf(num).toString();
	}

}
