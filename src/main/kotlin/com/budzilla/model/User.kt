package com.budzilla.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.IndexDirection
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Document
class User : UserDetails {
    @Id
    lateinit var id : String
    @Indexed(direction = IndexDirection.ASCENDING)
    lateinit var identity: String
    @Indexed(direction = IndexDirection.ASCENDING)
    lateinit var encodedPassword: String
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf()
    }

    override fun getPassword(): String {
        return encodedPassword
    }

    override fun getUsername(): String {
        return identity
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