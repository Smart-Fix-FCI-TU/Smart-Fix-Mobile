package com.fcitu.smartfix.di

import com.fcitu.smartfix.data.repository.BookingRepositoryImpl
import com.fcitu.smartfix.domain.repository.BookingRepository
import com.fcitu.smartfix.domain.useCase.BookingUseCase
import com.fcitu.smartfix.ui.theme.screen.describeProblem.BookingViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val BookingModule = module {
    single<BookingRepository> { BookingRepositoryImpl() }
    factoryOf(::BookingUseCase)
    viewModelOf(::BookingViewModel)
}