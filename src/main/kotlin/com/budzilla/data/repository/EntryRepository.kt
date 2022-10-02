package com.budzilla.data.repository

import com.budzilla.model.Entry
import org.springframework.data.mongodb.repository.MongoRepository


interface EntryRepository : MongoRepository<Entry, String> {
}