package io.gumichan01.gakusci.domain.service.penguin

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
import io.gumichan01.gakusci.domain.service.IService
import io.gumichan01.gakusci.domain.utils.toLocalDate
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
                    bookClient.retrieveResults(queryParam.copy(query = bookNumber.value))?.let {
                        val dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
                        ServiceResponse(
                            1,
                            listOf(
                                BookEntry(
                                    it.author,
                                    it.title,
                                    it.publishDate.toLocalDate(dateFormatter).year.toString(),
                                    it.isbn,
                                    it.link(),
                                    it.thumbnail()
                                )
                            )
                        )
                    }
                } else null
            }
        }
    }

    @Deprecated("remove it")
    private fun String.toLocalDate(): LocalDate {
        return LocalDate.parse(this, DateTimeFormatter.ofPattern("MM/dd/yyyy"))
    }

    private fun PenguinRandomHouseBookResponse.thumbnail(): String {
        return this@PenguinRandomHouseBookService.thumbnailLink + this.isbn
    }

    private fun PenguinRandomHouseBookResponse.link(): String {
        return this@PenguinRandomHouseBookService.bookLink + this.isbn
    }
}