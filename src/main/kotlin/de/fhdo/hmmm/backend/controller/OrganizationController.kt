package de.fhdo.hmmm.backend.controller

import de.fhdo.hmmm.backend.service.OrganizationService
import org.springframework.web.bind.annotation.RestController

@RestController
class OrganizationController(val service: OrganizationService) {

}