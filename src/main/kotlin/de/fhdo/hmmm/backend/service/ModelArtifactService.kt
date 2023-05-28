package de.fhdo.hmmm.backend.service

import de.fhdo.hmmm.backend.dto.MemberDto
import de.fhdo.hmmm.backend.dto.MicroserviceDto
import de.fhdo.hmmm.backend.dto.ModelArtifactDto
import de.fhdo.hmmm.backend.model.Member
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
     * @throws NoSuchElementException if serviceId points to a non-existing microservice.
     * @throws IllegalArgumentException if any field is left empty.
     */
    fun create(name : String, kind : String, location : String, serviceId: Long, sysId: Long) : ModelArtifactDto? {
        val foundService = microserviceRepo.findById(serviceId)
        if(name.isNullOrEmpty() || kind.isNullOrEmpty() || location.isNullOrEmpty() || serviceId == null || sysId == null)
            throw IllegalArgumentException("A field is null or empty. Please fill all fields.")
        if(foundService.isEmpty)
            throw NoSuchElementException("No microservice with id $serviceId found.")
        val newArtifact = modelArtifactRepo.save(ModelArtifact(
            name = name,
            kind = kind,
            location = location,
            microserviceId = serviceId,
            sysId = sysId))
        return ModelArtifact.toDto(newArtifact)
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
     * Reads *ModelArtifact*s that are a part of specific system.
     * @param sysId of the system of which the artifact is a part of.
     * @return Set of *ModelArtifact*s as *ModelArtifactDto*s.
     */
    fun readAllBySysId(sysId : Long) : MutableSet<ModelArtifactDto> {
        val retDto = mutableSetOf<ModelArtifactDto>()
        modelArtifactRepo.findModelArtifactsBySysId(sysId).forEach {
            ModelArtifact.toDto(it)?.let { dto -> retDto.add(dto) }
        }
        return retDto
    }

    /**
     * Reads *ModelArtifact*s that are related to a specific microservice.
     * @param serviceId of the microservice of which the artifact is a part of.
     * @return Set of *ModelArtifact*s as *ModelArtifactDto*s.
     */
    fun readAllByServiceId(serviceId : Long) : MutableSet<ModelArtifactDto> {
        val retDto = mutableSetOf<ModelArtifactDto>()
        modelArtifactRepo.findModelArtifactsByMicroserviceId(serviceId).forEach {
            ModelArtifact.toDto(it)?.let { dto -> retDto.add(dto) }
        }
        return retDto
    }


    /**
     * Updates an existing *ModelArtifact* by its *ModelArtifactDto*. Entity must have been persisted, i.e., the dto
     * must contain an *id*. The sysId of the ModelArtifact is intentionally not updated through this method.
     * @return A new Dto of the updated instance or null if the update was not successful.
     * @throws IllegalArgumentException if name is left empty.
     * @throws NoSuchElementException if no fitting microservice was found by microserviceId.
     */
    fun update(artifact : ModelArtifactDto) : ModelArtifactDto? {
        val found = artifact.id?.let { modelArtifactRepo.findById(it).orElse(null) }
        if(found != null) {
            if(artifact.name.isNullOrEmpty())
                throw IllegalArgumentException("name is not allowed to be empty.")
            if(artifact.microserviceId == null || !(microserviceRepo.existsById(artifact.microserviceId!!)))
                throw NoSuchElementException("Element seems corrupted. No microservice with given id found.")
            found.name = artifact.name.orEmpty()
            found.kind = artifact.kind.orEmpty()
            found.location = artifact.location.orEmpty()
            //sys Id is always taken from found and cannot be changed through this method
            //found.sysId = artifact.sysId
            found.microserviceId = artifact.microserviceId!!
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
            throw NoSuchElementException("Element seems corrupted. No model artifact with given id $id found.")
        }
    }
}