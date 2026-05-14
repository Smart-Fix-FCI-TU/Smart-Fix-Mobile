package com.fcitu.smartfix.ui.utils

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun LocalDateTime.Companion.now(timeZone: TimeZone = TimeZone.currentSystemDefault()): LocalDateTime {
    return Clock.System.now().toLocalDateTime(timeZone)
}

fun LocalDateTime.formatAsTime(am: String = "AM", pm: String = "PM"): String {
    val timeFormat = LocalDateTime.Format {
        amPmHour()
        char(':')
        minute()
        char(' ')
        amPmMarker(am, pm)
    }
    return this.format(timeFormat)
}

fun LocalDateTime.format(pattern: String = "dd-MM-yyyy"): String {
    return pattern
        .replace("dd", day.toString().padStart(2, '0'))
        .replace("MM", month.ordinal.plus(1).toString().padStart(2, '0'))
        .replace("yyyy", year.toString())
}