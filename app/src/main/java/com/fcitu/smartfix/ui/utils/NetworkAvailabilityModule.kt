package com.fcitu.smartfix.ui.utils

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val networkAvailabilityModule = module {
    single { InternetConnectionAvailability(androidContext()) }
}