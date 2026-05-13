package com.fcitu.smartfix.data.remote.dto.technician

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TechnicianDto(

	@SerialName("profilePictureUrl")
	val profilePictureUrl: String? = null,

	@SerialName("firstName")
	val firstName: String? = null,

	@SerialName("lastName")
	val lastName: String? = null,

	@SerialName("specialties")
	val specialties: List<String?>? = null,

	@SerialName("rating")
	val rating: Int? = null,

	@SerialName("bio")
	val bio: String? = null,

	@SerialName("location")
	val location: Location? = null,

	@SerialName("id")
	val id: String? = null,

	@SerialName("completedJobs")
	val completedJobs: Int? = null,

	@SerialName("availability")
	val availability: Boolean? = null,

	@SerialName("hourlyRate")
	val hourlyRate: Int? = null
)
@Serializable
data class Location(

	@SerialName("country")
	val country: String? = null,

	@SerialName("city")
	val city: String? = null,

	@SerialName("state")
	val state: String? = null
)
