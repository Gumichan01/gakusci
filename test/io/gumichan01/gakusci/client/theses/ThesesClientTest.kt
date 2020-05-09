package io.gumichan01.gakusci.client.theses

import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.utils.SearchType
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions
import kotlin.test.Test

class ThesesClientTest {

//    @Test
    fun `Call the Theses service - get results`() {
        val response = runBlocking { ThesesClient().retrieveResults(QueryParam("coroutine", SearchType.RESEARCH)) }
        Assertions.assertThat(response).isNotNull
    }
}