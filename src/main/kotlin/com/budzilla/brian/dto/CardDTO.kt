package com.budzilla.brian.dto

data class CardDTO(
    val id: Long?,
    val title: String,
    val data: String?,
    val scryfallId: String,
    val ownerId: Long
)
