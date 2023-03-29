package de.fhdo.hmmm.backend.service

import de.fhdo.hmmm.backend.dto.OrganizationDto
import de.fhdo.hmmm.backend.dto.SoftwaresystemDto
import de.fhdo.hmmm.backend.model.Softwaresystem
import de.fhdo.hmmm.backend.repository.MicroserviceRepository
import de.fhdo.hmmm.backend.repository.ServiceStoryRepository
import de.fhdo.hmmm.backend.repository.SoftwaresystemRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * The *SoftwaresystemService* bridges *SoftwaresystemDto*s and JPA-managed *Softwaresystem* entities. It serves as the
 * main entrance point for controller classes that want to interact with *Softwaresystem* entities.
 * @author Jonas Sorgalla
 */
@Service
class SoftwaresystemService {
    val logger = LoggerFactory.getLogger(SoftwaresystemService::class.java)

    @Autowired
    lateinit var systemRepo : SoftwaresystemRepository

    @Autowired
    lateinit var microserviceRepo : MicroserviceRepository

    @Autowired
    lateinit var storyRepo : ServiceStoryRepository

    /**
     * Adds a *Microservice* to the system. Microservice must have been persisted, i.e., contains an id.
     * @param systemId Identifier of the *Softwaresystem* that will add the *Microservice*.
     * @param serviceId Identifier of the Microservice that will be added.
     * @return *SoftwaresystemDto* of the updated *Softwaresystem* with added *Microservice*
     * @throws NoSuchElementException if system or microservice objects with the given ids cannot be found.
     */
    fun addMicroservice(systemId : Long, serviceId : Long) : SoftwaresystemDto? {
        val foundSystem = systemRepo.findById(systemId)
        val foundService = microserviceRepo.findById(serviceId)
        if(foundSystem.isEmpty) {
            throw NoSuchElementException("No System with id ${systemId} found.")
        }
        if(foundService.isEmpty) {
            throw NoSuchElementException("No Microservice with id ${serviceId} found.")
        }
        foundSystem.get().components.add(foundService.get())
        return Softwaresystem.toDto(systemRepo.save(foundSystem.get()))
    }

    /**
     * Removes a *Microservice* from a *Softwaresystem*.
     * @param systemId Identifier of the *Softwaresystem*.
     * @param serviceId Identifier of the *Microservice*.
     * @return true or false regarding operation success.
     * @throws NoSuchElementException if system or microservice objects with the given ids cannot be found.
     */
    fun removeMicroservice(systemId : Long, serviceId : Long) : Boolean {
        val foundSystem = systemRepo.findById(systemId)
        val foundService = microserviceRepo.findById(serviceId)
        if(foundSystem.isEmpty) {
            throw NoSuchElementException("No System with id ${systemId} found.")
        }
        if(foundService.isEmpty) {
            throw NoSuchElementException("No Microservice with id ${serviceId} found.")
        }
        if(foundSystem.get().components.remove(foundService.get())) {
            systemRepo.save(foundSystem.get())
            return true
        } else return false
    }

    /**
     * Adds a *ServiceStory* to a *Softwaresystem*. ServiceStory must have been persisted, i.e., it contains an id.
     * @param systemId Identifier of the *Softwaresystem*.
     * @param storyId Identifier of the *ServiceStory*.
     * @return *SoftwaresystemDto* of the updated *Softwaresystem*.
     * @throws NoSuchElementException if system or service story objects with the given ids cannot be found.
     */
    fun addServiceStory(systemId : Long, storyId : Long) : SoftwaresystemDto? {
        val foundSystem = systemRepo.findById(systemId)
        val foundStory = storyRepo.findById(storyId)
        if(foundSystem.isEmpty) {
            throw NoSuchElementException("No System with id ${systemId} found.")
        }
        if(foundStory.isEmpty) {
            throw NoSuchElementException("No ServiceStory with id ${storyId} found.")
        }
        foundSystem.get().stories.add(foundStory.get())
        return Softwaresystem.toDto(systemRepo.save(foundSystem.get()))
    }

    /**
     * Removes a *ServiceStory* from a *Softwaresystem*.
     * @param systemId Identifier of the *Softwaresystem*.
     * @param storyId Identifier of the *ServiceStory*.
     * @return true or false regarding operation success.
     * @throws NoSuchElementException if system or service story objects with the given ids cannot be found.
     */
    fun removeServiceStory(systemId : Long, storyId : Long) : Boolean {
        val foundSystem = systemRepo.findById(systemId)
        val foundStory = storyRepo.findById(storyId)
        if(foundSystem.isEmpty) {
            throw NoSuchElementException("No System with id ${systemId} found.")
        }
        if(foundStory.isEmpty) {
            throw NoSuchElementException("No ServiceStory with id ${storyId} found.")
        }
        if(foundSystem.get().stories.remove(foundStory.get())) {
            systemRepo.save(foundSystem.get())
            return true
        } else return false
    }


    /**
     * Creates a new *Softwaresystem* based on the given *name*.
     * @return Dto of the newly created Softwaresystem.
     */
    fun create(name : String) : SoftwaresystemDto? {
        val newSystem = systemRepo.save(Softwaresystem(name))
        return Softwaresystem.toDto(newSystem)
    }

    /**
     * Reads an existing *Softwaresystem* by its *id*.
     * @return *SoftwaresystemDto* of the found system else returns null.
     */
    fun read(id : Long) : SoftwaresystemDto? {
        val found = systemRepo.findById(id).orElse(null) ?: return null
        return Softwaresystem.toDto(found)
    }

    /**
     * Reads all existing *Softwaresystem*s.
     * @return Set of all *Softwaresystem*s as *SoftwaresystemDto*s.
     */
    fun readAll() : MutableSet<SoftwaresystemDto> {
        val retDto = mutableSetOf<SoftwaresystemDto>()
        systemRepo.findAll().forEach { Softwaresystem.toDto(it)?.let { dto -> retDto.add(dto) } }
        return retDto
    }

    /**
     * Updates an existing *Softwaresystem* by its *SoftwaresystemDto*. Entity must have been persisted, i.e., the dto
     * must contain an *id*. All fields including the associated components are simply taken from the dto.
     * @return A new Dto of the updated instance or null if the update was not successful
     */
    fun update(system : SoftwaresystemDto) : SoftwaresystemDto? {
        val found = system.id?.let { systemRepo.findById(it).orElse(null) }
        if(found != null) {
            found.name = system.name!!
            found.components.clear()
            system.componentIds.forEach {
                //TODO Check if this works or JPA struggles with OneToMany maintained in Many part.
                found.components.add(microserviceRepo.findById(it).get())
            }
            found.stories.clear()
            system.storyIds.forEach {
                //TODO Check if this works or JPA struggles with OneToMany maintained in Many part.
                found.stories.add(storyRepo.findById(it).get())
            }
            return Softwaresystem.toDto(systemRepo.save(found))
        }
        return null
    }

    /**
     * Deletes an existing *Softwaresystem* by its *id*.
     * @return Returns true if *id* was found and the system got deleted else returns false.
     */
    fun delete(id : Long) : Boolean {
        if(systemRepo.findById(id).isPresent) {
            systemRepo.deleteById(id)
            return true
        } else {
            logger.debug("Could not find softwaresystem with id = $id")
            return false
        }
    }
}