package de.fhdo.hmmm.backend.dto

import java.io.Serializable

data class MemberDto(
    var firstname: String? = null,
    var lastname: String? = null,
    var email: String? = null,
    var profileLink: String? = null,
    var expertise: String? = null,
    var sysId: Long? = null,
    var id: Long? = null
) : Serializable
