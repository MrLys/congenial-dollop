package com.budzilla.brian.controller

import com.budzilla.brian.model.Deck
import com.budzilla.brian.model.DeckType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/brian/category")
class CategoryController {
    @GetMapping("/all")
    fun getAll() : List<DeckType> {
       return DeckType.values().toList()
    }
}