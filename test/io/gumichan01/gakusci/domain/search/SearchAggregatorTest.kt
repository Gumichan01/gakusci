package io.gumichan01.gakusci.domain.search

import io.gumichan01.gakusci.domain.model.ResultEntry
import io.gumichan01.gakusci.domain.service.ArxivService
import io.gumichan01.gakusci.domain.service.HalService
import io.mockk.coEvery
import io.mockk.mockkClass
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@ExperimentalCoroutinesApi
class SearchAggregatorTest {

    private val halServiceMock = mockkClass(HalService::class)
    private val arxivServiceMock = mockkClass(ArxivService::class)

    @BeforeTest
    fun initMocks() {
        coEvery { halServiceMock.search("lorem") } returns listOf(ResultEntry("", ""))
        coEvery { arxivServiceMock.search("lorem") } returns listOf(ResultEntry("", ""))
    }

    @Test
    fun `test research aggregation, no service - produce nothing`() {
        val aggregator = SearchAggregator(emptySet())
        assertThat(runBlocking { aggregator.search("lorem") }).isEmpty()
    }

    @Test
    fun `test research aggregation, HAL service - produce results`() {
        val aggregator = SearchAggregator(setOf(halServiceMock))
        assertThat(runBlocking { aggregator.search("lorem") }).isNotEmpty
    }

    @Test
    fun `test research aggregation, Arxiv service - produce results`() {
        val aggregator = SearchAggregator(setOf(arxivServiceMock))
        assertThat(runBlocking { aggregator.search("lorem") }).isNotEmpty
    }

    @Test
    fun `test research aggregation, several services - produce results`() {
        val aggregator = SearchAggregator(setOf(arxivServiceMock, halServiceMock))
        assertThat(runBlocking { aggregator.search("lorem") }).isNotEmpty
    }

    @AfterTest
    fun unmock() {
        unmockkAll()
    }
}