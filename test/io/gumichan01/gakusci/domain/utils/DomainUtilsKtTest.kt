package io.gumichan01.gakusci.domain.utils

import io.gumichan01.gakusci.domain.model.ResultEntry
import io.gumichan01.gakusci.domain.model.SearchResponse
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test


internal class DomainUtilsKtTest {

    @Test
    fun `Take elements from list`(): Unit {
        val response = SearchResponse(1, 0, listOf(ResultEntry("", "")))
        assertThat(response.take(1).entries).contains(ResultEntry("", ""))
    }

    @Test
    fun `Retrieve sublist of list - normal case`(): Unit {
        val response = SearchResponse(1, 0, listOf(ResultEntry("", "")))
        assertThat(response.slice(IntRange(0, 0)).entries).contains(ResultEntry("", ""))
    }

    @Test
    fun `Retrieve sublist bigger than the list - return the list `() {
        val response = SearchResponse(2, 0, listOf(ResultEntry("a", "b"), ResultEntry("", "")))
        assertThat(response.slice(IntRange(0, 42)).entries).isEqualTo(
            listOf(
                ResultEntry("a", "b"),
                ResultEntry("", "")
            )
        )
    }

    @Test
    fun `Retrieve sublist at the end of the list with non-zero start - return the sublist `() {
        val response = SearchResponse(2, 0, listOf(ResultEntry("a", "b"), ResultEntry("", "")))
        assertThat(response.slice(IntRange(1, 4)).entries).isEqualTo(listOf(ResultEntry("", "")))
    }
}