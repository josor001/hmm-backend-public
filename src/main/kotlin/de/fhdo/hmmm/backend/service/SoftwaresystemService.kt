package de.fhdo.hmmm.backend.service

import de.fhdo.hmmm.backend.dto.OrganizationDto
import de.fhdo.hmmm.backend.dto.SoftwaresystemDto
import de.fhdo.hmmm.backend.model.Softwaresystem
import de.fhdo.hmmm.backend.repository.MicroserviceRepository
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
    val logger = LoggerFactory.getLogger(TeamService::class.java)

    @Autowired
    lateinit var systemRepo : SoftwaresystemRepository

    @Autowired
    lateinit var microserviceRepo : MicroserviceRepository

    /**
     * Adds a *Microservice* to the system. Microservice must have been persisted, i.e., contains an id.
     * @param systemId Identifier of the *Softwaresystem* that will add the *Microservice*.
     * @param serviceId Identifier of the Microservice that will be added.
     * @return *SoftwaresystemDto* of the updated *Softwaresystem* with added *Microservice* or *null*
     * if system or service could not be found by their ids.
     */
    fun addMicroservice(systemId : Long, serviceId : Long) : SoftwaresystemDto? {
        val foundSystem = systemRepo.findById(systemId)
        val foundService = microserviceRepo.findById(serviceId)
        if(foundSystem.isEmpty) {
            logger.info("Could not find system with id = $systemId")
            return null
        }
        if(foundService.isEmpty) {
            logger.info("Could not find microservice with id = $serviceId")
            return null
        }
        foundSystem.get().components.add(foundService.get())
        return Softwaresystem.toDto(systemRepo.save(foundSystem.get()))
    }

    fun removeMicroservice(systemId : Long, serviceId : Long) : SoftwaresystemDto? {
        TODO("NOT YET IMPLEMENTED")
    }
    /**
     * Creates a new *Softwaresystem* based on the given *name*.
     * @return Dto of the newly created team.
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
     * @return MutableSet of all *Softwaresystem*s as *SoftwaresystemDto*s.
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
                found.components.add(microserviceRepo.findById(it).get())
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