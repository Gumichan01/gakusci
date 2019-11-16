package io.gumichan01.gakusci.client.hal

import kotlinx.coroutines.runBlocking
import kotlin.test.Test

class HalClientTest {

    @Test
    fun `Call the HAL service - get results`() {
        runBlocking { HalClient().retrieveResults("lorem") }
    }
}