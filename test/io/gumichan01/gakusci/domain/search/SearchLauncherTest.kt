package io.gumichan01.gakusci.domain.search

import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.ServiceResponse
import io.gumichan01.gakusci.domain.model.entry.SimpleResultEntry
import io.gumichan01.gakusci.domain.service.IService
import io.gumichan01.gakusci.domain.utils.SearchType
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
        coEvery { search(QueryParam("lorem", SearchType.RESEARCH)) } returns ServiceResponse(
            1,
            listOf(SimpleResultEntry("", ""))
        )
    }
    private val fakeService2: IService = mockk {
        coEvery { search(QueryParam("lorem", SearchType.RESEARCH)) } returns ServiceResponse(
            1,
            listOf(SimpleResultEntry("", ""))
        )
    }
    private val fakeObjectService: IService = mockk {
        coEvery { search(QueryParam("lorem", SearchType.RESEARCH)) } returns
                ServiceResponse(
                    1, listOf(
                        SimpleResultEntry(
                            "lorem",
                            "ipsum"
                        )
                    )
                )
    }


    @Test
    fun `launch request with no service - return closed channel`() {
        val searchLauncher = SearchLauncher(emptySet())
        val channel: Channel<ServiceResponse> = searchLauncher.launch(QueryParam("lorem", SearchType.RESEARCH))
        assertThat(channel.isClosedForReceive).isTrue
        assertThat(channel.isClosedForSend).isTrue
    }

    @Test
    fun `launch request with one fake service - return non-closed channel`() {
        val searchLauncher = SearchLauncher(setOf(fakeService))
        val channel: Channel<ServiceResponse> = searchLauncher.launch(QueryParam("lorem", SearchType.RESEARCH))
        assertThat(channel.isClosedForReceive).isFalse
    }

    @Test
    fun `launch request with several fake services - return non-empty channel and get data`() {
        val searchLauncher = SearchLauncher(setOf(fakeService, fakeService2))
        val channel: Channel<ServiceResponse> = searchLauncher.launch(QueryParam("lorem", SearchType.RESEARCH))
        assertThat(runBlocking { channel.receive() })
            .isEqualTo(
                ServiceResponse(
                    1, listOf(
                        SimpleResultEntry(
                            "",
                            ""
                        )
                    )
                )
            )
    }

    @Test
    fun `launch request with several fake services - retrieve data`() {
        val searchLauncher = SearchLauncher(setOf(fakeService, fakeObjectService))
        val channel: Channel<ServiceResponse> = searchLauncher.launch(QueryParam("lorem", SearchType.RESEARCH))

        val expectedResults: Set<ServiceResponse> = setOf(
            ServiceResponse(
                1, listOf(
                    SimpleResultEntry(
                        "",
                        ""
                    )
                )
            ),
            ServiceResponse(
                1, listOf(
                    SimpleResultEntry(
                        "lorem",
                        "ipsum"
                    )
                )
            )
        )
        val tmpResults: MutableList<ServiceResponse> = mutableListOf()
        repeat(2) {
            tmpResults += runBlocking { channel.receive() }
        }
        val actualResults = tmpResults.toSet()
        assertThat(expectedResults).isEqualTo(actualResults)
    }
}