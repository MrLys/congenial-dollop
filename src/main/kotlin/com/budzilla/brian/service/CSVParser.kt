package com.budzilla.brian.service

import com.budzilla.brian.model.Card
import com.budzilla.context.Context
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.dataformat.csv.CsvSchema
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.stereotype.Component
import java.io.InputStream

@Component
class CSVParser(
    val context: Context
) {

    fun parseCardsCSV(inputStream: InputStream) : List<List<Card>> {
        val mapper = CsvMapper().registerModule(
            KotlinModule.Builder()
                .withReflectionCacheSize(512)
                .configure(KotlinFeature.NullToEmptyCollection, false)
                .configure(KotlinFeature.NullToEmptyMap, false)
                .configure(KotlinFeature.NullIsSameAsDefault, false)
                .configure(KotlinFeature.SingletonSupport, false)
                .configure(KotlinFeature.StrictNullChecks, false)
                .build()
        )
        val cards = mapper.readerFor(CSVCardEntry::class.java)
            .with(CsvSchema.emptySchema().withHeader())
            .readValues<CSVCardEntry>(inputStream)
            .readAll()
        return cards.map { entry ->
            val quantity = entry.quantity!!
            if (quantity > 1) {
                (1..quantity).map { Card(scryfallId = entry.scryfallId!!, title = entry.name!!, scryfallData = null, user = context.getUser()) }
            } else {
                listOf(Card(scryfallId = entry.scryfallId!!, title = entry.name!!, scryfallData = null, user = context.getUser()))
            }
        }.toList()
    }
}

private operator fun <E> List<E>.component6(): E {
    return this[5]
}
private operator fun <E> List<E>.component7(): E {
    return this[6]
}
private operator fun <E> List<E>.component8(): E {
    return this[7]
}
private operator fun <E> List<E>.component9(): E {
    return this[8]
}
