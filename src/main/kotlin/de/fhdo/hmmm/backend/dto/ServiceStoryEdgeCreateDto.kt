package de.fhdo.hmmm.backend.dto

import java.io.Serializable

data class ServiceStoryEdgeCreateDto(
    val sourceId: Long,
    val targetId: Long,
) : Serializable