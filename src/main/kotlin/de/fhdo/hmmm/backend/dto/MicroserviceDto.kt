package de.fhdo.hmmm.backend.dto

import java.io.Serializable

data class MicroserviceDto(
    var name: String? = null,
    var repositoryLink: String? = null,
    var modelIds: MutableSet<Long> = mutableSetOf(),
    var id: Long? = null
) : Serializable
