package de.fhdo.hmmm.backend.model

import de.fhdo.hmmm.backend.dto.ServiceStoryEdgeDto
import org.slf4j.LoggerFactory

import javax.persistence.*

@Entity
class ServiceStoryEdge(
    //@OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER, optional = false)
    //var source: Microservice? = null,
    var source: Long? = null,

    //@OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER, optional = false)
    //var target: Microservice? = null,
    var target: Long? = null,

    @Column(nullable = true)
    var description: String? = null,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
) {
    override fun toString(): String {
        return "ServiceStoryEdge(id=$id, " +
                "description=$description, " +
                "source=$source, " +
                "target=$target)"
    }

    companion object {
        val logger = LoggerFactory.getLogger(ServiceStoryEdge::class.java)
        fun toDto(edge: ServiceStoryEdge) : ServiceStoryEdgeDto? {
            try {
                var dto = ServiceStoryEdgeDto()
                dto.id = edge.id
                dto.description = edge.description
                dto.sourceId = edge.source
                dto.targetId = edge.target
                return dto
            } catch (e : Exception) {
                logger.info("An error occurred while transforming to Dto")
                e.printStackTrace()
                return null
            }
        }
    }

}