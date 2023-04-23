package de.fhdo.hmmm.backend

import de.fhdo.hmmm.backend.model.Microservice
import de.fhdo.hmmm.backend.model.ServiceStoryEdge
import de.fhdo.hmmm.backend.repository.MicroserviceRepository
import de.fhdo.hmmm.backend.service.ServiceStoryEdgeService
import de.fhdo.hmmm.backend.service.ServiceStoryService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension


//TODO THOSE TESTS ARE CURRENTLY BUGGED DUE TO A LAZYLOADING EXCEPTION WHEN SOTRING MICROSERVICES?!

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ServiceStoryAndEdgeTests {
    private val logger = LoggerFactory.getLogger(ServiceStoryAndEdgeTests::class.java)

    @Autowired
    lateinit var storyService: ServiceStoryService

    @Autowired
    lateinit var edgeService: ServiceStoryEdgeService

    @Autowired
    lateinit var microserviceRepo: MicroserviceRepository

    @Test
    fun contextLoads() {
        assertNotNull(storyService)
        assertNotNull(edgeService)
        assertNotNull(microserviceRepo)
    }

    @Test
    fun create() {
        //Create
        val sourceRaw = Microservice("ServiceA", sysId = 1)
        val targetRaw = Microservice("ServiceB", sysId = 1)
        val source = microserviceRepo.save(sourceRaw)
        val target = microserviceRepo.save(targetRaw)
        assertNotNull(source.id)
        assertNotNull(target.id)
        logger.info("Created microservices $source and $target with Repo.")
        val edgeRaw = ServiceStoryEdge(source.id, target.id)
        logger.info("Created edge $edgeRaw.")
        val edge = edgeService.create(source.id!!, target.id!!, "testDesc")
        assertEquals(source.id, edge!!.sourceId)
        assertEquals(target.id, edge!!.targetId)
        logger.info("Saved edge looks like this $edge.")
    }

    @Test
    fun createAndUpdateAndDelete() {
        //Create
        val source = microserviceRepo.save(Microservice("ServiceA", sysId = 1))
        val target = microserviceRepo.save(Microservice("ServiceB", sysId = 1))
        assertNotNull(source.id)
        assertNotNull(target.id)
        val edge = edgeService.create(source.id!!, target.id!!, "testDesc")
        assertEquals(source.id, edge!!.sourceId)
        assertEquals(target.id, edge!!.targetId)
        logger.info("Saved edge looks like this $edge")
        //Update
        val newTarget = microserviceRepo.save(Microservice("ServiceC", sysId = 1))
        edge.description = "this is a fine edge."
        edge.targetId = newTarget.id
        val changedEdge = edgeService.update(edge)
        assertEquals(changedEdge!!.id, edge.id)
        assertEquals(changedEdge.targetId, newTarget.id)
        assertNotNull(changedEdge.description)
        logger.info("Updated edge looks like this $changedEdge")
        //Delete
        assertTrue(edgeService.delete(changedEdge.id!!))
        assertNull(edgeService.read(changedEdge.id!!))
        //check if services still exist after delete
        assertNotNull(microserviceRepo.findById(source.id!!))
        assertNotNull(microserviceRepo.findById(target.id!!))
        assertNotNull(microserviceRepo.findById(newTarget.id!!))
    }

    @Test
    fun createAndInvalidUpdate() {
        //Create
        val source = microserviceRepo.save(Microservice("ServiceA", sysId = 1))
        val target = microserviceRepo.save(Microservice("ServiceB", sysId = 1))
        assertNotNull(source.id)
        assertNotNull(target.id)
        val edge = edgeService.create(source.id!!, target.id!!, "testDesc")
        assertEquals(source.id, edge!!.sourceId)
        assertEquals(target.id, edge!!.targetId)
        logger.info("Saved edge looks like this $edge")
        //Update
        val newTarget = microserviceRepo.save(Microservice("ServiceC", sysId = 1))
        edge.description = "this is a fine edge."
        edge.targetId = 123123
        val changedEdge = edgeService.update(edge)
        assertNull(changedEdge)
    }
}

