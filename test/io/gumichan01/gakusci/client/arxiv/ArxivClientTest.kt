package io.gumichan01.gakusci.client.arxiv

import kotlinx.coroutines.runBlocking
import kotlin.test.Test

class ArxivClientTest {

    @Test
    fun `Call the Arxiv service - get results`() {
        runBlocking { ArxivClient().retrieveResults("coroutine") }
    }
}

