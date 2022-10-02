package com.budzilla

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration
import java.util.*

@Configuration
class MongoConfig(
    @Value("\${budzilla.database.name}")
    private val databaseName : String,
    @Value("\${budzilla.database.password}")
    private val password : String,
    @Value("\${budzilla.database.user}")
    private val useruser  : String,
) : AbstractMongoClientConfiguration() {
    override fun getDatabaseName(): String {
        return databaseName
    }

    override fun mongoClient(): MongoClient {
        val connectionString : ConnectionString = ConnectionString("mongodb://${useruser}:${password}@localhost:27017/")
        val mongoClientSettings = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .build()
        return MongoClients.create(mongoClientSettings)
    }

    override fun getMappingBasePackages(): MutableCollection<String> {
        return Collections.singleton("com.budzilla")
    }
}