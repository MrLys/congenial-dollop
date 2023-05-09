package com.budzilla.controller

import com.budzilla.auth.UserPrincipal
import com.budzilla.data.repository.EntryRepository
import com.budzilla.dto.EntryDTO
import com.budzilla.model.Entry
import com.budzilla.model.User
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
    fun getAll() : ResponseEntity<List<EntryDTO>> {
        val entries = entryRepository.findAll()
        val entryDTOs = entries.map {
            EntryDTO(it.user.id,
                it.title, it.parent,
                it.category, it.body)
        }.toList()
        return ResponseEntity.ok().body(entryDTOs)
    }
    @PostMapping
    fun create(@RequestBody dto: EntryDTO) : ResponseEntity<EntryDTO> {
        val user = user()
        val entry = Entry(title = dto.title, body = dto.body,
            parent = dto.parent, category = dto.category,
            user = user)
        entryRepository.save(entry)
        return ResponseEntity.ok().body(dto)
    }
    @PutMapping("/{id}")
    fun update(@PathVariable id : Long, @RequestBody dto : EntryDTO) : ResponseEntity<EntryDTO> {
        val entry = entryRepository.findById(id)
        if (entry.isPresent) {
            val user = user()
            val e = entry.get()
            e.title = dto.title
            e.body = dto.body
            e.parent = dto.parent
            e.category = dto.category
            e.user = user
            entryRepository.save(e)
            return ResponseEntity.ok().body(dto)
        } else {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "entry $id does not exist")
        }
    }

    private fun user(): User {
        val auth = SecurityContextHolder.getContext().authentication
        val user = (auth.principal as UserPrincipal).user
        return user
    }
}
