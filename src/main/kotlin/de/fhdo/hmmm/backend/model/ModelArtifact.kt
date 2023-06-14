package de.fhdo.hmmm.backend.model

import de.fhdo.hmmm.backend.dto.ModelArtifactDto
import org.slf4j.LoggerFactory
import javax.persistence.*

@Entity
class ModelArtifact(
    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var microserviceId: Long,

    @Column(nullable = false)
    var sysId: Long,

    var kind: String? = null,

    var location: String? = null,



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?=null,
) {
    override fun toString(): String {
        return "ModelArtifact(name='$name', " +
                "kind=$kind, " +
                "microserviceId='$microserviceId', " +
                "location='$location', " +
                "sysId='$sysId', " +
                "id=$id)"
    }
    companion object {
        val logger = LoggerFactory.getLogger(ModelArtifact::class.java)
        fun toDto(model: ModelArtifact) : ModelArtifactDto? {
            try {
                var dto = ModelArtifactDto()
                dto.id = model.id
                dto.name = model.name
                dto.kind = model.kind
                dto.microserviceId = model.microserviceId
                dto.sysId = model.sysId
                dto.location = model.location
                return dto
            } catch (e : Exception) {
                logger.info("An error occurred while transforming to Dto")
                e.printStackTrace()
                return null
            }
        }
    }
}
