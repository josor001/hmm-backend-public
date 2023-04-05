package de.fhdo.hmmm.backend.controller

import de.fhdo.hmmm.backend.dto.TeamDto
import de.fhdo.hmmm.backend.service.TeamService
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

// This Cross Origin setting can/should be more specified. In this state all external calls are allowed!
@CrossOrigin
@RestController
@RequestMapping("/teams")
class TeamController(val service: TeamService) {

    @GetMapping("/{id}")
    private fun getTeamById(@PathVariable id: Long): Mono<TeamDto?> {
        return Mono.justOrEmpty(service.read(id))
    }

    @GetMapping("")
    private fun getAllTeams(): Flux<TeamDto> {
        return Flux.fromIterable(service.readAll())
    }

    @PostMapping("")
    private fun createTeam(@RequestBody name: String): Mono<TeamDto?>? {
        return Mono.justOrEmpty(service.create(name))
    }

    @PutMapping("")
    private fun updateTeam(@RequestBody team: TeamDto): Mono<TeamDto?>? {
        return Mono.justOrEmpty(service.update(team))
    }

    @PutMapping("/{id}/microservices")
    private fun addMicroservice(@PathVariable id: Long, @RequestBody serviceId: Long): Mono<TeamDto?>? {
        return Mono.justOrEmpty(service.addMicroservice(id, serviceId))
    }

    @DeleteMapping("/{id}/microservices/{serviceId}")
    private fun deleteMicroserviceFromTeamById(@PathVariable id: Long, @PathVariable serviceId: Long): Mono<Boolean?> {
        return Mono.justOrEmpty(service.removeMicroservice(id, serviceId))
    }

    @DeleteMapping("/{id}")
    private fun deleteTeamById(@PathVariable id: Long): Mono<Boolean?> {
        return Mono.justOrEmpty(service.delete(id))
    }
}