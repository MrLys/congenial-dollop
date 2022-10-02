package com.budzilla.auth

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

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