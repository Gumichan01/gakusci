package io.gumichan01.gakusci.client.hal

import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

class HalClientTest {

    @Test
    fun `Call the HAL service - get results`() {
        val result = runBlocking { HalClient().retrieveResults("lorem") }
        assertThat(result).isNotEmpty
    }

}