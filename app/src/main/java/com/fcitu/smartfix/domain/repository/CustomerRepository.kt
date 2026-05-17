package com.fcitu.smartfix.domain.repository

import com.fcitu.smartfix.domain.entity.User

interface CustomerRepository {
    suspend fun getMyProfile(): User
}