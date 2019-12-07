package io.gumichan01.gakusci.client.arxiv

import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions
import kotlin.test.Test

class ArxivClientTest {

    @Test
    fun `Call the Arxiv service - get results`() {
        runBlocking { ArxivClient().retrieveResults("coroutine") }
    }

    @Test
    fun `Call the Arxiv service violating the rate limit - must return null`() {
        var result: ArxivResponse? = ArxivResponse(0, 0, emptyList())
        val client = ArxivClient()
        runBlocking {
            client.retrieveResults("coroutine")
            result = client.retrieveResults("coroutine")
        }
        Assertions.assertThat(result).isNull()
    }
}

