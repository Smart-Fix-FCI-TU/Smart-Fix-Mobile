package com.fcitu.smartfix.domain.entity

import kotlinx.datetime.LocalDateTime

data class Review(
        val id: String,
        val reviewerName: String,
        val rating: Int,
        val comment: String,
        val createdAt: LocalDateTime
    )