package de.fhdo.hmmm.backend.repository

import de.fhdo.hmmm.backend.model.Organization
import org.springframework.data.jpa.repository.JpaRepository

interface OrganizationRepository : JpaRepository<Organization, Long> {
}