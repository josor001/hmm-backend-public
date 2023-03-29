package de.fhdo.hmmm.backend.controller

import de.fhdo.hmmm.backend.dto.MicroserviceDto
import de.fhdo.hmmm.backend.dto.OrganizationDto
import de.fhdo.hmmm.backend.service.MicroserviceService
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

//TODO completely everything
//TODO check what to do with ServiceStoryEdges? separate controller?

//This Cross Origin setting can/should be more specified. In this state all external calls are allowed!
@CrossOrigin
@RequestMapping("/microservices")
@RestController
class ServiceStoryController(val service: MicroserviceService) {
    @GetMapping("/{id}")
    private fun getMicroserviceById(@PathVariable id: Long): Mono<MicroserviceDto?> {
        return Mono.justOrEmpty(service.read(id))
    }

    @GetMapping("")
    private fun getAllMicroservices(): Flux<MicroserviceDto> {
        return Flux.fromIterable(service.readAll())
    }

    @PostMapping("")
    private fun createMicroservice(@RequestBody name: String): Mono<MicroserviceDto?>? {
        return Mono.justOrEmpty(service.create(name))
    }

    @PutMapping("/")
    private fun updateMicroservice(@RequestBody newService: MicroserviceDto): Mono<MicroserviceDto?>? {
        return Mono.justOrEmpty(service.update(newService))
    }

    @PutMapping("/{serviceId}/artifacts/")
    private fun addModelArtifact(@PathVariable serviceId: Long, @RequestBody artifactId: Long): Mono<MicroserviceDto?>? {
        return Mono.justOrEmpty(service.addModelArtifact(serviceId, artifactId))
    }

    @DeleteMapping("/{serviceId}/artifacts/{artifactId}")
    private fun removeModelArtifactsById(@PathVariable serviceId: Long, @PathVariable artifactId: Long): Mono<Boolean?> {
        return Mono.justOrEmpty(service.removeModelArtifact(serviceId, artifactId))
    }

    @DeleteMapping("/{id}")
    private fun deleteMicroserviceById(@PathVariable id: Long): Mono<Boolean?> {
        return Mono.justOrEmpty(service.delete(id))
    }
}