package io.gumichan01.gakusci.client.arxiv

import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.utils.SearchType
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat

class ArxivClientTest {

    //@Test
    fun `Call the Arxiv service - get results`() {
        val response = runBlocking { ArxivClient().retrieveResults(QueryParam("science", SearchType.RESEARCH)) }
        assertThat(response).isNotNull
        assertThat(response?.numFound).isGreaterThan(0)
    }

    //@Test
    fun `Call the Arxiv service to ge the 4 first entries`() {
        val response = runBlocking {
            ArxivClient().retrieveResults(QueryParam("science", SearchType.RESEARCH, rows = 4))
        }
        assertThat(response).isNotNull
        assertThat(response?.numFound).isGreaterThan(0)
        assertThat(response?.docs?.size).isEqualTo(4)
    }

    //@Test
    fun `Call the Arxiv service with request that returns no result`() {
        val response = runBlocking { ArxivClient().retrieveResults(QueryParam("azertyu", SearchType.RESEARCH)) }
        assertThat(response).isNotNull
        assertThat(response?.numFound).isEqualTo(0)
        assertThat(response?.docs).isEmpty()
    }
}

