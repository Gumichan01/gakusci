package io.gumichan01.gakusci.client.openlib

import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.utils.SearchType
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat

internal class OpenLibrarySearchClientTest {

    //@Test
    fun `Call the Open Library web-service - search for results`() {
        val response: OpenLibrarySearchResponse? =
            runBlocking { OpenLibrarySearchClient().retrieveResults(QueryParam("gunnm", SearchType.BOOKS)) }
        assertThat(response).isNotNull
    }
}