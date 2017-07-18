package com.danielbostwick.stopwatch.app

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import com.danielbostwick.stopwatch.app.service.StopwatchAndroidService
import com.google.common.eventbus.EventBus


class StopwatchApplication : Application() {
    private val TAG = StopwatchApplication::class.java.simpleName

    val eventBus = EventBus("stopwatch")
    var stopwatchService: StopwatchAndroidService? = null

    override fun onCreate() {
        super.onCreate()

        Log.d(TAG, "onCreate()")

        StopwatchApplication.instance = this
        bindStopwatchService()
    }

    private fun bindStopwatchService() = bindService(
        Intents.StopwatchAndroidService.create(this),
        stopwatchServiceConnection,
        Context.BIND_AUTO_CREATE)

    private val stopwatchServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.d(TAG, "onServiceConnected() - name:StopwatchAndroidService")
            stopwatchService = (service as StopwatchAndroidService.StopwatchServiceBinder).service
            eventBus.post(StopwatchServiceBound())
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d(TAG, "onServiceDisconnected() - name:StopwatchAndroidService")
            stopwatchService = null
        }
    }

    companion object {
        lateinit var instance: StopwatchApplication
    }
}
