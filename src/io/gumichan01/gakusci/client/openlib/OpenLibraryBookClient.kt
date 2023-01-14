package io.gumichan01.gakusci.client.openlib

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.client.utils.trace
import io.gumichan01.gakusci.domain.model.QueryParam
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.request.get
import io.ktor.client.statement.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class OpenLibraryBookClient : IClient<OpenLibraryBookResponse> {

    private val logger: Logger = LoggerFactory.getLogger(OpenLibraryBookClient::class.java)
    private val openLibrarySearchUrl = "http://openlibrary.org/api/books?bibkeys=%s&format=json"

    override suspend fun retrieveResults(queryParam: QueryParam): OpenLibraryBookResponse? {
        return try {
            HttpClient(Apache).use { it.get(openLibrarySearchUrl.format(queryParam.query)).bodyAsText() }.fromJson()
        } catch (e: Exception) {
            trace(logger, e)
            null
        }
    }

    private fun String.fromJson(): OpenLibraryBookResponse? {
        return jacksonObjectMapper().readValue(
            this,
            object : TypeReference<Map<String, OpenLibraryBookResponse>>() {}).values.toList().firstOrNull()
    }
}


