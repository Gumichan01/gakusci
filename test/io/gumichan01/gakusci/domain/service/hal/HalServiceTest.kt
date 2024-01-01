package io.gumichan01.gakusci.domain.service.hal

import io.gumichan01.gakusci.client.hal.HalClient
import io.gumichan01.gakusci.client.hal.HalResponse
import io.gumichan01.gakusci.client.hal.HalResponseBody
import io.gumichan01.gakusci.client.hal.HalResultEntry
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.ServiceResponse
import io.gumichan01.gakusci.domain.model.SimpleQuery
import io.gumichan01.gakusci.domain.utils.SearchType
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

class HalServiceTest {

    private val halClientMock: HalClient = mockk {
        coEvery { retrieveResults(SimpleQuery("lorem")) } returns
            HalResponse(HalResponseBody(1, 0, listOf(HalResultEntry(0, "", ""))))
        coEvery { retrieveResults(SimpleQuery("dfnkusfk")) } returns HalResponse(HalResponseBody(0,0, emptyList()))
    }

    @Test
    fun `HAL services, invalid search on real client - return nothing`() {
        val service = HalService(halClientMock)
        val response: ServiceResponse = runBlocking { service.search(SimpleQuery("dfnkusfk")) }
        assertThat(response.totalResults).isZero
        assertThat(response.entries).isEmpty()
    }

    @Test
    fun `HAL services, valid search on fake client - return results`() {
        val service = HalService(halClientMock)
        val results: ServiceResponse = runBlocking { service.search(SimpleQuery("lorem")) }
        assertThat(results.totalResults).isPositive
        assertThat(results.entries).isNotEmpty
    }
}