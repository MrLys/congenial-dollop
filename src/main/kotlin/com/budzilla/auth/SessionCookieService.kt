package com.budzilla.auth

import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import java.util.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Service
class SessionCookieService(
    private val jwtService: JwtService,
    @Value("\${budzilla.jwt.use.secure:true}")
    private val useSecureCookies : Boolean,
    @Value("\${budzilla.jwt.token.name}")
    private val sessionCookieName: String,
) {
    fun setupSessionToken(auth: Authentication, request: HttpServletRequest, response: HttpServletResponse): Cookie {
        val jwt = jwtService.generate(auth)

        val session = getOrCreateSessionCookie(request).orElse(Cookie(sessionCookieName, jwt))
        session.isHttpOnly = true
        session.secure = useSecureCookies
        session.path = "/"
        session.value = jwt
        session.maxAge = 60 * 60 * 24 * 30 // month
        response.addCookie(session)
        return session
    }


    private fun getOrCreateSessionCookie(request: HttpServletRequest) : Optional<Cookie> {
        val allCookies: Array<Cookie>? = request.cookies
        return if (allCookies != null) {
            Arrays.stream(allCookies).filter { x -> x.name.equals(sessionCookieName) }
                .findFirst()
        } else {
            Optional.empty()
        }
    }
}