package de.fhdo.hmmm.backend.model

import de.fhdo.hmmm.backend.dto.ServiceStoryDto
import org.slf4j.LoggerFactory
import javax.persistence.*

//inserted due to results from interview series
//TODO Explore the relationship between SAGA pattern (Richardson p 114) and ServiceStory
@Entity
class ServiceStory(

    @Column(nullable = false)
    var name: String,

    @Column(nullable = true)
    var description: String? = null,

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(nullable = true)
    val vertices: MutableSet<Long> = mutableSetOf(),

    @Column(nullable = true)
    @OneToMany(cascade = [CascadeType.PERSIST], fetch = FetchType.EAGER)
    val directedEdges: MutableSet<ServiceStoryEdge> = mutableSetOf(),

    @Column(nullable = false)
    var sysId: Long,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
) {

    override fun toString(): String {
        return "ServiceStory(name='$name', " +
                "description=$description, " +
                "vertices=$vertices, " +
                "directedEdges=$directedEdges, " +
                "sysId=$sysId, " +
                "id=$id)"
    }
    companion object {
        val logger = LoggerFactory.getLogger(ServiceStory::class.java)
        fun toDto(story: ServiceStory): ServiceStoryDto? {
            try {
                var dto = ServiceStoryDto()
                dto.id = story.id
                dto.sysId = story.sysId
                dto.name = story.name
                dto.description = story.description
                dto.vertexIds = story.vertices
                story.directedEdges.forEach { it.id?.let { id -> dto.directedEdgeIds.add(id) } }
                return dto
            } catch (e : Exception) {
                logger.info("An error occurred while transforming to Dto")
                e.printStackTrace()
                return null
            }
        }
    }

}
