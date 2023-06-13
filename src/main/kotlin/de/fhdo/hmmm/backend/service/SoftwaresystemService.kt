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

    /**
     * Creates a new *Softwaresystem* based on the given *name* and *description*.
     * @return Dto of the newly created Softwaresystem.
     */
    fun create(name : String, description: String) : SoftwaresystemDto? {
        val newSystem = systemRepo.save(Softwaresystem(name = name, description = description))
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
            found.description = system.description!!
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