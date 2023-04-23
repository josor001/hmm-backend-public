package de.fhdo.hmmm.backend.repository

import de.fhdo.hmmm.backend.model.Microservice
import de.fhdo.hmmm.backend.model.ServiceStory
import de.fhdo.hmmm.backend.model.ServiceStoryEdge
import org.springframework.data.jpa.repository.JpaRepository

interface ServiceStoryRepository : JpaRepository<ServiceStory, Long> {
    fun findServiceStoriesBySysId(sysId: Long) : List<ServiceStory>
    fun findServiceStoriesByVerticesContains(ms: Microservice) : List<ServiceStory>
    fun findServiceStoriesByDirectedEdgesContains(edge: ServiceStoryEdge) : List<ServiceStory>
}