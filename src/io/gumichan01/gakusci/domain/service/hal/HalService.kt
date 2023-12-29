package io.gumichan01.gakusci.domain.service.hal

import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.client.hal.HalResponse
import io.gumichan01.gakusci.client.hal.HalResultEntry
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.ServiceResponse
import io.gumichan01.gakusci.domain.model.entry.SimpleResultEntry
import io.gumichan01.gakusci.domain.service.IService
import io.gumichan01.gakusci.domain.utils.ServiceRequestCache

class HalService(private val halClient: IClient<HalResponse>) : IService {

    private val cache = ServiceRequestCache()

    override suspend fun search(queryParam: QueryParam): ServiceResponse? {
        return cache.coget(queryParam.uri) {
            halClient.retrieveResults(queryParam)?.let {
                val (totalResults: Int, _: Int, results: List<HalResultEntry>?) = it.body
                val entries: List<SimpleResultEntry> = results?.map { e ->
                    SimpleResultEntry(e.label, e.uri)
                } ?: emptyList()
                ServiceResponse(totalResults, entries)
            }
        }
    }
}
