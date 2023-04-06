package de.fhdo.hmmm.backend.dto

import java.io.Serializable

data class MemberCreateDto(
    val firstname: String,
    val lastname: String,
    val email: String,
) : Serializable
