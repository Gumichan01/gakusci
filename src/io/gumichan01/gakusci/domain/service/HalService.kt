package io.gumichan01.gakusci.domain.service

import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.client.hal.HalResponse
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.SimpleResultEntry
import io.gumichan01.gakusci.domain.model.ServiceResponse

class HalService(private val halClient: IClient<HalResponse>) : IService {

    override suspend fun search(queryParam: QueryParam): ServiceResponse? {
        return halClient.retrieveResults(queryParam)?.let {
            val (totalResults, _, results) = it.body
            val entries: List<SimpleResultEntry> = results?.map { e -> SimpleResultEntry(e.label, e.uri) } ?: emptyList()
            return ServiceResponse(totalResults, entries)
        }
    }
}