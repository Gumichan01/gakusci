package io.gumichan01.gakusci.domain.service

import io.gumichan01.gakusci.client.hal.HalClient
import io.gumichan01.gakusci.client.hal.HalResponse
import io.gumichan01.gakusci.client.hal.HalResponseBody
import io.gumichan01.gakusci.client.hal.HalResultEntry
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.ServiceResponse
import io.gumichan01.gakusci.domain.service.hal.HalService
import io.gumichan01.gakusci.domain.utils.SearchType
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

class HalServiceTest {

    private val halClientMock: HalClient = mockk {
        coEvery { retrieveResults(QueryParam("lorem", SearchType.RESEARCH)) } returns
                HalResponse(HalResponseBody(1, 0, listOf(HalResultEntry(0, "", ""))))
        coEvery { retrieveResults(QueryParam("dfnkusfk", SearchType.RESEARCH)) } returns null
    }

    @Test
    fun `HAL services, invalid search on real client - return nothing`() {
        val service = HalService(halClientMock)
        val response: ServiceResponse? = runBlocking { service.search(QueryParam("dfnkusfk", SearchType.RESEARCH)) }
        assertThat(response).isNull()
    }

    @Test
    fun `HAL services, valid search on fake client - return results`() {
        val service = HalService(halClientMock)
        val results: ServiceResponse? = runBlocking { service.search(QueryParam("lorem", SearchType.RESEARCH)) }
        assertThat(results).isNotNull
    }
}