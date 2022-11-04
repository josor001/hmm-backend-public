package de.fhdo.hmmm.backend.model

import de.fhdo.hmmm.backend.dto.MicroserviceDto
import de.fhdo.hmmm.backend.dto.ModelArtifactDto
import de.fhdo.hmmm.backend.dto.OrganizationDto
import javax.persistence.*

@Entity
class ModelArtifact(
    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val kind: ModelKind,

    @Column(nullable = true)
    var relativeLocation: String?,

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
                "relativeLocation='$relativeLocation', " +
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
                dto.relativeLocation = model.relativeLocation
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
