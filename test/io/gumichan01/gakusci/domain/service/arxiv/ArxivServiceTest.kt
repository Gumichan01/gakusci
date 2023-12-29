package io.gumichan01.gakusci.domain.service.arxiv

import io.gumichan01.gakusci.client.arxiv.ArxivClient
import io.gumichan01.gakusci.client.arxiv.ArxivResponse
import io.gumichan01.gakusci.client.arxiv.ArxivResultEntry
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.ServiceResponse
import io.gumichan01.gakusci.domain.utils.SearchType
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import java.util.*
import kotlin.test.Test

class ArxivServiceTest {

    private val arxivClientMock: ArxivClient = mockk {
        coEvery { retrieveResults(QueryParam("lorem", SearchType.RESEARCH, )) } returns ArxivResponse(
            1, listOf(
                ArxivResultEntry(
                    emptyList(),
                    "",
                    Date(0L),
                    ""
                )
            )
        )

        coEvery { retrieveResults(QueryParam("dfnkusfk", SearchType.RESEARCH, )) } returns null
    }

    @Test
    fun `arXiv services, valid search on fake client - return results`() {
        val service = ArxivService(arxivClientMock)
        val response: ServiceResponse? = runBlocking { service.search(QueryParam("lorem", SearchType.RESEARCH, )) }
        assertThat(response).isNotNull
    }

    @Test
    fun `arXiv services, invalid search on fake client - return nothing`() {
        val service = ArxivService(arxivClientMock)
        val response: ServiceResponse? = runBlocking { service.search(QueryParam("dfnkusfk", SearchType.RESEARCH, )) }
        assertThat(response).isNull()
    }
}