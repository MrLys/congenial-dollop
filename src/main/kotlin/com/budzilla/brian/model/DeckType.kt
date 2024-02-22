package com.budzilla.brian.model

enum class DeckType (
    val id: Long,
    val formatName: String
) {
    Commander(1, "Commander"),
    Standard(2, "Standard"),
    Library(3, "Library"),
    Modern(4, "Modern"),
    Legacy(5, "Legacy"),
    Pauper(6, "Pauper"),
    Vintage(7, "Vintage")

}