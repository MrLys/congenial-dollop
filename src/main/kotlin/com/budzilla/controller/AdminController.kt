package com.budzilla.controller

import com.budzilla.auth.JwtService
import com.budzilla.context.Context
import com.budzilla.data.repository.UserRepository
import com.budzilla.data.repository.UserRoleRepository
import com.budzilla.dto.LongLivedTokenDTO
import io.micrometer.core.annotation.Timed
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin")
class AdminController(
    val userRepository: UserRepository,
    val context: Context,
    val jwtService: JwtService
    ) {

    @PostMapping("/newToken/{id}")
    @Timed("admin.newToken")
    public fun createLongLivedToken(@PathVariable id : Long) : ResponseEntity<LongLivedTokenDTO> {
        val user = userRepository.findById(id)
        if (user.isEmpty) {
            return ResponseEntity.notFound().build()
        }
        return ResponseEntity.ok().body(LongLivedTokenDTO(id, jwtService.generateLongLivedToken(user.get().identity)))
    }
}