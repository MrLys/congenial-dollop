package com.budzilla

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.ComponentScans
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@SpringBootApplication
@ComponentScan("com.budzilla.auth", "com.budzilla.controller", "com.budzilla")
@EnableMongoRepositories("com.budzilla.data.repository")
@EntityScan("com.budzilla.model")
class BudzillaApplication

fun main(args: Array<String>) {
	runApplication<BudzillaApplication>(*args)
}
