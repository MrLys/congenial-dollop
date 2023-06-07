package com.budzilla.data.repository

import com.budzilla.model.UserRole
import org.springframework.data.jpa.repository.JpaRepository

interface UserRoleRepository : JpaRepository<UserRole, Long> {
    fun findByUserId(userId: Long): List<UserRole>
}