package de.fhdo.hmmm.backend.controller

import de.fhdo.hmmm.backend.dto.create.TeamCreateDto
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

    @GetMapping("/microservice/{serviceId}")
    private fun getTeamByServiceId(@PathVariable serviceId: Long): Mono<TeamDto?> {
        return Mono.justOrEmpty(service.readByServiceId(serviceId))
    }

    @GetMapping("/member/{memberId}")
    private fun getTeamByMemberId(@PathVariable memberId: Long): Mono<TeamDto?> {
        return Mono.justOrEmpty(service.readByMemberId(memberId))
    }

    @GetMapping("/{id}")
    private fun getTeamById(@PathVariable id: Long): Mono<TeamDto?> {
        return Mono.justOrEmpty(service.read(id))
    }

    @GetMapping("")
    private fun getAllTeams(@RequestParam(required = false) sysId : Long?): Flux<TeamDto> {
        return if(sysId == null) {
            return Flux.fromIterable(service.readAll())
        } else {
            return Flux.fromIterable(service.readAllBySysId(sysId))
        }
    }

    @PostMapping("")
    private fun createTeam(@RequestBody newTeam: TeamCreateDto): Mono<TeamDto?>? {
        return Mono.justOrEmpty(service.create(newTeam.name, newTeam.sysId))
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

    @PutMapping("/{id}/members")
    private fun addMember(@PathVariable id: Long, @RequestBody memberId: Long): Mono<TeamDto?>? {
        return Mono.justOrEmpty(service.addMember(id, memberId))
    }

    @DeleteMapping("/{id}/members/{memberId}")
    private fun deleteMemberFromTeamById(@PathVariable id: Long, @PathVariable memberId: Long): Mono<Boolean?> {
        return Mono.justOrEmpty(service.removeMember(id, memberId))
    }

    @DeleteMapping("/{id}")
    private fun deleteTeamById(@PathVariable id: Long): Mono<Boolean?> {
        return Mono.justOrEmpty(service.delete(id))
    }
}