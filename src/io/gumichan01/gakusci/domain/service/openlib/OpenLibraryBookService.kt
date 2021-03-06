package io.gumichan01.gakusci.domain.service.openlib

import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.client.openlib.OpenLibraryBookResponse
import io.gumichan01.gakusci.client.utils.BookNumber
import io.gumichan01.gakusci.client.utils.generateBookNumberFromText
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.ServiceResponse
import io.gumichan01.gakusci.domain.model.entry.BookEntry
import io.gumichan01.gakusci.domain.search.cache.CacheHandler
import io.gumichan01.gakusci.domain.search.cache.SearchCache
import io.gumichan01.gakusci.domain.service.IService

class OpenLibraryBookService(private val openLibBookClient: IClient<OpenLibraryBookResponse>) : IService {

    private val cache: SearchCache = CacheHandler().createFreshCache()

    override suspend fun search(queryParam: QueryParam): ServiceResponse? {
        return cache.getOrUpdateCache(queryParam) {
            generateBookNumberFromText(queryParam.query)?.let { bookNumber ->
                openLibBookClient.retrieveResults(queryParam.copy(query = bookNumber.format()))?.let {
                    ServiceResponse(
                        1,
                        listOf(BookEntry(bibKey = it.bibKey, url = it.infoUrl, thumbnailUrl = it.thumbnailUrl ?: ""))
                    )
                }
            }
        }
    }

    private fun BookNumber.format(): String = "${type.value}:${value}"
}