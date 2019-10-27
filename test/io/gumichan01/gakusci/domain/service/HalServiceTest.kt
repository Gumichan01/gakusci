package io.gumichan01.gakusci.domain.service

import io.gumichan01.gakusci.client.hal.HalClient
import io.gumichan01.gakusci.client.hal.HalResultEntry
import io.gumichan01.gakusci.domain.model.ResultEntry
import io.mockk.coEvery
import io.mockk.mockkClass
import io.mockk.unmockkAll
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class HalServiceTest {

    private val halClientMock = mockkClass(HalClient::class)

    @BeforeTest
    fun initMock() {
        coEvery { halClientMock.retrieveResults("lorem") } returns listOf(HalResultEntry(0, "", ""))
        coEvery { halClientMock.retrieveResults("dfnkusfk") } returns emptyList()
    }

    @Test
    fun `HAL services, valid search on fake client - return results`() {
        val service = HalService(halClientMock)
        val results: List<ResultEntry> = runBlocking { service.search("lorem") }
        assertThat(results).isNotEmpty
    }

    @Test
    fun `HAL services, invalid search on real client - return nothing`() {
        val service = HalService(halClientMock)
        val results: List<ResultEntry> = runBlocking { service.search("dfnkusfk") }
        assertThat(results).isEmpty()
    }

    @AfterTest
    fun unmock() {
        unmockkAll()
    }
}