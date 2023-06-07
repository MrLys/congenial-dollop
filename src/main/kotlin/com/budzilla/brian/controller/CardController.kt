package com.budzilla.brian.controller

import com.budzilla.brian.data.repository.CardRepository
import com.budzilla.brian.dto.CardDTO
import com.budzilla.context.Context
import com.budzilla.dto.EntryDTO
import io.micrometer.core.annotation.Timed
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContext
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/card")
class CardController (
    val cardRepository: CardRepository,
    val context: Context
) {
    @GetMapping("/all")
    @Timed("card.all")
    fun getAll() : ResponseEntity<List<CardDTO>> {
        val entries = cardRepository.findByUserId(context.getUserId())
        val cardDTOs = entries.map {
            CardDTO(it.id, it.scryfallData, it.user.id!!)
        }.toList()
        return ResponseEntity.ok().body(cardDTOs)
    }
}