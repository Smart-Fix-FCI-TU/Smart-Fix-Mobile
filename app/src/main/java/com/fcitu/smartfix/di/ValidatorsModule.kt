package com.fcitu.smartfix.di

import com.fcitu.smartfix.domain.useCase.validation.mobileNumber.MobileNumberValidator
import com.fcitu.smartfix.domain.useCase.validation.password.PasswordValidator
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val validatorsModule = module {
    // Validators
    singleOf(::MobileNumberValidator)
    singleOf(::PasswordValidator)
}