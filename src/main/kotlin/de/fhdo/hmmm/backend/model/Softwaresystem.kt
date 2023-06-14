package de.fhdo.hmmm.backend.model

import de.fhdo.hmmm.backend.dto.OrganizationDto
import de.fhdo.hmmm.backend.dto.SoftwaresystemDto
import org.slf4j.LoggerFactory
import javax.persistence.*

@Entity
class Softwaresystem(
    @Column(nullable = false)
    var name: String,

    @Column(nullable = true)
    var description: String? = null,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
) {
    override fun toString(): String {
        return "Softwaresystem(name='$name', " +
                "description=$description, " +
                "id=$id)"
    }

    companion object {
        val logger = LoggerFactory.getLogger(Softwaresystem::class.java)
        fun toDto(system: Softwaresystem) : SoftwaresystemDto? {
            try {
                var dto = SoftwaresystemDto()
                dto.id = system.id
                dto.name = system.name
                dto.description = system.description
                return dto
            } catch (e : Exception) {
                logger.info("An error occurred while transforming to Dto")
                e.printStackTrace()
                return null
            }
        }
    }
}
