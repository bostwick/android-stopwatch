package com.danielbostwick.stopwatch.app.stopwatch

import android.os.Bundle
import android.util.Log
import com.danielbostwick.stopwatch.R
import com.danielbostwick.stopwatch.app.BaseActivity
import com.danielbostwick.stopwatch.app.StopwatchApplication
import com.danielbostwick.stopwatch.app.StopwatchServiceBound
import com.google.common.eventbus.Subscribe

class StopwatchActivity : BaseActivity() {
    private val TAG = StopwatchActivity::class.java.simpleName
    private val eventBus = StopwatchApplication.instance.eventBus
    private val stopwatchService = StopwatchApplication.instance.stopwatchService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate()")
        setContentView(R.layout.activity_stopwatch)
    }

    override fun onResume() {
        super.onResume()
        eventBus.register(this)

        if (stopwatchService != null) showStopwatch()
    }

    override fun onPause() {
        super.onPause()
        eventBus.unregister(this)
    }

    @Subscribe fun onStopwatchServiceBound(event: StopwatchServiceBound) {
        showStopwatch()
    }

    private fun showStopwatch() {
        supportFragmentManager.beginTransaction()
            .add(R.id.activity_stopwatch_container, StopwatchFragment())
            .commit()
    }
}
