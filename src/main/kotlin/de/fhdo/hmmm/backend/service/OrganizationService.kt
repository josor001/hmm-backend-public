package de.fhdo.hmmm.backend.service

import de.fhdo.hmmm.backend.dto.OrganizationDto
import de.fhdo.hmmm.backend.dto.TeamDto
import de.fhdo.hmmm.backend.model.Organization
import de.fhdo.hmmm.backend.model.Softwaresystem
import de.fhdo.hmmm.backend.repository.OrganizationRepository
import de.fhdo.hmmm.backend.repository.SoftwaresystemRepository
import de.fhdo.hmmm.backend.repository.TeamRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * The *OrganizationService* bridges *OrganizationDto*s and JPA-managed *Organization* entities. It serves as the
 * main entrance point for controller classes that want to interact with *Organization* entities.
 * @author Jonas Sorgalla
 */
@Service
class OrganizationService {

    val logger = LoggerFactory.getLogger(OrganizationService::class.java)

    @Autowired
    lateinit var orgaRepo : OrganizationRepository

    @Autowired
    lateinit var systemRepo : SoftwaresystemRepository

    @Autowired
    lateinit var teamRepo : TeamRepository

    fun addTeam(orgaId : Long, teamId : Long) : OrganizationDto? {
        val foundOrga = orgaRepo.findById(orgaId)
        if(foundOrga.isEmpty)
            throw NoSuchElementException("No Organization with id ${orgaId} found.")
        val foundteam = teamRepo.findById(teamId)
        if(foundteam.isEmpty)
            throw NoSuchElementException("No Team with id ${teamId} found.")
        if(foundOrga.get().teams.add(foundteam.get()))
            return Organization.toDto(orgaRepo.save(foundOrga.get()))
        else throw Exception("Error while adding team with id ${teamId} to organization with id ${orgaId}.")
    }

    fun removeTeam(orgaId : Long, teamId : Long) : Boolean {
        val foundOrga = orgaRepo.findById(orgaId)
        val foundTeam = teamRepo.findById(teamId)
        if(foundOrga.isEmpty) {
            throw NoSuchElementException("No Organization with id ${orgaId} found.")
        }
        if(foundTeam.isEmpty) {
            throw NoSuchElementException("No Team with id ${teamId} found.")
        }
        if(foundOrga.get().teams.remove(foundTeam.get())) {
            orgaRepo.save(foundOrga.get())
            return true
        } else return false
    }

    fun linkSoftwaresystem(orgaId : Long, systemId : Long) : OrganizationDto? {
        val foundOrga = orgaRepo.findById(orgaId)
        if(foundOrga.isEmpty)
            throw NoSuchElementException("No Organization with id ${orgaId} found.")
        val foundSystem = systemRepo.findById(systemId)
        if(foundSystem.isEmpty)
            throw NoSuchElementException("No System with id ${systemId} found.")
        foundOrga.get().systemUnderDevelopment = foundSystem.get()
        return Organization.toDto(orgaRepo.save(foundOrga.get()))
    }

    fun unlinkSoftwaresystem(orgaId : Long) : Boolean {
        val foundOrga = orgaRepo.findById(orgaId)
        if(foundOrga.isEmpty)
            throw NoSuchElementException("No Organization with id ${orgaId} found.")
        foundOrga.get().systemUnderDevelopment = null
        if(orgaRepo.save(foundOrga.get()).systemUnderDevelopment == null) {
            return true
        } else return false

        TODO("CHECK CASCADE TYPES for all classes.")
        // In this case, the softwaresystem instance although not linked should not be deleted!
    }
    /**
     * Creates a new Organization based on the given name.
     * @return Dto of the newly created organization.
     */
    fun create(name : String) : OrganizationDto? {
        val newOrga = orgaRepo.save(Organization(name))
        return Organization.toDto(newOrga)
    }

    /**
     * Reads an existing organization by its Id.
     * @return Dto of the found organization else returns null.
     */
    fun read(id : Long) : OrganizationDto? {
        val found = orgaRepo.findById(id).orElse(null) ?: return null
        return Organization.toDto(found)
    }

    /**
     * Reads all existing organizations.
     * @return MutableSet of all Organizations as Dtos.
     */
    fun readAll() : MutableSet<OrganizationDto> {
        val retDto = mutableSetOf<OrganizationDto>()
        orgaRepo.findAll().forEach { Organization.toDto(it)?.let { dto -> retDto.add(dto) } }
        return retDto
    }

    /**
     * Updates an existing Organization by its Dto. Organization must have been persisted, i.e., the Dto
     * must contain an id. All fields including the associated teams are simply taken from the Dto.
     * @return A new Dto of the updated instance or null if the update was not successful
     */
    fun update(orga : OrganizationDto) : OrganizationDto? {
        val found = orga.id?.let { orgaRepo.findById(it).orElse(null) }
        if(found != null) {
            found.name = orga.name!!
            val system = orga.systemUnderDevelopmentId?.let { systemRepo.findById(it) }
            found.systemUnderDevelopment = system?.orElse(null)
            found.teams.clear()
            orga.teamIds.forEach {
                found.teams.add(teamRepo.findById(it).get())
            }
            return Organization.toDto(orgaRepo.save(found))
        }
        return null
    }

    /**
     * Deletes an existing organization by its Id.
     * @return Returns true if Id was found and the organization got deleted else returns false.
     */
    fun delete(id : Long) : Boolean {
        if(orgaRepo.findById(id).isPresent) {
            orgaRepo.deleteById(id)
            return true
        } else {
            logger.debug("Could not find organization with id = $id")
            return false
        }
    }
}