package com.danielbostwick.stopwatch.core.event

import com.danielbostwick.stopwatch.core.model.Stopwatch

interface StopwatchEvent

data class StopwatchStarted(val stopwatch: Stopwatch)
data class StopwatchPaused(val stopwatch: Stopwatch)
data class StopwatchWasReset(val stopwatch: Stopwatch)
