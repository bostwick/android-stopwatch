package com.danielbostwick.stopwatch.app

import android.content.Context
import android.content.Intent
import com.danielbostwick.stopwatch.app.stopwatch.StopwatchActivity

object Intents {
    object StopwatchAndroidService {
        fun create(context: Context) = Intent(
            context, com.danielbostwick.stopwatch.app.service.StopwatchAndroidService::class.java)
    }

    object Stopwatch {
        fun create(context: Context) = Intent(context, StopwatchActivity::class.java)
    }
}
