package io.gumichan01.gakusci.client.utils

import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

internal class BookNumberUtilsTest {

    @Test
    fun `check valid ISBN-10 - no dash, no space`() {
        assertThat(isValidISBN("1421500574")).isTrue()
    }

    @Test
    fun `check valid ISBN-10 - with dashes`() {
        assertThat(isValidISBN("2-1234-5680-2")).isTrue()
    }

    @Test
    fun `check valid ISBN-10 - with spaces`() {
        assertThat(isValidISBN("2 1234 5680 2")).isTrue()
    }

    @Test
    fun `check invalid ISBN-10 - mixed dashes and space`() {
        assertThat(isValidISBN("2 1234-5680 2")).isFalse()
    }

    @Test
    fun `check invalid ISBN-10 - short number`() {
        assertThat(isValidISBN("123")).isFalse()
    }

    @Test
    fun `check invalid ISBN - too long number`() {
        assertThat(isValidISBN("12345-1234567-123456-01")).isFalse()
    }

    @Test
    fun `check invalid ISBN-10 - it contains non-digit, non-dash and non-space characters`() {
        assertThat(isValidISBN("2-1234-K580-2")).isFalse()
    }

    @Test
    fun `check valid ISBN-10 - valid checksum`() {
        assertThat(isValidISBN("0-19-852663-6")).isTrue()
    }

    @Test
    fun `check invalid ISBN-10 - invalid checksum`() {
        assertThat(isValidISBN("0-19-852663-0")).isFalse()
    }
}