package com.budzilla.data.repository

import com.budzilla.model.User
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository : MongoRepository<User, String> {
    fun findByIdentity(username: String): User?
    fun existsByIdentity(username: String): Boolean
}