package io.gumichan01.gakusci.client.hal

import io.gumichan01.gakusci.domain.model.QueryParam
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

class HalClientTest {

    @Test
    fun `Call the HAL service - get results`() {
        assertThat(runBlocking { HalClient().retrieveResults(QueryParam("science")) }.body.numFound).isGreaterThan(0)
    }
}