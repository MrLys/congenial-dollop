package com.budzilla.auth

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class JwtConfiguration(
    @Value("\${budzilla.jwt.secret}")
    val jwtSecret : String,
    @Value("\${budzilla.longlived.jwt.secret}")
    val longLivedjwtSecret : String,
    @Value("\${budzilla.jwt.expiration}")
    val jwtExpiration : Long,
    @Value("\${budzilla.jwt.format}")
    val format: String
)
{

}