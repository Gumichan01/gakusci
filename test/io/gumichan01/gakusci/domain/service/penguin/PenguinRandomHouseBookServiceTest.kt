package io.gumichan01.gakusci.domain.service.penguin

import io.gumichan01.gakusci.client.penguin.PenguinRandomHouseBookClient
import io.gumichan01.gakusci.client.penguin.PenguinRandomHouseBookResponse
import io.gumichan01.gakusci.domain.model.ServiceResponse
import io.gumichan01.gakusci.domain.model.SimpleQuery
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions
import kotlin.test.Test


class PenguinRandomHouseBookServiceTest {

    private val penguinBookClient: PenguinRandomHouseBookClient = mockk {
        coEvery {
            retrieveResults(
                SimpleQuery("9780140439212")
            )
        } returns PenguinRandomHouseBookResponse("9780140439212", "marc", "ipsum", "01/01/2020")

        coEvery { retrieveResults(SimpleQuery("dfnkusfk")) } returns null
    }

    @Test
    fun `penguin random house services, valid ISBN search on fake client - return results`() {
        val service = PenguinRandomHouseBookService(penguinBookClient)
        val response: ServiceResponse = runBlocking { service.search(SimpleQuery("9780140439212")) }
        Assertions.assertThat(response.totalResults).isPositive
        Assertions.assertThat(response.entries).isNotEmpty
    }

    @Test
    fun `penguin random house services, invalid ISBN search on fake client - return null`() {
        val service = PenguinRandomHouseBookService(penguinBookClient)
        val response: ServiceResponse = runBlocking { service.search(SimpleQuery("dfnkusfk")) }
        Assertions.assertThat(response.totalResults).isZero
        Assertions.assertThat(response.entries).isEmpty()
    }
}