package io.gumichan01.gakusci.client.openlib

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.client.hal.HalClient
import io.gumichan01.gakusci.client.utils.BookNumber
import io.gumichan01.gakusci.client.utils.generateBookNumberFromText
import io.gumichan01.gakusci.domain.model.QueryParam
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.request.get
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class OpenLibraryBookClient : IClient<OpenLibraryBookResponse> {

    private val logger: Logger = LoggerFactory.getLogger(OpenLibraryBookClient::class.java)
    private val openLibrarySearchUrl = "http://openlibrary.org/api/books?bibkeys=%s&format=json"

    override suspend fun retrieveResults(queryParam: QueryParam): OpenLibraryBookResponse? {
        val bookNumber: BookNumber? = generateBookNumberFromText(queryParam.query)
        return if (bookNumber != null) {
            try {
                HttpClient(Apache).use { it.get<String>(openLibrarySearchUrl.format(bookNumber.format())) }.fromJson()
            } catch (e: Exception) {
                logger.trace(e.message)
                if (logger.isTraceEnabled) {
                    e.printStackTrace()
                }
                null
            }
        } else {
            null
        }
    }

    private fun BookNumber.format(): String = "${type.value}:${value}"

    private fun String.fromJson(): OpenLibraryBookResponse? {
        return jacksonObjectMapper().readValue<Map<String, OpenLibraryBookResponse>>(
            this,
            object : TypeReference<Map<String, OpenLibraryBookResponse>>() {}).values.toList().firstOrNull()
    }
}