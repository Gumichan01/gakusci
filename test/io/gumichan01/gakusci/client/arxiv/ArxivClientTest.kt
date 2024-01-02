package io.gumichan01.gakusci.client.arxiv

import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.SimpleQuery
import io.gumichan01.gakusci.domain.utils.SearchType
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ArxivClientTest {

    //@Test
    fun `Call the Arxiv service - get results`() {
        val response: ArxivResponse = runBlocking { ArxivClient().retrieveResults(SimpleQuery("science")) }
        assertThat(response).isNotNull
        assertThat(response.docs.size).isGreaterThan(0)
        assertThat(response.numFound).isGreaterThan(0)
    }

    //@Test
    fun `Call the Arxiv service with query containing spaces - get results`() {
        val response: ArxivResponse = runBlocking {
            ArxivClient().retrieveResults(SimpleQuery("book sales"))
        }
        assertThat(response).isNotNull
        assertThat(response.docs.size).isGreaterThan(0)
        assertThat(response.numFound).isGreaterThan(0)
    }

    //@Test
    fun `Call the Arxiv service with request that returns no result`() {
        val response: ArxivResponse = runBlocking { ArxivClient().retrieveResults(SimpleQuery("azertyu")) }
        assertThat(response).isNotNull
        assertThat(response.numFound).isEqualTo(0)
        assertThat(response.docs).isEmpty()
    }
}

