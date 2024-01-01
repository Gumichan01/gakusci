package io.gumichan01.gakusci.client.penguin

import io.gumichan01.gakusci.domain.model.SimpleQuery
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions

internal class PenguinRandomHouseBookClientTest {

    //    @Test
    fun `test basic access to the web-service - get result in text format`() {
        val res = runBlocking {
            PenguinRandomHouseBookClient().retrieveResults(SimpleQuery("9780140439212"))
        }
        println(res)
        Assertions.assertThat(res).isNotNull
    }
}