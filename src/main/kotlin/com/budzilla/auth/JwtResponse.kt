package com.budzilla.auth

class JwtResponse(val jwt: String, val id: String, val identity: String, val roles: List<String>) {
}