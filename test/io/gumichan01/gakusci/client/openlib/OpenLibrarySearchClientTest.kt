package io.gumichan01.gakusci.client.openlib

import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.utils.SearchType
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import kotlin.test.assertTrue

internal class OpenLibrarySearchClientTest {

    //@Test
    fun `Call the Open Library service - search for results`() {
        val response: OpenLibrarySearchResponse? =
            runBlocking { OpenLibrarySearchClient().retrieveResults(QueryParam("gunnm", SearchType.BOOKS)) }
        assertThat(response).isNotNull
    }

    //@Test
    fun `Call the Open Library with request containing spaces - must not fail`() {
        runBlocking { OpenLibrarySearchClient().retrieveResults(QueryParam("gunnm last order", SearchType.BOOKS)) }
        assertTrue { true }
    }
}