package de.fhdo.hmmm.backend.dto

import java.io.Serializable

data class SoftwaresystemDto(
    var name: String? = null,
    val componentIds: MutableSet<Long> = mutableSetOf(),
    val storyIds: MutableSet<Long> = mutableSetOf(),
    var id: Long? = null
) : Serializable
