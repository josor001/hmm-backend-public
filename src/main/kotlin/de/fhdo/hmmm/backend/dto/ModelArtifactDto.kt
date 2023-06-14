package de.fhdo.hmmm.backend.dto

import java.io.Serializable

data class ModelArtifactDto(
    var name: String? = null,
    var kind: String? = null,
    var microserviceId: Long? =null,
    var location: String? = null,
    var sysId: Long? = null,
    var id: Long? = null
) : Serializable
