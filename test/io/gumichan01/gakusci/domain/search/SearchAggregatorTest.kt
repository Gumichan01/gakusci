package io.gumichan01.gakusci.domain.search

import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.SearchResponse
import io.gumichan01.gakusci.domain.model.ServiceResponse
import io.gumichan01.gakusci.domain.model.entry.SimpleResultEntry
import io.gumichan01.gakusci.domain.search.cache.SearchCache
import io.gumichan01.gakusci.domain.utils.SearchType
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

@FlowPreview
@ExperimentalCoroutinesApi
class SearchAggregatorTest {

    private val fakeLauncher: SearchLauncher = mockk {
        every { launch(QueryParam("lorem", SearchType.RESEARCH, )) } returns Channel<ServiceResponse>(4).run {
            runBlocking {
                send(
                    ServiceResponse(
                        1, listOf(
                            SimpleResultEntry(
                                "lorem",
                                "ipsum"
                            )
                        )
                    )
                ); close()
            }; this
        }
    }
    private val fakeCache: SearchCache = mockk()

    @Test
    fun `aggregate result entries - return results`() {
        val aggregator = SearchAggregator(fakeLauncher)
        val results: SearchResponse =
            runBlocking { aggregator.retrieveResults(QueryParam("lorem", SearchType.RESEARCH, )) }
        assertThat(results.totalResults).isEqualTo(1)
        assertThat(results.entries).containsAnyOf(
            SimpleResultEntry(
                "lorem",
                "ipsum"
            )
        )
    }

    @Test
    fun `build research aggregator linked to no service`() {
        assertThat(SearchAggregator.Builder().build()).isInstanceOf(SearchAggregator::class.java)
    }

    @Test
    fun `build research aggregator with research services`() {
        assertThat(SearchAggregator.Builder().withResearchServices().build())
            .isInstanceOf(SearchAggregator::class.java)
    }

    @Test
    fun `build research aggregator with book services`() {
        assertThat(SearchAggregator.Builder().withBookServices().build())
            .isInstanceOf(SearchAggregator::class.java)
    }
}