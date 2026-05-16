package com.fcitu.smartfix.di

import com.fcitu.smartfix.BuildConfig
import com.fcitu.smartfix.data.remote.NetworkConstants
import com.fcitu.smartfix.data.remote.service.AuthService
import com.fcitu.smartfix.data.remote.service.ReviewService
import com.fcitu.smartfix.data.remote.util.AuthInterceptor
import com.fcitu.smartfix.data.remote.util.TokenAuthenticator
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    single {
        Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
    }

    single {
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
            if (BuildConfig.DEBUG) {
                redactHeader("Authorization")
                redactHeader("Cookie")
            }
        }
    }

    single { AuthInterceptor(get()) }

    // ── Dedicated Auth Client (No Authenticator to avoid deadlock) ──
    single(named("AuthClient")) {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    single(named("AuthRetrofit")) {
        val contentType = "application/json".toMediaType()
        Retrofit.Builder()
            .baseUrl(NetworkConstants.BASE_URL)
            .client(get(named("AuthClient")))
            .addConverterFactory(get<Json>().asConverterFactory(contentType))
            .build()
    }

    single(named("RefreshAuthService")) {
        get<Retrofit>(named("AuthRetrofit")).create(AuthService::class.java)
    }

    single { TokenAuthenticator(get(), get(named("RefreshAuthService"))) }

    // ── Main Client (With Authenticator) ──
    single {
        OkHttpClient.Builder()
            .addInterceptor(get<AuthInterceptor>())
            .addInterceptor(get<HttpLoggingInterceptor>())
            .authenticator(get<TokenAuthenticator>())
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    single {
        val contentType = "application/json".toMediaType()
        Retrofit.Builder()
            .baseUrl(NetworkConstants.BASE_URL)
            .client(get())
            .addConverterFactory(get<Json>().asConverterFactory(contentType))
            .build()
    }

    single { get<Retrofit>().create(AuthService::class.java) }
    single { get<Retrofit>().create(ReviewService::class.java) }
}