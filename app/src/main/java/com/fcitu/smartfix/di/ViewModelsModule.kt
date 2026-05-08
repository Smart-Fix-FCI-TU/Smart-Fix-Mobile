package com.fcitu.smartfix.di

import com.fcitu.smartfix.ui.theme.screen.booking.BookingViewModel
import com.fcitu.smartfix.ui.theme.screen.login.LoginScreenViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelsModule = module {
    // Login Screen ViewModel
    viewModelOf(::LoginScreenViewModel)
    viewModelOf(::BookingViewModel)
}