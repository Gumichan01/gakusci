package io.gumichan01.gakusci.domain.service.penguin

import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.client.penguin.PenguinRandomHouseBookResponse
import io.gumichan01.gakusci.client.utils.BookNumberType
import io.gumichan01.gakusci.client.utils.generateBookNumberFromText
import io.gumichan01.gakusci.client.utils.isValidISBN13
import io.gumichan01.gakusci.domain.model.ServiceResponse
import io.gumichan01.gakusci.domain.model.SimpleQuery
import io.gumichan01.gakusci.domain.model.entry.BookEntry
import io.gumichan01.gakusci.domain.service.IService
import io.gumichan01.gakusci.domain.utils.toLocalDate
import java.time.format.DateTimeFormatter

class PenguinRandomHouseBookService(private val bookClient: IClient<PenguinRandomHouseBookResponse>) : IService {

    private val bookLink = "https://penguinrandomhouse.com/search/site?q="
    private val thumbnailLink = "https://images1.penguinrandomhouse.com/cover/"

    override suspend fun search(query: SimpleQuery): ServiceResponse {
        return generateBookNumberFromText(query.query)?.let { bookNumber ->
            if (bookNumber.type == BookNumberType.ISBN && isValidISBN13(bookNumber.value)) {
                bookClient.retrieveResults(query.copy(query = bookNumber.value))?.let {
                    val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
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
            } else ServiceResponse(0, emptyList())
        } ?: ServiceResponse(0, emptyList())
    }

    private fun PenguinRandomHouseBookResponse.thumbnail(): String {
        return this@PenguinRandomHouseBookService.thumbnailLink + this.isbn
    }

    private fun PenguinRandomHouseBookResponse.link(): String {
        return this@PenguinRandomHouseBookService.bookLink + this.isbn
    }
}