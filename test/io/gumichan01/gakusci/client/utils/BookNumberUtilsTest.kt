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

    @Test
    fun `check valid ISBN-13 - no dash, no space`() {
        assertThat(isValidISBN("9781421500577")).isTrue()
    }

    @Test
    fun `check valid ISBN-13 - with dashes`() {
        assertThat(isValidISBN("978-1-4215-0057-7")).isTrue()
    }

    @Test
    fun `check valid ISBN-13 - with spaces`() {
        assertThat(isValidISBN("978 1 4215 0057 7")).isTrue()
    }

    @Test
    fun `check invalid ISBN-13 - mixed dashes and space`() {
        assertThat(isValidISBN("978 1-4215-0057 7")).isFalse()
    }

    @Test
    fun `check invalid ISBN-13 - it contains non-digit, non-dash and non-space characters`() {
        assertThat(isValidISBN("978-1-4215-K057-7")).isFalse()
    }

    @Test
    fun `check valid ISBN-13 - valid checksum`() {
        assertThat(isValidISBN("978-1-4215-0057-7")).isTrue()
    }

    @Test
    fun `check invalid ISBN-13 - invalid checksum`() {
        assertThat(isValidISBN("978-1-4215-0057-0")).isFalse()
    }

    @Test
    fun `normalize LCCN - already normalized`() {
        assertThat(normalizeLccn("n78890351")).isEqualTo("n78890351")
    }

    @Test
    fun `normalize LCCN containing spaces`() {
        assertThat(normalizeLccn("n7 8890 351")).isEqualTo("n78890351")
    }

    @Test
    fun `normalize LCCN containing at least a forward slash`() {
        assertThat(normalizeLccn("n78890351/acc/r75")).isEqualTo("n78890351")
    }

    @Test
    fun `normalize LCCN containing one hyphen - nominal case`() {
        assertThat(normalizeLccn("2001-000002")).isEqualTo("2001000002")
    }

    @Test
    fun `normalize LCCN containing one hyphen - substring following (to the right of) the (removed) hyphen`() {
        assertThat(normalizeLccn("85-2")).isEqualTo("85000002")
    }

    @Test
    fun `normalize LCCN - empty string`() {
        assertThat(normalizeLccn("")).isEqualTo("")
    }
}