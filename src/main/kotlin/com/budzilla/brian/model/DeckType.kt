package com.budzilla.brian.model

enum class DeckType (
    id: Long,
    name: String
) {
    Commander(1, "Commander"),
    Standard(2, "Standard"),
    Library(3, "Library")

}