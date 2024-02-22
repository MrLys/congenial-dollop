package com.budzilla.brian.data.repository

import com.budzilla.brian.model.DeckRelation
import org.springframework.data.jpa.repository.JpaRepository

interface DeckRelationRepository : JpaRepository<DeckRelation, Long> {
    fun findByUserId(userId: Long): List<DeckRelation>
    fun findByDeckIdAndUserId(id: Long, userId: Long): List<DeckRelation>
    fun findByDeckIdAndUserIdAndCardId(id: Long, userId: Long, cardId: Long): List<DeckRelation>
}