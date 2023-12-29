package io.gumichan01.gakusci.client.penguin

import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.utils.SearchType
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions
import kotlin.test.assertTrue

class PenguinRandomHouseSearchClientTest {

//    @Test
    fun `check valid request to PRH - must return something`() {
        val client: IClient<PenguinRandomHouseSearchResponse> = PenguinRandomHouseSearchClient()
        val queryParam = QueryParam("lorem", SearchType.BOOKS, )
        val response: PenguinRandomHouseSearchResponse? =
            runBlocking { client.retrieveResults(queryParam) }
        Assertions.assertThat(response).isNotNull
    }

//    @Test
    fun `check valid request to PRH with 'marx' and retrieve at most 100 entries - must return at most 100 entries`() {
        val client: IClient<PenguinRandomHouseSearchResponse> = PenguinRandomHouseSearchClient()
        val queryParam = QueryParam("marx", SearchType.BOOKS, rows = 100, )
        val response: PenguinRandomHouseSearchResponse? =
            runBlocking { client.retrieveResults(queryParam) }
        Assertions.assertThat(response?.entries?.size).isLessThanOrEqualTo(100)
    }

//    @Test
    fun `check valid request to PRH with request containing spaces like 'karl marx' - must not fail, entries are not important`() {
        val client: IClient<PenguinRandomHouseSearchResponse> = PenguinRandomHouseSearchClient()
        val queryParam = QueryParam("karl marx", SearchType.BOOKS, rows = 1, )
        runBlocking { client.retrieveResults(queryParam) }
        assertTrue { true }
    }
}
