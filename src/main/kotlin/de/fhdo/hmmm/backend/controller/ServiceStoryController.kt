package de.fhdo.hmmm.backend.controller

import de.fhdo.hmmm.backend.dto.create.ServiceStoryCreateDto
import de.fhdo.hmmm.backend.dto.ServiceStoryDto
import de.fhdo.hmmm.backend.service.ServiceStoryService
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

//This Cross Origin setting can/should be more specified. In this state all external calls are allowed!
@CrossOrigin
@RequestMapping("/stories")
@RestController
class ServiceStoryController(val storyService: ServiceStoryService) {
    @GetMapping("/{id}")
    private fun getStoryById(@PathVariable id: Long): Mono<ServiceStoryDto?> {
        return Mono.justOrEmpty(storyService.read(id))
    }

    @GetMapping("")
    private fun getAllStories(@RequestParam(required = false)  sysId : Long?): Flux<ServiceStoryDto> {
        return if(sysId == null) {
            return Flux.fromIterable(storyService.readAll())
        } else {
            return Flux.fromIterable(storyService.readAllBySysId(sysId))
        }
    }

    @PostMapping("")
    private fun createStory(@RequestBody newStory: ServiceStoryCreateDto): Mono<ServiceStoryDto?>? {
        return Mono.justOrEmpty(storyService.create(newStory.name, newStory.sysId))
    }

    @PutMapping("")
    private fun updateStory(@RequestBody updatedStory: ServiceStoryDto): Mono<ServiceStoryDto?>? {
        return Mono.justOrEmpty(storyService.update(updatedStory))
    }

    @PutMapping("/{storyId}/vertices")
    private fun addVertex(@PathVariable storyId: Long, @RequestBody vertexId: Long): Mono<ServiceStoryDto?>? {
        return Mono.justOrEmpty(storyService.addVertex(storyId, vertexId))
    }

    @DeleteMapping("/{storyId}/vertices/{vertexId}")
    private fun removeVertexFromStoryById(@PathVariable storyId: Long, @PathVariable vertexId: Long): Mono<Boolean?> {
        return Mono.justOrEmpty(storyService.removeVertex(storyId, vertexId))
    }

    @PutMapping("/{storyId}/edges")
    private fun addEdge(@PathVariable storyId: Long, @RequestBody edgeId: Long): Mono<ServiceStoryDto?>? {
        return Mono.justOrEmpty(storyService.addEdge(storyId, edgeId))
    }

    @DeleteMapping("/{storyId}/edges/{edgeId}")
    private fun removeEdgeFromStoryById(@PathVariable storyId: Long, @PathVariable edgeId: Long): Mono<Boolean?> {
        return Mono.justOrEmpty(storyService.removeEdge(storyId, edgeId))
    }

    @DeleteMapping("/{id}")
    private fun deleteStoryById(@PathVariable id: Long): Mono<Boolean?> {
        return Mono.justOrEmpty(storyService.delete(id))
    }
}