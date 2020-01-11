package io.gumichan01.gakusci.domain.service

import io.gumichan01.gakusci.client.openlib.OpenLibraryBookClient
import io.gumichan01.gakusci.client.openlib.OpenLibraryBookResponse
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.utils.SearchType
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions
import kotlin.test.Test

internal class OpenLibraryBookServiceTest {

    private val openLibBookMock: OpenLibraryBookClient = mockk {
        coEvery { retrieveResults(QueryParam("1421500574", SearchType.BOOKS)) } returns OpenLibraryBookResponse("","","","","")
    }

    @Test
    fun `OpenLibrary book service - valid search on fake client, get results`() {
        val service = OpenLibraryBookService(openLibBookMock)
        val response = runBlocking { service.search(QueryParam("1421500574", SearchType.BOOKS)) }
        Assertions.assertThat(response).isNotNull
    }

    @Test
    fun `OpenLibrary book service - valid search on real client, get results`() {
        val service = OpenLibraryBookService(OpenLibraryBookClient())
        val response = runBlocking { service.search(QueryParam("1421500574", SearchType.BOOKS)) }.also { println(it) }
        Assertions.assertThat(response).isNotNull
    }
}