package io.gumichan01.gakusci.domain.service.openlib

import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.client.openlib.OpenLibraryBookResponse
import io.gumichan01.gakusci.client.utils.BookNumber
import io.gumichan01.gakusci.client.utils.generateBookNumberFromText
import io.gumichan01.gakusci.domain.model.ServiceResponse
import io.gumichan01.gakusci.domain.model.SimpleQuery
import io.gumichan01.gakusci.domain.model.entry.BookEntry
import io.gumichan01.gakusci.domain.service.IService
import io.gumichan01.gakusci.domain.utils.ServiceRequestCache
import io.gumichan01.gakusci.domain.utils.defaultThumbnailLink

class OpenLibraryBookService(private val openLibBookClient: IClient<OpenLibraryBookResponse>) : IService {

    private val cache = ServiceRequestCache()

    override suspend fun search(query: SimpleQuery): ServiceResponse {
        return cache.coget(query.query) {
            generateBookNumberFromText(query.query)?.let { bookNumber ->
                openLibBookClient.retrieveResults(query.copy(query = bookNumber.format()))?.let { book ->
                    ServiceResponse(
                        1,
                        listOf(BookEntry(bibKey = book.bibKey, url = book.infoUrl, thumbnailUrl = book.thumbnailUrl
                            ?: defaultThumbnailLink()))
                    )
                }
            } ?: ServiceResponse(0, emptyList())
        }
    }

    private fun BookNumber.format(): String = "${type.value}:${value}"
}