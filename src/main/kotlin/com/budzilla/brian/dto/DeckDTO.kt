package com.budzilla.brian.dto

import com.budzilla.brian.model.DeckType
import javax.persistence.*

data class DeckDTO (
    val id : Long?,
    val title : String,
    val description: String,
    val category : DeckType,
    val ownerId: Long?
)
