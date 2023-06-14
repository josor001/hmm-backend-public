package de.fhdo.hmmm.backend.dto.create

import java.io.Serializable

data class ModelArtifactCreateDto(
    val name: String,
    val kind: String,
    val location: String,
    val microserviceId: Long,
    val sysId: Long,
) : Serializable