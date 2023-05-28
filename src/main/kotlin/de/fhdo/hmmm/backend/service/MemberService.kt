package de.fhdo.hmmm.backend.service

import de.fhdo.hmmm.backend.dto.MemberDto
import de.fhdo.hmmm.backend.model.Member
import de.fhdo.hmmm.backend.repository.MemberRepository
import de.fhdo.hmmm.backend.repository.MicroserviceRepository
import de.fhdo.hmmm.backend.repository.TeamRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


/**
 * The *MemberService* bridges *MemberDto*s and JPA-managed *Member* entities. It serves as the
 * main entrance point for controller classes that want to interact with *Member* entities.
 * @author Jonas Sorgalla
 */
@Service
class MemberService {
    val logger = LoggerFactory.getLogger(MemberService::class.java)

    @Autowired
    lateinit var memberRepo: MemberRepository

    @Autowired
    lateinit var teamRepo: TeamRepository

    @Autowired
    lateinit var serviceRepo: MicroserviceRepository

    /**
     * Creates a new *MemberService* based on the given *name*.
     * @return Dto of the newly created artifact.
     */
    fun create(firstname : String, lastname : String, email : String, sysId: Long) : MemberDto? {
        val newMember = memberRepo.save(Member(firstname = firstname,lastname = lastname, email= email,sysId = sysId))
        return Member.toDto(newMember)
    }

    /**
     * Reads an existing *Member* by its *id*.
     * @return *MemberDto* of the found member else returns null.
     */
    fun read(id : Long) : MemberDto? {
        val found = memberRepo.findById(id).orElse(null) ?: return null
        return Member.toDto(found)
    }

    /**
     * Reads all existing *Member*s.
     * @return Set of all *Member*s as *MemberDto*s.
     */
    fun readAll() : MutableSet<MemberDto> {
        val retDto = mutableSetOf<MemberDto>()
        memberRepo.findAll().forEach { Member.toDto(it)?.let { dto -> retDto.add(dto) } }
        return retDto
    }

    /**
     * Reads all *Member*s for the given sysId.
     * @param sysId the systemId to search for.
     * @return Set of all *Member*s as *MemberDto*s.
     */
    fun readAllBySysId(sysId : Long) : MutableSet<MemberDto> {
        val retDto = mutableSetOf<MemberDto>()
        memberRepo.findMembersBySysId(sysId).forEach { Member.toDto(it)?.let { dto -> retDto.add(dto) } }
        return retDto
    }

    /**
     * Updates an existing *Member* by its *MemberDto*. Entity must have been persisted, i.e., the dto
     * must contain an *id*.
     * @return A new Dto of the updated instance or null if the update was not successful
     */
    fun update(member : MemberDto) : MemberDto? {
        val found = member.id?.let { memberRepo.findById(it).orElse(null) }
        if(found != null) {
            found.email = member.email.orEmpty()
            found.firstname = member.firstname.orEmpty()
            found.lastname = member.lastname.orEmpty()
            found.profileLink = member.profileLink.orEmpty()
            found.expertise = member.expertise.orEmpty()
            return Member.toDto(memberRepo.save(found))
        }
        return null
    }

    /**
     * Deletes an existing *Member* by its *id*.
     * @return Returns true if *id* was found and the member got deleted else returns false.
     */
    fun delete(id : Long) : Boolean {

        val toBeDeleted = memberRepo.findById(id)

        if(toBeDeleted.isPresent) {
            //Delete the Member from any teams.
            val possibleTeam = teamRepo.findTeamByMembersContains(toBeDeleted.get())
            if(possibleTeam.isPresent) {
                val deletedFromTeam = possibleTeam.get().members.removeIf {it.id == toBeDeleted.get().id}
                if(deletedFromTeam)
                    teamRepo.save(possibleTeam.get())
            }
            //Delete the Member as spoc from any microservices.
            val possibleMicroservice = serviceRepo.findMicroserviceByContactPerson_Id(toBeDeleted.get().id!!)
            if(possibleMicroservice.isPresent) {
                possibleMicroservice.get().contactPerson = null;
                serviceRepo.save(possibleMicroservice.get())
            }
            //Actual Delete
            memberRepo.deleteById(id)
            return true
        } else {
            logger.debug("Could not find member with id = $id")
            return false
        }
    }
}