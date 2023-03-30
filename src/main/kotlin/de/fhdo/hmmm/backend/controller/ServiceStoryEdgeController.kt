package de.fhdo.hmmm.backend.controller

import de.fhdo.hmmm.backend.dto.ServiceStoryEdgeDto
import de.fhdo.hmmm.backend.service.ServiceStoryEdgeService
import org.springframework.web.bind.annotation.*
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

    @PostMapping("")
    private fun createEdge(@RequestBody sourceId: Long, @RequestBody targetId: Long): Mono<ServiceStoryEdgeDto?>? {
        return Mono.justOrEmpty(edgeService.create(sourceId, targetId))
    }

    @PutMapping("/")
    private fun updateEdge(@RequestBody updatedEdge: ServiceStoryEdgeDto): Mono<ServiceStoryEdgeDto?>? {
        return Mono.justOrEmpty(edgeService.update(updatedEdge))
    }

    @DeleteMapping("/{id}")
    private fun deleteEdgeById(@PathVariable id: Long): Mono<Boolean?> {
        return Mono.justOrEmpty(edgeService.delete(id))
    }
}