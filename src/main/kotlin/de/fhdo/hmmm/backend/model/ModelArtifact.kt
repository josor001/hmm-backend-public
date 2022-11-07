package de.fhdo.hmmm.backend.model

import de.fhdo.hmmm.backend.dto.ModelArtifactDto
import javax.persistence.*

@Entity
class ModelArtifact(
    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val kind: String,

    @Column(nullable = false)
    var location: String,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "microservice_id")
    var microservice: Microservice,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?=null,
) {
    override fun toString(): String {
        return "ModelArtifact(name='$name', " +
                "kind=$kind, " +
                "relativeLocation='$location', " +
                "microservice=$microservice, " +
                "id=$id)"
    }
    companion object {
        fun toDto(model: ModelArtifact) : ModelArtifactDto? {
            try {
                var dto = ModelArtifactDto()
                dto.id = model.id
                dto.name = model.name
                dto.kind = model.kind
                dto.location = model.location
                dto.microserviceId = model.microservice.id
                return dto
            } catch (e : Exception) {
                Microservice.logger.info("An error occurred while transforming to Dto")
                e.printStackTrace()
                return null
            }
        }
    }
}
