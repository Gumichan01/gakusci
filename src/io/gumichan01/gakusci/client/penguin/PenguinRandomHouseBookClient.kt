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

class PenguinRandomHouseBookClient : IClient<PenguinRandomHouseBookResponse> {

    private val logger: Logger = LoggerFactory.getLogger(PenguinRandomHouseBookClient::class.java)
    private val penguinIsbnUrl = "https://reststop.randomhouse.com/resources/titles/%s"

    override suspend fun retrieveResults(queryParam: QueryParam): PenguinRandomHouseBookResponse? {
        val url: String = penguinIsbnUrl.format(queryParam.query)
        val client = HttpClient(Apache) {
            install(JsonFeature) {
                serializer = JacksonSerializer()
            }
        }
        return try {
            client.use { it.get(url) }
        } catch (e: Exception) {
            trace(logger, e)
            null
        }
    }
}
