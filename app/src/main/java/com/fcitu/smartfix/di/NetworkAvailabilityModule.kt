package com.fcitu.smartfix.di

import com.fcitu.smartfix.ui.utils.InternetConnectionAvailability
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val networkAvailabilityModule = module {
    single { InternetConnectionAvailability(androidContext()) }
}