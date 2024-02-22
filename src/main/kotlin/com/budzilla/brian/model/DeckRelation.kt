package com.budzilla.brian.model

import com.budzilla.model.User
import javax.persistence.*

@Entity
class DeckRelation(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Long?=null,
    @ManyToOne
    @JoinColumn(name = "ownerid", nullable = false)
    var user: User,
    @ManyToOne
    @JoinColumn(name = "deckid", nullable = false)
    var deck: Deck,
    @ManyToOne
    @JoinColumn(name = "cardId", nullable = false)
    var card: Card
)