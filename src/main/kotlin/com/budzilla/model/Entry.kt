package com.budzilla.model

import javax.persistence.*


@Entity
class Entry (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Long?=null,
    @Column(length = 1024, unique = true)
    var title : String,
    @Column(length = 2048)
    var body : String,
    @Column(length = 1024)
    var parent : String,
    @Column(length = 1024)
    var category : String,
)
