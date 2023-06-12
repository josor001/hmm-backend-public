package de.fhdo.hmmm.backend.dto

import java.io.Serializable

data class SoftwaresystemDto(
    var name: String? = null,
    var description: String? = null,
    var id: Long? = null
) : Serializable
