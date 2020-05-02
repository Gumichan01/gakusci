package io.gumichan01.gakusci.client.penguin

import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.utils.SearchType
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions

internal class PenguinRandomHouseBookClientTest {

//    @Test
    fun `test basic access to the web-service - get result in text format`() {
        val res = runBlocking {
            PenguinRandomHouseBookClient().retrieveResults(QueryParam("9780140439212", SearchType.BOOKS))
        }
        println(res)
        Assertions.assertThat(res).isNotNull
    }
}