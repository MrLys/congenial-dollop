package com.budzilla

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@ComponentScan("com.budzilla.auth", "com.budzilla.controller", "com.budzilla", "com.budzilla.brian")
@EnableJpaRepositories(basePackages = ["com.budzilla.data.repository", "com.budzilla.brian.data.repository"])
@EntityScan("com.budzilla.model", "com.budzilla.brian.model")
@EnableConfigurationProperties
class BudzillaApplication

fun main(args: Array<String>) {
	runApplication<BudzillaApplication>(*args, "--debug")
}
