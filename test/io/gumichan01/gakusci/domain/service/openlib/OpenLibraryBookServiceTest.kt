package io.gumichan01.gakusci.domain.service.openlib

import io.gumichan01.gakusci.client.openlib.OpenLibraryBookClient
import io.gumichan01.gakusci.client.openlib.OpenLibraryBookResponse
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.ServiceResponse
import io.gumichan01.gakusci.domain.model.SimpleQuery
import io.gumichan01.gakusci.domain.model.entry.BookEntry
import io.gumichan01.gakusci.domain.utils.SearchType
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

internal class OpenLibraryBookServiceTest {

    private val openLibBookMock: OpenLibraryBookClient = mockk {
        coEvery {
            retrieveResults(SimpleQuery("ISBN:1421500574"))
        } returns OpenLibraryBookResponse(
            "1421500574",
            "http://openlibrary.org/books/OL8490428M/Battle_Angel_Alita",
            "nopreview",
            "http://openlibrary.org/books/OL8490428M/Battle_Angel_Alita",
            "https://covers.openlibrary.org/b/id/764825-S.jpg"
        )
    }

    @Test
    fun `OpenLibrary book service - valid search on fake client, get results`() {
        val service = OpenLibraryBookService(openLibBookMock)
        val response: ServiceResponse = runBlocking { service.search(SimpleQuery("1421500574")) }
        assertThat(response.totalResults).isEqualTo(1)
        assertThat(response.entries).contains(
            BookEntry(
                bibKey = "1421500574", url = "http://openlibrary.org/books/OL8490428M/Battle_Angel_Alita",
                thumbnailUrl = "https://covers.openlibrary.org/b/id/764825-S.jpg"
            )
        )
    }

    @Test
    fun `OpenLibrary book service - invalid search on fake client, get no result`() {
        val service = OpenLibraryBookService(openLibBookMock)
        val response: ServiceResponse = runBlocking { service.search(SimpleQuery("invalid-isbn")) }
        assertThat(response.totalResults).isZero
        assertThat(response.entries).isEmpty()
    }
}