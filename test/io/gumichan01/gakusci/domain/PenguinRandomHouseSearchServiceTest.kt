package io.gumichan01.gakusci.domain

import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.client.penguin.PenguinRandomHouseSearchResponse
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.ServiceResponse
import io.gumichan01.gakusci.domain.service.IService
import io.gumichan01.gakusci.domain.utils.SearchType
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions
import kotlin.test.Test

class PenguinRandomHouseSearchServiceTest {

    private val searchClient: IClient<PenguinRandomHouseSearchResponse> = mockk {
        coEvery { retrieveResults(QueryParam("lorem", SearchType.BOOKS)) } returns
                PenguinRandomHouseSearchResponse(listOf(""))
        coEvery {
            retrieveResults(
                QueryParam(
                    "9780140043204",
                    SearchType.BOOKS
                )
            )
        } returns PenguinRandomHouseSearchResponse(emptyList())
    }

    @Test
    fun `Send valid search request - returns results`() {
        val service: IService = PenguinRandomHouseSearchService(searchClient)
        val result: ServiceResponse? = runBlocking { service.search(QueryParam("lorem", SearchType.BOOKS)) }
        Assertions.assertThat(result).isNotNull
    }

    @Test
    fun `Send valid search request but get no result - returns response with no entry`() {
        val service: IService = PenguinRandomHouseSearchService(searchClient)
        val result: ServiceResponse? = runBlocking { service.search(QueryParam("9780140043204", SearchType.BOOKS)) }
        Assertions.assertThat(result).isNotNull
        Assertions.assertThat(result?.totalResults).isZero()
        Assertions.assertThat(result?.entries).isEmpty()
    }
}