package com.danielbostwick.stopwatch.ext

import org.joda.time.Duration

fun Duration.toTimeElapsedString(): String {
    val hours = this.standardHours
    val minutes = this.standardMinutes
    val seconds = this.standardSeconds

    val minuteStr = if (minutes > 10) "${minutes}" else "0${minutes}"
    val secondStr = if (seconds > 10) "${seconds}" else "0${seconds}"

    return if (hours > 0) "${hours}:${minuteStr}:${secondStr}"
    else "${minuteStr}:${secondStr}"
}
