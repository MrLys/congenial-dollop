package com.budzilla

import com.budzilla.brian.data.repository.CardRepository
import com.budzilla.brian.data.repository.DeckRelationRepository
import com.budzilla.brian.data.repository.DeckRepository
import com.budzilla.data.repository.EntryRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class RepositoryTests {
    @Autowired
    lateinit var entryRepository: EntryRepository;
    @Autowired
    lateinit var cardRepository: CardRepository;
    @Autowired
    lateinit var deckRepository: DeckRepository;
    @Autowired
    lateinit var deckRelationRepository: DeckRelationRepository;
    @Test
    fun definedQueryTests() {
        entryRepository.findByUserId(1)
        cardRepository.findByUserId(1)
        deckRepository.findByUserId(1)
        deckRelationRepository.findByUserId(1)
    }
}