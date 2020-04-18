package io.gumichan01.gakusci.client.penguin

import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.utils.SearchType
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions
import kotlin.test.Test

class PenguinRandomHouseSearchClientTest {

    @Test
    fun `check valid request to external service - must return result`() {
        val client: IClient<String> = PenguinRandomHouseSearchClient()
        val response = runBlocking { client.retrieveResults(QueryParam("marx", SearchType.BOOKS)) }
        Assertions.assertThat(response).isNotNull()
    }
}