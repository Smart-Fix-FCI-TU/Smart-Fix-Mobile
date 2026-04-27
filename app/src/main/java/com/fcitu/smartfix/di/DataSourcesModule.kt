package com.fcitu.smartfix.di

import com.fcitu.smartfix.data.local.UserDataStore
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataSourceModule = module {
    // Data Sources
    single { UserDataStore(androidContext()) }
}