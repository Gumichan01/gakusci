package io.gumichan01.gakusci.client.hal

import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.client.utils.NUM_ENTRIES_PER_SERVICE
import io.gumichan01.gakusci.client.utils.trace
import io.gumichan01.gakusci.domain.model.SimpleQuery
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

class HalClient : IClient<HalResponse> {

    private val logger: Logger = LoggerFactory.getLogger(HalClient::class.java)
    private val halUrl = "https://api.archives-ouvertes.fr/search/?q=%s&rows=%d&wt=json"
    private val client = HttpClient(Apache) {
        install(HttpCache)
        install(ContentNegotiation) {
            jackson()
        }
    }

    override suspend fun retrieveResults(query: SimpleQuery): HalResponse? {
        val url: String = halUrl.format(URLEncoder.encode(query.query, Charsets.UTF_8), NUM_ENTRIES_PER_SERVICE)
        return retrieveData(url)
    }

    private suspend fun retrieveData(url: String): HalResponse? {
        return try {
            client.get(url).body()
        } catch (e: Exception) {
            trace(logger, e)
            null
        }
    }
}