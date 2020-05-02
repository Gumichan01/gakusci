package io.gumichan01.gakusci.domain.service

import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.client.penguin.PenguinRandomHouseBookResponse
import io.gumichan01.gakusci.client.utils.BookNumberType
import io.gumichan01.gakusci.client.utils.generateBookNumberFromText
import io.gumichan01.gakusci.client.utils.isValidISBN13
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.ServiceResponse
import io.gumichan01.gakusci.domain.model.entry.BookEntry
import io.gumichan01.gakusci.domain.search.cache.CacheHandler
import io.gumichan01.gakusci.domain.search.cache.SearchCache
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class PenguinRandomHouseBookService(private val bookClient: IClient<PenguinRandomHouseBookResponse>) : IService {

    private val bookLink = "https://penguinrandomhouse.com/search/site?q="
    private val thumbnailLink = "https://images1.penguinrandomhouse.com/cover/"
    private val cache: SearchCache = CacheHandler().createFreshCache()

    override suspend fun search(queryParam: QueryParam): ServiceResponse? {
        return cache.getOrUpdateCache(queryParam) {
            generateBookNumberFromText(queryParam.query)?.let { bookNumber ->
                if (bookNumber.type == BookNumberType.ISBN && isValidISBN13(bookNumber.value)) {
                    bookClient.retrieveResults(queryParam.copy(query = bookNumber.value))?.let { response ->
                        ServiceResponse(1, listOf(BookEntry(label(response), link(response), thumbnail(response))))
                    }
                } else null
            }
        }
    }

    private fun label(bookResponse: PenguinRandomHouseBookResponse): String {
        val pattern: DateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
        val date: LocalDate = LocalDate.parse(bookResponse.publishDate, pattern)
        return bookResponse.run { "$title, $author, ${date.year}" }
    }

    private fun thumbnail(bookResponse: PenguinRandomHouseBookResponse): String {
        return thumbnailLink + bookResponse.isbn
    }

    private fun link(bookResponse: PenguinRandomHouseBookResponse): String {
        return bookLink + bookResponse.isbn
    }
}