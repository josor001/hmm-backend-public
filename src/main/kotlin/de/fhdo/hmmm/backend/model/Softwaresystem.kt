package de.fhdo.hmmm.backend.model

import de.fhdo.hmmm.backend.dto.OrganizationDto
import de.fhdo.hmmm.backend.dto.SoftwaresystemDto
import javax.persistence.*

@Entity
class Softwaresystem(
    @Column(nullable = false)
    var name: String,

    @Column(nullable = true)
    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    val components: MutableSet<Microservice> = mutableSetOf(),

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
) {
    override fun toString(): String {
        return "Softwaresystem(name='$name', " +
                "id=$id)"
    }

    companion object {
        fun toDto(system: Softwaresystem) : SoftwaresystemDto? {
            try {
                var dto = SoftwaresystemDto()
                dto.id = system.id
                dto.name = system.name
                system.components.forEach { it.id?.let { id -> dto.componentIds.add(id) } }
                return dto
            } catch (e : Exception) {
                Microservice.logger.info("An error occurred while transforming to Dto")
                e.printStackTrace()
                return null
            }
        }
    }
}
