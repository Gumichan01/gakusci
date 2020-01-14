package io.gumichan01.gakusci.client.hal

import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.utils.SearchType
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat

class HalClientTest {

    //@Test
    fun `Call the HAL service - get results`() {
        val response = runBlocking { HalClient().retrieveResults(QueryParam("science", SearchType.RESEARCH)) }
        assertThat(response?.body?.numFound).isGreaterThan(0)
        assertThat(response?.body?.start).isEqualTo(0)
        assertThat(response?.body?.docs?.size).isGreaterThan(0)
    }

    //@Test
    fun `Call the HAL service to get the 4 first entries `() {
        val response = runBlocking { HalClient().retrieveResults(QueryParam("science", SearchType.RESEARCH, rows = 4)) }
        assertThat(response?.body?.numFound).isGreaterThan(0)
        assertThat(response?.body?.start).isEqualTo(0)
        assertThat(response?.body?.docs?.size).isEqualTo(4)
    }
}