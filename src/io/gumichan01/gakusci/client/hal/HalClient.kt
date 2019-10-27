package io.gumichan01.gakusci.client.hal

import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.json.JacksonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.get
import org.slf4j.LoggerFactory

class HalClient {

    private val logger = LoggerFactory.getLogger(HalClient::class.java)
    private val halUrl = "https://api.archives-ouvertes.fr/search/?q=%s&wt=json"

    suspend fun retrieveResults(query: String): List<HalResultEntry> {
        val url = halUrl.format(query)
        return retrieveData(url)?.response?.docs ?: emptyList()
    }

    private suspend fun retrieveData(url: String): HalResponse? {
        val client = HttpClient(Apache) {
            install(JsonFeature) {
                serializer = JacksonSerializer()
            }
        }

        return try {
            client.get(url)
        } catch (e: Exception) {
            logger.warn(e.message)
            null
        } finally {
            client.close()
        }
    }
}