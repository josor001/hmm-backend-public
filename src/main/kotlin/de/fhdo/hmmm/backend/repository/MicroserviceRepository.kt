package de.fhdo.hmmm.backend.repository

import de.fhdo.hmmm.backend.model.Microservice
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface MicroserviceRepository : JpaRepository<Microservice, Long> {
    fun findMicroservicesBySysId(sysId: Long) : List<Microservice>
    fun findMicroserviceByContactPerson_Id(contactId: Long) : Optional<Microservice>
}