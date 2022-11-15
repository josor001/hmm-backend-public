package de.fhdo.hmmm.backend.model

import de.fhdo.hmmm.backend.dto.MicroserviceDto
import de.fhdo.hmmm.backend.dto.ModelArtifactDto
import org.slf4j.LoggerFactory
import javax.persistence.*

@Entity
class Microservice(

    @Column(nullable = false)
    var name: String,

    @Column(nullable = true)
    var repositoryLink: String? = null,

    @Column(nullable = true)
    @OneToMany(mappedBy = "microservice", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    val models: MutableSet<ModelArtifact> = mutableSetOf(),

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
) {
    override fun toString(): String {
        return "Microservice(name='$name', " +
                "repositoryLink='$repositoryLink', " +
                "models=${models.joinToString(prefix = "[", postfix = "]", separator= ",")}, " +
                "id=$id)"
    }
    companion object {
        val logger = LoggerFactory.getLogger(Microservice::class.java)
        fun toDto(microservice: Microservice): MicroserviceDto? {
            try {
                var dto = MicroserviceDto()
                dto.id = microservice.id
                dto.name = microservice.name
                dto.repositoryLink = microservice.repositoryLink
                microservice.models.forEach { it.id?.let { id -> dto.modelIds.add(id) } }
                return dto
            } catch (e : Exception) {
                logger.info("An error occurred while transforming to Dto")
                e.printStackTrace()
                return null
            }
        }
    }
}
