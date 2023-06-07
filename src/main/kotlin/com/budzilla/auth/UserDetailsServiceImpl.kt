package com.budzilla.auth

import com.budzilla.data.repository.UserRepository
import com.budzilla.data.repository.UserRoleRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(
    val userRepository: UserRepository,
    val userRoleRepository: UserRoleRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        if (username == null) {
            throw UsernameNotFoundException("Username cannot be null")
        }
        val user = (userRepository.findByIdentity(username)
            ?: throw UsernameNotFoundException("user for $username not found"))
        val roles = userRoleRepository.findByUserId(user.id!!)
        return UserPrincipal(
            user,
            roles.map { it.role }.toList())
    }
}
