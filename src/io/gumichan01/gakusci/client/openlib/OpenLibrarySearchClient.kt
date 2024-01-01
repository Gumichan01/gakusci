package io.gumichan01.gakusci.client.openlib

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.client.utils.trace
import io.gumichan01.gakusci.domain.model.SimpleQuery
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.plugins.cache.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.URLEncoder

class OpenLibrarySearchClient : IClient<OpenLibrarySearchResponse> {

    private val logger: Logger = LoggerFactory.getLogger(OpenLibrarySearchClient::class.java)

    // NOTE This URL refers to an experimental Open Library API, so this class must be considered experimental
    // In order to avoid performance results are limited to 10 entries
    private val openLibMaxCount = 10
    private val openLibrarySearchUrl = "https://openlibrary.org/search.json?q=%s&limit=$openLibMaxCount"
    private val client = HttpClient(Apache) { install(HttpCache) }

    override suspend fun retrieveResults(query: SimpleQuery): OpenLibrarySearchResponse? {
        val url: String = openLibrarySearchUrl.format(URLEncoder.encode(query.query, Charsets.UTF_8))
        return retrieveData(url)
    }

    private suspend fun retrieveData(url: String): OpenLibrarySearchResponse? {
        return try {
            client.get(url).bodyAsText().fromJson()
        } catch (e: Exception) {
            trace(logger, e)
            null
        }
    }

    private fun String.fromJson(): OpenLibrarySearchResponse = jacksonObjectMapper().readValue(this)
}
