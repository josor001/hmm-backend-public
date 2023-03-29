package de.fhdo.hmmm.backend.repository

import de.fhdo.hmmm.backend.model.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, Long> {
}