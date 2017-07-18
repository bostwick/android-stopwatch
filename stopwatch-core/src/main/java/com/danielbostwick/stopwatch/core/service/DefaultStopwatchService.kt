package com.danielbostwick.stopwatch.core.service

import com.danielbostwick.stopwatch.core.model.Stopwatch
import com.danielbostwick.stopwatch.core.model.StopwatchState.PAUSED
import com.danielbostwick.stopwatch.core.model.StopwatchState.STARTED
import org.joda.time.DateTime
import org.joda.time.Duration
import org.joda.time.Interval


class DefaultStopwatchService : StopwatchService {
    override fun create() = Stopwatch(PAUSED, DateTime.now(), Duration.ZERO)

    override fun start(stopwatch: Stopwatch, startedAt: DateTime) = when (stopwatch.state) {
        PAUSED -> Stopwatch(STARTED, DateTime.now(), stopwatch.offset)
        STARTED -> stopwatch
    }

    override fun pause(stopwatch: Stopwatch, pausedAt: DateTime) = when (stopwatch.state) {
        PAUSED -> stopwatch
        STARTED -> Stopwatch(PAUSED, DateTime.now(),
            newOffset(stopwatch.offset, stopwatch.startedAt, pausedAt))
    }

    override fun reset(stopwatch: Stopwatch) = create()

    override fun timeElapsed(stopwatch: Stopwatch, now: DateTime): Duration = when (stopwatch.state) {
        PAUSED -> stopwatch.offset
        STARTED -> stopwatch.offset.plus(Interval(stopwatch.startedAt, now).toDuration())
    }

    private fun newOffset(existingOffset: Duration, startedAt: DateTime, pausedAt: DateTime) =
        existingOffset.plus(Interval(startedAt, pausedAt).toDuration())
}
