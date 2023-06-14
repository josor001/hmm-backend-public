package de.fhdo.hmmm.backend.dto.create

import java.io.Serializable

data class MemberCreateDto(
    val firstname: String,
    val lastname: String,
    val email: String,
    val sysId: Long,
) : Serializable
