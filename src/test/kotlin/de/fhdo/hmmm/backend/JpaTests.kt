package de.fhdo.hmmm.backend

import de.fhdo.hmmm.backend.model.Organization
import de.fhdo.hmmm.backend.model.Softwaresystem
import de.fhdo.hmmm.backend.model.Team
import de.fhdo.hmmm.backend.repository.OrganizationRepository
import de.fhdo.hmmm.backend.repository.SoftwaresystemRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager


@DataJpaTest
class JpaTests {
    private val logger = LoggerFactory.getLogger(JpaTests::class.java)

    @Autowired
    lateinit var em: TestEntityManager

    @Autowired
    lateinit var systemRepo: SoftwaresystemRepository

    @Autowired
    lateinit  var orgaRepo: OrganizationRepository

    @Test
    fun contextLoads() {
        Assertions.assertNotNull(em)
    }

    @Test
    fun verifyBootstrappingByPersistingAnOrganization() {
        val org = Organization("MyTestOrga")
        Assertions.assertNull(org.id)
        em.persist<Any>(org)
        logger.info("Id of persisted org is ${org.id}")
        Assertions.assertNotNull(org.id)
    }

    @Test
    fun linkingOrgaAndSystem() {
        var org = Organization("MyTestOrga")
        val system = Softwaresystem("SuperSystem")
        org.systemUnderDevelopment = system
        org = em.persist(org)
        logger.info("Id of persisted org is ${org.id}")
        assertNotNull(org.systemUnderDevelopment)
        logger.info("Id of persisted system of org is ${org.systemUnderDevelopment!!.id}")
        logger.info(org.toString())
    }
    @Test
    fun storeMultipleTeamsInOrga() {
        var org = Organization("MyTestOrga")
        val system = Softwaresystem("SuperSystem")
        org.systemUnderDevelopment = system
        org.teams.add(Team("Team1"))
        org.teams.add(Team("Team2"))
        org = em.persist(org)
        logger.info("Id of persisted org is ${org.id}")
        assertNotNull(org.systemUnderDevelopment)
        assertTrue(org.teams.size == 2)
        logger.info(org.toString())
    }
}

