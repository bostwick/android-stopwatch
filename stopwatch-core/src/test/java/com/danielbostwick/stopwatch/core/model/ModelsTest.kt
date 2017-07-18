package com.danielbostwick.stopwatch.core.model

import org.joda.time.DateTime
import org.joda.time.Duration
import org.junit.Test
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.notNullValue

class ModelsTest {
    @Test fun stopwatchCanBeInstantiated() {
        val s = Stopwatch(StopwatchState.PAUSED, DateTime.now(), Duration.ZERO)

        assertThat(s, notNullValue())
    }
}
