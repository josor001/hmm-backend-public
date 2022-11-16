package de.fhdo.hmmm.backend.service

import de.fhdo.hmmm.backend.dto.MicroserviceDto
import de.fhdo.hmmm.backend.model.Microservice
import de.fhdo.hmmm.backend.repository.MicroserviceRepository
import de.fhdo.hmmm.backend.repository.ModelArtifactRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * The *MicroserviceService* bridges *MicroserviceDto*s and JPA-managed *Microservice* entities. It serves as the
 * main entrance point for controller classes that want to interact with *Microservice* entities.
 * @author Jonas Sorgalla
 */
@Service
class MicroserviceService {

    val logger = LoggerFactory.getLogger(MicroserviceService::class.java)

    @Autowired
    lateinit var microserviceRepo : MicroserviceRepository

    @Autowired
    lateinit var modelArtifactRepo : ModelArtifactRepository

    /**
     * Adds a *ModelArtifact* to a microservice. The artifact must have been persisted, i.e., it must contain an id.
     * @param serviceId Identifier of the Microservice that will add the *ModelArtifact*.
     * @param artifactId Identifier of the *ModelArtifact* that will be added to the *Microservice*.
     * @return *MicroserviceDto* of the updated *Microservice* with added *ModelArtifact*.
     * @throws NoSuchElementException if model or microservice objects with the given ids cannot be found.
     */
    fun addModelArtifact(serviceId : Long, artifactId : Long) : MicroserviceDto? {
        val foundService = microserviceRepo.findById(serviceId)
        val foundModel = modelArtifactRepo.findById(artifactId)
        if(foundService.isEmpty) {
            throw NoSuchElementException("No microservice with id ${serviceId} found.")
        }
        if(foundModel.isEmpty) {
            throw NoSuchElementException("No model artifact with id ${artifactId} found.")
        }
        foundService.get().models.add(foundModel.get())
        return Microservice.toDto(microserviceRepo.save(foundService.get()))
    }

    /**
     * Removes a *ModelArtifact* from a microservice.
     * @param serviceId Identifier of the Microservice that will add the *ModelArtifact*.
     * @param artifactId Identifier of the *ModelArtifact* that will be added to the *Microservice*.
     * @return True if removal was successful. Otherwise false.
     */
    fun removeModelArtifact(serviceId : Long, artifactId : Long) : Boolean {
        val foundService = microserviceRepo.findById(serviceId)
        val foundModel = modelArtifactRepo.findById(artifactId)
        if(foundService.isEmpty) {
            throw NoSuchElementException("No microservice with id ${serviceId} found.")
        }
        if(foundModel.isEmpty) {
            throw NoSuchElementException("No model artifact with id ${artifactId} found.")
        }
        if(foundService.get().models.remove(foundModel.get())) {
            microserviceRepo.save(foundService.get())
            return true
        } else return false
    }

    /**
     * Creates a new *Microservice* based on the given *name*.
     * @return Dto of the newly created microservice.
     */
    fun create(name : String) : MicroserviceDto? {
        val newService = microserviceRepo.save(Microservice(name))
        return Microservice.toDto(newService)
    }

    /**
     * Reads an existing *Microservice* by its *id*.
     * @return *MicroserviceDto* of the found microservice else returns null.
     */
    fun read(id : Long) : MicroserviceDto? {
        val found = microserviceRepo.findById(id).orElse(null) ?: return null
        return Microservice.toDto(found)
    }

    /**
     * Reads all existing *Microservice*s.
     * @return Set of all *Microservice*s as *MicroserviceDto*s.
     */
    fun readAll() : MutableSet<MicroserviceDto> {
        val retDto = mutableSetOf<MicroserviceDto>()
        microserviceRepo.findAll().forEach { Microservice.toDto(it)?.let { dto -> retDto.add(dto) } }
        return retDto
    }

    /**
     * Updates an existing *Microservice* by its *MicroserviceDto*. Entity must have been persisted, i.e., the dto
     * must contain an *id*. All fields including the associated model artifacts are simply taken from the dto.
     * @return A new Dto of the updated instance or null if the update was not successful
     */
    fun update(service : MicroserviceDto) : MicroserviceDto? {
        val found = service.id?.let { microserviceRepo.findById(it).orElse(null) }
        if(found != null) {
            found.name = service.name!!
            found.repositoryLink = service.repositoryLink
            found.models.clear()
            service.modelIds.forEach {
                // due to JPA the ref is maintained in the One-part of ManyToOne.
                // I.e. in this case it is necessary to update each artifact.
                val artifact = modelArtifactRepo.findById(it).get()
                artifact.microservice = found
                modelArtifactRepo.save(artifact)
            }
            return Microservice.toDto(microserviceRepo.save(found))
        }
        return null
    }

    /**
     * Deletes an existing *Microservice* by its *id*.
     * @return Returns true if *id* was found and the microservice got deleted else returns false.
     */
    fun delete(id : Long) : Boolean {
        if(microserviceRepo.findById(id).isPresent) {
            microserviceRepo.deleteById(id)
            return true
        } else {
            logger.debug("Could not find microservice with id = $id")
            return false
        }
    }
}