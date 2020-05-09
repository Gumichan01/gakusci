package io.gumichan01.gakusci.domain.service

import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.ServiceResponse
import io.gumichan01.gakusci.domain.utils.SearchType
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions
import kotlin.test.Test

class ThesesServiceTest {

    private val clientMock: IClient<String> = mockk {
        coEvery { retrieveResults(QueryParam("lorem", SearchType.RESEARCH)) } returns "ipsum"
        coEvery { retrieveResults(QueryParam("ipsum", SearchType.RESEARCH)) } returns "para bellum"
    }

    @Test
    fun `Theses service, valid search with a client - return results`() {
        val service = ThesesService(clientMock)
        val results: ServiceResponse? = runBlocking { service.search(QueryParam("lorem", SearchType.RESEARCH)) }
        Assertions.assertThat(results).isNotNull
    }

    @Test
    fun `Theses service, double search for same value - return same result twice`() {
        val service = ThesesService(clientMock)
        val result1: ServiceResponse? = runBlocking { service.search(QueryParam("lorem", SearchType.RESEARCH)) }
        val result2: ServiceResponse? = runBlocking { service.search(QueryParam("lorem", SearchType.RESEARCH)) }
        Assertions.assertThat(result1).isEqualTo(result2)
    }

    @Test
    fun `Theses service, two different searches - return different results`() {
        val service = ThesesService(clientMock)
        val result1: ServiceResponse? = runBlocking { service.search(QueryParam("lorem", SearchType.RESEARCH)) }
        val result2: ServiceResponse? = runBlocking { service.search(QueryParam("ipsum", SearchType.RESEARCH)) }
        Assertions.assertThat(result1).isNotEqualTo(result2)
    }
}