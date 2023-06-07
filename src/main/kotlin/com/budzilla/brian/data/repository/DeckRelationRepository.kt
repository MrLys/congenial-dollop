package com.budzilla.brian.data.repository

import com.budzilla.brian.model.DeckRelation
import org.springframework.data.jpa.repository.JpaRepository

interface DeckRelationRepository : JpaRepository<DeckRelation, Long> {
    fun findByUserId(userId: Long): List<DeckRelation>
}