package de.fhdo.hmmm.backend.service

import de.fhdo.hmmm.backend.dto.ModelArtifactDto
import de.fhdo.hmmm.backend.model.Microservice
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
    fun create(name : String) : ModelArtifactDto? {
        val newService = modelArtifactRepo.save(ModelArtifact(name))
        return ModelArtifact.toDto(newService)
    }

    /**
     * Reads an existing *ModelArtifact* by its *id*.
     * @return *SoftwareArtifactDto* of the found artifact else returns null.
     */
    fun read(id : Long) : ModelArtifactDto? {
        val found = modelArtifactRepo.findById(id).orElse(null) ?: return null
        return ModelArtifact.toDto(found)
    }

    /**
     * Reads all existing *ModelArtifact*s.
     * @return Set of all *ModelArtifact*s as *ModelArtifactDto*s.
     */
    fun readAll() : MutableSet<ModelArtifactDto> {
        val retDto = mutableSetOf<ModelArtifactDto>()
        modelArtifactRepo.findAll().forEach { ModelArtifact.toDto(it)?.let { dto -> retDto.add(dto) } }
        return retDto
    }

    /**
     * Updates an existing *ModelArtifact* by its *ModelArtifactDto*. Entity must have been persisted, i.e., the dto
     * must contain an *id*.
     * @return A new Dto of the updated instance or null if the update was not successful
     */
    fun update(artifact : ModelArtifactDto) : ModelArtifactDto? {
        val found = artifact.id?.let { modelArtifactRepo.findById(it).orElse(null) }
        if(found != null) {
            found.name = artifact.name.orEmpty()
            found.kind = artifact.kind.orEmpty()
            found.location = artifact.location.orEmpty()
            if(artifact.microserviceId == null)
                throw NoSuchElementException("Id is not set in found service. Id is mandatory. Element seems corrupted.")
            else found.microservice = microserviceRepo.findById(artifact.microserviceId!!).get()
            return ModelArtifact.toDto(modelArtifactRepo.save(found))
        }
        return null
    }

    /**
     * Deletes an existing *ModelArtifact* by its *id*.
     * @return Returns true if *id* was found and the artifact got deleted else returns false.
     */
    fun delete(id : Long) : Boolean {
        if(modelArtifactRepo.findById(id).isPresent) {
            modelArtifactRepo.deleteById(id)
            return true
        } else {
            logger.debug("Could not find model artifact with id = $id")
            return false
        }
    }
}