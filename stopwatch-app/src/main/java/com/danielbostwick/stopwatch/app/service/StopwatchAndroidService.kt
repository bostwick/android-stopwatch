package com.danielbostwick.stopwatch.app.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.util.Log
import com.danielbostwick.stopwatch.R
import com.danielbostwick.stopwatch.app.Intents
import com.danielbostwick.stopwatch.ext.toTimeElapsedString
import com.danielbostwick.stopwatch.app.StopwatchApplication
import com.danielbostwick.stopwatch.core.event.StopwatchPaused
import com.danielbostwick.stopwatch.core.event.StopwatchStarted
import com.danielbostwick.stopwatch.core.event.StopwatchWasReset
import com.danielbostwick.stopwatch.core.manager.StopwatchManager
import com.danielbostwick.stopwatch.core.model.Stopwatch
import com.danielbostwick.stopwatch.core.service.DefaultStopwatchService
import com.danielbostwick.stopwatch.core.service.StopwatchService
import org.joda.time.DateTime
import org.joda.time.Duration
import java.util.concurrent.atomic.AtomicReference


class StopwatchAndroidService : Service(), StopwatchService, StopwatchManager {
    private val ONGOING_NOTIFICATION_ID = 100;

    private val TAG = StopwatchAndroidService::class.java.simpleName
    private val stopwatchRef: AtomicReference<Stopwatch> = AtomicReference()
    private val stopwatchService = DefaultStopwatchService()
    private var notification: Notification? = null
    private val binder = StopwatchServiceBinder()
    private val eventBus = StopwatchApplication.instance.eventBus

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate()")
        stopwatchRef.set(stopwatchService.create())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand()")
        return START_STICKY
    }

    override fun onBind(intent: Intent?) = binder

    override fun getStopwatch() = stopwatchRef.get()

    override fun create() = stopwatchService.create()

    override fun start(stopwatch: Stopwatch, startedAt: DateTime): Stopwatch {
        Log.d(TAG, "start()")

        val newStopwatch = stopwatchService.start(stopwatch, startedAt)

        stopwatchRef.set(newStopwatch)
        eventBus.post(StopwatchStarted(newStopwatch))

        notification = notification ?: createNotification(newStopwatch)
        startForeground(ONGOING_NOTIFICATION_ID, notification)

        return newStopwatch
    }

    override fun pause(stopwatch: Stopwatch, pausedAt: DateTime): Stopwatch {
        Log.d(TAG, "pause()")

        val newStopwatch = stopwatchService.pause(stopwatch, pausedAt)

        stopwatchRef.set(newStopwatch)
        eventBus.post(StopwatchPaused(newStopwatch))

        return newStopwatch
    }

    override fun reset(stopwatch: Stopwatch): Stopwatch {
        Log.d(TAG, "reset()")

        val newStopwatch = stopwatchService.reset(stopwatch)

        stopwatchRef.set(newStopwatch)
        eventBus.post(StopwatchWasReset(newStopwatch))

        stopForeground(true)

        return newStopwatch
    }

    override fun timeElapsed(stopwatch: Stopwatch, now: DateTime): Duration =
        stopwatchService.timeElapsed(stopwatch, now)

    private fun createNotification(stopwatch: Stopwatch): Notification {
        val notificationIntent = Intents.Stopwatch.create(applicationContext)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)
        val timeElapsedStr = stopwatchService.timeElapsed(stopwatch, DateTime.now()).toTimeElapsedString()

        return Notification.Builder(this)
            .setContentTitle(getText(R.string.notification_title))
            .setContentText(timeElapsedStr)
            .setContentIntent(pendingIntent)
            .build()
    }

    /**
     * Class for clients to access.  Because we know this service always runs in the same process
     * as its clients, we don't need to deal with IPC.
     */
    inner class StopwatchServiceBinder : Binder() {
        internal val service: StopwatchAndroidService
            get() = this@StopwatchAndroidService
    }
}
