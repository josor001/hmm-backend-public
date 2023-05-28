package de.fhdo.hmmm.backend.controller

import de.fhdo.hmmm.backend.dto.*
import de.fhdo.hmmm.backend.dto.create.MicroserviceCreateDto
import de.fhdo.hmmm.backend.service.MicroserviceService
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

//TODO Error Handling for ALL controllers.
// see https://www.baeldung.com/spring-webflux-errors  (probably .onError() method?! maybe global?)

// This Cross Origin setting can/should be more specified. In this state all external calls are allowed!
@CrossOrigin
@RequestMapping("/microservices")
@RestController
class MicroserviceController(val service: MicroserviceService) {
    @GetMapping("/{id}")
    private fun getMicroserviceById(@PathVariable id: Long): Mono<MicroserviceDto?> {
        return Mono.justOrEmpty(service.read(id))
    }

    @GetMapping("")
    private fun getAllMicroservices(@RequestParam(required = false) sysId : Long?): Flux<MicroserviceDto> {
        return if(sysId == null) {
            Flux.fromIterable(service.readAll())
        } else {
            Flux.fromIterable(service.readAllBySysId(sysId))
        }

    }

    @PostMapping("")
    private fun createMicroservice(@RequestBody newMicroservice: MicroserviceCreateDto): Mono<MicroserviceDto?>? {
        return Mono.justOrEmpty(service.create(newMicroservice.name, newMicroservice.sysId))
    }

    @PutMapping("")
    private fun updateMicroservice(@RequestBody newService: MicroserviceDto): Mono<MicroserviceDto?>? {
        return Mono.justOrEmpty(service.update(newService))
    }

    @DeleteMapping("/{id}")
    private fun deleteMicroserviceById(@PathVariable id: Long): Mono<Boolean?> {
        return Mono.justOrEmpty(service.delete(id))
    }
}