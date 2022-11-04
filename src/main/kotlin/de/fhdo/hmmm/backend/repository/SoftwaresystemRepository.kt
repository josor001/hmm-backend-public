package de.fhdo.hmmm.backend.repository

import de.fhdo.hmmm.backend.model.Softwaresystem
import org.springframework.data.jpa.repository.JpaRepository

interface SoftwaresystemRepository : JpaRepository<Softwaresystem, Long> {
}