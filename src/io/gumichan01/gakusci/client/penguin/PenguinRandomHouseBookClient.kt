package io.gumichan01.gakusci.client.penguin

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

class PenguinRandomHouseBookClient : IClient<PenguinRandomHouseBookResponse> {

    private val logger: Logger = LoggerFactory.getLogger(PenguinRandomHouseBookClient::class.java)
    private val penguinIsbnUrl = "https://reststop.randomhouse.com/resources/titles/%s"

    override suspend fun retrieveResults(queryParam: QueryParam): PenguinRandomHouseBookResponse? {
        val url: String = penguinIsbnUrl.format(queryParam.query)
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
