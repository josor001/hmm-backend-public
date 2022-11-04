package de.fhdo.hmmm.backend.repository

import de.fhdo.hmmm.backend.model.Team
import org.springframework.data.jpa.repository.JpaRepository

interface TeamRepository : JpaRepository<Team, Long> {
}