package com.budzilla.brian.model

import com.budzilla.model.User
import org.hibernate.annotations.Type
import javax.persistence.*

@Entity
class Deck (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Long?=null,
    @Column(length = 1024, unique = true)
    var title : String,
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    var description: String,
    @Column
    var category : DeckType,
    @ManyToOne
    @JoinColumn(name = "ownerid", nullable = false)
    var user: User
)