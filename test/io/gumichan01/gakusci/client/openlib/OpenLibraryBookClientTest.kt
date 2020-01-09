package io.gumichan01.gakusci.client.openlib

import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.utils.SearchType
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

internal class OpenLibraryBookClientTest {

    @Test
    fun `Call the Open Library web-service - get book by ISBN`() {
        val client: IClient<String> = OpenLibraryBookClient()
        val response: String? = runBlocking { client.retrieveResults(QueryParam("ISBN:1421500574", SearchType.BOOKS)) }
        assertThat(response).isNotNull()
    }
}