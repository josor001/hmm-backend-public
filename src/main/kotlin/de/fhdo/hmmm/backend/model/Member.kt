package de.fhdo.hmmm.backend.model

import de.fhdo.hmmm.backend.dto.MemberDto
import de.fhdo.hmmm.backend.dto.SoftwaresystemDto
import de.fhdo.hmmm.backend.dto.TeamDto
import java.net.URL
import javax.persistence.*

@Entity
class Member(
    @Column(nullable = false)
    var firstname: String,

    @Column(nullable = false)
    var lastname: String,

    @Column(nullable = false)
    var email: String,

    @Column(nullable = true)
    var profileLink: String,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
) {

    override fun toString(): String {
        return "Member(firstname='$firstname', " +
                "lastname='$lastname', " +
                "email='$email', " +
                "profileLink='$profileLink', " +
                "id=$id)"
    }

    companion object {
        fun toDto(member: Member) : MemberDto? {
            try {
                var dto = MemberDto()
                dto.id = member.id
                dto.firstname = member.firstname
                dto.lastname = member.lastname
                dto.email = member.email
                dto.profileLink = member.profileLink
                return dto
            } catch (e : Exception) {
                Microservice.logger.info("An error occurred while transforming to Dto")
                e.printStackTrace()
                return null
            }
        }
    }

}