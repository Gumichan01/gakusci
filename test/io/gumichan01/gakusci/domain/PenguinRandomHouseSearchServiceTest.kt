package io.gumichan01.gakusci.domain

import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.client.penguin.PenguinRandomHouseSearchResponse
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.ServiceResponse
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
        coEvery { retrieveResults(QueryParam("marx", SearchType.BOOKS, 1)) } returns
                PenguinRandomHouseSearchResponse(listOf(Pair("9780140043204", emptySet())))
        coEvery { retrieveResults(QueryParam("lorem", SearchType.BOOKS)) } returns
                PenguinRandomHouseSearchResponse(
                    listOf(
                        Pair("9780140043204", emptySet()),
                        Pair("9780140150964", emptySet())
                    )
                )
        coEvery {
            retrieveResults(
                QueryParam(
                    "9780140043204",
                    SearchType.BOOKS
                )
            )
        } returns PenguinRandomHouseSearchResponse(emptyList())
    }

    private val searchServiceMock: IService = mockk {
        coEvery { search(QueryParam("9780140043204", SearchType.BOOKS)) } returns
                ServiceResponse(
                    1,
                    listOf(
                        BookEntry(
                            "marx",
                            "https//penguinrandomhouse.com/search/site?q=",
                            "https://penguinrandomhouse.com/cover/"
                        )
                    )
                )
        coEvery { search(QueryParam("9780140150964", SearchType.BOOKS)) } returns
                ServiceResponse(
                    1,
                    listOf(
                        BookEntry(
                            "marx2",
                            "https//penguinrandomhouse.com/search/site?q=",
                            "https://penguinrandomhouse.com/cover/"
                        )
                    )
                )
    }

    @Test
    fun `Send valid search request but get no result - returns response with no entry`() {
        val service: IService = PenguinRandomHouseSearchService(searchClientMock, searchServiceMock)
        val result: ServiceResponse? = runBlocking { service.search(QueryParam("9780140043204", SearchType.BOOKS)) }
        Assertions.assertThat(result).isNotNull
        Assertions.assertThat(result?.totalResults).isZero()
        Assertions.assertThat(result?.entries).isEmpty()
    }

    @Test
    fun `Send valid search request and get one result - returns response with one entry`() {
        val service: IService = PenguinRandomHouseSearchService(searchClientMock, searchServiceMock)
        val result: ServiceResponse? = runBlocking { service.search(QueryParam("marx", SearchType.BOOKS, 1)) }
        Assertions.assertThat(result).isNotNull
        Assertions.assertThat(result?.totalResults).isNotZero()
        Assertions.assertThat(result?.totalResults).isOne()
        Assertions.assertThat(result?.entries).isNotEmpty()
        Assertions.assertThat(result?.entries?.size).isOne()
    }

    @Test
    fun `Send valid search request and get one result - returns response with one valid entry`() {
        val service: IService = PenguinRandomHouseSearchService(searchClientMock, searchServiceMock)
        val result: ServiceResponse? = runBlocking { service.search(QueryParam("marx", SearchType.BOOKS, 1)) }
        Assertions.assertThat(result).isNotNull
        Assertions.assertThat(result?.entries?.first()).isInstanceOf(BookEntry::class.java)
        val bookEntry: BookEntry = result?.entries?.first() as BookEntry
        Assertions.assertThat(bookEntry.label).containsIgnoringCase("marx")
        Assertions.assertThat(bookEntry.url).startsWith("https").contains("penguinrandomhouse.com/search/site?q=")
        Assertions.assertThat(bookEntry.thumbnailUrl).startsWith("https").contains("penguinrandomhouse.com/cover/")
    }

    @Test
    fun `Send valid search request and get several results - returns response with several entries`() {
        val service: IService = PenguinRandomHouseSearchService(searchClientMock, searchServiceMock)
        val result: ServiceResponse? = runBlocking { service.search(QueryParam("lorem", SearchType.BOOKS)) }
        Assertions.assertThat(result).isNotNull
        Assertions.assertThat(result?.entries).hasAtLeastOneElementOfType(BookEntry::class.java)
        Assertions.assertThat(result?.entries?.size).isGreaterThan(1)
    }
}