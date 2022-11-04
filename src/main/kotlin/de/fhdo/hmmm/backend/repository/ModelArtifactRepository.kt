package de.fhdo.hmmm.backend.repository

import de.fhdo.hmmm.backend.model.ModelArtifact
import org.springframework.data.jpa.repository.JpaRepository

interface ModelArtifactRepository : JpaRepository<ModelArtifact, Long> {
}