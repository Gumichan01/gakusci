package io.gumichan01.gakusci.client.openlib

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.client.utils.trace
import io.gumichan01.gakusci.domain.model.SimpleQuery
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.plugins.cache.*
import io.ktor.client.request.get
import io.ktor.client.statement.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/*
* NOTE: (2025-02-22) This client uses the legacy book API
*
* Some book numbers (LCCNs, OCLC numbers and OLIDs) works with this for now.
* For book that can be found by ISBN, a dedicated endpoint could be used instead of this.
*
*/
class OpenLibraryBookClient : IClient<OpenLibraryBookResponse> {

    private val logger: Logger = LoggerFactory.getLogger(OpenLibraryBookClient::class.java)
    private val openLibrarySearchUrl = "http://openlibrary.org/api/books?bibkeys=%s&format=json"
    private val client = HttpClient(Apache) { install(HttpCache) }

    override suspend fun retrieveResults(query: SimpleQuery): OpenLibraryBookResponse? {
        return try {
            client.get(openLibrarySearchUrl.format(query.query)).bodyAsText().fromJson()
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


