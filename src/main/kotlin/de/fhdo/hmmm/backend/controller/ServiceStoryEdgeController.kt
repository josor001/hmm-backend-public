package de.fhdo.hmmm.backend.controller

import de.fhdo.hmmm.backend.dto.ServiceStoryDto
import de.fhdo.hmmm.backend.dto.ServiceStoryEdgeCreateDto
import de.fhdo.hmmm.backend.dto.ServiceStoryEdgeDto
import de.fhdo.hmmm.backend.service.ServiceStoryEdgeService
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

//This Cross Origin setting can/should be more specified. In this state all external calls are allowed!
@CrossOrigin
@RequestMapping("/stories/edges")
@RestController
class ServiceStoryEdgeController(val edgeService: ServiceStoryEdgeService) {
    @GetMapping("/{id}")
    private fun getEdgeById(@PathVariable id: Long): Mono<ServiceStoryEdgeDto?> {
        return Mono.justOrEmpty(edgeService.read(id))
    }

    @GetMapping("")
    private fun getAllEdges(@RequestParam(required = false)  sysId : Long?): Flux<ServiceStoryEdgeDto> {
        return if(sysId == null) {
            return Flux.fromIterable(edgeService.readAll())
        } else {
            return Flux.fromIterable(edgeService.readAllBySysId(sysId))
        }
    }

    @PostMapping("")
    private fun createEdge(@RequestBody createDto : ServiceStoryEdgeCreateDto): Mono<ServiceStoryEdgeDto?>? {
        return Mono.justOrEmpty(edgeService.create(createDto.sourceId, createDto.targetId, createDto.description, createDto.sysId))
    }

    @PutMapping("")
    private fun updateEdge(@RequestBody updatedEdge: ServiceStoryEdgeDto): Mono<ServiceStoryEdgeDto?>? {
        return Mono.justOrEmpty(edgeService.update(updatedEdge))
    }

    @DeleteMapping("/{id}")
    private fun deleteEdgeById(@PathVariable id: Long): Mono<Boolean?> {
        return Mono.justOrEmpty(edgeService.delete(id))
    }
}