package io.gumichan01.gakusci.domain.service.openlib

import io.gumichan01.gakusci.client.openlib.OpenLibrarySearchClient
import io.gumichan01.gakusci.client.openlib.OpenLibrarySearchResponse
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.ServiceResponse
import io.gumichan01.gakusci.domain.utils.SearchType
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

internal class OpenLibrarySearchServiceTest {

    private val openLibResponseMock: OpenLibrarySearchResponse = mockk {
        coEvery { numFound } returns 0
        coEvery { docs } returns emptyList()
    }
    private val openLibSearchMock: OpenLibrarySearchClient = mockk {
        coEvery { retrieveResults(QueryParam("gunnm", SearchType.BOOKS)) } returns openLibResponseMock
    }

    @Test
    fun `OpenLibrary service - valid search on fake client, get results`() {
        val service = OpenLibrarySearchService(openLibSearchMock)
        val response: ServiceResponse = runBlocking { service.search(QueryParam("gunnm", SearchType.BOOKS)) }
        assertThat(response.totalResults).isZero
    }
}