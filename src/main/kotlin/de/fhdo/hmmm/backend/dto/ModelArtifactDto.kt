package de.fhdo.hmmm.backend.dto

import de.fhdo.hmmm.backend.model.ModelKind
import java.io.Serializable

data class ModelArtifactDto(
    var name: String? = null,
    var kind: ModelKind? = null,
    var relativeLocation: String? = null,
    var microserviceId: Long? = null,
    var id: Long? = null
) : Serializable
