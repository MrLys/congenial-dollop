package com.budzilla.model

import javax.persistence.*


@Entity
class Entry (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Long?=null,
    @Column(length = 1024, unique = true)
    var title : String,
    @Column(length = 65535, columnDefinition = "TEXT")
    var body : String,
    @Column(length = 1024)
    var parent : String,
    @Column(length = 1024)
    var category : String,
    @ManyToOne
    @JoinColumn(name = "ownerId", nullable = false)
    var user: User
)
