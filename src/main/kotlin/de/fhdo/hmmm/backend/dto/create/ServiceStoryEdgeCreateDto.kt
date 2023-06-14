package de.fhdo.hmmm.backend.dto.create

import java.io.Serializable

data class ServiceStoryEdgeCreateDto(
    val sourceId: Long,
    val targetId: Long,
    val description: String,
    val sysId: Long
) : Serializable