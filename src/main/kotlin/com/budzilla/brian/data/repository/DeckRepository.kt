package com.budzilla.brian.data.repository

import com.budzilla.brian.model.Deck
import org.springframework.data.jpa.repository.JpaRepository

interface DeckRepository : JpaRepository<Deck, Long> {
    fun findByUserId(userId: Long): List<Deck>
}