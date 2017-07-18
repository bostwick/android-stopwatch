package com.danielbostwick.stopwatch.core.model

import org.joda.time.DateTime
import org.joda.time.Duration

data class Stopwatch(
    val state : StopwatchState,
    val startedAt : DateTime,
    val offset : Duration)

enum class StopwatchState { PAUSED, STARTED }
