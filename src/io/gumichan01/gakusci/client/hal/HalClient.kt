package io.gumichan01.gakusci.client.hal

import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.json.JacksonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.get

class HalClient {

    private val halUrl = "https://api.archives-ouvertes.fr/search/?q=%s&wt=json"
    suspend fun retrieveResults(query: String): List<HalResultEntry> {
        val client = HttpClient(Apache) {
            install(JsonFeature) {
                serializer = JacksonSerializer()
            }
        }

        val url = halUrl.format(query)
        val entries: List<HalResultEntry> = client.get<HalResponse>(url).response.docs ?: listOf()
        client.close()
        return entries
    }
}