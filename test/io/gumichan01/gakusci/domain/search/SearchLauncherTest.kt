package io.gumichan01.gakusci.domain.search

import io.gumichan01.gakusci.domain.model.ResultEntry
import io.gumichan01.gakusci.domain.model.ServiceResponse
import io.gumichan01.gakusci.domain.service.IService
import io.gumichan01.gakusci.utils.Option
import io.gumichan01.gakusci.utils.Some
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

@ExperimentalCoroutinesApi
class SearchLauncherTest {

    private val fakeService: IService = mockk {
        coEvery { search("lorem") } returns Some(ServiceResponse(1, listOf(ResultEntry("", ""))))
    }
    private val fakeService2: IService = mockk {
        coEvery { search("lorem") } returns Some(ServiceResponse(1, listOf(ResultEntry("", ""))))
    }
    private val fakeObjectService: IService = mockk {
        coEvery { search("lorem") } returns Some(
            ServiceResponse(1, listOf(ResultEntry("lorem", "ipsum")))
        )
    }


    @Test
    fun `launch request with no service - return closed channel`() {
        val searchLauncher = SearchLauncher(emptySet())
        val channel: Channel<Option<ServiceResponse>> = searchLauncher.launch("lorem")
        assertThat(channel.isClosedForReceive).isTrue()
        assertThat(channel.isClosedForSend).isTrue()
    }

    @Test
    fun `launch request with one fake service - return non-closed channel`() {
        val searchLauncher = SearchLauncher(setOf(fakeService))
        val channel: Channel<Option<ServiceResponse>> = searchLauncher.launch("lorem")
        assertThat(channel.isClosedForReceive).isFalse()
    }

    @Test
    fun `launch request with several fake services - return non-empty channel and get data`() {
        val searchLauncher = SearchLauncher(setOf(fakeService, fakeService2))
        val channel: Channel<Option<ServiceResponse>> = searchLauncher.launch("lorem")
        assertThat(runBlocking { channel.receive() }).isEqualTo(
            Some(ServiceResponse(1, listOf(ResultEntry("", ""))))
        )
    }

    @Test
    fun `launch request with several fake services - retrieve data`() {
        val searchLauncher = SearchLauncher(setOf(fakeService, fakeObjectService))
        val channel: Channel<Option<ServiceResponse>> = searchLauncher.launch("lorem")

        val expectedResults: Set<Some<ServiceResponse>> = setOf(
            Some(ServiceResponse(1, listOf(ResultEntry("", "")))),
            Some(ServiceResponse(1, listOf(ResultEntry("lorem", "ipsum"))))
        )
        val tmpResults: MutableList<Option<ServiceResponse>> = mutableListOf()
        repeat(2) {
            tmpResults += runBlocking { channel.receive() }
        }
        val actualResults = tmpResults.toSet()
        assertThat(expectedResults).isEqualTo(actualResults)
    }
}