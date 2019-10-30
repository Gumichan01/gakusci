package io.gumichan01.gakusci.domain.service

import io.gumichan01.gakusci.client.arxiv.ArxivClient
import io.gumichan01.gakusci.client.arxiv.ArxivResultEntry
import io.gumichan01.gakusci.domain.model.ResultEntry
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions
import java.util.*
import kotlin.test.Test


class ArxivServiceTest {

    private val ArxivClientMock: ArxivClient = mockk {
        coEvery { retrieveResults("lorem") } returns listOf(
            ArxivResultEntry(
                emptyList(),
                "",
                Date(0L),
                ""
            )
        )

        coEvery { retrieveResults("dfnkusfk") } returns emptyList()
    }

    @Test
    fun `HAL services, valid search on fake client - return results`() {
        val service = ArxivService(ArxivClientMock)
        val results: List<ResultEntry> = runBlocking { service.search("lorem") }
        Assertions.assertThat(results).isNotEmpty
    }

    @Test
    fun `HAL services, invalid search on real client - return nothing`() {
        val service = ArxivService(ArxivClientMock)
        val results: List<ResultEntry> = runBlocking { service.search("dfnkusfk") }
        Assertions.assertThat(results).isEmpty()
    }
}