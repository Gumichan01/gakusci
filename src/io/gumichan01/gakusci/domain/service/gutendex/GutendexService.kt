package io.gumichan01.gakusci.domain.service.gutendex

import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.client.gutendex.GutendexResponse
import io.gumichan01.gakusci.domain.model.ServiceResponse
import io.gumichan01.gakusci.domain.model.SimpleQuery
import io.gumichan01.gakusci.domain.model.entry.BookEntry
import io.gumichan01.gakusci.domain.model.entry.IResultEntry
import io.gumichan01.gakusci.domain.service.IService
import io.gumichan01.gakusci.domain.utils.ServiceRequestCache

class GutendexService(private val client: IClient<GutendexResponse>) : IService {

    private val cache = ServiceRequestCache()

    override suspend fun search(query: SimpleQuery): ServiceResponse {
        return cache.coget(query.query) {
            client.retrieveResults(query)?.let { gutendexResponse ->
                val entries: List<IResultEntry> = gutendexResponse.results
                    .filter { e -> e.isAccessible() }
                    .map { e -> BookEntry(e.title, e.authors.map { a -> a.name }.toString(), url = e.link(), thumbnailUrl = e.thumbnail()) }
                ServiceResponse(entries.size, entries)
            } ?: ServiceResponse(0, emptyList())
        }
    }
}