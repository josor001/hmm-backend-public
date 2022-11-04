package de.fhdo.hmmm.backend.dto

import java.io.Serializable

data class OrganizationDto(
    var name: String? = null,
    var systemUnderDevelopmentId: Long? = null,
    val teamIds: MutableSet<Long> = mutableSetOf(),
    var id: Long? = null
) : Serializable
