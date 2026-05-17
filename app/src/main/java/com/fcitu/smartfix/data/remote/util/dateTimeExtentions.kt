@file:OptIn(ExperimentalTime::class)

package com.fcitu.smartfix.data.remote.util

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

fun Instant.toLocalDateTime() = toLocalDateTime(TimeZone.currentSystemDefault())
