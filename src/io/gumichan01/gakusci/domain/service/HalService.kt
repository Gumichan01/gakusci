package io.gumichan01.gakusci.domain.service

import io.gumichan01.gakusci.client.hal.HalClient
import io.gumichan01.gakusci.domain.model.ResultEntry

class HalService(private val halClient: HalClient) : IService {
    override suspend fun search(query: String): List<ResultEntry> {
        return halClient.retrieveResults(query).map { r -> ResultEntry(r.label, r.uri) }
    }
}