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
        val queryParam = QueryParam("lorem", SearchType.BOOKS)
        val response: PenguinRandomHouseSearchResponse? =
            runBlocking { client.retrieveResults(queryParam) }
        Assertions.assertThat(response).isNotNull
    }
}
