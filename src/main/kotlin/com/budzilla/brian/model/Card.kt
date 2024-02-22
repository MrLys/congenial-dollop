package com.budzilla.brian.model

import com.budzilla.model.User
import org.hibernate.annotations.Type
import javax.persistence.*

@Entity
class Card(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Long?=null,
    @Column(length = 1024)
    var title : String,

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    var scryfallData: String?,

    @Column
    var scryfallId: String,
    @ManyToOne
    @JoinColumn(name = "ownerid", nullable = false)
    var user: User,
)
