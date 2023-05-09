package com.budzilla.dto

data class EntryDTO (
    var userId: Long?,
    val title: String,
    val parent: String,
    val category: String,
    val body: String
)
