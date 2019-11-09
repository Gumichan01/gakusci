package io.gumichan01.gakusci.client.arxiv

import io.gumichan01.gakusci.client.exception.RateLimitViolationException
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import kotlin.test.Test

class ArxivClientTest {

    @Test
    fun `Call the Arxiv service - get results`() {
        val result = runBlocking { ArxivClient().retrieveResults("coroutine") }
        assertThat(result).isNotEmpty
    }

    @Test
    fun `Call the Arixiv service violating the rate limit - must fail`() {
        assertThatExceptionOfType(RateLimitViolationException::class.java).isThrownBy {
            runBlocking {
                val client = ArxivClient()
                client.retrieveResults("coroutine")
                client.retrieveResults("coroutine")
            }
        }.withMessageContaining("Arxiv")
    }
}

