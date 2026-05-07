package com.fcitu.smartfix.di

import com.fcitu.smartfix.data.local.UserDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dataSourceModule = module {
    // Application Scope
    single(named("ApplicationScope")) { CoroutineScope(Dispatchers.IO + SupervisorJob()) }

    // Data Sources
    single { UserDataStore(androidContext(), get(named("ApplicationScope"))) }
}