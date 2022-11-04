package de.fhdo.hmmm.backend.model

import de.fhdo.hmmm.backend.dto.SoftwaresystemDto
import de.fhdo.hmmm.backend.dto.TeamDto
import javax.persistence.*

@Entity
class Team(
    @Column(nullable = false)
    var name: String,

    @Column(nullable = true)
    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    val ownedMicroservices: MutableSet<Microservice> = mutableSetOf(),

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
) {
    override fun toString(): String {
        return "Team(name='$name', " +
                "ownedMicroservices=${ownedMicroservices.joinToString(prefix = "[", postfix = "]", separator= ",")}, " +
                "id=$id)"
    }

    companion object {
        fun toDto(team: Team) : TeamDto? {
            try {
                var dto = TeamDto()
                dto.id = team.id
                dto.name = team.name
                team.ownedMicroservices.forEach { it.id?.let { id -> dto.ownedMicroserviceIds.add(id) } }
                return dto
            } catch (e : Exception) {
                Microservice.logger.info("An error occurred while transforming to Dto")
                e.printStackTrace()
                return null
            }
        }
    }
}