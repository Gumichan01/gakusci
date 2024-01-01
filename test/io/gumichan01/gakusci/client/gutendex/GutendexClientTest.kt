package io.gumichan01.gakusci.client.gutendex

import io.gumichan01.gakusci.domain.model.SimpleQuery
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat

internal class GutendexClientTest {

    //@Test
    fun `Call the Gutendex client - get results`() {
        val response: GutendexResponse? = runBlocking { GutendexClient().retrieveResults(SimpleQuery("science")) }
        assertThat(response).isNotNull
    }
}