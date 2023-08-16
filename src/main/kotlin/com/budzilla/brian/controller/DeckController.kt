package com.budzilla.brian.controller

import com.budzilla.brian.data.repository.CardRepository
import com.budzilla.brian.data.repository.DeckRepository
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/brian/deck")
class DeckController (
    private val cardRepository: CardRepository,
    private val deckRepository: DeckRepository
){

}