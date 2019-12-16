package io.gumichan01.gakusci.domain.service

import io.gumichan01.gakusci.client.hal.HalClient
import io.gumichan01.gakusci.client.hal.HalResponse
import io.gumichan01.gakusci.client.hal.HalResponseBody
import io.gumichan01.gakusci.client.hal.HalResultEntry
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.ServiceResponse
import io.gumichan01.gakusci.utils.None
import io.gumichan01.gakusci.utils.Option
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

class HalServiceTest {

    private val halClientMock: HalClient = mockk {
        coEvery { retrieveResults(QueryParam("lorem")) } returns
                HalResponse(HalResponseBody(1, 0, listOf(HalResultEntry(0, "", ""))))
        coEvery { retrieveResults(QueryParam("dfnkusfk")) } throws Exception()
    }

    @Test
    fun `HAL services, invalid search on real client - return nothing`() {
        val service = HalService(halClientMock)
        val response: Option<ServiceResponse> = runBlocking { service.search(QueryParam("dfnkusfk")) }
        assertThat(response).isEqualTo(None)
    }

    @Test
    fun `HAL services, valid search on fake client - return results`() {
        val service = HalService(halClientMock)
        val results: Option<ServiceResponse> = runBlocking { service.search(QueryParam("lorem")) }
        assertThat(results).isNotEqualTo(None)
    }
}