package com.budzilla.service

import com.budzilla.data.repository.UserRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class SignInService(
    val userRepository: UserRepository,
    val authenticationManager: AuthenticationManager
) {
    fun authenticate(username: String, password: String){
        val auth = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(username, password)
        )
        SecurityContextHolder.getContext().authentication = auth
    }
}