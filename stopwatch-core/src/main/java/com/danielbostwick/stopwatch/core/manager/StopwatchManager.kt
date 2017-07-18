package com.danielbostwick.stopwatch.core.manager

import com.danielbostwick.stopwatch.core.model.Stopwatch

interface StopwatchManager {
    fun getStopwatch(): Stopwatch
}
