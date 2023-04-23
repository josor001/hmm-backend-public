package de.fhdo.hmmm.backend.dto

import java.io.Serializable

data class TeamCreateDto(
    val name: String,
    val sysId: Long,
) : Serializable
