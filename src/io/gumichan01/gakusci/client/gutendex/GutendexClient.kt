package io.gumichan01.gakusci.client.gutendex

import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.client.utils.trace
import io.gumichan01.gakusci.domain.model.QueryParam
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.apache.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.jackson.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.URLEncoder

class GutendexClient: IClient<GutendexResponse> {

    private val logger: Logger = LoggerFactory.getLogger(GutendexClient::class.java)
    private val gutendexUrl = "https://gutendex.com/books?search=%s"

    override suspend fun retrieveResults(queryParam: QueryParam): GutendexResponse? {
        val url: String = gutendexUrl.format(URLEncoder.encode(queryParam.query, Charsets.UTF_8))
        val client = HttpClient(Apache) {
            install(ContentNegotiation) {
                jackson()
            }
        }
        return try {
            client.use { it.get(url).body() }
        } catch (e: Exception) {
            trace(logger, e)
            null
        }
    }
}