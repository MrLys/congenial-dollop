package com.budzilla.brian.data.repository

import com.budzilla.brian.model.Card
import org.springframework.data.jpa.repository.JpaRepository

interface CardRepository : JpaRepository<Card, Long> {
    fun findByUserId(userId: Long): List<Card>
}