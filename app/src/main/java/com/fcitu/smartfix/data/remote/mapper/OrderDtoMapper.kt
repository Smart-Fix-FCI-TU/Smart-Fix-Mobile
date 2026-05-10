package com.fcitu.smartfix.data.remote.mapper

import com.fcitu.smartfix.data.remote.dto.OrderDto
import com.fcitu.smartfix.domain.entity.Address
import com.fcitu.smartfix.domain.entity.Order
import com.fcitu.smartfix.domain.model.OrderStatus
import com.fcitu.smartfix.domain.model.ServiceCategory
import kotlinx.datetime.LocalDateTime

fun OrderDto.toDomain(): Order = Order(
    id = id,
    customer = customer.toDomain(),
    technician = technician?.toDomain() ?: Order.UserInfo(id = "", name = ""),
    details = details.toDomain(),
    repairPhotos = repairPhotos.toDomain(),
    status = OrderStatus.valueOf(status),
    timeline = timeline.toDomain(),
)

fun OrderDto.UserInfoDto.toDomain(): Order.UserInfo = Order.UserInfo(
    id = id,
    name = name,
)

fun OrderDto.OrderDetailsDto.toDomain(): Order.OrderDetails = Order.OrderDetails(
    serviceCategory = ServiceCategory.valueOf(serviceCategory),
    title = title,
    description = description,
    problemPhotoUrls = problemPhotoUrls,
    address = address.toDomain(),
    additionalNotes = additionalNotes,
)

fun OrderDto.AddressDto.toDomain(): Address =
    Address(id, fullAddress, location = location?.toEntity(), floor = floor, apartmentNo)

fun OrderDto.RepairPhotosDto.toDomain(): Order.RepairPhotos = Order.RepairPhotos(
    beforeRepairUrls = beforeRepairUrls,
    afterRepairUrls = afterRepairUrls,
)

fun OrderDto.AddressDto.LocationDto.toEntity(): Address.Location =
    Address.Location(latitude = latitude, longitude = longitude)

fun OrderDto.OrderTimelineDto.toDomain(): Order.OrderTimeline = Order.OrderTimeline(
    createdAt = LocalDateTime.parse(createdAt),
    acceptedAt = LocalDateTime.parse(acceptedAt),
    arrivedAt = LocalDateTime.parse(arrivedAt),
    startedAt = LocalDateTime.parse(startedAt),
    completedAt = LocalDateTime.parse(completedAt),
)