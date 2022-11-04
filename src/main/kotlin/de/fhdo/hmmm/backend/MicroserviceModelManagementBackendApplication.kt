package de.fhdo.hmmm.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MicroserviceModelManagementBackendApplication

fun main(args: Array<String>) {
    runApplication<MicroserviceModelManagementBackendApplication>(*args)
}
