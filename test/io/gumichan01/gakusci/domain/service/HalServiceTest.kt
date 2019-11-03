package io.gumichan01.gakusci.domain.service

import io.gumichan01.gakusci.client.hal.HalClient
import io.gumichan01.gakusci.client.hal.HalResultEntry
import io.gumichan01.gakusci.domain.model.DataSource
import io.gumichan01.gakusci.domain.model.ResultEntry
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

class HalServiceTest {

    private val halClientMock: HalClient = mockk {
        coEvery { retrieveResults("lorem") } returns listOf(HalResultEntry(0, "", ""))
        coEvery { retrieveResults("dfnkusfk") } returns emptyList()
    }

    @Test
    fun `HAL services, valid search on fake client - return results`() {
        val service = HalService(halClientMock)
        val results: List<ResultEntry> = runBlocking { service.search("lorem") }
        assertThat(results).isNotEmpty
        assertThat(results).allMatch { r -> r.source == DataSource.HAL }
    }

    @Test
    fun `HAL services, invalid search on real client - return nothing`() {
        val service = HalService(halClientMock)
        val results: List<ResultEntry> = runBlocking { service.search("dfnkusfk") }
        assertThat(results).isEmpty()
    }
}