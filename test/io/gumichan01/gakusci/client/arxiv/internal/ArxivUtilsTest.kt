package io.gumichan01.gakusci.client.arxiv.internal

import io.gumichan01.gakusci.client.arxiv.internal.model.Link
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import java.time.LocalDate
import kotlin.test.Test

internal class ArxivUtilsTest {

    @Test
    fun `Retrieve date from string - nominal case`() {
        val expectedDate = LocalDate.parse("1970-01-01")
        assertThat(ArxivUtils.toDate("1970-01-01")).isEqualTo(expectedDate)
    }

    @Test
    fun `Retrieve date from string - IOSO-8601 complete`() {
        val expectedDate = LocalDate.parse("1970-01-01")
        assertThat(ArxivUtils.toDate("1970-01-01T21:49:33Z")).isEqualTo(expectedDate)
    }

    @Test
    fun `Retrieve date from string - bad format`() {
        assertThatThrownBy { ArxivUtils.toDate("1970-01-01/bad") }.isInstanceOf(Exception::class.java)
    }

    @Test
    fun `Get website link with type HTML - nominal case`() {
        val expectedLink = Link("", "text/html")
        assertThat(ArxivUtils.getWebsiteLink(listOf(Link("", ""), expectedLink))).isEqualTo(expectedLink)
    }
}