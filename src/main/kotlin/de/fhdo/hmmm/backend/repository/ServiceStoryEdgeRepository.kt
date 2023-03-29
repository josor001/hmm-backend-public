package de.fhdo.hmmm.backend.repository

import de.fhdo.hmmm.backend.model.ServiceStoryEdge
import org.springframework.data.jpa.repository.JpaRepository

interface ServiceStoryEdgeRepository : JpaRepository<ServiceStoryEdge, Long> {
}