package de.fhdo.hmmm.backend.controller

import de.fhdo.hmmm.backend.dto.MicroserviceDto
import de.fhdo.hmmm.backend.dto.ModelArtifactDto
import de.fhdo.hmmm.backend.model.ModelArtifact
import de.fhdo.hmmm.backend.service.ModelArtifactService
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RequestMapping("/artifacts")
@RestController
class ModelArtifactController(val service: ModelArtifactService) {
    @GetMapping("/{id}")
    private fun getModelArtifactById(@PathVariable id: Long): Mono<ModelArtifactDto?> {
        return Mono.justOrEmpty(service.read(id))
    }

    @GetMapping("")
    private fun getAllModelArtifacts(): Flux<ModelArtifactDto> {
        return Flux.fromIterable(service.readAll())
    }

    @PostMapping("")
    private fun createModelArtifact(@RequestBody name: String): Mono<ModelArtifactDto?>? {
        return Mono.justOrEmpty(service.create(name, ))
    }

    @PutMapping("/")
    private fun updateModelArtifact(@RequestBody orga: ModelArtifactDto): Mono<ModelArtifactDto?>? {
        return Mono.justOrEmpty(service.update(orga))
    }

    @DeleteMapping("/{id}")
    private fun deleteModelArtifactById(@PathVariable id: Long): Mono<Boolean?> {
        return Mono.justOrEmpty(service.delete(id))
    }
}