package io.gumichan01.gakusci.client.theses

import io.gumichan01.gakusci.domain.model.SimpleQuery
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions
import kotlin.test.assertTrue

class ThesesClientTest {

    //@Test
    fun `Call the Theses service - get results`() {
        val response = runBlocking { ThesesClient().retrieveResults(SimpleQuery("coroutine")) }
        Assertions.assertThat(response).isNotNull
        Assertions.assertThat(response!!.total).isPositive
        Assertions.assertThat(response.docs.size).isPositive
    }

    //@Test
    fun `Call the Theses service with query containing spaces - should not fail `() {
        runBlocking { ThesesClient().retrieveResults(SimpleQuery("gunnm last order")) }
        assertTrue { true } // No need to check. The request must not fail in order to validate the test
    }
}