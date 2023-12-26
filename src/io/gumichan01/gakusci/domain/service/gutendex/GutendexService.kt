package io.gumichan01.gakusci.domain.service.gutendex

import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.client.gutendex.GutendexResponse
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.ServiceResponse
import io.gumichan01.gakusci.domain.model.entry.BookEntry
import io.gumichan01.gakusci.domain.model.entry.IResultEntry
import io.gumichan01.gakusci.domain.service.IService

class GutendexService(private val client: IClient<GutendexResponse>) : IService {
    override suspend fun search(queryParam: QueryParam): ServiceResponse? {
        val response: GutendexResponse? = client.retrieveResults(queryParam)
        return response?.let {
            val count: Int = it.count
            val entries: List<IResultEntry> = it.results
                    .filter { e -> e.isAccessible() }
                    .map { e -> BookEntry(e.title, e.authors.map { a -> a.name }.toString(), url = e.link(), thumbnailUrl = e.thumbnail()) }
            ServiceResponse(count, entries)
        }
    }
}