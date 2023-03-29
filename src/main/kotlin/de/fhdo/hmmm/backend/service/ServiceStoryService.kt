package de.fhdo.hmmm.backend.service

import de.fhdo.hmmm.backend.dto.ServiceStoryDto
import de.fhdo.hmmm.backend.model.ServiceStory
import de.fhdo.hmmm.backend.repository.MicroserviceRepository
import de.fhdo.hmmm.backend.repository.ServiceStoryEdgeRepository
import de.fhdo.hmmm.backend.repository.ServiceStoryRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * The *ServiceStoryService* bridges *ServiceStoryDto*s and JPA-managed *ServiceStoryService* entities. It serves as the
 * main entrance point for controller classes that want to interact with *ServiceStory* entities and provides
 * means to manage edges (*ServiceStoryEdges*) of a *ServiceStory*.
 * @author Jonas Sorgalla
 */
@Service
class ServiceStoryService {
    val logger = LoggerFactory.getLogger(ServiceStoryService::class.java)

    @Autowired
    lateinit var storyRepo : ServiceStoryRepository

    @Autowired
    lateinit var storyEdgeRepo : ServiceStoryEdgeRepository

    @Autowired
    lateinit var vertexRepo : MicroserviceRepository

    /**
     * Adds a *Microservice* to the story as a vertex. Microservice must have been persisted, i.e., contains an id.
     * @param storyId Identifier of the *ServiceStory* where the *Microservice* vertex shall be added.
     * @param serviceId Identifier of the Microservice that will be added.
     * @return *ServiceStoryDto* of the updated *ServiceStory* with added *Microservice*
     * @throws NoSuchElementException if story or microservice objects with the given ids cannot be found.
     */
    fun addVertice(storyId : Long, serviceId : Long) : ServiceStoryDto? {
        val foundStory = storyRepo.findById(storyId)
        val foundService = vertexRepo.findById(serviceId)
        if(foundStory.isEmpty) {
            throw NoSuchElementException("No ServiceStory with id ${storyId} found.")
        }
        if(foundService.isEmpty) {
            throw NoSuchElementException("No Microservice with id ${serviceId} found.")
        }
        foundStory.get().vertices.add(foundService.get())
        return ServiceStory.toDto(storyRepo.save(foundStory.get()))
    }

    /**
     * Removes a *Microservice* vertex from a *ServiceStory*.
     * @param storyId Identifier of the *ServiceStory* containg the to be removed vertex.
     * @param serviceId Identifier of the Microservice that shall be removed as vertex.
     * @return true or false depending on operation success
     * @throws NoSuchElementException if story or microservice objects with the given ids cannot be found.
     */
    fun removeVertex(storyId : Long, serviceId : Long) : Boolean {
        val foundStory = storyRepo.findById(storyId)
        val foundService = vertexRepo.findById(serviceId)
        if(foundStory.isEmpty) {
            throw NoSuchElementException("No System with id ${storyId} found.")
        }
        if(foundService.isEmpty) {
            throw NoSuchElementException("No Microservice with id ${serviceId} found.")
        }
        if(foundStory.get().vertices.remove(foundService.get())) {
            storyRepo.save(foundStory.get())
            return true
        } else return false
    }

    /**
     * Adds a *ServiceStoryEdge* to a ServiceStory. Edge as well as story must have been persisted previously, i.e.,
     * they need to be managed with an id.
     * @param storyId Identifier of the *ServiceStory*.
     * @param edgeId Identifier of the *ServiceStoryEdge*.
     * @return *ServiceStoryDto* of the updated *ServiceStory* with added *ServiceStoryEdge*
     * @throws NoSuchElementException if story or microservice objects with the given ids cannot be found.
     */
    fun addEdge(storyId : Long, edgeId : Long) : ServiceStoryDto? {
        //TODO hier sollte noch geprüft werden, ob die Knoten der Kante bereits der Story hinzugefügt sind!
        val foundStory = storyRepo.findById(storyId)
        val foundEdge = storyEdgeRepo.findById(edgeId)
        if(foundStory.isEmpty) {
            throw NoSuchElementException("No ServiceStory with id ${storyId} found.")
        }
        if(foundEdge.isEmpty) {
            throw NoSuchElementException("No ServiceStoryEdge with id ${edgeId} found.")
        }
        foundStory.get().directedEdges.add(foundEdge.get())
        return ServiceStory.toDto(storyRepo.save(foundStory.get()))
    }

    /**
     * Removes a *ServiceStoryEdge* from a *ServiceStory*. The orphaned edge gets deleted in the process.
     * @param storyId Identifier of the *ServiceStory*.
     * @param edgeId Identifier of the *ServiceStoryEdge*.
     * @return true or false depending on operation success.
     * @throws NoSuchElementException if story or microservice objects with the given ids cannot be found.
     */
    fun removeEdge(storyId : Long, edgeId : Long) : Boolean {
        val foundStory = storyRepo.findById(storyId)
        val foundEdge = storyEdgeRepo.findById(edgeId)
        if(foundStory.isEmpty) {
            throw NoSuchElementException("No System with id ${storyId} found.")
        }
        if(foundEdge.isEmpty) {
            throw NoSuchElementException("No ServiceStoryEdge with id ${edgeId} found.")
        }
        if(foundStory.get().directedEdges.remove(foundEdge.get())) {
            storyRepo.save(foundStory.get())
            storyEdgeRepo.deleteById(foundEdge.get().id!!)
            return true
        } else return false
    }

    /**
     * Creates a new *ServiceStory* based on the given *name*.
     * @return Dto of the newly created team.
     */
    fun create(name : String) : ServiceStoryDto? {
        val newStory = storyRepo.save(ServiceStory(name))
        return ServiceStory.toDto(newStory)
    }

    /**
     * Reads an existing *ServiceStory* by its *id*.
     * @return *ServiceStoryDto* of the found story else returns null.
     */
    fun read(id : Long) : ServiceStoryDto? {
        val found = storyRepo.findById(id).orElse(null) ?: return null
        return ServiceStory.toDto(found)
    }

    /**
     * Reads all existing *ServiceStory*s.
     * @return Set of all *ServiceStory*s as *ServiceStoryDto*s.
     */
    fun readAll() : MutableSet<ServiceStoryDto> {
        val retDto = mutableSetOf<ServiceStoryDto>()
        storyRepo.findAll().forEach { ServiceStory.toDto(it)?.let { dto -> retDto.add(dto) } }
        return retDto
    }

    /**
     * Updates an existing *ServiceStory* by its *ServiceStoryDto*. Entity must have been persisted, i.e., the dto
     * must contain an *id*. All fields including the associated components are simply taken from the dto.
     * @return A new Dto of the updated instance or null if the update was not successful.
     */
    fun update(story : ServiceStoryDto) : ServiceStoryDto? {
        val found = story.id?.let { storyRepo.findById(it).orElse(null) }
        if(found != null) {
            found.name = story.name!!
            found.description = story.description
            found.directedEdges.clear()
            story.directedEdgesIds.forEach {
                //TODO Check if this works or JPA struggles with OneToMany maintained in Many part.
                found.directedEdges.add(storyEdgeRepo.findById(it).get())
            }

            found.vertices.clear()
            story.verticesIds.forEach {
                //TODO Check if this works or JPA struggles with OneToMany maintained in Many part.
                found.vertices.add(vertexRepo.findById(it).get())
            }
            return ServiceStory.toDto(storyRepo.save(found))
        }
        return null
    }

    /**
     * Deletes an existing *ServiceStory* by its *id*.
     * @return Returns true if *id* was found and the story got deleted else returns false.
     */
    fun delete(id : Long) : Boolean {
        if(storyRepo.findById(id).isPresent) {
            storyRepo.deleteById(id)
            return true
        } else {
            logger.debug("Could not find service story with id = $id")
            return false
        }
    }
}