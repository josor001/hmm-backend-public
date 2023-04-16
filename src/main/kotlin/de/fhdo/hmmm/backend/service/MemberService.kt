package de.fhdo.hmmm.backend.service

import de.fhdo.hmmm.backend.dto.MemberDto
import de.fhdo.hmmm.backend.dto.ModelArtifactDto
import de.fhdo.hmmm.backend.model.Member
import de.fhdo.hmmm.backend.model.Microservice
import de.fhdo.hmmm.backend.model.ModelArtifact
import de.fhdo.hmmm.backend.repository.MemberRepository
import de.fhdo.hmmm.backend.repository.MicroserviceRepository
import de.fhdo.hmmm.backend.repository.ModelArtifactRepository
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

    /**
     * Creates a new *MemberService* based on the given *name*.
     * @return Dto of the newly created artifact.
     */
    fun create(firstname : String, lastname : String, email : String) : MemberDto? {
        val newMember = memberRepo.save(Member(firstname, lastname, email))
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
            found.expertise = member.expertise
            return Member.toDto(memberRepo.save(found))
        }
        return null
    }

    /**
     * Deletes an existing *Member* by its *id*.
     * @return Returns true if *id* was found and the member got deleted else returns false.
     */
    fun delete(id : Long) : Boolean {
        if(memberRepo.findById(id).isPresent) {
            memberRepo.deleteById(id)
            return true
        } else {
            logger.debug("Could not find member with id = $id")
            return false
        }
    }
}