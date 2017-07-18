package com.danielbostwick.stopwatch.core.service

import com.danielbostwick.stopwatch.core.model.Stopwatch
import org.joda.time.DateTime
import org.joda.time.Duration

interface StopwatchService {
    fun create(): Stopwatch
    fun start(stopwatch: Stopwatch, startedAt: DateTime): Stopwatch
    fun pause(stopwatch: Stopwatch, pausedAt: DateTime): Stopwatch
    fun reset(stopwatch: Stopwatch): Stopwatch
    fun timeElapsed(stopwatch: Stopwatch, now: DateTime): Duration
}
