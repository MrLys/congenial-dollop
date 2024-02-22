package com.budzilla.brian.data.repository

import com.budzilla.brian.model.Deck
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface DeckRepository : JpaRepository<Deck, Long> {
    fun findByUserId(userId: Long): List<Deck>
    fun findByIdAndUserId(id: Long, userId: Long): Deck
    @Query(
        value = "SELECT * FROM Deck d WHERE d.ownerId = ? AND d.category = ? LIMIT 1",
        nativeQuery = true)
    fun findByUserIdAndCategoryId(userId: Long, categoryId: Long): Deck?
}