package com.budzilla.brian.service

import com.fasterxml.jackson.annotation.JsonProperty

class CSVCardEntry (
    @JsonProperty("Name")
    val name: String?,
    @JsonProperty("Set code")
    val setCode: String?,
    @JsonProperty("Set name")
    val setName: String?,
    @JsonProperty("Collector number")
    val collectorNumber: String?,
    @JsonProperty("Foil")
    val foil: String?,
    @JsonProperty("Rarity")
    val rarity: String?,
    @JsonProperty("Quantity")
    val quantity: Int?,
    @JsonProperty("ManaBox ID")
    val manaboxId: String?,
    @JsonProperty("Scryfall ID")
    val scryfallId: String?,
    @JsonProperty("Purchase price")
    val purchasePrice: Double?,
    @JsonProperty("Misprint")
    val misprint: Boolean?,
    @JsonProperty("Altered")
    val altered: Boolean?,
    @JsonProperty("Condition")
    val condition: String?,
    @JsonProperty("Language")
    val language: String?,
    @JsonProperty("Purchase price currency")
    val purchasePriceCurrency: String?
) {
}