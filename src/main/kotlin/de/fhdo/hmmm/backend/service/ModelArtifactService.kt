package de.fhdo.hmmm.backend.service

import de.fhdo.hmmm.backend.dto.ModelArtifactDto
import de.fhdo.hmmm.backend.model.ModelArtifact
import de.fhdo.hmmm.backend.repository.MicroserviceRepository
import de.fhdo.hmmm.backend.repository.ModelArtifactRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


/**
 * The *ModelArtifactService* bridges *ModelArtifactDto*s and JPA-managed *ModelArtifact* entities. It serves as the
 * main entrance point for controller classes that want to interact with *ModelArtifact* entities.
 * @author Jonas Sorgalla
 */
@Service
class ModelArtifactService {
    val logger = LoggerFactory.getLogger(ModelArtifactService::class.java)

    @Autowired
    lateinit var modelArtifactRepo : ModelArtifactRepository

    @Autowired
    lateinit var microserviceRepo : MicroserviceRepository

    /**
     * Creates a new *ModelArtifact* based on the given *name*.
     * @return Dto of the newly created artifact.
     */
    fun create(name : String, kind: String, uri : String, microserviceId : Long) : ModelArtifactDto? {
        val foundMicroservice = microserviceRepo.findById(microserviceId)
        if(foundMicroservice.isEmpty)
            throw NoSuchElementException("No Microservice with id ${microserviceId} found.")
        val newModelArtifact = modelArtifactRepo.save(ModelArtifact(name, kind, uri, foundMicroservice.get()))
        return ModelArtifact.toDto(newModelArtifact)
    }
    // TODO CONTINUE HERE
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