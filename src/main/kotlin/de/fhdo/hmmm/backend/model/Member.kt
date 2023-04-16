package de.fhdo.hmmm.backend.model

import de.fhdo.hmmm.backend.dto.MemberDto
import de.fhdo.hmmm.backend.dto.SoftwaresystemDto
import de.fhdo.hmmm.backend.dto.TeamDto
import org.slf4j.LoggerFactory
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
    var profileLink: String? = null,

    @ElementCollection
    @Column(nullable = false)
    var expertise: MutableList<String> = mutableListOf(),

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
) {

    override fun toString(): String {
        return "Member(firstname='$firstname', " +
                "lastname='$lastname', " +
                "email='$email', " +
                "profileLink='$profileLink', " +
                "expertise=${expertise.joinToString(prefix = "[", postfix = "]", separator= ",")}, " +
                "id=$id)"
    }

    companion object {
        val logger = LoggerFactory.getLogger(Member::class.java)
        fun toDto(member: Member) : MemberDto? {
            try {
                var dto = MemberDto()
                dto.id = member.id
                dto.firstname = member.firstname
                dto.lastname = member.lastname
                dto.email = member.email
                dto.profileLink = member.profileLink
                dto.expertise.addAll(member.expertise)
                return dto
            } catch (e : Exception) {
                logger.info("An error occurred while transforming to Dto")
                e.printStackTrace()
                return null
            }
        }
    }

}