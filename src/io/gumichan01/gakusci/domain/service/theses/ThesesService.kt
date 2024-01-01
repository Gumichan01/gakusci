package io.gumichan01.gakusci.domain.service.theses

import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.client.theses.ThesesResponse
import io.gumichan01.gakusci.domain.model.ServiceResponse
import io.gumichan01.gakusci.domain.model.SimpleQuery
import io.gumichan01.gakusci.domain.model.entry.SimpleResultEntry
import io.gumichan01.gakusci.domain.service.IService
import io.gumichan01.gakusci.domain.utils.ServiceRequestCache

class ThesesService(private val thesesClient: IClient<ThesesResponse>) : IService {

    private val cache = ServiceRequestCache()

    override suspend fun search(query: SimpleQuery): ServiceResponse {
        return cache.coget(query.query) {
            thesesClient.retrieveResults(query)?.body?.let { response ->
                val entries: List<SimpleResultEntry> = response.docs.asSequence().filter { d -> d.isPresented() }.filter { d -> d.hasAccess() }
                    .map { doc -> SimpleResultEntry(doc.label(), doc.link()) }.toList()
                ServiceResponse(entries.size, entries)
            } ?: ServiceResponse(0, emptyList())
        }
    }
}