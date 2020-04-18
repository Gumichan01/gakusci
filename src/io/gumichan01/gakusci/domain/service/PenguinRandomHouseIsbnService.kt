package io.gumichan01.gakusci.domain.service

import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.client.penguin.PenguinRandomHouseIsbnResponse
import io.gumichan01.gakusci.client.utils.generateBookNumberFromText
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.ServiceResponse
import io.gumichan01.gakusci.domain.model.entry.BookEntry

class PenguinRandomHouseIsbnService(private val isbnClient: IClient<PenguinRandomHouseIsbnResponse>) : IService {

    private val bookLink = "https://penguinrandomhouse.com/search/site?q="
    private val thumbnailLink = "https://images1.penguinrandomhouse.com/cover/"

    override suspend fun search(queryParam: QueryParam): ServiceResponse? {
        return generateBookNumberFromText(queryParam.query)?.let { book ->
            isbnClient.retrieveResults(queryParam.copy(query = book.value))?.let {
                ServiceResponse(1, listOf(BookEntry(label(it), link(it), thumbnail(it))))
            }
        }
    }

    private fun label(isbnResponse: PenguinRandomHouseIsbnResponse): String {
        return isbnResponse.run { "$title, $author, ${publishDate.split("/").last()}" }
    }

    private fun thumbnail(isbnResponse: PenguinRandomHouseIsbnResponse): String {
        return thumbnailLink + isbnResponse.isbn
    }

    private fun link(isbnResponse: PenguinRandomHouseIsbnResponse): String {
        return bookLink + isbnResponse.isbn
    }
}