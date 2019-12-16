package io.gumichan01.gakusci.client.arxiv

import io.gumichan01.gakusci.domain.model.QueryParam
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

class ArxivClientTest {

    @Test
    fun `Call the Arxiv service - get results`() {
        val response = runBlocking { ArxivClient().retrieveResults(QueryParam("coroutine")) }
        assertThat(response).isNotNull
        assertThat(response?.numFound).isGreaterThan(0)
    }
}

