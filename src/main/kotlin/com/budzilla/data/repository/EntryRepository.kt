package com.budzilla.data.repository

import com.budzilla.model.Entry
import org.springframework.data.jpa.repository.JpaRepository



interface EntryRepository : JpaRepository<Entry, Long> {
}
