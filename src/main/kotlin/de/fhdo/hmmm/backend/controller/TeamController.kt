package de.fhdo.hmmm.backend.controller

import de.fhdo.hmmm.backend.service.TeamService
import org.springframework.web.bind.annotation.RestController

@RestController
class TeamController(val service: TeamService) {

}