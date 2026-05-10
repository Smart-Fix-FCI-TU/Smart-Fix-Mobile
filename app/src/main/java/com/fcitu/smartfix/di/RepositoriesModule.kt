package com.fcitu.smartfix.di

import com.fcitu.smartfix.data.repository.AuthRepositoryImpl
import com.fcitu.smartfix.data.repository.CustomerRepositoryImpl
import com.fcitu.smartfix.data.repository.IdentityRepositoryImpl
import com.fcitu.smartfix.data.repository.OrderRepositoryImpl
import com.fcitu.smartfix.domain.repository.AuthRepository
import com.fcitu.smartfix.domain.repository.CustomerRepository
import com.fcitu.smartfix.domain.repository.IdentityRepository
import com.fcitu.smartfix.domain.repository.OrderRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoriesModule = module {
    // Repositories
    singleOf(::IdentityRepositoryImpl) bind IdentityRepository::class
    singleOf(::AuthRepositoryImpl) bind AuthRepository::class
    singleOf(::OrderRepositoryImpl) bind OrderRepository::class
    singleOf(::CustomerRepositoryImpl) bind CustomerRepository::class
}