package io.gumichan01.gakusci.client.hal

import io.gumichan01.gakusci.domain.model.SimpleQuery
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.assertTrue

class HalClientTest {

    //@Test
    fun `Call the HAL service - get results`() {
        val response: HalResponse? = runBlocking { HalClient().retrieveResults(SimpleQuery("science")) }
        assertThat(response?.body?.numFound).isGreaterThan(0)
        assertThat(response?.body?.start).isEqualTo(0)
        assertThat(response?.body?.docs?.size).isGreaterThan(0)
    }

    //@Test
    fun `Call the HAL service with query containing spaces - should not fail `() {
        runBlocking { HalClient().retrieveResults(SimpleQuery("gunnm last order")) }
        assertTrue { true } // No need to check. The request must not fail in order to validate the test
    }
}