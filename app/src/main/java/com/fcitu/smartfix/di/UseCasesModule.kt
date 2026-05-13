package com.fcitu.smartfix.di

import com.fcitu.smartfix.domain.useCase.GetCustomerOrdersUseCase
import com.fcitu.smartfix.domain.useCase.GetTechnicianProfileUseCase
import com.fcitu.smartfix.domain.useCase.LoginUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val useCasesModule = module {
    // Use Cases
    factoryOf(::LoginUseCase)
    factoryOf(::GetCustomerOrdersUseCase)
    factoryOf(::GetTechnicianProfileUseCase)
}