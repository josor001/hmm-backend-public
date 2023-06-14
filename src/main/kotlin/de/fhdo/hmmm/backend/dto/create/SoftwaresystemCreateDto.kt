package de.fhdo.hmmm.backend.dto.create

import java.io.Serializable

data class SoftwaresystemCreateDto(
    val name: String,
    val description: String,
) : Serializable