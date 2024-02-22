package com.budzilla.controller

import com.budzilla.UserDTO
import com.budzilla.auth.*
import com.budzilla.context.Context
import com.budzilla.data.repository.UserRepository
import com.budzilla.data.repository.UserRoleRepository
import com.budzilla.model.Role
import com.budzilla.model.User
import com.budzilla.model.UserRole
import com.budzilla.service.SignInService
import com.budzilla.service.SignupService
import io.micrometer.core.annotation.Timed
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import java.nio.file.AccessDeniedException
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/api/")
class UserController (
    private val userRepository: UserRepository,
    private val userRoleRepository: UserRoleRepository,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManager: AuthenticationManager,
    private val jwtService: JwtService,
    @Value("\${budzilla.signup.secret}")
    private val signupSecret : String,
    private val sessionCookieService: SessionCookieService,
    private val context: Context,
    private val signupService: SignupService,
    private val signInService: SignInService
) {

    @PostMapping("/auth/signup")
    @Timed("auth.signup")
    fun signup(@RequestBody dto : UserDTO): ResponseEntity<Any> {
        if (dto.secret == null || dto.secret != signupSecret) {
            throw AccessDeniedException("Invalid secret");
        }
        if (!signupService.validUsername(dto.username))  {
            return ResponseEntity
                .badRequest()
                .body("Error: Username is already in use")
        }
        signupService.signup(dto.username, dto.password)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/auth/login")
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

    @PostMapping("/auth/webLogin")
    @Timed("auth.webLogin")
    fun webLogin(@RequestBody dto : UserDTO, response: HttpServletResponse, request: HttpServletRequest) : ResponseEntity<Any> {
        signInService.authenticate(dto.username, dto.password)
        sessionCookieService.setupSessionToken(context.getAuth(), request, response)
        return ResponseEntity.noContent().build()
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
