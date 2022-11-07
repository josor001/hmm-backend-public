package de.fhdo.hmmm.backend.controller

import de.fhdo.hmmm.backend.service.ModelArtifactService
import org.springframework.web.bind.annotation.RestController

@RestController
class ModelArtifactController(val service: ModelArtifactService) {


    /**
    GET - /users - Returns a list of users
    GET - users/100 - Returns a specific user
    POST - /users - Create a new user
    PUT - /users/ - Updates a specific user
    DELETE - /users/711 - Deletes a specific user
     */
}