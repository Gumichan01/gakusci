package io.gumichan01.gakusci.domain.service

import io.gumichan01.gakusci.client.openlib.OpenLibraryBookClient
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.utils.SearchType
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions
import kotlin.test.Test

internal class OpenLibraryBookServiceTest {

    private val openLibBookMock: OpenLibraryBookClient = mockk {
        coEvery { retrieveResults(QueryParam("ISBN:1421500574", SearchType.BOOKS)) } returns "fake result"
    }

    @Test
    fun `OpenLibrary book service - valid search on fake client, get results`() {
        val service = OpenLibraryBookService(openLibBookMock)
        val response = runBlocking { service.search(QueryParam("ISBN:1421500574", SearchType.BOOKS)) }
        Assertions.assertThat(response).isNotNull
    }
}