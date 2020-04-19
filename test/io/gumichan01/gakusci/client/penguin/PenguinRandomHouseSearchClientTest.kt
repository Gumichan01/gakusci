package io.gumichan01.gakusci.client.penguin

import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.utils.SearchType
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions
import kotlin.test.Test

class PenguinRandomHouseSearchClientTest {

//    @Test
    fun `check valid request to external service - must return result`() {
        val client: IClient<PenguinRandomHouseSearchResponse> = PenguinRandomHouseSearchClient()
        val queryParam = QueryParam("marx", SearchType.BOOKS)
        val response: PenguinRandomHouseSearchResponse? =
            runBlocking { client.retrieveResults(queryParam) }
        Assertions.assertThat(response).isNotNull
        Assertions.assertThat(response?.isbnEntries).isNotEmpty
    }
}