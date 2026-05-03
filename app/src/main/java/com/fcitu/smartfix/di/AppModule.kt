package com.fcitu.smartfix.di

import org.koin.dsl.module

val appModule =
    module {
        includes(
            dataSourceModule,
            repositoriesModule,
            validatorsModule,
            useCasesModule,
            viewModelsModule,
            BookingModule
        )
    }