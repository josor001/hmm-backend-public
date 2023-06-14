package de.fhdo.hmmm.backend.controller

import de.fhdo.hmmm.backend.dto.MemberDto
import de.fhdo.hmmm.backend.dto.ModelArtifactDto
import de.fhdo.hmmm.backend.dto.TeamDto
import de.fhdo.hmmm.backend.dto.create.MemberCreateDto
import de.fhdo.hmmm.backend.dto.create.ModelArtifactCreateDto
import de.fhdo.hmmm.backend.service.ModelArtifactService
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

// This Cross Origin setting can/should be more specified. In this state all external calls are allowed!
@CrossOrigin
@RequestMapping("/artifacts")
@RestController
class ModelArtifactController(val service: ModelArtifactService) {

    @GetMapping("/microservice/{serviceId}")
    private fun getModelArtifactsByServiceId(@PathVariable serviceId: Long): Flux<ModelArtifactDto> {
        return Flux.fromIterable(service.readAllByServiceId(serviceId))
    }

    @GetMapping("/{id}")
    private fun getModelArtifactById(@PathVariable id: Long): Mono<ModelArtifactDto?> {
        return Mono.justOrEmpty(service.read(id))
    }

    @GetMapping("")
    private fun getAllModelArtifacts(@RequestParam(required = false) sysId : Long?): Flux<ModelArtifactDto> {
        return if(sysId == null) {
            Flux.fromIterable(service.readAll())
        } else {
            Flux.fromIterable(service.readAllBySysId(sysId.toLong()))
        }
    }

    @PostMapping("")
    private fun createModelArtifact(@RequestBody newArtifact: ModelArtifactCreateDto): Mono<ModelArtifactDto?>? {
        return Mono.justOrEmpty(service.create(newArtifact.name, newArtifact.kind, newArtifact.location, newArtifact.microserviceId, newArtifact.sysId))
    }

    @PutMapping("")
    private fun updateModelArtifact(@RequestBody orga: ModelArtifactDto): Mono<ModelArtifactDto?>? {
        return Mono.justOrEmpty(service.update(orga))
    }

    @DeleteMapping("/{id}")
    private fun deleteModelArtifactById(@PathVariable id: Long): Mono<Boolean?> {
        return Mono.justOrEmpty(service.delete(id))
    }
}