package de.fhdo.hmmm.backend.dto

import java.io.Serializable

data class ServiceStoryEdgeDto(
    var sourceId: Long? = null,
    var targetId: Long? = null,
    var description: String? = null,
    var sysId: Long? = null,
    var id: Long? = null
) : Serializable