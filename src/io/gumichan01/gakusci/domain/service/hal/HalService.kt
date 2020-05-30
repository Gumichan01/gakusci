package io.gumichan01.gakusci.domain.service.hal

import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.client.hal.HalResponse
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.ServiceResponse
import io.gumichan01.gakusci.domain.model.entry.SimpleResultEntry
import io.gumichan01.gakusci.domain.search.cache.CacheHandler
import io.gumichan01.gakusci.domain.search.cache.SearchCache
import io.gumichan01.gakusci.domain.service.IService

class HalService(private val halClient: IClient<HalResponse>) : IService {

    private val cache: SearchCache = CacheHandler().createFreshCache()

    override suspend fun search(queryParam: QueryParam): ServiceResponse? {
        return cache.getOrUpdateCache(queryParam) {
            halClient.retrieveResults(queryParam)?.let {
                val (totalResults, _, results) = it.body
                val entries: List<SimpleResultEntry> = results?.map { e ->
                    SimpleResultEntry(e.label, e.uri)
                } ?: emptyList()
                ServiceResponse(totalResults, entries)
            }
        }
    }
}