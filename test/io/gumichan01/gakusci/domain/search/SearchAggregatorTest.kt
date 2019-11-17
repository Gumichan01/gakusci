package io.gumichan01.gakusci.domain.search

import io.gumichan01.gakusci.domain.model.ResultEntry
import io.gumichan01.gakusci.domain.model.ServiceResponse
import io.gumichan01.gakusci.utils.Option
import io.gumichan01.gakusci.utils.Some
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

@ExperimentalCoroutinesApi
class SearchAggregatorTest {

    private val fakeLauncher: SearchLauncher = mockk {
        every { launch("lorem") } returns Channel<Option<ServiceResponse>>(4).run {
            runBlocking { send(Some(ServiceResponse(1, 0, listOf(ResultEntry("lorem", "ipsum"))))); close() }; this
        }
    }

    @Test
    fun `aggregate result entries - return results`() {
        val aggregator = SearchAggregator(fakeLauncher)
        val results: ServiceResponse = runBlocking { aggregator.retrieveResults("lorem") }
        assertThat(results.totalResults).isEqualTo(1)
        assertThat(results.start).isEqualTo(0)
        assertThat(results.entries).containsAnyOf(ResultEntry("lorem", "ipsum"))
    }
}