package io.gumichan01.gakusci.client.openlib

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.domain.model.QueryParam
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.request.get
import org.slf4j.Logger
import org.slf4j.LoggerFactory

// TODO Use the Book API if the query name is a(n) ISBN/LCCN/OCLC number
// https://openlibrary.org/dev/docs/api/books
class OpenLibraryClient : IClient<OpenLibrarySearchResponse> {

    private val logger: Logger = LoggerFactory.getLogger(OpenLibraryClient::class.java)
    // NOTE This URL refers to an experimental Open Library API, so this class must be considered experimental
    private val openLibrarySearchUrl = "https://openlibrary.org/search.json?q=%s"

    override suspend fun retrieveResults(queryParam: QueryParam): OpenLibrarySearchResponse? {
        val url = openLibrarySearchUrl.format(queryParam.query)
        return retrieveData(url)
    }

    private suspend fun retrieveData(url: String): OpenLibrarySearchResponse? {
        return try {
            HttpClient(Apache).use { it.get<String>(url) }.fromJson()
        } catch (e: Exception) {
            logger.trace(e.message)
            if (logger.isTraceEnabled) {
                e.printStackTrace()
            }
            null
        }
    }

    private fun String.fromJson(): OpenLibrarySearchResponse = jacksonObjectMapper().readValue(this)
}
