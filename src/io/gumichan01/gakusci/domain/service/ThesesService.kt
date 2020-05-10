package io.gumichan01.gakusci.domain.service

import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.client.theses.ThesesResponse
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.ServiceResponse
import io.gumichan01.gakusci.domain.model.entry.SimpleResultEntry
import io.gumichan01.gakusci.domain.search.cache.CacheHandler
import io.gumichan01.gakusci.domain.search.cache.SearchCache

class ThesesService(private val thesesClient: IClient<ThesesResponse>) : IService {

    private val cache: SearchCache = CacheHandler().createFreshCache()

    override suspend fun search(queryParam: QueryParam): ServiceResponse? {
        return cache.getOrUpdateCache(queryParam) {
            thesesClient.retrieveResults(queryParam)?.body?.let {
                val entries = it.docs.asSequence().filter { d -> d.isPresented() }.filter { d -> d.hasAccess() }
                    .map { doc -> SimpleResultEntry(doc.label(), doc.link()) }.toList()
                ServiceResponse(entries.size, entries)
            }
        }
    }
}