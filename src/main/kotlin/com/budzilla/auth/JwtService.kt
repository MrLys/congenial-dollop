package com.budzilla.auth

import com.budzilla.model.User
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders.BASE64
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import java.lang.Exception
import java.util.*
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

@Service
class JwtService(private val jwtConfiguration: JwtConfiguration) {
    private val key : SecretKey = SecretKeySpec(BASE64.decode(jwtConfiguration.jwtSecret), jwtConfiguration.format) //Keys.secretKeyFor(SignatureAlgorithm.HS512)
    private val parser  = Jwts.parserBuilder().setSigningKey(key).build()
    fun generate(auth : Authentication) : String {
        val userPrincipal : User = auth.principal as User
        return Jwts.builder().setSubject(userPrincipal.identity)
            .setIssuedAt(Date())
            .setExpiration(getExpiryDateFromNow())
            .signWith(key, SignatureAlgorithm.HS512)
            .compact()

    }
    private fun getExpiryDateFromNow() : Date {
        val now = Date()
        val expiry = now.time + jwtConfiguration.jwtExpiration
        return Date(expiry)
    }

    fun validate(jwt: String): Boolean {
        try {
            parser.parseClaimsJws(jwt)
            return true
        } catch (exception : Exception) {
           println(exception.message)
        }
        return false
    }

    fun getUsername(jwt: String): String {
        return parser.parseClaimsJws(jwt).body.subject
    }

}