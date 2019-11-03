package io.gumichan01.gakusci.domain.search

import io.gumichan01.gakusci.domain.model.DataSource
import io.gumichan01.gakusci.domain.model.ResultEntry
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

@ExperimentalCoroutinesApi
class SearchAggregatorTest {

    private val source: DataSource = mockk()

    private val fakeLauncher: SearchLauncher = mockk {
        every { launch("lorem") } returns Channel<ResultEntry>(4).run {
            runBlocking { send(ResultEntry("lorem", "ipsum", source)); close() }; this
        }
    }

    @Test
    fun `aggregate result entries - return results`() {
        val aggregator = SearchAggregator(fakeLauncher)
        val results: List<ResultEntry> = runBlocking { aggregator.retrieveResultsFromQuery("lorem") }
        assertThat(results.isEmpty()).isFalse()
        assertThat(results).containsAnyOf(ResultEntry("lorem", "ipsum", source))
    }
}