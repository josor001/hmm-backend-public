package de.fhdo.hmmm.backend.controller

import de.fhdo.hmmm.backend.dto.SoftwaresystemDto
import de.fhdo.hmmm.backend.dto.create.SoftwaresystemCreateDto
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
    private fun createSystem(@RequestBody newSystem: SoftwaresystemCreateDto): Mono<SoftwaresystemDto?>? {
        return Mono.justOrEmpty(service.create(newSystem.name, newSystem.description))
    }

    @PutMapping("")
    private fun updateSystem(@RequestBody updatedSystem: SoftwaresystemDto): Mono<SoftwaresystemDto?>? {
        return Mono.justOrEmpty(service.update(updatedSystem))
    }

    @DeleteMapping("/{id}")
    private fun deleteSystemById(@PathVariable id: Long): Mono<Boolean?> {
        return Mono.justOrEmpty(service.delete(id))
    }
}