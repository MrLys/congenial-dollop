package com.budzilla.auth

class JwtResponse(val jwt: String, val id: Long, val identity: String, val roles: List<String>) {
}
