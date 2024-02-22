package com.budzilla.brian.controller

import com.budzilla.brian.data.repository.CardRepository
import com.budzilla.brian.data.repository.DeckRelationRepository
import com.budzilla.brian.data.repository.DeckRepository
import com.budzilla.brian.dto.CardDTO
import com.budzilla.brian.dto.DeckDTO
import com.budzilla.brian.dto.DeckTypeDTO
import com.budzilla.brian.model.Card
import com.budzilla.brian.model.Deck
import com.budzilla.brian.model.DeckRelation
import com.budzilla.brian.model.DeckType
import com.budzilla.brian.service.CSVParser
import com.budzilla.context.Context
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/brian/deck")
class DeckController (
    private val cardRepository: CardRepository,
    private val deckRepository: DeckRepository,
    private val deckRelationRepository: DeckRelationRepository,
    private val context: Context,
    private val csvParser: CSVParser
){

    @GetMapping("/all")
    fun getAll() : List<DeckDTO> {
        return deckRepository.findByUserId(context.getUserId()).map { deck ->
            val cards = deckRelationRepository.findByDeckIdAndUserId(deck.id!!, context.getUserId()).map { it.card }.map { card ->
                CardDTO(card.id, title = card.title, card.scryfallData, card.scryfallId, card.user.id!!)
            }
            DeckDTO(deck.id, deck.title, deck.description,deck.category?.let { DeckTypeDTO(id = it.id, name = it.formatName) }, deck.user.id, cards)
        }
    }
    @PostMapping("/new")
    fun newDeck(@RequestBody dto : DeckDTO) : DeckDTO {
        val deck = Deck(
            title = dto.title,
            description = dto.description,
            category = dto.category?.let { DeckType.valueOf(it.name) },
            user = context.getUser()
        )
        deckRepository.save(deck)
        dto.cards.forEach { it ->
            val card = if (it.id == null) {
                val newCard = cardRepository.findByUserIdAndScryfallId(context.getUserId(), it.scryfallId) ?: Card(scryfallData = it.data, scryfallId = it.scryfallId, title = it.title, user = context.getUser()).let { cardRepository.save(it) }
                cardRepository.save(newCard)
                newCard
            } else {
                cardRepository.findByUserIdAndScryfallId(context.getUserId(), it.scryfallId)!!
            }
            DeckRelation(user = context.getUser(), deck = deck, card = card).let { deckRelationRepository.save(it) }
        }
        return DeckDTO(deck.id, deck.title, deck.description, dto.category, deck.user.id, dto.cards)
    }


    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody dto : DeckDTO) : DeckDTO {

        val deck = deckRepository.findByIdAndUserId(id, context.getUserId())
        deckRelationRepository.findByDeckIdAndUserId(id, context.getUserId()).let { deckRelationRepository.deleteAll(it) }
        dto.cards.forEach { it ->
            val card = if (it.id == null) {
                val newCard = cardRepository.findByUserIdAndScryfallId(context.getUserId(), it.scryfallId) ?: Card(scryfallData = it.data, scryfallId = it.scryfallId, title = it.title, user = context.getUser()).let { cardRepository.save(it) }
                cardRepository.save(newCard)
                newCard
            } else {
                cardRepository.findByUserIdAndScryfallId(context.getUserId(), it.scryfallId)!!
            }
            DeckRelation(user = context.getUser(), deck = deck, card = card).let { deckRelationRepository.save(it) }
        }
        return DeckDTO(deck.id, deck.title, deck.description, dto.category, deck.user.id, dto.cards)
    }
    @PutMapping("/{id}/addCard")
    fun removeCard(@PathVariable id: Long, @RequestBody dto : CardDTO)  {

        val deck = deckRepository.findByIdAndUserId(id, context.getUserId())
        val card = if (dto.id == null) {
            val newCard = cardRepository.findByUserIdAndScryfallId(context.getUserId(), dto.scryfallId) ?: Card(scryfallData = dto.data, scryfallId = dto.scryfallId, title = dto.title, user = context.getUser()).let { cardRepository.save(it) }
            cardRepository.save(newCard)
            newCard
        } else {
            cardRepository.findByUserIdAndScryfallId(context.getUserId(), dto.scryfallId)!!
        }
        DeckRelation(user = context.getUser(), deck = deck, card = card).let { deckRelationRepository.save(it) }
    }
    @PutMapping("/{deckId}/removeCard/{cardId}")
    fun removeCard(@PathVariable deckId: Long, @PathVariable cardId: String)  {

        val deck = deckRepository.findByIdAndUserId(deckId, context.getUserId())
        val card = cardRepository.findByUserIdAndScryfallId(context.getUserId(), cardId) ?: throw Exception("Card not found")
        val deckRelation =
            deckRelationRepository.findByDeckIdAndUserIdAndCardId(deckId, context.getUserId(), card.id!!)
        if (deckRelation.isNotEmpty()) {
            deckRelation[0].let { deckRelationRepository.delete(it) }
        }
    }
    @PostMapping("/import", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun importCsv(@RequestParam("file") file: MultipartFile) {
        // Parse the CSV file
        val cards = csvParser.parseCardsCSV(file.inputStream)
        importToCollection(cards)
    }

    private fun importToCollection(cards: List<List<Card>>) {
        val library = deckRepository.findByUserIdAndCategoryId(context.getUser().id!!, DeckType.Library.ordinal.toLong()) ?: Deck(title = "Library", description = "Library", category = DeckType.Library, user = context.getUser()).let { deckRepository.save(it) }
        cards.forEach { duplicates ->
            val card = cardRepository.findByUserIdAndScryfallId(context.getUserId(), duplicates[0].scryfallId) ?: Card(scryfallData = duplicates[0].scryfallData, scryfallId = duplicates[0].scryfallId, title = duplicates[0].title, user = context.getUser()).let { cardRepository.save(it) }
            val deckRelations = deckRelationRepository.findByDeckIdAndUserIdAndCardId(
                library.id!!,
                context.getUserId(),
                card.id!!
            )
            println("deck relations: ${deckRelations.size}")
            if (deckRelations.size == duplicates.size) {
                // we already have all the cards in the library
                println("Already have all the cards in the library")
                return
            } else {
                (0..(duplicates.size - deckRelations.size + 1)).forEach { i ->
                    println(i)
                    DeckRelation(user = context.getUser(), deck = library, card = card).let { deckRelationRepository.save(it) }
                    println("Added card to library")
                }
            }
        }
    }

}