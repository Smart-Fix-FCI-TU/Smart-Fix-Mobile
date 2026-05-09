package com.fcitu.smartfix.di

import com.fcitu.smartfix.domain.useCase.validation.email.EmailValidator
import com.fcitu.smartfix.domain.useCase.validation.password.PasswordValidator
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val validatorsModule = module {
    // Validators
    singleOf(::EmailValidator)
    singleOf(::PasswordValidator)
}