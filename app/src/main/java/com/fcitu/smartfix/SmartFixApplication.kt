package com.fcitu.smartfix

import android.app.Application
import com.fcitu.smartfix.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class SmartFixApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@SmartFixApplication)
            modules(appModule)
        }
    }
}