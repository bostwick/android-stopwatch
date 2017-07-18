package com.danielbostwick.stopwatch.core.event

import com.danielbostwick.stopwatch.core.model.Stopwatch
import com.danielbostwick.stopwatch.core.model.StopwatchState
import org.joda.time.DateTime
import org.joda.time.Duration
import org.junit.Test
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.notNullValue
import org.hamcrest.Matchers.equalTo

class EventsTest {
    val stopwatch = Stopwatch(StopwatchState.PAUSED, DateTime.now(), Duration.ZERO)

    @Test fun stopwatchStartedEventExists() {
        val evt = StopwatchStarted(stopwatch)

        assertThat(evt, notNullValue())
        assertThat(evt.stopwatch, equalTo(stopwatch))
    }

    @Test fun stopwatchPausedEventExists() {
        val evt = StopwatchPaused(stopwatch)

        assertThat(evt, notNullValue())
        assertThat(evt.stopwatch, equalTo(stopwatch))
    }

    @Test fun stopwatchResetEventExists() {
        val evt = StopwatchWasReset(stopwatch)

        assertThat(evt, notNullValue())
        assertThat(evt.stopwatch, equalTo(stopwatch))
    }
}
