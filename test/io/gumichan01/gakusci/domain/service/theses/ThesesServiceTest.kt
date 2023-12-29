package io.gumichan01.gakusci.domain.service.theses

import io.gumichan01.gakusci.client.theses.ThesesClient
import io.gumichan01.gakusci.client.theses.ThesesResponse
import io.gumichan01.gakusci.client.theses.ThesesResponseBody
import io.gumichan01.gakusci.client.theses.ThesesResultEntry
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.ServiceResponse
import io.gumichan01.gakusci.domain.utils.SearchType
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions
import java.util.*
import kotlin.test.Test

class ThesesServiceTest {

    private val clientMock: ThesesClient = mockk {
        coEvery { retrieveResults(QueryParam("lorem", SearchType.RESEARCH)) } returns ThesesResponse(
            ThesesResponseBody(
                3, 0, listOf(
                ThesesResultEntry("1", "ipsum", "", "soutenue", "oui", Date(0L)),
                ThesesResultEntry("2", "ipsum2", "", "soutenue", "non", Date(0L)),
                ThesesResultEntry("3", "ipsum3", "", "enCours", "non", Date(0L))
            )
            )
        )
        coEvery { retrieveResults(QueryParam("ipsum", SearchType.RESEARCH)) } returns ThesesResponse(
            ThesesResponseBody(
                1, 0, listOf(
                ThesesResultEntry("4", "para bellum", "", "soutenue", "non", Date(0L))
            )
            )
        )
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

    @Test
    fun `Theses service, search for presented and available theses - return requested results`() {
        val service = ThesesService(clientMock)
        val result: ServiceResponse? = runBlocking { service.search(QueryParam("lorem", SearchType.RESEARCH)) }
        Assertions.assertThat(result?.totalResults).isEqualTo(1)
        Assertions.assertThat(result?.entries?.size).isEqualTo(1)
    }
}