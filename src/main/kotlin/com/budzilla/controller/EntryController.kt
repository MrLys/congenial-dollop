package com.budzilla.controller

import com.budzilla.auth.UserPrincipal
import com.budzilla.data.repository.EntryRepository
import com.budzilla.model.Entry
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/entry")
class EntryController
    (
    val entryRepository: EntryRepository
) {
    @GetMapping("/all")
    fun getAll() : ResponseEntity<List<Entry>> {
        return ResponseEntity.ok().body(entryRepository.findAll())
    }
    @PostMapping
    fun create(@RequestBody entry : Entry) : ResponseEntity<Entry> {
        val auth = SecurityContextHolder.getContext().authentication
        val user = (auth.principal as UserPrincipal).user
        entry.user = user
        entryRepository.save(entry)
        return ResponseEntity.ok().body(entry)
    }
    @PutMapping("/{id}")
    fun update(@PathVariable id : Long, @RequestBody dto : Entry) : ResponseEntity<Entry> {
        val entry = entryRepository.findById(id)
        if (entry.isPresent) {
            dto.id = id
            entryRepository.save(dto)
            return ResponseEntity.ok().body(dto)
        } else {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "entry $id does not exist")
        }
    }
}
