package io.gumichan01.gakusci.domain.service

import io.gumichan01.gakusci.client.penguin.PenguinRandomHouseIsbnClient
import io.gumichan01.gakusci.client.penguin.PenguinRandomHouseIsbnResponse
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.ServiceResponse
import io.gumichan01.gakusci.domain.utils.SearchType
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions
import kotlin.test.Test


class PenguinRandomHouseIsbnServiceTest {

    private val penguinIsbnClient: PenguinRandomHouseIsbnClient = mockk {
        coEvery {
            retrieveResults(
                QueryParam(
                    "9780140439212",
                    SearchType.BOOKS
                )
            )
        } returns PenguinRandomHouseIsbnResponse("9780140439212", "marc", "ipsum", "01/01/2020")

        coEvery { retrieveResults(QueryParam("dfnkusfk", SearchType.BOOKS)) } returns null
    }

    @Test
    fun `penguin random house services, valid ISBN search on fake client - return results`() {
        val service = PenguinRandomHouseIsbnService(penguinIsbnClient)
        val response: ServiceResponse? = runBlocking { service.search(QueryParam("9780140439212", SearchType.BOOKS)) }
        Assertions.assertThat(response).isNotNull
    }

    @Test
    fun `penguin random house services, invalid ISBN search on fake client - return null`() {
        val service = PenguinRandomHouseIsbnService(penguinIsbnClient)
        val response: ServiceResponse? = runBlocking { service.search(QueryParam("dfnkusfk", SearchType.BOOKS)) }
        Assertions.assertThat(response).isNull()
    }
}