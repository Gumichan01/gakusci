package io.gumichan01.gakusci.client.hal

import io.gumichan01.gakusci.domain.model.QueryParam
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

class HalClientTest {

    @Test
    fun `Call the HAL service - get results`() {
        val response = runBlocking { HalClient().retrieveResults(QueryParam("science")) }
        assertThat(response.body.numFound).isGreaterThan(0)
        assertThat(response.body.start).isEqualTo(0)
        assertThat(response.body.docs?.size).isGreaterThan(0)
    }

    @Test
    fun `Call the HAL service to get the 4 first entries `() {
        val response = runBlocking { HalClient().retrieveResults(QueryParam("science", rows = 4)) }
        assertThat(response.body.numFound).isGreaterThan(0)
        assertThat(response.body.start).isEqualTo(0)
        assertThat(response.body.docs?.size).isEqualTo(4)
    }
}