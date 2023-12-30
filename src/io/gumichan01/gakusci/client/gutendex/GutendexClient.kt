package io.gumichan01.gakusci.client.gutendex

import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.client.utils.calculatePageToSearchFor
import io.gumichan01.gakusci.client.utils.trace
import io.gumichan01.gakusci.domain.model.QueryParam
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.apache.*
import io.ktor.client.plugins.cache.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.jackson.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.URLEncoder

class GutendexClient : IClient<GutendexResponse> {

    private val logger: Logger = LoggerFactory.getLogger(GutendexClient::class.java)
    private val gutendexUrl = "https://gutendex.com/books?search=%s"
    private val gutendexUrlOnPage = "https://gutendex.com/books?search=%s&page=%d"
    private val nbEntriesPerPage = 32 // See https://gutendex.com/#docs
    private val client = HttpClient(Apache) {
        install(HttpCache)
        install(ContentNegotiation) {
            jackson()
        }
    }

    override suspend fun retrieveResults(queryParam: QueryParam): GutendexResponse? {
        val page: Int = calculatePageToSearchFor(queryParam.start, nbEntriesPerPage)
        val url: String = if (page > 1) {
            gutendexUrlOnPage.format(URLEncoder.encode(queryParam.query, Charsets.UTF_8), page)
        } else {
            gutendexUrl.format(URLEncoder.encode(queryParam.query, Charsets.UTF_8))
        }

        return try {
            client.get(url).body()
        } catch (e: Exception) {
            trace(logger, e)
            null
        }
    }
}