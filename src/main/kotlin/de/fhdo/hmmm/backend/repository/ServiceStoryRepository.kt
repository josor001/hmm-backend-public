package de.fhdo.hmmm.backend.repository

import de.fhdo.hmmm.backend.model.ServiceStory
import org.springframework.data.jpa.repository.JpaRepository

interface ServiceStoryRepository : JpaRepository<ServiceStory, Long> {
}