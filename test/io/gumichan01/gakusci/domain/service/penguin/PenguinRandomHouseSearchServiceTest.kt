package io.gumichan01.gakusci.domain.service.penguin

import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.client.penguin.PenguinRandomHouseSearchResponse
import io.gumichan01.gakusci.domain.model.ServiceResponse
import io.gumichan01.gakusci.domain.model.SimpleQuery
import io.gumichan01.gakusci.domain.model.entry.BookEntry
import io.gumichan01.gakusci.domain.service.IService
import io.gumichan01.gakusci.domain.utils.SearchType
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions
import kotlin.test.Test

@ExperimentalCoroutinesApi
class PenguinRandomHouseSearchServiceTest {

    private val searchClientMock: IClient<PenguinRandomHouseSearchResponse> = mockk {
        coEvery { retrieveResults(SimpleQuery("marx")) } returns
            PenguinRandomHouseSearchResponse(listOf(Pair("9780140043204", setOf("9780140043204"))))
        coEvery { retrieveResults(SimpleQuery("lorem")) } returns
            PenguinRandomHouseSearchResponse(
                listOf(
                    Pair("9780140043204", setOf("9780140043204")),
                    Pair("9780140150964", setOf("9780140150964"))
                )
            )
        coEvery {
            retrieveResults(
                SimpleQuery("9780140043204")
            )
        } returns PenguinRandomHouseSearchResponse(emptyList())
    }
    private val searchServiceMock: IService = mockk {
        coEvery { search(SimpleQuery("9780140043204")) } returns
            ServiceResponse(
                1,
                listOf(
                    BookEntry(
                        author = "marx",
                        bibKey = "marx",
                        url = "https//penguinrandomhouse.com/search/site?q=",
                        thumbnailUrl = "https://penguinrandomhouse.com/cover/"
                    )
                )
            )
        coEvery { search(SimpleQuery("9780140150964")) } returns
            ServiceResponse(
                1,
                listOf(
                    BookEntry(
                        bibKey = "marx2",
                        url = "https//penguinrandomhouse.com/search/site?q=",
                        thumbnailUrl = "https://penguinrandomhouse.com/cover/"
                    )
                )
            )
    }

    @Test
    fun `Send valid search request but get no result - returns response with no entry`() {
        val service: IService = PenguinRandomHouseSearchService(searchClientMock, searchServiceMock)
        val result: ServiceResponse = runBlocking { service.search(SimpleQuery("9780140043204")) }
        Assertions.assertThat(result).isNotNull
        Assertions.assertThat(result.totalResults).isZero
        Assertions.assertThat(result.entries).isEmpty()
    }

    @Test
    fun `Send valid search request and get one result - returns response with at least one valid entry`() {
        val service: IService = PenguinRandomHouseSearchService(searchClientMock, searchServiceMock)
        val result: ServiceResponse = runBlocking { service.search(SimpleQuery("marx")) }
        Assertions.assertThat(result).isNotNull
        Assertions.assertThat(result.entries.first()).isInstanceOf(BookEntry::class.java)
        val bookEntry: BookEntry = result.entries.first() as BookEntry
        Assertions.assertThat(bookEntry.author).containsIgnoringCase("marx")
        Assertions.assertThat(bookEntry.url).startsWith("https").contains("penguinrandomhouse.com/search/site?q=")
        Assertions.assertThat(bookEntry.thumbnailUrl).startsWith("https").contains("penguinrandomhouse.com/cover/")
    }
}