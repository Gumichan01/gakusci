package io.gumichan01.gakusci.client.penguin

import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.client.utils.trace
import io.gumichan01.gakusci.domain.model.QueryParam
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.json.JacksonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.get
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class PenguinRandomHouseSearchClient : IClient<String> {

    private val logger: Logger = LoggerFactory.getLogger(PenguinRandomHouseSearchClient::class.java)
    private val searchUrl = "https://reststop.randomhouse.com/resources/titles?start=0&max=%d&expandLevel=0&search=%s"

    override suspend fun retrieveResults(queryParam: QueryParam): String? {
        val url: String = searchUrl.format(queryParam.rows, queryParam.query)
        val client = HttpClient(Apache) {
//            install(JsonFeature) {
//                serializer = JacksonSerializer()
//            }
        }
        return try {
            client.use { it.get(url) }
        } catch (e: Exception) {
            trace(logger, e)
            null
        }
    }
}