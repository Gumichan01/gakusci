package io.gumichan01.gakusci.client.openlib

import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.utils.SearchType
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

internal class OpenLibraryClientTest {

    //@Test
    fun `Call the Open Library web-service - get results`() {
        val response: OpenLibraryResponse? =
            runBlocking { OpenLibraryClient().retrieveResults(QueryParam("gunnm", SearchType.BOOKS)) }
        assertThat(response).isNotNull
    }
}