package com.budzilla.auth

import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtTokenFilter(
    private val jwtService: JwtService,
    private val userDetailsService: UserDetailsService
) : OncePerRequestFilter(){
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val jwt = getBearerToken(request)
        if (jwt != null && jwtService.validate(jwt)) {
            val username : String = jwtService.getUsername(jwt)
            val userDetails = userDetailsService.loadUserByUsername(username)
            val auth = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
            SecurityContextHolder.getContext().authentication = auth
        }
        filterChain.doFilter(request, response)
    }

    private fun getBearerToken(request: HttpServletRequest): String? {
        val authHeader : String? = request.getHeader(AUTHORIZATION)
        if (authHeader?.contains("Bearer ") == true) {
            return authHeader.substring(7, authHeader.length)
        }
        // more to come
        return authHeader
    }

}
