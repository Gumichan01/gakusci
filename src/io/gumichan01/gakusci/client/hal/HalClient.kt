package io.gumichan01.gakusci.client.hal

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

class HalClient : IClient<HalResponse> {

    private val logger: Logger = LoggerFactory.getLogger(HalClient::class.java)
    private val halUrl = "https://api.archives-ouvertes.fr/search/?q=%s&rows=%d&wt=json"

    override suspend fun retrieveResults(queryParam: QueryParam): HalResponse? {
        val url = halUrl.format(queryParam.query, queryParam.rows)
        return retrieveData(url)
    }

    private suspend fun retrieveData(url: String): HalResponse? {
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