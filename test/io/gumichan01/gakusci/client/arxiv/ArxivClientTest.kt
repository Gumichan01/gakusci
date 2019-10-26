package io.gumichan01.gakusci.client.arxiv

import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions
import org.junit.Test

class ArxivClientTest {

    @Test
    fun `Call the Arxiv service - get results`() {
        val result = runBlocking { ArxivClient().retrieveResults("coroutine") }
        Assertions.assertThat(result).isNotEmpty
    }
}

