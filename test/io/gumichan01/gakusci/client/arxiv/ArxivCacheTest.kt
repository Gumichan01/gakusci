package io.gumichan01.gakusci.client.arxiv

import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.utils.SearchType
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.sql.Date

internal class ArxivCacheTest {

    private val fakeDate = Date.valueOf("2023-01-01")
    private val expectedResponse = ArxivResponse(1, listOf(ArxivResultEntry(emptyList(), "test-arxiv", fakeDate, "///fake/path")))
    private val arxivClientMock: ArxivClient = mockk {
        coEvery { retrieveResults(QueryParam("loremi", SearchType.RESEARCH, )) } returns expectedResponse
    }

    @Test
    fun `Arxiv Cache - Save response, expect cached data`() {
        val cache = ArxivCache()
        val expectedValue = ArxivResponse(0, emptyList())
        cache.put("lorem", ArxivResponse(0, emptyList()))
        val value: ArxivResponse = cache.get("lorem") {
            ArxivResponse(1, listOf(ArxivResultEntry(emptyList(), "FAIL TEST", fakeDate, "")))
        }
        assertThat(cache.getIfPresent("lorem")).isNotNull
        assertThat(value).isEqualTo(expectedValue)
        assertThat(value.numFound).isZero
        assertThat(value.docs).isEmpty()
    }

    @Test
    fun `Arxiv Cache - Retrieve data, cache it, retrieve it again, expect cached same data`() {
        val cache = ArxivCache()
        val resp01: ArxivResponse = cache.get("loremi") {
            runBlocking { arxivClientMock.retrieveResults(QueryParam("loremi", SearchType.RESEARCH, )) }
        }
        val resp02: ArxivResponse = cache.get("loremi") {
            runBlocking { arxivClientMock.retrieveResults(QueryParam("loremi", SearchType.RESEARCH, )) }
        }
        val value: ArxivResponse = cache.get("loremi") {
            ArxivResponse(1, listOf(ArxivResultEntry(emptyList(), "FAIL TEST", fakeDate, "")))
        }
        assertThat(cache.getIfPresent("loremi")).isNotNull
        assertThat(value).isEqualTo(expectedResponse)
        assertThat(value).isEqualTo(resp01)
        assertThat(value).isEqualTo(resp02)
    }
}