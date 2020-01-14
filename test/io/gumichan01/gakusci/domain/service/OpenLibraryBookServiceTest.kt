package io.gumichan01.gakusci.domain.service

import io.gumichan01.gakusci.client.openlib.OpenLibraryBookClient
import io.gumichan01.gakusci.client.openlib.OpenLibraryBookResponse
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.utils.SearchType
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

internal class OpenLibraryBookServiceTest {

    private val openLibBookMock: OpenLibraryBookClient = mockk {
        coEvery {
            retrieveResults(QueryParam("1421500574", SearchType.BOOKS))
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
        val response = runBlocking { service.search(QueryParam("1421500574", SearchType.BOOKS)) }
        assertThat(response).isNotNull
    }

    @Test
    fun `OpenLibrary book service - valid search on real client, get results`() {
        val service = OpenLibraryBookService(OpenLibraryBookClient())
        val response = runBlocking { service.search(QueryParam("1421500574", SearchType.BOOKS)) }.also { println(it) }
        assertThat(response).isNotNull
    }
}