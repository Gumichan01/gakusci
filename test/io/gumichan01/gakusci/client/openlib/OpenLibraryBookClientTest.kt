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
        val client: IClient<List<OpenLibraryBookResponse>> = OpenLibraryBookClient()
        val response: List<OpenLibraryBookResponse>? =
            runBlocking { client.retrieveResults(QueryParam("1421500574", SearchType.BOOKS)) }
        assertThat(response).isNotNull
    }

    @Test
    fun `Call the Open Library web-service - get book by LCCN`() {
        val client: IClient<List<OpenLibraryBookResponse>> = OpenLibraryBookClient()
        val response: List<OpenLibraryBookResponse>? =
            runBlocking { client.retrieveResults(QueryParam("62019420", SearchType.BOOKS)) }
        assertThat(response).isNotNull
    }

    @Test
    fun `Call the Open Library web-service - get book by OCLC number`() {
        val client: IClient<List<OpenLibraryBookResponse>> = OpenLibraryBookClient()
        val response: List<OpenLibraryBookResponse>? =
            runBlocking { client.retrieveResults(QueryParam("18936737", SearchType.BOOKS)) }
        assertThat(response).isNotNull
    }

    @Test
    fun `Don't call the Open Library web-service - invalid ISBN or LCCN or OCLC`() {
        val client: IClient<List<OpenLibraryBookResponse>> = OpenLibraryBookClient()
        val response: List<OpenLibraryBookResponse>? =
            runBlocking { client.retrieveResults(QueryParam("1$9367e7", SearchType.BOOKS)) }
        assertThat(response).isNull()
    }
}