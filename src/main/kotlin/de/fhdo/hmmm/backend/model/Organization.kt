package de.fhdo.hmmm.backend.model

import de.fhdo.hmmm.backend.dto.ModelArtifactDto
import de.fhdo.hmmm.backend.dto.OrganizationDto
import org.slf4j.LoggerFactory
import javax.persistence.*

@Entity
class Organization(
    @Column(nullable = false)
    var name: String,

    //owning side
    //TODO CHECK THIS, GAVE SOME TROUBLE IN ServiceStoryEdge Testing, thus I moved to only using IDs for the microservices at edge class
    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER, optional = true)
    var systemUnderDevelopment: Softwaresystem? = null,

    @Column(nullable = true)
    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    val teams: MutableSet<Team> = mutableSetOf(),

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?=null,
) {
    override fun toString(): String {
        return "Organization(name='$name', " +
                "systemUnderDevelopment=$systemUnderDevelopment, " +
                "teams=${teams.joinToString(prefix = "[", postfix = "]", separator= ",")}, " +
                "id=$id)"
    }

    companion object {
        val logger = LoggerFactory.getLogger(Organization::class.java)
        fun toDto(orga: Organization) : OrganizationDto? {
            try {
                var dto = OrganizationDto()
                dto.id = orga.id
                dto.name = orga.name
                dto.systemUnderDevelopmentId = orga.systemUnderDevelopment?.id
                orga.teams.forEach { it.id?.let { id -> dto.teamIds.add(id) } }
                return dto
            } catch (e : Exception) {
                logger.info("An error occurred while transforming to Dto")
                e.printStackTrace()
                return null
            }
        }
    }
}
