package io.gumichan01.gakusci.domain.service

import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.client.penguin.PenguinRandomHouseResponse
import io.gumichan01.gakusci.client.utils.generateBookNumberFromText
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.ServiceResponse
import io.gumichan01.gakusci.domain.model.entry.BookEntry

class PenguinRandomHouseIsbnService(private val isbnClient: IClient<PenguinRandomHouseResponse>) : IService {
    override suspend fun search(queryParam: QueryParam): ServiceResponse? {
        return generateBookNumberFromText(queryParam.query)?.let { book ->
            isbnClient.retrieveResults(queryParam.copy(query = book.value))?.let {
                ServiceResponse(1, listOf(BookEntry(it.label(), it.link(), it.thumbnail())))
            }
        }
    }
}