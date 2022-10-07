package com.budzilla.auth

import com.budzilla.data.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(
    val userRepository: UserRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        if (username == null) {
            throw UsernameNotFoundException("Username cannot be null")
        }
        return UserPrincipal(userRepository.findByIdentity(username)
            ?: throw UsernameNotFoundException("user for $username not found"))
    }
}
