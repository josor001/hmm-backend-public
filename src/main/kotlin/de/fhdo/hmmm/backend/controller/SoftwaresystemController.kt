package de.fhdo.hmmm.backend.controller

import de.fhdo.hmmm.backend.dto.SoftwaresystemDto
import de.fhdo.hmmm.backend.service.SoftwaresystemService
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

// This Cross Origin setting can/should be more specified. In this state all external calls are allowed!
@CrossOrigin
@RequestMapping("/systems")
@RestController
class SoftwaresystemController(val service: SoftwaresystemService) {
    @GetMapping("/{id}")
    private fun getSoftwaresystemById(@PathVariable id: Long): Mono<SoftwaresystemDto?> {
        return Mono.justOrEmpty(service.read(id))
    }

    @GetMapping("")
    private fun getAllSystems(): Flux<SoftwaresystemDto> {
        return Flux.fromIterable(service.readAll())
    }

    @PostMapping("")
    private fun createSystem(@RequestBody name: String): Mono<SoftwaresystemDto?>? {
        return Mono.justOrEmpty(service.create(name))
    }

    @PutMapping("/")
    private fun updateSystem(@RequestBody updatedSystem: SoftwaresystemDto): Mono<SoftwaresystemDto?>? {
        return Mono.justOrEmpty(service.update(updatedSystem))
    }

    @PutMapping("/{id}/microservices/")
    private fun addMicroservice(@PathVariable id: Long, @RequestBody serviceId: Long): Mono<SoftwaresystemDto?>? {
        return Mono.justOrEmpty(service.addMicroservice(id, serviceId))
    }

    @DeleteMapping("/{id}/microservices/{serviceId}")
    private fun deleteMicroserviceFromSystemById(@PathVariable id: Long, @PathVariable serviceId: Long): Mono<Boolean?> {
        return Mono.justOrEmpty(service.removeMicroservice(id, serviceId))
    }

    @PutMapping("/{id}/stories/")
    private fun addStory(@PathVariable id: Long, @RequestBody storyId: Long): Mono<SoftwaresystemDto?>? {
        return Mono.justOrEmpty(service.addServiceStory(id, storyId))
    }

    @DeleteMapping("/{id}/stories/{storyId}")
    private fun deleteStoryFromSystemById(@PathVariable id: Long, @PathVariable storyId: Long): Mono<Boolean?> {
        return Mono.justOrEmpty(service.removeServiceStory(id, storyId))
    }

    @DeleteMapping("/{id}")
    private fun deleteSystemById(@PathVariable id: Long): Mono<Boolean?> {
        return Mono.justOrEmpty(service.delete(id))
    }

}