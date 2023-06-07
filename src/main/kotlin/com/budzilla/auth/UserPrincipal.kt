package com.budzilla.auth

import com.budzilla.model.Role
import com.budzilla.model.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserPrincipal(
    val user: User,
    roles: List<Role>
) : UserDetails {
    private val authorities: MutableCollection<out GrantedAuthority>  = roles.stream().map { role ->
        SimpleGrantedAuthority(role.name)
    }.toList().toMutableList()
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return authorities
    }

    override fun getPassword(): String {
        return user.encodedPassword
    }

    override fun getUsername(): String {
        return user.identity
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}
