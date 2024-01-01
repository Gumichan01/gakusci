package io.gumichan01.gakusci.client.openlib

import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.SimpleQuery
import io.gumichan01.gakusci.domain.utils.SearchType
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.assertTrue

internal class OpenLibrarySearchClientTest {

    //@Test
    fun `Call the Open Library client - search for results`() {
        val response: OpenLibrarySearchResponse? =
            runBlocking { OpenLibrarySearchClient().retrieveResults(SimpleQuery("gunnm")) }
        assertThat(response).isNotNull
    }

    //@Test
    fun `Call the Open Library with request containing spaces - must not fail`() {
        runBlocking { OpenLibrarySearchClient().retrieveResults(SimpleQuery("gunnm last order")) }
        assertTrue { true }
    }
}