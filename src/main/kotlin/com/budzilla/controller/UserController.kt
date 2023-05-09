package com.budzilla.controller

import com.budzilla.UserDTO
import com.budzilla.auth.JwtResponse
import com.budzilla.auth.JwtService
import com.budzilla.auth.UserPrincipal
import com.budzilla.data.repository.UserRepository
import com.budzilla.model.User
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class UserController (
    val userRepository: UserRepository,
    val passwordEncoder: PasswordEncoder,
    val authenticationManager: AuthenticationManager,
    val jwtService: JwtService
    ) {

    @PostMapping("/login")
    fun login(@RequestBody dto : UserDTO) : ResponseEntity<JwtResponse> {
        val auth = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(dto.username, dto.password))
        SecurityContextHolder.getContext().authentication = auth
        val jwt = jwtService.generate(auth)
        val user = (auth.principal as UserPrincipal)
        val roles = user.authorities.map { i -> i.authority }
        return ResponseEntity.ok().body(JwtResponse(jwt, user.user.id!!, user.user.identity, roles))
    }
    @PostMapping("/update")
    fun update(@RequestBody dto : UserDTO): ResponseEntity<Any> {
        val auth = SecurityContextHolder.getContext().authentication
        val user = (auth.principal as UserPrincipal).user
        val updatedUser = User(user.id!!, dto.username, passwordEncoder.encode(dto.password))
        userRepository.save(updatedUser)
        return ResponseEntity.noContent().build()
    }
}
