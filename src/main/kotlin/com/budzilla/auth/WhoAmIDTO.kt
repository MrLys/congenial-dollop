package com.budzilla.auth

class WhoAmIDTO(
    val id: Long,
    val identity: String,
    val roles: List<String>
) {}
