package io.gumichan01.gakusci.domain.service

import io.gumichan01.gakusci.client.arxiv.ArxivClient
import io.gumichan01.gakusci.client.arxiv.ArxivResponse
import io.gumichan01.gakusci.client.arxiv.ArxivResultEntry
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.ServiceResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import java.util.*
import kotlin.test.Test

class ArxivServiceTest {

    private val arxivClientMock: ArxivClient = mockk {
        coEvery { retrieveResults(QueryParam("lorem")) } returns ArxivResponse(
            1, listOf(
                ArxivResultEntry(
                    emptyList(),
                    "",
                    Date(0L),
                    ""
                )
            )
        )

        coEvery { retrieveResults(QueryParam("dfnkusfk")) } returns null
    }

    @Test
    fun `arXiv services, valid search on fake client - return results`() {
        val service = ArxivService(arxivClientMock)
        val response: ServiceResponse? = runBlocking { service.search(QueryParam("lorem")) }
        assertThat(response).isNotNull
    }

    @Test
    fun `arXiv services, invalid search on fake client - return nothing`() {
        val service = ArxivService(arxivClientMock)
        val response: ServiceResponse? = runBlocking { service.search(QueryParam("dfnkusfk")) }
        assertThat(response).isNull()
    }
}