package com.danielbostwick.stopwatch.core.command

import com.danielbostwick.stopwatch.core.model.Stopwatch
import org.joda.time.DateTime

interface StopwatchCmd

data class StopwatchStart(val stopwatch: Stopwatch, val startedAt: DateTime) : StopwatchCmd
data class StopwatchPause(val stopwatch: Stopwatch, val pausedAt: DateTime) : StopwatchCmd
data class StopwatchReset(val stopwatch: Stopwatch) : StopwatchCmd
