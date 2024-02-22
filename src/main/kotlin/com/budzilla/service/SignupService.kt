package com.budzilla.service

import com.budzilla.data.repository.UserRepository
import com.budzilla.data.repository.UserRoleRepository
import com.budzilla.model.Role
import com.budzilla.model.User
import com.budzilla.model.UserRole
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class SignupService(
    val userRepository: UserRepository,
    val userRoleRepository: UserRoleRepository,
    val passwordEncoder: PasswordEncoder

) {
    fun signup(username: String, password: String) {
        val user = User(identity = username, encodedPassword = passwordEncoder.encode(password))
        userRepository.save(user)
        userRoleRepository.save(UserRole(user = user, role = Role.USER))
    }
    fun validUsername(username: String): Boolean {
        return !userRepository.existsByIdentity(username)
    }
}