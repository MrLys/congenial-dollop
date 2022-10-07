package com.budzilla.data.repository

import com.budzilla.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByIdentity(username: String): User?
    fun existsByIdentity(username: String): Boolean
}
