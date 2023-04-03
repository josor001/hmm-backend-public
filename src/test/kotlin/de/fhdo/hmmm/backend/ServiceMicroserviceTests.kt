package de.fhdo.hmmm.backend

import de.fhdo.hmmm.backend.model.Microservice
import de.fhdo.hmmm.backend.repository.MicroserviceRepository
import de.fhdo.hmmm.backend.service.MicroserviceService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension


@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ServiceMicroserviceTests {
    private val logger = LoggerFactory.getLogger(ServiceMicroserviceTests::class.java)

    @Autowired
    lateinit var msService: MicroserviceService

    @Autowired
    lateinit  var msRepo: MicroserviceRepository

    @Test
    fun contextLoads() {
        assertNotNull(msService)
        assertNotNull(msRepo)
    }

    @Test
    fun createThroughRepository() {
        val ms = Microservice("MyService")
        val msPersisted = msRepo.save(ms)
        assertNotNull(msPersisted.id)
        logger.info("Created $msPersisted with repository.")
    }

    @Test
    fun createThroughService() {
        val msDto = msService.create("MyTestService")
        assertNotNull(msDto)
        logger.info("Created microservice and received the following dto $msDto with service.")
    }

}

