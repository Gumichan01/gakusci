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
                // Since the API is experimental and returns by default at most 100 results per page (even the first page)
                // The service will take 100 results
                val maxNumFound = 100
                val numFound = (if (it.numFound < maxNumFound) it.numFound else maxNumFound)
                val entries: List<IResultEntry> = it.docs?.asSequence()?.map { doc ->
                    BookEntry(
                        doc.authors(),
                        doc.title,
                        doc.publishDate(),
                        url = doc.link(),
                        thumbnailUrl = doc.thumbnail()
                    )
                }?.take(numFound)?.sortedByDescending { b -> b.thumbnailUrl }?.toList() ?: emptyList()
                ServiceResponse(numFound, entries)
            }
        }
    }
}