package de.fhdo.hmmm.backend.model

import de.fhdo.hmmm.backend.dto.SoftwaresystemDto
import de.fhdo.hmmm.backend.dto.TeamDto
import org.slf4j.LoggerFactory
import javax.persistence.*

@Entity
class Team(
    @Column(nullable = false)
    var name: String,

    @Column(nullable = true)
    @OneToMany(cascade = [CascadeType.PERSIST], fetch = FetchType.EAGER)
    val ownedMicroservices: MutableSet<Microservice> = mutableSetOf(),

    @Column(nullable = true)
    @OneToMany(cascade = [CascadeType.PERSIST],  fetch = FetchType.EAGER)
    val members: MutableSet<Member> = mutableSetOf(),

    @Column(nullable = false)
    var sysId: Long,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
) {
    override fun toString(): String {
        return "Team(name='$name', " +
                "ownedMicroservices=${ownedMicroservices.joinToString(prefix = "[", postfix = "]", separator= ",")}, " +
                "members=${members.joinToString(prefix = "[", postfix = "]", separator= ",")}, " +
                "sysId='$sysId', " +
                "id=$id)"
    }

    companion object {
        val logger = LoggerFactory.getLogger(Team::class.java)
        fun toDto(team: Team) : TeamDto? {
            try {
                var dto = TeamDto()
                dto.id = team.id
                dto.sysId = team.sysId
                dto.name = team.name
                team.members.forEach { it.id?.let { id -> dto.memberIds.add(id) } }
                team.ownedMicroservices.forEach { it.id?.let { id -> dto.ownedMicroserviceIds.add(id) } }
                return dto
            } catch (e : Exception) {
                logger.info("An error occurred while transforming to Dto")
                e.printStackTrace()
                return null
            }
        }
    }
}