package com.budzilla.auth

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders.BASE64
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import java.lang.Exception
import java.util.*
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

@Service
class JwtService(private val jwtConfiguration: JwtConfiguration) {
    private val key : SecretKey = SecretKeySpec(BASE64.decode(jwtConfiguration.jwtSecret), jwtConfiguration.format) //Keys.secretKeyFor(SignatureAlgorithm.HS512)
    private val longLivedKey : SecretKey = SecretKeySpec(BASE64.decode(jwtConfiguration.jwtSecret), jwtConfiguration.format) //Keys.secretKeyFor(SignatureAlgorithm.HS512)
    private val parser  = Jwts.parserBuilder().setSigningKey(key).build()
    private val longLivedParser  = Jwts.parserBuilder().setSigningKey(longLivedKey).build()
    fun generate(auth : Authentication) : String {
        val userPrincipal = auth.principal as UserPrincipal
        return generateToken(userPrincipal.user.identity)

    }

    private fun generateToken(identity: String): String {
        return Jwts.builder().setSubject(identity)
            .setIssuedAt(Date())
            .setExpiration(getExpiryDateFromNow())
            .signWith(key, SignatureAlgorithm.HS512)
            .compact()
    }
    public fun generateLongLivedToken(identity: String): String {
        return Jwts.builder().setSubject(identity)
            .setIssuedAt(Date())
            .setExpiration(getExpiryDateFromNow())
            .signWith(longLivedKey, SignatureAlgorithm.HS512)
            .compact()
    }

    private fun getLongLivedExpiryDateFromNow() : Date {
        val now = Date()
        val expiry = now.time + 31536000;
        return Date(expiry)
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
    fun validateLongLived(token: String): Boolean {
        try {
            parser.parseClaimsJws(token)
            return true
        } catch (exception : Exception) {
            println(exception.message)
        }
        return false
    }

    fun getUsername(jwt: String): String {
        return parser.parseClaimsJws(jwt).body.subject
    }
    fun getUsernameLongLived(token: String): String {
        return longLivedParser.parseClaimsJws(token).body.subject
    }

}
