package io.gumichan01.gakusci.domain.service.gutendex

import io.gumichan01.gakusci.client.gutendex.*
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.ServiceResponse
import io.gumichan01.gakusci.domain.utils.SearchType
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions
import org.junit.Test


internal class GutendexServiceTest {

    private val gutendexClientMock: GutendexClient = mockk {
        coEvery { retrieveResults(QueryParam("lorem", SearchType.BOOKS)) } returns
            GutendexResponse(1, "", "", listOf(GutendexBookEntry(42, "lorem", listOf(GutendexAuthor("ipsum")),
                GutendexFormat("", ""))))
        coEvery { retrieveResults(QueryParam("dfnkusfk", SearchType.BOOKS)) } returns GutendexResponse(0, null, null, emptyList())
    }

    @Test
    fun `Gutendex services, invalid search - return nothing`() {
        val service = GutendexService(gutendexClientMock)
        val response: ServiceResponse = runBlocking { service.search(QueryParam("dfnkusfk", SearchType.BOOKS)) }
        Assertions.assertThat(response.totalResults).isZero
        Assertions.assertThat(response.entries).isEmpty()
    }

    @Test
    fun `Gutendex services, valid search - return results`() {
        val service = GutendexService(gutendexClientMock)
        val results: ServiceResponse = runBlocking { service.search(QueryParam("lorem", SearchType.BOOKS)) }
        Assertions.assertThat(results.totalResults).isPositive
        Assertions.assertThat(results.entries).isNotEmpty
    }

}