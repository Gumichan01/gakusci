package io.gumichan01.gakusci.client.theses

import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.utils.SearchType
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions
import kotlin.test.assertTrue

class ThesesClientTest {

    //@Test
    fun `Call the Theses service - get results`() {
        val response = runBlocking { ThesesClient().retrieveResults(QueryParam("coroutine", SearchType.RESEARCH)) }
        Assertions.assertThat(response).isNotNull
        Assertions.assertThat(response!!.body.numFound).isPositive
        Assertions.assertThat(response.body.start).isZero
        Assertions.assertThat(response.body.docs.size).isPositive
    }

    //@Test
    fun `Call the Theses service with query containing spaces - should not fail `() {
        runBlocking { ThesesClient().retrieveResults(QueryParam("gunnm last order", SearchType.RESEARCH)) }
        assertTrue { true } // No need to check. The request must not fail in order to validate the test
    }
}