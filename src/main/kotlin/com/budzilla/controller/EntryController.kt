package com.budzilla.controller

import com.budzilla.auth.UserPrincipal
import com.budzilla.common.Metrics
import com.budzilla.context.Context
import com.budzilla.data.repository.EntryRepository
import com.budzilla.dto.EntryDTO
import com.budzilla.model.Entry
import com.budzilla.model.User
import io.micrometer.core.annotation.Timed
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/entry")
class EntryController
    (
    private val entryRepository: EntryRepository,
    private val context: Context,
    private val metrics: Metrics
) {
    @GetMapping("/all")
    @Timed("entry.all")
    fun getAll() : ResponseEntity<List<EntryDTO>> {
        println(context.getUserId())
        val entries = entryRepository.findByUserId(context.getUserId())
        val entryDTOs = entries.map {
            EntryDTO(it.user.id,
                it.title, it.parent,
                it.category, it.body)
        }.toList()

        return ResponseEntity.ok().body(entryDTOs)
    }
    @PostMapping
    @Timed("entry.create")
    fun create(@RequestBody dto: EntryDTO) : ResponseEntity<EntryDTO> {
        val user = context.getUser()
        val entry = Entry(title = dto.title, body = dto.body,
            parent = dto.parent, category = dto.category,
            user = user)
        entryRepository.save(entry)
        metrics.newEntryCreated.increment()
        return ResponseEntity.ok().body(dto)
    }
    @PutMapping("/{id}")
    @Timed("entry.update")
    fun update(@PathVariable id : Long, @RequestBody dto : EntryDTO) : ResponseEntity<EntryDTO> {
        val entry = entryRepository.findById(id)
        if (entry.isPresent && entry.get().user.id == context.getUserId()) {
            val user = context.getUser()
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
}
