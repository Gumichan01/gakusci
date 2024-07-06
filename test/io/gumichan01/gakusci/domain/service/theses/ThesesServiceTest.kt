package io.gumichan01.gakusci.domain.service.theses

import io.gumichan01.gakusci.client.theses.ThesesAuthor
import io.gumichan01.gakusci.client.theses.ThesesClient
import io.gumichan01.gakusci.client.theses.ThesesResponse
import io.gumichan01.gakusci.client.theses.ThesesResultEntry
import io.gumichan01.gakusci.domain.model.ServiceResponse
import io.gumichan01.gakusci.domain.model.SimpleQuery
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions
import kotlin.test.Test

class ThesesServiceTest {

    private val clientMock: ThesesClient = mockk {
        coEvery { retrieveResults(SimpleQuery("lorem")) } returns ThesesResponse(
            3, listOf(
            ThesesResultEntry("1", "ipsum", listOf(ThesesAuthor("name1", "fname1")), "soutenue", "01/01/2010"),
            ThesesResultEntry("2", "ipsum2", listOf(ThesesAuthor("name21", "fname21"), ThesesAuthor("name21", "fname21")),
                "soutenue", "04/01/2000"),
            ThesesResultEntry("3", "ipsum3", listOf(ThesesAuthor("name1", "fname1")), "enCours", "01/02/1990")))
        coEvery { retrieveResults(SimpleQuery("ipsum")) } returns ThesesResponse(
            1, listOf(
            ThesesResultEntry("4", "para bellum", listOf(ThesesAuthor("name1", "fname1")), "soutenue", "01/01/2010")))
    }

    @Test
    fun `Theses service, valid search with a client - return results`() {
        val service = ThesesService(clientMock)
        val results: ServiceResponse = runBlocking { service.search(SimpleQuery("lorem")) }
        Assertions.assertThat(results.totalResults).isPositive
        Assertions.assertThat(results.entries).isNotEmpty
    }

    @Test
    fun `Theses service, double search for same value - return same result twice`() {
        val service = ThesesService(clientMock)
        val result1: ServiceResponse = runBlocking { service.search(SimpleQuery("lorem")) }
        val result2: ServiceResponse = runBlocking { service.search(SimpleQuery("lorem")) }
        Assertions.assertThat(result1).isEqualTo(result2)
    }

    @Test
    fun `Theses service, two different searches - return different results`() {
        val service = ThesesService(clientMock)
        val result1: ServiceResponse = runBlocking { service.search(SimpleQuery("lorem")) }
        val result2: ServiceResponse = runBlocking { service.search(SimpleQuery("ipsum")) }
        Assertions.assertThat(result1).isNotEqualTo(result2)
    }

    @Test
    fun `Theses service, search for presented and available theses - return requested results`() {
        val service = ThesesService(clientMock)
        val result: ServiceResponse = runBlocking { service.search(SimpleQuery("ipsum")) }
        Assertions.assertThat(result.totalResults).isEqualTo(1)
        Assertions.assertThat(result.entries.size).isEqualTo(1)
    }
}