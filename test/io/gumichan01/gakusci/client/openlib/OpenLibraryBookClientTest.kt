package io.gumichan01.gakusci.client.openlib

import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.utils.SearchType
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

internal class OpenLibraryBookClientTest {

//    @Test
    fun `Call the Open Library web-service - get book by ISBN`() {
        val client: IClient<List<OpenLibraryBookResponse>> = OpenLibraryBookClient()
        val response: List<OpenLibraryBookResponse>? = runBlocking { client.retrieveResults(QueryParam("1421500574", SearchType.BOOKS)) }
        assertThat(response).isNotNull
    }

//    @Test
    fun `Call the Open Library web-service - get book by LCCN`() {
        val client: IClient<List<OpenLibraryBookResponse>> = OpenLibraryBookClient()
        val response: List<OpenLibraryBookResponse>? = runBlocking { client.retrieveResults(QueryParam("62019420", SearchType.BOOKS)) }
        assertThat(response).isNotNull
    }

//    @Test
    fun `Call the Open Library web-service - get book by OCLC number`() {
        val client: IClient<List<OpenLibraryBookResponse>> = OpenLibraryBookClient()
        val response: List<OpenLibraryBookResponse>? = runBlocking { client.retrieveResults(QueryParam("18936737", SearchType.BOOKS)) }
        assertThat(response).isNotNull
    }

    @Test
    fun `Don't call the Open Library web-service - invalid ISBN or LCCN or OCLC`() {
        val client: IClient<List<OpenLibraryBookResponse>> = OpenLibraryBookClient()
        val response: List<OpenLibraryBookResponse>? = runBlocking { client.retrieveResults(QueryParam("1$9367e7", SearchType.BOOKS)) }
        assertThat(response).isNull()
    }
//    @Test // Note: formatBookNumber()  is private, so you need to make it public in order to test is
//    fun `format valid ISBN - get formatted text`() {
//        assertThat(OpenLibraryBookClient().getFormattedBookNumber("978-1-4215-0057-7")).isEqualTo("ISBN:9781421500577")
//    }
//
//    @Test
//    fun `format invalid ISBN - get empty text`() {
//        assertThat(OpenLibraryBookClient().getFormattedBookNumber("978-1-4215-0057-0")).isBlank()
//    }
//
//    @Test
//    fun `format valid LCCN - get formatted text`() {
//        assertThat(OpenLibraryBookClient().getFormattedBookNumber("n78890351")).isEqualTo("LCCN:n78890351")
//    }
//
//    @Test
//    fun `format invalid LCCN - get empty text`() {
//        assertThat(OpenLibraryBookClient().getFormattedBookNumber("478890351")).isBlank()
//    }
//
//    @Test
//    fun `format valid OCLC - get formatted text`() {
//        assertThat(OpenLibraryBookClient().getFormattedBookNumber("18936737")).isEqualTo("OCLC:18936737")
//    }
//
//    @Test
//    fun `format invalid OCLC - get empty text`() {
//        assertThat(OpenLibraryBookClient().getFormattedBookNumber("1$9367e7")).isBlank()
//    }
}