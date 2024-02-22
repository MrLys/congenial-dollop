package com.budzilla

import com.budzilla.brian.service.CSVParser
import com.budzilla.data.repository.EntryRepository
import com.budzilla.data.repository.UserRepository
import com.budzilla.data.repository.UserRoleRepository
import com.budzilla.service.SignInService
import com.budzilla.service.SignupService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.io.File
import java.util.UUID


@SpringBootTest
class CSVParserTest {
    @Autowired
    lateinit var cSVParser: CSVParser
    @Autowired
    lateinit var signupService: SignupService
    @Autowired
    lateinit var signInService: SignInService
    @Autowired
    lateinit var userRepository: UserRepository
    @Autowired
    lateinit var userRoleRepository: UserRoleRepository

    @Test
    fun parseCollection() {
        val username = UUID.randomUUID().toString()
        signupService.signup(username, "test")
        signInService.authenticate(username, "test")
        val entries = cSVParser.parseCardsCSV2(File("collection.csv").inputStream())
        entries.forEach { entryList ->
            entryList.forEach { entry ->
                println("${entry.title}, ${entry.scryfallId}")
            }
        }
        val user = userRepository.findByIdentity(username) ?: return
        userRoleRepository.findByUserId(user.id!!).forEach {
            userRoleRepository.delete(it)
        }
        userRepository.delete(user)
    }

}