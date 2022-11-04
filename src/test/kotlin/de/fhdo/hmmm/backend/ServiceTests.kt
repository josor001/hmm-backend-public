package de.fhdo.hmmm.backend

import de.fhdo.hmmm.backend.model.Organization
import de.fhdo.hmmm.backend.repository.OrganizationRepository
import de.fhdo.hmmm.backend.service.OrganizationService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner


@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ServiceTests {
    private val logger = LoggerFactory.getLogger(ServiceTests::class.java)

    @Autowired
    lateinit var orgaService: OrganizationService

    @Autowired
    lateinit  var orgaRepo: OrganizationRepository

    @Test
    fun contextLoads() {
        assertNotNull(orgaService)
    }

    @Test
    fun createAndStoreOrganization_DeleteThroughService() {
        var org = Organization("MyTestOrga")
        org = orgaRepo.save(org)
        logger.info("Saved orga looks like this $org")
        assertNotNull(orgaRepo.findById(org.id!!))
        assertTrue(orgaService.delete(org.id!!))
        assertTrue(orgaRepo.findById(org.id!!).isEmpty)
    }

    @Test
    fun createAndStoreOrganization_TransformToDto() {
        var org = Organization("MyTestOrga")
        org = orgaRepo.save(org)
        logger.info("Saved orga looks like this $org")
        assertNotNull(orgaRepo.findById(org.id!!))
        val dto = Organization.toDto(org)
        logger.info(dto.toString())
        assertEquals(dto!!.name, org.name)
    }
}

