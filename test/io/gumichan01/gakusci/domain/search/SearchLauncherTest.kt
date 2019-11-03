package io.gumichan01.gakusci.domain.search

import io.gumichan01.gakusci.domain.model.DataSource
import io.gumichan01.gakusci.domain.model.ResultEntry
import io.gumichan01.gakusci.domain.service.IService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

@ExperimentalCoroutinesApi
class SearchLauncherTest {

    private val source: DataSource = mockk()

    private val fakeService: IService = mockk {
        coEvery { search("lorem") } returns listOf(ResultEntry("", "", source))
    }
    private val fakeService2: IService = mockk {
        coEvery { search("lorem") } returns listOf(ResultEntry("", "", source))
    }
    private val fakeObjectService: IService = mockk {
        coEvery { search("lorem") } returns listOf(ResultEntry("lorem", "ipsum", source))
    }


    @Test
    fun `launch request with no service - return closed channel`() {
        val searchLauncher = SearchLauncher(emptySet())
        val channel: Channel<ResultEntry> = searchLauncher.launch("lorem")
        assertThat(channel.isClosedForReceive).isTrue()
        assertThat(channel.isClosedForSend).isTrue()
    }

    @Test
    fun `launch request with one fake service - return non-closed channel`() {
        val searchLauncher = SearchLauncher(setOf(fakeService))
        val channel: Channel<ResultEntry> = searchLauncher.launch("lorem")
        assertThat(channel.isClosedForReceive).isFalse()
    }

    @Test
    fun `launch request with several fake services - return non-empty channel and get data`() {
        val searchLauncher = SearchLauncher(setOf(fakeService, fakeService2))
        val channel: Channel<ResultEntry> = searchLauncher.launch("lorem")
        assertThat(runBlocking { channel.receive() }).isEqualTo(ResultEntry("", "", source))
    }

    @Test
    fun `launch request with several fake services - retrieve data`() {
        val searchLauncher = SearchLauncher(setOf(fakeService, fakeObjectService))
        val channel: Channel<ResultEntry> = searchLauncher.launch("lorem")

        val expectedResults = setOf(ResultEntry("", "", source), ResultEntry("lorem", "ipsum", source))
        val tmpResults: MutableList<ResultEntry> = mutableListOf()
        repeat(2) {
            tmpResults += runBlocking { channel.receive() }
        }
        val actualResults = tmpResults.toSet()
        assertThat(expectedResults).isEqualTo(actualResults)
    }
}