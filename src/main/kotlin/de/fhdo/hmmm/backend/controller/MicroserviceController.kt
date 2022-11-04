package de.fhdo.hmmm.backend.controller

import de.fhdo.hmmm.backend.service.MicroserviceService
import org.springframework.web.bind.annotation.RestController

@RestController
class MicroserviceController(val service: MicroserviceService) {

}