package io.gumichan01.gakusci.client.gutendex

import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.utils.SearchType
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat

internal class GutendexClientTest {

    //@Test
    fun `Call the Gutendex client - get results`() {
        val response: GutendexResponse? = runBlocking { GutendexClient().retrieveResults(QueryParam("science", SearchType.RESEARCH)) }
        assertThat(response).isNotNull
    }

    //@Test
    fun `Call the Gutendex client, get elements from 42nd index - get results on the the second`() {
        val response: GutendexResponse? = runBlocking {
            GutendexClient().retrieveResults(QueryParam("science", SearchType.RESEARCH, start = 42))
        }
        assertThat(response).isNotNull
    }
}