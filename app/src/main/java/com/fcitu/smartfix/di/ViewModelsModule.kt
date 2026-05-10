package com.fcitu.smartfix.di

import com.fcitu.smartfix.ui.screen.customer.home.HomeViewModel
import com.fcitu.smartfix.ui.screen.shared.login.LoginScreenViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelsModule = module {
    // Login Screen ViewModel
    viewModelOf(::LoginScreenViewModel)
    viewModelOf(::HomeViewModel)
}