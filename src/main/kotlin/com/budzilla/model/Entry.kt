package com.budzilla.model

import org.hibernate.annotations.Type
import javax.persistence.*


@Entity
class Entry (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Long?=null,
    @Column(length = 1024, unique = true)
    var title : String,
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    var body : String,
    @Column(length = 1024)
    var parent : String,
    @Column(length = 1024)
    var category : String,
    @ManyToOne
    @JoinColumn(name = "ownerid", nullable = false)
    var user: User
)
