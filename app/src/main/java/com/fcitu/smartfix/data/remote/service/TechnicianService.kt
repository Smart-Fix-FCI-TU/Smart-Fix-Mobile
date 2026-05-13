package com.fcitu.smartfix.data.remote.service

import com.fcitu.smartfix.data.remote.dto.ApiResponse
import com.fcitu.smartfix.data.remote.dto.technician.TechnicianDto
import retrofit2.http.GET
import retrofit2.http.Path

interface TechnicianService {

    @GET("/api/v1/technicians/{id}")
    suspend fun getTechnicianById(
        @Path("id") technicianId: String
    ): ApiResponse<TechnicianDto>
}