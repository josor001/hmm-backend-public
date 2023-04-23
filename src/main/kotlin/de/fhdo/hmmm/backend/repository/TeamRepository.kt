package de.fhdo.hmmm.backend.repository

import de.fhdo.hmmm.backend.model.Member
import de.fhdo.hmmm.backend.model.Microservice
import de.fhdo.hmmm.backend.model.Team
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface TeamRepository : JpaRepository<Team, Long> {
    fun findTeamsBySysId(sysId : Long) : List<Team>
    fun findTeamByOwnedMicroservicesContains(ms : Microservice) : Optional<Team>
    fun findTeamByMembersContains(mem : Member) : Optional<Team>
}