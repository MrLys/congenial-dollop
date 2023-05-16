package com.budzilla.brian.model

import com.budzilla.model.User
import javax.persistence.*

@Entity
class Card(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Long?=null,
    @Column(length = 65535, columnDefinition = "TEXT")
    var scryfallData: String,
    @ManyToOne
    @JoinColumn(name = "ownerid", nullable = false)
    var user: User,
)
