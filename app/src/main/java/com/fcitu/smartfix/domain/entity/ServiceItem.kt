package com.fcitu.smartfix.domain.entity

import com.fcitu.smartfix.domain.model.ServiceCategory

data class ServiceItem(
    val serviceCategory: ServiceCategory,
    val serviceName: String,
    val activeIcon: Int,
    val inactiveIcon: Int
)
