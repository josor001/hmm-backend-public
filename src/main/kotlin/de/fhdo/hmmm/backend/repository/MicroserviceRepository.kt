package de.fhdo.hmmm.backend.repository

import de.fhdo.hmmm.backend.model.Microservice
import org.springframework.data.jpa.repository.JpaRepository

interface MicroserviceRepository : JpaRepository<Microservice, Long> {
}