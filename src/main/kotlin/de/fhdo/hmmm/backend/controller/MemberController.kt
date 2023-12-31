package de.fhdo.hmmm.backend.controller

import de.fhdo.hmmm.backend.dto.create.MemberCreateDto
import de.fhdo.hmmm.backend.dto.MemberDto
import de.fhdo.hmmm.backend.service.MemberService
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

// This Cross Origin setting can/should be more specified. In this state all external calls are allowed!
@CrossOrigin
@RestController
@RequestMapping("/members")
class MemberController(val service: MemberService) {

    @GetMapping("/{id}")
    private fun getMemberById(@PathVariable id: Long): Mono<MemberDto?> {
        return Mono.justOrEmpty(service.read(id))
    }

    @GetMapping("")
    private fun getAllMembers(@RequestParam(required = false) sysId : Long?): Flux<MemberDto> {
        return if(sysId == null) {
            Flux.fromIterable(service.readAll())
        } else {
            Flux.fromIterable(service.readAllBySysId(sysId.toLong()))
        }
    }

    @PostMapping("")
    private fun createMember(@RequestBody newMember: MemberCreateDto): Mono<MemberDto?> {
        return Mono.justOrEmpty(service.create(newMember.firstname, newMember.lastname, newMember.email, newMember.sysId))
    }

    @PutMapping("")
    private fun updateMember(@RequestBody member: MemberDto): Mono<MemberDto?> {
        return Mono.justOrEmpty(service.update(member))
    }

    @DeleteMapping("/{id}")
    private fun deleteMemberById(@PathVariable id: Long): Mono<Boolean?> {
        return Mono.justOrEmpty(service.delete(id))
    }
}