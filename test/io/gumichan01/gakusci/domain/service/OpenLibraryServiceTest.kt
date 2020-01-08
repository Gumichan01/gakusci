package io.gumichan01.gakusci.domain.service

import io.gumichan01.gakusci.client.openlib.OpenLibraryClient
import io.gumichan01.gakusci.client.openlib.OpenLibrarySearchResponse
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.utils.SearchType
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

internal class OpenLibraryServiceTest {

    private val openLibResponseMock: OpenLibrarySearchResponse = mockk {
        coEvery { numFound } returns 1
        coEvery { docs } returns emptyList()
    }
    private val openLibMock: OpenLibraryClient = mockk {
        coEvery { retrieveResults(QueryParam("gunnm", SearchType.BOOKS)) } returns openLibResponseMock
    }

    @Test
    fun `OpenLibrary service - valid search on fake client, get results`() {
        val service = OpenLibraryService(openLibMock)
        val response = runBlocking { service.search(QueryParam("gunnm", SearchType.BOOKS)) }
        assertThat(response).isNotNull
    }
}