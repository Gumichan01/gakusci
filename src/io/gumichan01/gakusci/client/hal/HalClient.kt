package io.gumichan01.gakusci.client.hal

import io.gumichan01.gakusci.domain.model.QueryParam
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.json.JacksonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.get
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class HalClient {

    private val logger: Logger = LoggerFactory.getLogger(HalClient::class.java)
    private val halUrl = "https://api.archives-ouvertes.fr/search/?q=%s&rows=%d&wt=json"

    suspend fun retrieveResults(queryParam: QueryParam): HalResponse? {
        val url = halUrl.format(queryParam.query, queryParam.rows)
        return retrieveData(url)
    }

    private suspend fun retrieveData(url: String): HalResponse? {
        val client = HttpClient(Apache) {
            install(JsonFeature) {
                serializer = JacksonSerializer()
            }
        }
        return try {
            client.use { it.get(url) }
        } catch (e: Exception) {
            logger.trace(e.message)
            if (logger.isTraceEnabled) {
                e.printStackTrace()
            }
            null
        }
    }
}