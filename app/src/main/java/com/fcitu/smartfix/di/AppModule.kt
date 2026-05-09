package com.fcitu.smartfix.di

import com.fcitu.smartfix.ui.utils.networkAvailabilityModule
import org.koin.dsl.module

val appModule =
    module {
        includes(
            dataSourceModule,
            repositoriesModule,
            validatorsModule,
            useCasesModule,
            viewModelsModule,
            networkAvailabilityModule
        )
    }