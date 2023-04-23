package de.fhdo.hmmm.backend.service

import de.fhdo.hmmm.backend.dto.TeamDto
import de.fhdo.hmmm.backend.model.Team
import de.fhdo.hmmm.backend.repository.MemberRepository
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
    lateinit var memberRepo : MemberRepository

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

    fun removeMicroservice(teamId : Long, serviceId : Long) : Boolean? {
        val foundTeam = teamRepo.findById(teamId)
        val foundService = microserviceRepo.findById(serviceId)
        if(foundTeam.isEmpty) {
            throw NoSuchElementException("No Team with id $teamId found.")
        }
        if(foundService.isEmpty) {
            throw NoSuchElementException("No Microservice with id $serviceId found.")
        }
        return if(foundTeam.get().ownedMicroservices.remove(foundService.get())) {
            teamRepo.save(foundTeam.get())
            true
        } else false
    }

    /**
     * Adds a *Member* to the team. Member must have been persisted, i.e., contains an id.
     * @param teamId Identifier of the team that will have the *Member*.
     * @param memberId Identifier of the Member that will be added.
     * @return *TeamDto* of the updated *Team* with added *Member* or *null*
     * if team or member could not be found by their ids.
     */
    fun addMember(teamId : Long, memberId : Long) : TeamDto? {
        val foundTeam = teamRepo.findById(teamId)
        val foundMember = memberRepo.findById(memberId)
        if(foundTeam.isEmpty) {
            logger.info("Could not find team with id = $teamId")
            return null
        }
        if(foundMember.isEmpty) {
            logger.info("Could not find member with id = $memberId")
            return null
        }
        foundTeam.get().members.add(foundMember.get())
        return Team.toDto(teamRepo.save(foundTeam.get()))
    }

    fun removeMember(teamId : Long, memberId : Long) : Boolean? {
        val foundTeam = teamRepo.findById(teamId)
        val foundMember = memberRepo.findById(memberId)
        if(foundTeam.isEmpty) {
            throw NoSuchElementException("No Team with id $teamId found.")
        }
        if(foundMember.isEmpty) {
            throw NoSuchElementException("No Member with id $memberId found.")
        }
        return if(foundTeam.get().members.remove(foundMember.get())) {
            teamRepo.save(foundTeam.get())
            true
        } else false
    }

    /**
     * Creates a new *Team* based on the given *name*.
     * @return *TeamDto* of the newly created team.
     */
    fun create(name : String, sysId: Long) : TeamDto? {
        val newTeam = teamRepo.save(Team(name = name, sysId = sysId))
        return Team.toDto(newTeam)
    }

    /**
     * Reads an existing *Team* by its *id*.
     * @return *TeamDto* of the found team.
     * @throws NoSuchElementException when no fitting entity could be found.
     */
    fun read(id : Long) : TeamDto? {
        val found = teamRepo.findById(id).orElseThrow()
        return Team.toDto(found)
    }

    /**
     * Reads *Team*s based on the given list of IDs.
     * @return Set of Teams as *TeamDto*s.
     */
    fun readSome(ids: MutableList<Long>) : MutableSet<TeamDto> {
        val retDto = mutableSetOf<TeamDto>()
        teamRepo.findAllById(ids).forEach { Team.toDto(it)?.let { dto -> retDto.add(dto) } }
        return retDto
    }

    /**
     * Reads an existing *Team* by the id of its contained *Microservice*.
     * @return *TeamDto* of the found team.
     * @throws NoSuchElementException when no fitting entity could be found.
     */
    fun readByServiceId(serviceId: Long): TeamDto? {
        val foundService = microserviceRepo.findById(serviceId).orElseThrow()
        val found = teamRepo.findTeamByOwnedMicroservicesContains(foundService)
        //val found = teamRepo.findAll().find { team ->  team.ownedMicroservices.contains(foundService)
        return if (found.isEmpty) null else Team.toDto(found.get())
    }

    /**
     * Reads all existing *Team*s.
     * @return Set of all Teams as *TeamDto*s.
     */
    fun readAll() : MutableSet<TeamDto> {
        val retDto = mutableSetOf<TeamDto>()
        teamRepo.findAll().forEach { Team.toDto(it)?.let { dto -> retDto.add(dto) } }
        return retDto
    }

    /**
     * Reads all *Team*s for the given sysId.
     * @param sysId the systemId to search for.
     * @return Set of all *Team*s as *TeamDto*s.
     */
    fun readAllBySysId(sysId : Long) : MutableSet<TeamDto> {
        val retDto = mutableSetOf<TeamDto>()
        teamRepo.findTeamsBySysId(sysId).forEach { Team.toDto(it)?.let { dto -> retDto.add(dto) } }
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
                //TODO Check if this works or JPA struggles with OneToMany maintained in Many part.
                found.ownedMicroservices.add(microserviceRepo.findById(it).get())
            }
            found.members.clear()
            team.memberIds.forEach {
                //TODO Check if this works or JPA struggles with OneToMany maintained in Many part.
                found.members.add(memberRepo.findById(it).get())
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
        return if(teamRepo.findById(id).isPresent) {
            teamRepo.deleteById(id)
            true
        } else {
            logger.debug("Could not find team with id = $id")
            false
        }
    }
}