package io.gumichan01.gakusci.client.penguin

import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.domain.model.QueryParam
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.json.JacksonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.get
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class PenguinRandomHouseIsbnClient : IClient<PenguinRandomHouseIsbnResponse> {

    private val logger: Logger = LoggerFactory.getLogger(PenguinRandomHouseIsbnClient::class.java)

    //private val penguinUrl = "https://reststop.randomhouse.com/resources/titles?start=0&max=%d&expandLevel=0&search=%s"
    private val penguinIsbnUrl = "https://reststop.randomhouse.com/resources/titles/%s"

    override suspend fun retrieveResults(queryParam: QueryParam): PenguinRandomHouseIsbnResponse? {
        val url: String = penguinIsbnUrl.format(queryParam.query)
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
