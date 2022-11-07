package de.fhdo.hmmm.backend

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import de.fhdo.hmmm.backend.dto.OrganizationDto
import de.fhdo.hmmm.backend.dto.SoftwaresystemDto
import de.fhdo.hmmm.backend.service.*
import org.slf4j.LoggerFactory
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.test.web.reactive.server.WebTestClient

/**
 * This test class performs tests regarding the OrganizationController class by mocking the services.
 * This is constrained by not being able to properly store objects and receiving Ids from JPA.
 * This needs to be revamped in the future #TODO .
 */
@WebFluxTest
class OrganizationControllerTests(@Autowired val client: WebTestClient) {

    private val logger = LoggerFactory.getLogger(OrganizationControllerTests::class.java)

    // Apparently it is necessary to load mock all services if they have inter-service dependencies!
    @MockkBean
    lateinit var organizationService: OrganizationService
    @MockkBean
    lateinit var microserviceService: MicroserviceService
    @MockkBean
    lateinit var modelArtifactService: ModelArtifactService
    @MockkBean
    lateinit var softwaresystemService: SoftwaresystemService
    @MockkBean
    lateinit var teamService: TeamService

    val mapper = jacksonObjectMapper()
    val testOrga = OrganizationDto("NewOrg" )

    @Test
    fun givenExistingOrganization_whenGetRequest_thenReturnsOrganizationWithStatus200() {
        every { organizationService.read(1) } returns testOrga;

        val result = client.get().uri("/organizations/1").exchange()
            .expectStatus().isOk
            .returnResult(OrganizationDto::class.java)

        logger.info("READ ME ${result.toString()}")
    }

}