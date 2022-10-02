package com.budzilla.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document
class Entry {
    @Id
    lateinit var id : String
    @Indexed
    lateinit var title : String
    @Indexed
    lateinit var body : String
    @Indexed
    lateinit var parent : String
    @Indexed
    lateinit var category : String
}