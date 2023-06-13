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
    var issueLink: String? = null,

    //inserted due to results from interview series
    @ElementCollection(fetch = FetchType.EAGER)
    @Column(nullable = false)
    var plannedFeatures: MutableMap<String, String> = mutableMapOf(),

    //inserted due to results from interview series
    //owning side
    @OneToOne(cascade = [CascadeType.PERSIST], fetch = FetchType.EAGER, optional = true)
    var contactPerson: Member? = null,

    @Column(nullable = false)
    var sysId: Long,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = true)
    var purpose: String? = null,
) {

    companion object {
        val logger = LoggerFactory.getLogger(Microservice::class.java)
        fun toDto(microservice: Microservice): MicroserviceDto? {
            try {
                var dto = MicroserviceDto()
                dto.id = microservice.id
                dto.sysId = microservice.sysId
                dto.name = microservice.name
                dto.purpose = microservice.purpose
                dto.contactPersonId = microservice.contactPerson?.id
                dto.repositoryLink = microservice.repositoryLink
                dto.issueLink = microservice.issueLink
                dto.plannedFeatures.putAll(microservice.plannedFeatures)
                return dto
            } catch (e : Exception) {
                logger.info("An error occurred while transforming to Dto")
                e.printStackTrace()
                return null
            }
        }
    }

    override fun toString(): String {
        return "Microservice(name='$name', repositoryLink=$repositoryLink, issueLink=$issueLink, plannedFeatures=$plannedFeatures, contactPerson=$contactPerson, sysId=$sysId, id=$id, purpose=$purpose)"
    }
}
