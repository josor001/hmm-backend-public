package de.fhdo.hmmm.backend.dto

import java.io.Serializable

data class TeamDto(
    var name: String? = null,
    val ownedMicroserviceIds: MutableSet<Long> = mutableSetOf(),
    val memberIds: MutableSet<Long> = mutableSetOf(),
    var sysId: Long? = null,
    var id: Long? = null
) : Serializable
