package io.gumichan01.gakusci.client.openlib

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.client.utils.calculatePageToSearchFor
import io.gumichan01.gakusci.client.utils.trace
import io.gumichan01.gakusci.domain.model.QueryParam
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.plugins.cache.*
import io.ktor.client.request.get
import io.ktor.client.statement.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.URLEncoder

class OpenLibrarySearchClient : IClient<OpenLibrarySearchResponse> {

    private val logger: Logger = LoggerFactory.getLogger(OpenLibrarySearchClient::class.java)

    // NOTE This URL refers to an experimental Open Library API, so this class must be considered experimental
    private val nbEntriesPerPage = 10
    private val openLibrarySearchUrl = "https://openlibrary.org/search.json?q=%s&page=%d&limit=10"
    private val client = HttpClient(Apache) { install(HttpCache) }

    override suspend fun retrieveResults(queryParam: QueryParam): OpenLibrarySearchResponse? {
        val page: Int = calculatePageToSearchFor(queryParam.start, nbEntriesPerPage)
        val url: String = openLibrarySearchUrl.format(URLEncoder.encode(queryParam.query, Charsets.UTF_8), page)
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
