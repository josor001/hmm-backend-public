package de.fhdo.hmmm.backend.dto

import java.io.Serializable

data class ServiceStoryDto(
    var name: String? = null,
    var description: String? = null,
    val verticesIds: MutableSet<Long> = mutableSetOf(),
    val directedEdgesIds: MutableSet<Long> = mutableSetOf(),
    var id: Long? = null
) : Serializable