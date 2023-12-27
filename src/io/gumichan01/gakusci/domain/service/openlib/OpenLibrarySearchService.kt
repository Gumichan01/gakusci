package io.gumichan01.gakusci.domain.service.openlib

import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.client.openlib.OpenLibrarySearchResponse
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.ServiceResponse
import io.gumichan01.gakusci.domain.model.entry.BookEntry
import io.gumichan01.gakusci.domain.model.entry.IResultEntry
import io.gumichan01.gakusci.domain.search.cache.CacheHandler
import io.gumichan01.gakusci.domain.search.cache.SearchCache
import io.gumichan01.gakusci.domain.service.IService

class OpenLibrarySearchService(private val openLibrarySearchClient: IClient<OpenLibrarySearchResponse>) : IService {

    private val cache: SearchCache = CacheHandler().createFreshCache()

    override suspend fun search(queryParam: QueryParam): ServiceResponse? {
        return cache.getOrUpdateCache(queryParam) {
            openLibrarySearchClient.retrieveResults(queryParam)?.let {
                val nbEntries: Int = it.numFound
                val entries: List<IResultEntry> = it.docs?.asSequence()?.map { doc ->
                    BookEntry(
                        doc.authors(),
                        doc.title,
                        doc.publishDate(),
                        url = doc.link(),
                        thumbnailUrl = doc.thumbnail()
                    )
                }?.sortedByDescending { b -> b.thumbnailUrl }?.toList() ?: emptyList()
                ServiceResponse(nbEntries, entries)
            }
        }
    }
}