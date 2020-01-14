package io.gumichan01.gakusci.client.openlib

import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.utils.SearchType
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

internal class OpenLibraryBookClientTest {

//    @Test
    fun `Call the Open Library web-service (real external service) - get book by ISBN`() {
        val client: IClient<OpenLibraryBookResponse> = OpenLibraryBookClient()
        val response: OpenLibraryBookResponse? =
            runBlocking { client.retrieveResults(QueryParam("1421500574", SearchType.BOOKS)) }
        assertThat(response).isNotNull
        assertThat(response?.bibKey).contains("1421500574")
    }

    @Test
    fun `Don't call the Open Library web-service - invalid ISBN or LCCN or OCLC`() {
        val client: IClient<OpenLibraryBookResponse> = OpenLibraryBookClient()
        val response: OpenLibraryBookResponse? =
            runBlocking { client.retrieveResults(QueryParam("1$9367e7", SearchType.BOOKS)) }
        assertThat(response).isNull()
    }
}