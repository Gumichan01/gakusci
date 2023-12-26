package io.gumichan01.gakusci.client.gutendex

import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.utils.SearchType
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

internal class GutendexClientTest {

    //@Test
    fun `Call the Gutendex client - get results`() {
        val response : GutendexResponse? = runBlocking { GutendexClient().retrieveResults(QueryParam("science", SearchType.RESEARCH)) }
        assertThat(response).isNotNull
    }
}