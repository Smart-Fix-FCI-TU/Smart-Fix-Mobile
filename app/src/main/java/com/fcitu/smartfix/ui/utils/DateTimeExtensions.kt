package com.fcitu.smartfix.ui.utils

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import java.util.Locale
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun LocalDateTime.toOrderTimeFormat(): String {

    val currentDate = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .date

    val dayLabel = when (this.date) {
        currentDate -> "Today"
        currentDate.minus(1, DateTimeUnit.DAY) -> "Yesterday"
        else -> "${this.date.day} ${
            this.date.month.name.lowercase(Locale.getDefault())
                .replaceFirstChar { it.uppercase(Locale.getDefault()) }
        }"
    }

    val hour = this.hour
    val minute = this.minute

    val amPm = if (hour < 12) "AM" else "PM"

    val hour12 = when (hour) {
        0    -> 12           // 12 AM (midnight)
        12   -> 12           // 12 PM (noon) ← explicit
        in 13..23 -> hour - 12
        else -> hour
    }

    val minuteFormatted = String.format(Locale.US, "%02d", minute)

    return "$dayLabel, $hour12:$minuteFormatted $amPm"
}