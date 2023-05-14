package de.fhdo.hmmm.backend.dto

import java.io.Serializable

data class ServiceStoryDto(
    var name: String? = null,
    var description: String? = null,
    var vertexIds: MutableSet<Long> = mutableSetOf(),
    var directedEdgeIds: MutableSet<Long> = mutableSetOf(),
    var sysId: Long? = null,
    var id: Long? = null
) : Serializable