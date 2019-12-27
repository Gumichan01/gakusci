package io.gumichan01.gakusci.domain.search

import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.ResultEntry
import io.gumichan01.gakusci.domain.model.SearchResponse
import io.gumichan01.gakusci.domain.model.ServiceResponse
import io.gumichan01.gakusci.domain.search.cache.CacheHandler
import io.gumichan01.gakusci.domain.search.cache.SearchCache
import io.gumichan01.gakusci.domain.utils.SearchType
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import kotlin.test.Test

@ExperimentalCoroutinesApi
class SearchAggregatorTest {

    private val fakeLauncher: SearchLauncher = mockk {
        every { launch(QueryParam("lorem", SearchType.RESEARCH)) } returns Channel<ServiceResponse>(4).run {
            runBlocking { send(ServiceResponse(1, listOf(ResultEntry("lorem", "ipsum")))); close() }; this
        }
    }
    private val fakeCache: SearchCache = mockk()

    private val testCache: SearchCache = CacheHandler().provideCache(SearchType.RESEARCH)

    @Test
    fun `aggregate result entries - return results`() {
        val aggregator = SearchAggregator(fakeLauncher, testCache)
        val results: SearchResponse =
            runBlocking { aggregator.retrieveResults(QueryParam("lorem", SearchType.RESEARCH)) }
        assertThat(results.totalResults).isEqualTo(1)
        assertThat(results.entries).containsAnyOf(ResultEntry("lorem", "ipsum"))
    }

    @Test
    fun `build research aggregator linked to no service`() {
        assertThat(SearchAggregator.Builder().withCache(fakeCache).build()).isInstanceOf(SearchAggregator::class.java)
    }

    @Test
    fun `build research aggregator with no cache - must thrown NPE`() {
        assertThatThrownBy { SearchAggregator.Builder().build() }.isInstanceOf(NullPointerException::class.java)
    }

    @Test
    fun `build research aggregator with research services and a cache system`() {
        assertThat(SearchAggregator.Builder().withResearchServices().withCache(fakeCache).build())
            .isInstanceOf(SearchAggregator::class.java)
    }

    @Test
    fun `build research aggregator with book services and a cache system`() {
        assertThat(SearchAggregator.Builder().withBookServices().withCache(fakeCache).build())
            .isInstanceOf(SearchAggregator::class.java)
    }
}