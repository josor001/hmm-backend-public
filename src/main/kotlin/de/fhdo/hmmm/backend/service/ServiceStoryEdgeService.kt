package de.fhdo.hmmm.backend.service

import de.fhdo.hmmm.backend.dto.OrganizationDto
import de.fhdo.hmmm.backend.dto.ServiceStoryEdgeDto
import de.fhdo.hmmm.backend.dto.TeamDto
import de.fhdo.hmmm.backend.model.Organization
import de.fhdo.hmmm.backend.model.ServiceStoryEdge
import de.fhdo.hmmm.backend.model.Softwaresystem
import de.fhdo.hmmm.backend.repository.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * The *ServiceStoryEdgeService* bridges *ServiceStoryEdgeDto*s and JPA-managed *ServiceStoryEdge* entities.
 * It serves as the main entrance point for controller classes that want to interact with *ServiceStoryEdge* entities.
 * @author Jonas Sorgalla
 */
@Service
class ServiceStoryEdgeService {

    val logger = LoggerFactory.getLogger(ServiceStoryEdgeService::class.java)

    @Autowired
    lateinit var edgeRepo : ServiceStoryEdgeRepository

    @Autowired
    lateinit var serviceRepo : MicroserviceRepository

    /**
     * Creates a new ServiceStoryEdge based on the source and target vertices, i.e., microservices.
     * @return *ServiceStoryEdgeDto* Dto of the newly created ServiceStoryEdge.
     */
    fun create(sourceId : Long, targetId : Long) : ServiceStoryEdgeDto? {
        val foundSource = serviceRepo.findById(sourceId)
        val foundTarget = serviceRepo.findById(targetId)
        if(foundSource.isEmpty) {
            throw NoSuchElementException("No Microservice with id ${sourceId} as source found.")
        }
        if(foundTarget.isEmpty) {
            throw NoSuchElementException("No Microservice with id ${targetId} as target found.")
        }
        val newEdge = edgeRepo.save(ServiceStoryEdge(foundSource.get(), foundTarget.get()))
        return ServiceStoryEdge.toDto(newEdge)
    }

    /**
     * Reads an existing ServiceStoryEdge by its Id.
     * @return Dto of the found ServiceStoryEdge else returns null.
     */
    fun read(id : Long) : ServiceStoryEdgeDto? {
        val found = edgeRepo.findById(id).orElse(null) ?: return null
        return ServiceStoryEdge.toDto(found)
    }

    /**
     * Updates an existing ServiceStoryEdge by its Dto. ServiceStoryEdge must have been persisted, i.e., the Dto
     * must contain an id. All fields are simply taken from the Dto.
     * @return A new Dto of the updated instance or null if the update was not successful
     */
    fun update(edge : ServiceStoryEdgeDto) : ServiceStoryEdgeDto? {
        val found = edge.id?.let { edgeRepo.findById(it).orElse(null) }
        if(found != null) {
            found.description = edge.description
            val source = edge.sourceId?.let { serviceRepo.findById(it) }
            val target = edge.targetId?.let { serviceRepo.findById(it) }
            found.source = source?.orElseThrow()
            found.target = target?.orElseThrow()

            return ServiceStoryEdge.toDto(edgeRepo.save(found))
        }
        return null
    }

    /**
     * Deletes an existing ServiceStoryEdge by its Id.
     * @return Returns true if Id was found and the ServiceStoryEdge got deleted else returns false.
     */
    fun delete(id : Long) : Boolean {
        if(edgeRepo.findById(id).isPresent) {
            edgeRepo.deleteById(id)
            return true
        } else {
            logger.debug("Could not find ServiceStoryEdge with id = $id")
            return false
        }
    }
}