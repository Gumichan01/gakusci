package io.gumichan01.gakusci.client.penguin

import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.SimpleQuery
import io.gumichan01.gakusci.domain.utils.SearchType
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions

class PenguinRandomHouseSearchClientTest {

    //    @Test
    fun `check valid request to PRH - must return something`() {
        val client: IClient<PenguinRandomHouseSearchResponse> = PenguinRandomHouseSearchClient()
        val queryParam = SimpleQuery("lorem")
        val response: PenguinRandomHouseSearchResponse? =
            runBlocking { client.retrieveResults(queryParam) }
        Assertions.assertThat(response).isNotNull
    }
}
