package com.fcitu.smartfix.data.remote.util

import com.fcitu.smartfix.domain.exception.BadRequestException
import com.fcitu.smartfix.domain.exception.NoNetworkException
import com.fcitu.smartfix.domain.exception.NotFoundException
import com.fcitu.smartfix.domain.exception.UnauthorizedException
import com.fcitu.smartfix.domain.exception.UnknownException
import retrofit2.HttpException
import java.io.IOException

suspend fun <T> safeApiCall(apiCall: suspend () -> T): T {
    return try {
        apiCall()
    } catch (throwable: Throwable) {
        throw when (throwable) {
            is IOException -> NoNetworkException()
            is HttpException -> {
                when (throwable.code()) {
                    400 -> BadRequestException()
                    401 -> UnauthorizedException()
                    404 -> NotFoundException()
                    else -> UnknownException()
                }
            }
            else -> throwable
        }
    }
}