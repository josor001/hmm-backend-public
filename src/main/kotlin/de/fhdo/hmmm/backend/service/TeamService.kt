package de.fhdo.hmmm.backend.service

import de.fhdo.hmmm.backend.dto.SoftwaresystemDto
import de.fhdo.hmmm.backend.dto.TeamDto
import de.fhdo.hmmm.backend.model.Team
import de.fhdo.hmmm.backend.repository.MicroserviceRepository
import de.fhdo.hmmm.backend.repository.TeamRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * The *TeamService* bridges *TeamDto*s and JPA-managed *Team* entities. It serves as the
 * main entrance point for controller classes that want to interact with *Team* entities.
 * @author Jonas Sorgalla
 */
@Service
class TeamService {
    val logger = LoggerFactory.getLogger(TeamService::class.java)

    @Autowired
    lateinit var teamRepo : TeamRepository

    @Autowired
    lateinit var microserviceRepo : MicroserviceRepository

    /**
     * Adds a *Microservice* to the team. Microservice must have been persisted, i.e., contains an id.
     * @param teamId Identifier of the team that will own the *Microservice*.
     * @param serviceId Identifier of the Microservice that will be owned.
     * @return *TeamDto* of the updated *Team* with added *Microservice* or *null*
     * if team or service could not be found by their ids.
     */
    fun addMicroservice(teamId : Long, serviceId : Long) : TeamDto? {
        val foundTeam = teamRepo.findById(teamId)
        val foundService = microserviceRepo.findById(serviceId)
        if(foundTeam.isEmpty) {
            logger.info("Could not find team with id = $teamId")
            return null
        }
        if(foundService.isEmpty) {
            logger.info("Could not find microservice with id = $serviceId")
            return null
        }
        foundTeam.get().ownedMicroservices.add(foundService.get())
        return Team.toDto(teamRepo.save(foundTeam.get()))
    }

    fun removeMicroservice(teamId : Long, serviceId : Long) : SoftwaresystemDto? {
        TODO("NOT YET IMPLEMENTED")
    }
    /**
     * Creates a new *Team* based on the given *name*.
     * @return *TeamDto* of the newly created team.
     */
    fun create(name : String) : TeamDto? {
        val newTeam = teamRepo.save(Team(name))
        return Team.toDto(newTeam)
    }

    /**
     * Reads an existing *Team* by its *id*.
     * @return *TeamDto* of the found team else returns null.
     */
    fun read(id : Long) : TeamDto? {
        val found = teamRepo.findById(id).orElse(null) ?: return null
        return Team.toDto(found)
    }

    /**
     * Reads all existing *Team*s.
     * @return MutableSet of all Teams as *TeamDto*s.
     */
    fun readAll() : MutableSet<TeamDto> {
        val retDto = mutableSetOf<TeamDto>()
        teamRepo.findAll().forEach { Team.toDto(it)?.let { dto -> retDto.add(dto) } }
        return retDto
    }

    /**
     * Updates an existing *Team* by its *TeamDto*. Team must have been persisted, i.e., the Dto
     * must contain an *id* set by the backend.
     * All fields including the associated microservices are simply copied from the *TeamDto*.
     * @return A new *TeamDto* of the updated instance or null if the update was not successful.
     */
    fun update(team : TeamDto) : TeamDto? {
        val found = team.id?.let { teamRepo.findById(it).orElse(null) }
        if(found != null) {
            found.name = team.name!!
            found.ownedMicroservices.clear()
            team.ownedMicroserviceIds.forEach {
                found.ownedMicroservices.add(microserviceRepo.findById(it).get())
            }
            return Team.toDto(teamRepo.save(found))
        }
        return null
    }

    /**
     * Deletes an existing *Team* by its *id*.
     * @return Returns true if a Team with the *id* exists got successfully deleted else returns false.
     */
    fun delete(id : Long) : Boolean {
        if(teamRepo.findById(id).isPresent) {
            teamRepo.deleteById(id)
            return true
        } else {
            logger.debug("Could not find team with id = $id")
            return false
        }
    }
}