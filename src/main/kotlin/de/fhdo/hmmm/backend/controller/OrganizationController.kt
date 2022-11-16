package de.fhdo.hmmm.backend.controller

import de.fhdo.hmmm.backend.dto.OrganizationDto
import de.fhdo.hmmm.backend.dto.SoftwaresystemDto
import de.fhdo.hmmm.backend.service.OrganizationService
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

// This Cross Origin setting can/should be more specified. In this state all external calls are allowed!
@CrossOrigin
@RequestMapping("/organizations")
@RestController
class OrganizationController(val service: OrganizationService) {
    @GetMapping("/{id}")
    private fun getOrganizationById(@PathVariable id: Long): Mono<OrganizationDto?> {
        return Mono.justOrEmpty(service.read(id))
    }

    @GetMapping("")
    private fun getAllOrganizations(): Flux<OrganizationDto> {
        return Flux.fromIterable(service.readAll())
    }

    @PostMapping("")
    private fun createOrganization(@RequestBody name: String): Mono<OrganizationDto?>? {
        return Mono.justOrEmpty(service.create(name))
    }

    @PutMapping("/")
    private fun updateOrganization(@RequestBody orga: OrganizationDto): Mono<OrganizationDto?>? {
        return Mono.justOrEmpty(service.update(orga))
    }

    @PutMapping("/{orgaId}/teams/")
    private fun addTeam(@PathVariable orgaId: Long, @RequestBody teamId: Long): Mono<OrganizationDto?>? {
        return Mono.justOrEmpty(service.addTeam(orgaId, teamId))
    }

    @DeleteMapping("/{orgaId}/teams/{teamId}")
    private fun removeTeamById(@PathVariable orgaId: Long, @PathVariable teamId: Long): Mono<Boolean?> {
        return Mono.justOrEmpty(service.removeTeam(orgaId, teamId))
    }

    @DeleteMapping("/{id}")
    private fun deleteOrganizationById(@PathVariable id: Long): Mono<Boolean?> {
        return Mono.justOrEmpty(service.delete(id))
    }
}