package de.fhdo.hmmm.backend.dto

import java.io.Serializable

data class MicroserviceDto(
    var name: String? = null,
    var purpose: String? = null,
    var repositoryLink: String? = null,
    var contactPersonId: Long? = null,
    var plannedFeatures: MutableList<String> = mutableListOf(),
    var modelIds: MutableSet<Long> = mutableSetOf(),
    var sysId: Long? = null,
    var id: Long? = null
) : Serializable
