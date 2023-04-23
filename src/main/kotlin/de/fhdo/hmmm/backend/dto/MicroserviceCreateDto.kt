package de.fhdo.hmmm.backend.dto

import java.io.Serializable

data class MicroserviceCreateDto(
    val name: String,
    val sysId: Long,
) : Serializable
