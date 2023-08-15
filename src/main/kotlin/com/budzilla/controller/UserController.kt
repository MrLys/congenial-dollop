package com.budzilla.controller

import com.budzilla.UserDTO
import com.budzilla.auth.JwtResponse
import com.budzilla.auth.JwtService
import com.budzilla.auth.UserPrincipal
import com.budzilla.auth.WhoAmIDTO
import com.budzilla.context.Context
import com.budzilla.data.repository.UserRepository
import com.budzilla.data.repository.UserRoleRepository
import com.budzilla.model.Role
import com.budzilla.model.User
import com.budzilla.model.UserRole
import io.micrometer.core.annotation.Timed
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import java.nio.file.AccessDeniedException

@RestController
@RequestMapping("/api/auth")
class UserController (
    val userRepository: UserRepository,
    val userRoleRepository: UserRoleRepository,
    val passwordEncoder: PasswordEncoder,
    val authenticationManager: AuthenticationManager,
    val jwtService: JwtService,
    @Value("\${budzilla.signup.secret}")
    val signupSecret : String,
    val context: Context
    ) {

    @PostMapping("/signup")
    @Timed("auth.signup")
    fun signup(@RequestBody dto : UserDTO): ResponseEntity<Any> {
        if (dto.secret == null || dto.secret != signupSecret) {
            throw AccessDeniedException("Invalid secret");
        }
        if (userRepository.existsByIdentity(dto.username)) {
            return ResponseEntity
                .badRequest()
                .body("Error: Username is already in use")
        }
        val user = User(identity = dto.username, encodedPassword = passwordEncoder.encode(dto.password))
        userRepository.save(user)
        userRoleRepository.save(UserRole(user = user, role = Role.USER))
        return ResponseEntity.noContent().build()
    }
    @PostMapping("/login")
    @Timed("auth.login")
    fun login(@RequestBody dto : UserDTO) : ResponseEntity<JwtResponse> {
        val auth = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(dto.username, dto.password))
        SecurityContextHolder.getContext().authentication = auth
        val jwt = jwtService.generate(auth)
        val user = context.getUser()
        val roles = userRoleRepository.findByUserId(user.id!!).map { it.role.name }.toList()
        return ResponseEntity.ok().body(JwtResponse(jwt, user.id!!, user.identity, roles))
    }
    @PostMapping("/update")
    @Timed("auth.update")
    fun update(@RequestBody dto : UserDTO): ResponseEntity<Any> {
        val user = context.getUser()
        val updatedUser = User(user.id!!, dto.username, passwordEncoder.encode(dto.password))
        userRepository.save(updatedUser)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/whoAmI")
    @Timed("auth.whoAmI")
    fun whoAmI(): ResponseEntity<WhoAmIDTO> {
        val user = context.getUser()
        val roles = userRoleRepository.findByUserId(user.id!!).map { it.role.name }.toList()
        return ResponseEntity.ok().body(WhoAmIDTO(user.id!!, user.identity, roles))
    }
}
