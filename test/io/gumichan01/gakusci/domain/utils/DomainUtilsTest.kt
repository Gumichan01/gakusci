package io.gumichan01.gakusci.domain.utils

import io.gumichan01.gakusci.domain.model.SearchResponse
import io.gumichan01.gakusci.domain.model.entry.SimpleResultEntry
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test


internal class DomainUtilsTest {

    @Test
    fun `Take elements from list`() {
        val response = SearchResponse(
            1, 0, listOf(
            SimpleResultEntry(
                "",
                ""
            )))
        assertThat(response.take(1).entries).contains(
            SimpleResultEntry(
                "",
                ""))
    }

    @Test
    fun `Retrieve sublist of list - normal case`(): Unit {
        val response = SearchResponse(
            1, 0, listOf(
            SimpleResultEntry(
                "",
                "")))
        assertThat(response.slice(IntRange(0, 0)).entries).contains(
            SimpleResultEntry(
                "",
                ""))
    }

    @Test
    fun `Retrieve sublist bigger than the list - return the list `() {
        val response = SearchResponse(
            2, 0, listOf(
            SimpleResultEntry(
                "a",
                "b"
            ), SimpleResultEntry("", "")))
        assertThat(response.slice(IntRange(0, 42)).entries).isEqualTo(
            listOf(
                SimpleResultEntry("a", "b"),
                SimpleResultEntry("", "")))
    }

    @Test
    fun `Retrieve sublist at the end of the list with non-zero start - return the sublist `() {
        val response = SearchResponse(
            2, 0, listOf(
            SimpleResultEntry(
                "a",
                "b"
            ), SimpleResultEntry("", "")))
        assertThat(response.slice(IntRange(1, 4)).entries).isEqualTo(
            listOf(
                SimpleResultEntry(
                    "",
                    "")))
    }
}