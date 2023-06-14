package de.fhdo.hmmm.backend.repository

import de.fhdo.hmmm.backend.model.Member
import de.fhdo.hmmm.backend.model.ModelArtifact
import org.springframework.data.jpa.repository.JpaRepository

interface ModelArtifactRepository : JpaRepository<ModelArtifact, Long> {
    fun findModelArtifactsBySysId(sysId : Long) : List<ModelArtifact>
    fun findModelArtifactsByMicroserviceId(serviceId : Long) : List<ModelArtifact>
}